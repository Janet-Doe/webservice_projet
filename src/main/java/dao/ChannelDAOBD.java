package dao;
import dto.Channel;
import dto.User;

import java.sql.*;
import java.util.ArrayList;

public class ChannelDAOBD implements ChannelDAO {
    private final DbConnectionManager db;

    protected ChannelDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Channel> findAll() {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM channel;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Channel> canaux = new ArrayList<>();
            while(rs.next()){
                canaux.add(new Channel(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomcreateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Channel> findAllPublic() {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM channel WHERE ispublic=TRUE;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Channel> canaux = new ArrayList<>();
            while(rs.next()){
                canaux.add(new Channel(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomcreateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<Channel> findAllJoined(User utilisateur) {
        try (Connection conn = db.getConnection()){
            String query = """
                    SELECT id, nom, ispublic, nomcreateur, datecreation, datemodification
                    FROM channel
                    JOIN participe ON channel.id=participe.idcanal
                    WHERE nomutilisateur = ?;
                    """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, utilisateur.getUser());
            ResultSet rs = stmt.executeQuery();
            ArrayList<Channel> canaux = new ArrayList<>();
            while (rs.next()) {
                canaux.add(new Channel(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomcreateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"))
                );
            }
            return canaux;
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public Channel findById(int id) {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM channel WHERE id = ?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                return new Channel(id,
                        rs.getString("nom"),
                        rs.getBoolean("ispublic"),
                        rs.getString("nomcreateur"),
                        rs.getDate("datecreation"),
                        rs.getDate("datemodification"));
            }
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Channel channel) {
        try (Connection conn = db.getConnection()) {
            String query = """
                            UPDATE channel set ispublic = ?,
                                             datemodification = ?,
                            where id = ?;
                            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, channel.is_public());
            stmt.setTimestamp(2, (Timestamp) channel.getDateModification());
            stmt.setInt(3, channel.getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.err.printf("Erreur : %s", e.getMessage());
        }
    }
}
