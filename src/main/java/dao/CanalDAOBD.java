package dao;
import dto.Canal;
import dto.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            while(!rs.next()){
                canaux.add(new Canal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("isPublic"),
                        rs.getInt("idCreateur"),
                        rs.getDate("dateCreation"),
                        rs.getDate("dateModification"))
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
            while(!rs.next()){
                canaux.add(new Canal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("isPublic"),
                        rs.getInt("idCreateur"),
                        rs.getDate("dateCreation"),
                        rs.getDate("dateModification"))
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
        return null;
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
                        rs.getBoolean("isPublic"),
                        rs.getInt("idCreateur"),
                        rs.getDate("dateCreation"),
                        rs.getDate("dateModification"));
            }
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Canal canal) {

    }

    @Override
    public void save(Canal canal) {

    }

    @Override
    public void update(Canal canal) {

    }
}
