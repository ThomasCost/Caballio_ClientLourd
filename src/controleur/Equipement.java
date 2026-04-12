package controleur;
import java.sql.Date;

public class Equipement {
    private int idEquipement, idCentre, idProprietaire;
    private String nomEquipement;
    private Date dateAjoutEquipement;

    public Equipement(int idEquipement, String nomEquipement, Date dateAjoutEquipement, int idCentre, int idProprietaire) {
        this.idEquipement = idEquipement; this.nomEquipement = nomEquipement; 
        this.dateAjoutEquipement = dateAjoutEquipement; this.idCentre = idCentre; this.idProprietaire = idProprietaire;
    }

    public int getIdEquipement() { return idEquipement; }
    public String getNomEquipement() { return nomEquipement; }
    public Date getDateAjoutEquipement() { return dateAjoutEquipement; }
    public int getIdCentre() { return idCentre; }
    public int getIdProprietaire() { return idProprietaire; }
}