package dao;

import dto.Channel;
import dto.User;

import java.util.ArrayList;

public interface ChannelDAO {

    ArrayList<Channel> findAll();
    Channel findById(int id);
    void update(Channel channel);
    ArrayList<Channel> findAllPublic();
    ArrayList<Channel> findAllJoined(User utilisateur);
}
