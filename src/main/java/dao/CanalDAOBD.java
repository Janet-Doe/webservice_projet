package dao;
import dto.Canal;
import dto.Utilisateur;

import java.util.ArrayList;

public class CanalDAOBD implements CanalDAO {
    private final DbConnectionManager db;

    protected CanalDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Canal> findAll() {
        return null;
    }

    @Override
    public ArrayList<Canal> findAllPublic() {
        return null;
    }

    @Override
    public ArrayList<Canal> findAllJoined(Utilisateur utilisateur) {
        return null;
    }

    @Override
    public Canal findById(int id) {
        return null;
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
