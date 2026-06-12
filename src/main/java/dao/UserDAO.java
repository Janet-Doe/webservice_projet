package dao;

import dto.User;

import java.util.ArrayList;

public interface UserDAO {
    ArrayList<User> findAll();
    User findByUser(String id);
}
