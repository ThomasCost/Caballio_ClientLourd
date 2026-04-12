package controleur;
import java.sql.Date;

public class Cheval {
    private int idCheval, idCentre, idClient;
    private String nom, sexe, race;
    private Date dateAjout;

    public Cheval(int idCheval, String nom, String sexe, String race, Date dateAjout, int idCentre, int idClient) {
        this.idCheval = idCheval; this.nom = nom; this.sexe = sexe; this.race = race;
        this.dateAjout = dateAjout; this.idCentre = idCentre; this.idClient = idClient;
    }

    public Cheval(String nom, String sexe, String race, int idCentre, int idClient) {
        this.idCheval = 0; this.nom = nom; this.sexe = sexe; this.race = race;
        this.idCentre = idCentre; this.idClient = idClient;
    }

    public int getIdCheval() { return idCheval; }
    public String getNom() { return nom; }
    public String getSexe() { return sexe; }
    public String getRace() { return race; }
    public Date getDateAjout() { return dateAjout; }
    public int getIdCentre() { return idCentre; }
    public int getIdClient() { return idClient; }
}