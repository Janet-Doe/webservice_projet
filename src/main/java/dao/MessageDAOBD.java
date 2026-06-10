package dao;
import dto.Canal;
import dto.Message;

import java.sql.*;
import java.util.ArrayList;

public class MessageDAOBD implements MessageDAO {
    private final DbConnectionManager db;

    protected MessageDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Message> findAll() {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM message;";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Message> messages = new ArrayList<>();
            while(rs.next()){
                messages.add(new Message(
                        rs.getInt("id"),
                        rs.getInt("idcanal"),
                        rs.getString("nomAuteur"),
                        rs.getString("text"),
                        rs.getTimestamp("dateEnvoi"),
                        rs.getTimestamp("dateModification"))
                );
            }
            return messages;
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public Message findById(int id) {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM message where id=?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                return null;
            } else {
                return new Message(
                        rs.getInt("id"),
                        rs.getInt("idcanal"),
                        rs.getString("nomAuteur"),
                        rs.getString("text"),
                        rs.getTimestamp("dateEnvoi"),
                        rs.getTimestamp("dateModification")
                );
            }
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(Message message) {
        try (Connection conn = db.getConnection()){
            String query = "delete FROM message where id=?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, message.getId());
            stmt.executeQuery();
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
        }
    }

    @Override
    public Message save(Message message) {
        try (Connection conn = db.getConnection()) {
            String query = """
                            INSERT INTO message (contenu, dateEnvoi, dateModification, idCanal, nomAuteur) VALUES
                               (?, ?, ?, ?, ?) returning id;
                            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, message.getText());
            stmt.setTimestamp(2, (Timestamp) message.getDateEnvoie());
            stmt.setTimestamp(3, (Timestamp) message.getLastModification());
            stmt.setInt(4, message.getIdCanal());
            stmt.setString(5, message.getIdAuteur());
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return new Message(rs.getInt("id"),
                    message.getIdCanal(),
                    message.getIdAuteur(),
                    message.getText(),
                    message.getDateEnvoie(),
                    message.getLastModification());

        } catch (SQLException e) {
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Message message) {
        try (Connection conn = db.getConnection()) {
            String query = """
                            UPDATE message set contenu = ?,
                                             dateModification = ?
                            where id = ?;
                            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, message.getText());
            stmt.setTimestamp(2, (Timestamp) message.getLastModification());
            stmt.setInt(3, message.getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            System.err.printf("Erreur : %s", e.getMessage());
        }
    }

    @Override
    public ArrayList<Message> findAllInCanal(Canal canal) {
        try (Connection conn = db.getConnection()){
            String query = "SELECT * FROM message where idCanal=?;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, canal.getId());
            ResultSet rs = stmt.executeQuery();
            ArrayList<Message> messages = new ArrayList<>();
            while(rs.next()){
                messages.add(new Message(
                        rs.getInt("id"),
                        rs.getInt("idcanal"),
                        rs.getString("nomAuteur"),
                        rs.getString("contenu"),
                        rs.getTimestamp("dateEnvoi"),
                        rs.getTimestamp("dateModification"))
                );
            }
            return messages;
        } catch (SQLException e){
            System.err.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

}
