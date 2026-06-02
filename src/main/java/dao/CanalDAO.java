package dao;

import dto.Canal;
import dto.Utilisateur;

import java.util.ArrayList;

public interface CanalDAO {

    abstract ArrayList<Canal> findAll();
    abstract ArrayList<Canal> findAllPublic();
    abstract ArrayList<Canal> findAllForUser(Utilisateur utilisateur);
    abstract Canal findById(int id);
    abstract void delete(Canal canal);
    abstract void save(Canal canal);
    abstract void update(Canal canal);
}
