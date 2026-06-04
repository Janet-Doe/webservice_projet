package dao;

import dto.Canal;
import dto.Message;
import dto.Utilisateur;

import java.util.ArrayList;

public interface MessageDAO {
    abstract ArrayList<Message> findAll();
    abstract Message findById(int id);
    abstract void delete(Message message);
    abstract void save(Message message);
    abstract void update(Message message);
    abstract ArrayList<Message> findAllInCanal(Canal canal);
    // get new messages
}
