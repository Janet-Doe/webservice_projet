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
    public Utilisateur findByUser(String name) {
        return null;
    }
}
