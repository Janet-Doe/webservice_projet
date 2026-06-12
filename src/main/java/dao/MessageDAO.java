package dao;

import dto.Channel;
import dto.Message;

import java.util.ArrayList;

public interface MessageDAO {
    ArrayList<Message> findAll();
    Message findById(int id);
    boolean delete(Message message);
    Message save(Message message);
    void update(Message message);
    ArrayList<Message> findAllInCanal(Channel channel);
}
