package dao;
import dto.Canal;
import dto.Message;
import dto.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        rs.getDate("dateEnvoi"),
                        rs.getDate("dateModification"))
                );
            }
            return messages;
        } catch (SQLException e){
            System.out.printf("Erreur : %s", e.getMessage());
            return null;
        }
    }

    @Override
    public Message findById(int id) {
        return null;
    }

    @Override
    public void delete(Message message) {

    }

    @Override
    public void save(Message message) {

    }

    @Override
    public void update(Message message) {

    }

    @Override
    public ArrayList<Message> findAllInCanal(Canal canal) {
        return null;
    }

}
