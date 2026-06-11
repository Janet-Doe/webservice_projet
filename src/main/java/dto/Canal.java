package dto;

import java.util.Date;

public class Canal {
    private int id;
    private String nom;
    private boolean is_public;
    private String id_createur;
    private Date dateCreation;
    private Date dateModification;

    public Canal(int id, String nom, boolean is_public, String id_createur, Date dateCreation, Date dateModification) {
        this.nom = nom;
        this.id = id;
        this.is_public = is_public;
        this.id_createur = id_createur;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean is_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public String getId_createur() {
        return id_createur;
    }

    public void setId_createur(String id_createur) {
        this.id_createur = id_createur;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Canal)) return false;
        Canal canal = (Canal) o;
        return id == canal.getId();
    }

}
