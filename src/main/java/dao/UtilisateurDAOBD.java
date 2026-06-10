package dao;
import dto.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UtilisateurDAOBD implements UtilisateurDAO {
    private final DbConnectionManager db;

    protected UtilisateurDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Utilisateur> findAll() {
        ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT nom, mdp, dateInscription, derniereConnexion FROM utilisateur";

        try (Connection conn = db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String user = rs.getString("nom");
                
                byte[] mdpBytes = rs.getBytes("mdp");
                String mdp = (mdpBytes != null) ? new String(mdpBytes) : null;
                
                java.util.Date dateInscription = rs.getTimestamp("dateInscription");
                java.util.Date derniereConnexion = rs.getTimestamp("derniereConnexion");

                Utilisateur u = new Utilisateur(user, mdp, dateInscription, derniereConnexion);

                utilisateurs.add(u);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération des utilisateurs : " + e.getMessage());
            e.printStackTrace();
        }
        
        return utilisateurs;
    }

    @Override
    public Utilisateur findByUser(String name) {
        Utilisateur utilisateur = null;
    String query = "SELECT nom, mdp, dateInscription, derniereConnexion FROM utilisateur WHERE nom = ?";

    try (Connection conn = db.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, name);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String user = rs.getString("nom");
                
                byte[] mdpBytes = rs.getBytes("mdp");
                String mdp = null;
                if (mdpBytes != null) {
                    StringBuilder hex = new StringBuilder();
                    for (byte b : mdpBytes) {
                        hex.append(String.format("%02x", b));
                    }
                    mdp = hex.toString();
                }
                
                java.util.Date dateInscription = rs.getTimestamp("dateInscription");
                java.util.Date derniereConnexion = rs.getTimestamp("derniereConnexion");

                utilisateur = new Utilisateur(user, mdp, dateInscription, derniereConnexion);
            }
        }
        
    } catch (SQLException e) {
        System.err.println("Erreur recherche utilisateur : " + e.getMessage());
        e.printStackTrace();
    }
    
    return utilisateur;
    }
}
