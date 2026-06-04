package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DAOFactory;
import dao.UtilisateurDAO;
import dto.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;


@WebServlet("/users/*")
public class UserServlet extends HttpServlet{
    private UtilisateurDAO utilisateurDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
        this.objectMapper = new ObjectMapper();
    }

    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    response.setContentType("application/json;charset=UTF-8");
 
        String path = request.getPathInfo(); 
 
        if ("/login".equals(path)) {

            Map<?, ?> body;
            try {
            body = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(),
                Map.of("erreur", "Corps JSON invalide ou manquant"));
            return;
        }
        String nom = (String) body.get("nom");
        String mdp = (String) body.get("mdp");
        if (nom == null || nom.isBlank() || mdp == null || mdp.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(response.getWriter(),
                Map.of("erreur", "Les champs 'nom' et 'mdp' sont obligatoires"));
            return;
        }
        Utilisateur utilisateur = utilisateurDAO.findByUser(nom);
 
        if (utilisateur == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(response.getWriter(),
                Map.of("erreur", "Utilisateur ou mot de passe incorrect"));
            return;
        }

        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(response.getWriter(),
                Map.of("erreur", "Endpoint introuvable"));
        }
  }
    
}
