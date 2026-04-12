package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDD {
    private String serveur = "localhost";
    private String bdd = "centre_equestre";
    private String user = "root"; 
    private String mdp = ""; // Laisse vide si tu utilises WAMP/XAMPP par défaut
    private Connection maConnexion;

    public BDD() {
        this.chargerPilote();
    }

    public BDD(String serveur, String bdd, String user, String mdp) {
        this.serveur = serveur;
        this.bdd = bdd;
        this.user = user;
        this.mdp = mdp;
        this.chargerPilote();
    }

    private void chargerPilote() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exp) {
            System.out.println("Erreur de chargement du pilote JDBC");
        }
    }

    public void seConnecter() {
        String url = "jdbc:mysql://" + this.serveur + "/" + this.bdd + "?serverTimezone=UTC";
        try {
            this.maConnexion = DriverManager.getConnection(url, this.user, this.mdp);
        } catch (SQLException exp) {
            System.out.println("Erreur de connexion à la base de données : " + this.bdd);
        }
    }

    public void seDeconnecter() {
        try {
            if (this.maConnexion != null && !this.maConnexion.isClosed()) {
                this.maConnexion.close();
            }
        } catch (SQLException exp) {
            System.out.println("Erreur de fermeture de connexion");
        }
    }

    public Connection getMaConnexion() {
        return this.maConnexion;
    }
}