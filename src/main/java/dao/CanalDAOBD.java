package dao;
import dto.Canal;
import dto.Utilisateur;

import java.sql.*;
import java.util.ArrayList;

public class CanalDAOBD implements CanalDAO {
    private final DbConnectionManager db;

    protected CanalDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Canal> findAll() {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM canal;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Canal> canaux = new ArrayList<>();
            while(rs.next()){
                canaux.add(new Canal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomutilisateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Canal> findAllPublic() {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM canal WHERE public=TRUE;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Canal> canaux = new ArrayList<>();
            while(rs.next()){
                canaux.add(new Canal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomutilisateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Canal> findAllJoined(Utilisateur utilisateur) {
        try (Connection conn = db.getConnection()){
            String query = """
                    SELECT id, nom, ispublic, idutilisateur, datecreation, datemodification
                    FROM canal
                    JOIN participe ON canal.id=participe.idcanal
                    ON WHERE idutilisateur = ?;
                    """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, utilisateur.getUser());
            ResultSet rs = stmt.executeQuery();
            ArrayList<Canal> canaux = new ArrayList<>();
            while (rs.next()) {
                canaux.add(new Canal(rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomutilisateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public Canal findById(int id) {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM canal WHERE id = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                return new Canal(id,
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomutilisateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"));
            }
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }


    @Override
    public void update(Canal canal) {
        try (Connection conn = db.getConnection()) {
            String query = """
                            UPDATE canal set  
                            where id = ?;
                            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBoolean(3, canal.isIs_public());
            stmt.setTimestamp(4, (Timestamp) canal.getDateCreation());
            stmt.setTimestamp(5, (Timestamp) canal.getDateModification());
            stmt.setInt(6, canal.getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.printf("Erreur : %s", e.getMessage());
        }
    }
}
