package dao;

import dto.Utilisateur;

import java.util.ArrayList;

public interface UtilisateurDAO {
    abstract ArrayList<Utilisateur> findAll();
    abstract Utilisateur findByUser(String id);
}
