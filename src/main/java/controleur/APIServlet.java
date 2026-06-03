package controleur;

import dao.CanalDAO;
import dao.DAOFactory;
import dao.MessageDAO;
import dao.UtilisateurDAO;
import dto.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/*")
public class APIServlet extends HttpServlet {
    CanalDAO canalDAO = DAOFactory.getCanalDAO();
    MessageDAO messageDAO = DAOFactory.getMessageDAO();
    UtilisateurDAO utilisateurDAO = DAOFactory.getUtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

}
