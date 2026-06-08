package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CanalDAO;
import dao.DAOFactory;
import dao.MessageDAO;
import dto.Canal;
import dto.Message;
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
    static ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            ArrayList<Canal> channels = canalDAO.findAll();
            if (channels == null || channels.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            out.println(om.writeValueAsString(channels));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else { // case: "site_url/channels/1/"
            String[] splitPath = pathInfo.split("/");
            try {
                int id = Integer.parseInt(splitPath[1]);
                Canal canal = canalDAO.findById(id);
                ArrayList<Message> messages = messageDAO.findAllInCanal(canal);
                if (messages == null || messages.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
                out.println(om.writeValueAsString(messages));
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        else { // case: "site_url/channels/1/"
            int canalId = Integer.parseInt(pathInfo.split("/")[1]);

            // read json from POST request:
            BufferedReader reader = req.getReader();
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String json = builder.toString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(json, Map.class);

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
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { // case: "site_url/channels/"
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        else { // case: "site_url/channels/1/messages/2"
            String[] parsedPath = pathInfo.split("/");
            if (parsedPath.length < 4 || parsedPath[3].isEmpty()) { // case: "site_url/channels/1/" or "site_url/channels/1/messages/"
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            int canalId = Integer.parseInt(parsedPath[1]);
            int messageId = Integer.parseInt(parsedPath[3]);

            // read json from PUT request:
            BufferedReader reader = req.getReader();
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String json = builder.toString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(json, Map.class);

            // check values validity:
            Object text = map.get("text");
            if(text == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Message message = new Message(messageId, canalId, "", (String)text, timestamp, timestamp);

            // save new message:
            try {
                messageDAO.update(message);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
