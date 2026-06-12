package dto;

import java.util.Date;

public class User {
    private String user;
    private String mdp;
    private Date dateInscription;
    private Date derniereConnexion;

    public User(String user, String mdp, Date dateInscription, Date derniereConnexion) {
        this.user = user;
        this.mdp = mdp;
        this.dateInscription = dateInscription;
        this.derniereConnexion = derniereConnexion;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Date getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(Date derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }
}
