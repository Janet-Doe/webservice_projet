package dto;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int idCanal;
    private String idAuteur;
    private String text;
    private Timestamp dateEnvoie;
    private Timestamp lastModification;

    public Message(int id, int idCanal, String idAuteur, String text, Timestamp dateEnvoie, Timestamp lastModification) {
        this.id = id;
        this.idCanal = idCanal;
        this.idAuteur = idAuteur;
        this.text = text;
        this.dateEnvoie = dateEnvoie;
        this.lastModification = lastModification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getLastModification() {
        return lastModification;
    }

    public void setLastModification(Timestamp lastModification) {
        this.lastModification = lastModification;
    }

    public Timestamp getDateEnvoie() {
        return dateEnvoie;
    }

    public void setDateEnvoie(Timestamp dateEnvoie) {
        this.dateEnvoie = dateEnvoie;
    }

    public String getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(String idAuteur) {
        this.idAuteur = idAuteur;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(int idCanal) {
        this.idCanal = idCanal;
    }
}
