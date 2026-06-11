package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CanalDAO;
import dao.DAOFactory;
import dao.MessageDAO;
import dao.UtilisateurDAO;
import dto.Canal;
import dto.Message;
import dto.Utilisateur;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/channels/*")
public class CanalMessageServlet extends HttpServlet {
    CanalDAO canalDAO = DAOFactory.getCanalDAO();
    MessageDAO messageDAO = DAOFactory.getMessageDAO();
    UtilisateurDAO utilisateurDAO = DAOFactory.getUtilisateurDAO();

    static ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        resp.setContentType("application/json");

        // 1. Vérification du token JWT
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token manquant");
            return;
        }
        String username = utils.JwtUtil.validateToken(authHeader.substring(7));
        if (username == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré");
            return;
        }

        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Retourne uniquement les canaux publics + les canaux privés dont l'user est membre
            Utilisateur utilisateur = utilisateurDAO.findByUser(username);
            ArrayList<Canal> publics = canalDAO.findAllPublic();
            ArrayList<Canal> rejoints = canalDAO.findAllJoined(utilisateur);
            if (publics == null) {
                publics = new ArrayList<>(); // Évite un crash si findAllPublic() renvoie null
            }

            // Fusionner sans doublons (seulement si la liste n'est pas nulle)
            if (rejoints != null) {
                for (Canal c : rejoints) {
                    if (publics.stream().noneMatch(p -> p.getId() == c.getId())) {
                        publics.add(c);
                    }
                }
            }

            if (publics.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            out.println(om.writeValueAsString(publics));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // case: "site_url/channels/1/"
        String[] splitPath = pathInfo.split("/");
        try {
            int id = Integer.parseInt(splitPath[1]);
            Canal canal = canalDAO.findById(id);

            if (canal == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Canal introuvable");
                return;
            }

            // 2. Vérification d'accès pour les canaux privés
            if (!canal.isIs_public()) {
                Utilisateur utilisateur = utilisateurDAO.findByUser(username);
                ArrayList<Canal> rejoints = canalDAO.findAllJoined(utilisateur);
                boolean estMembre = rejoints.stream().anyMatch(c -> c.getId() == canal.getId());
                if (!estMembre) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé à ce canal privé");
                    return;
                }
            }

            ArrayList<Message> messages = messageDAO.findAllInCanal(canal);
            if (messages == null || messages.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            out.println(om.writeValueAsString(messages));
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Canal introuvable");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
         // case: "site_url/channels/1/"
        int canalId = Integer.parseInt(pathInfo.split("/")[1]);

        Map<String,Object> map = getJsonFromRequest(req);

        // check values validity:
        Object idAuteur = map.get("idAuteur");
        Object text = map.get("text");
        if(idAuteur == null || text == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message(-1, canalId, (String)idAuteur, (String)text, timestamp, timestamp);

        // save new message:
        try {
            message = messageDAO.save(message);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().println(om.writeValueAsString(message));
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String[] parsedPath = pathInfo.split("/");
        if (parsedPath.length < 4 || parsedPath[3].isEmpty()) { // case: "site_url/channels/1/" or "site_url/channels/1/messages/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        // case: "site_url/channels/1/messages/2"
        int canalId = Integer.parseInt(parsedPath[1]);
        int messageId = Integer.parseInt(parsedPath[3]);

        Map<String,Object> map = getJsonFromRequest(req);

        // check values validity:
        Object text = map.get("text");
        if(text == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message(messageId, canalId, "", (String)text, timestamp, timestamp);

        // update message:
        try {
            messageDAO.update(message);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String[] parsedPath = pathInfo.split("/");
        if (parsedPath.length < 4 || parsedPath[3].isEmpty()) { // case: "site_url/channels/1/" or "site_url/channels/1/messages/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        // case: "site_url/channels/1/messages/2"
        int messageId = Integer.parseInt(parsedPath[3]);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Message message = new Message(messageId, -1, "", "", timestamp, timestamp);

        // delete message:
       if (!messageDAO.delete(message)){
           resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
           resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected Map<String, Object> getJsonFromRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String json = builder.toString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Map.class);
    }

}


