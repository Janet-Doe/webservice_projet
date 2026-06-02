package dao;
import dto.Canal;
import dto.Message;
import dto.Utilisateur;

import java.util.ArrayList;

public class MessageDAOBD implements MessageDAO {
    private final DbConnectionManager db;

    protected MessageDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Message> findAll() {
        return null;
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

    @Override
    public Message findAllFromUser(Utilisateur utilisateur) {
        return null;
    }
}
