package dao;

import dto.Utilisateur;

import java.util.ArrayList;

public interface UtilisateurDAO {
    abstract ArrayList<Utilisateur> findAll();
    abstract Utilisateur findById(int id);
    abstract void save(Utilisateur utilisateur);
    abstract void update(Utilisateur utilisateur);
    abstract void delete(Utilisateur utilisateur);
}
