package dao;
import dto.Utilisateur;

import java.util.ArrayList;

public class UtilisateurDAOBD implements UtilisateurDAO {
    private final DbConnectionManager db;

    protected UtilisateurDAOBD(DbConnectionManager dbConnectionManager) {
        this.db = dbConnectionManager;
    }

    @Override
    public ArrayList<Utilisateur> findAll() {
        return null;
    }

    @Override
    public Utilisateur findById(int id) {
        return null;
    }

    @Override
    public void save(Utilisateur utilisateur) {

    }

    @Override
    public void update(Utilisateur utilisateur) {

    }

    @Override
    public void delete(Utilisateur utilisateur) {

    }
}
