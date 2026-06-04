package dto;

import java.util.Date;

public class Message {
    private int id;
    private int idCanal;
    private String idAuteur;
    private String text;
    private Date dateEnvoie;
    private Date lastModification;

    public Message(int id, int idCanal, String idAuteur, String text, Date dateEnvoie, Date lastModification) {
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

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public Date getDateEnvoie() {
        return dateEnvoie;
    }

    public void setDateEnvoie(Date dateEnvoie) {
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
