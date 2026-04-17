package modele;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

import controleur.*;

public class Modele {
    
    private static BDD uneBdd = new BDD("localhost", "centre_equestre", "root", "");

    // ========================================================================
    // 0. AUTHENTIFICATION
    // ========================================================================
    
    
 // ========================================================================
    // SÉCURITÉ : HACHAGE SHA-256 + GRAIN DE SEL
    // ========================================================================
    public static String hacherMdp(String mdpAClair) {
        try {

            String sel = "Caballio_Secret_2026!"; 
            String mdpAvecSel = mdpAClair + sel;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(mdpAvecSel.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", ex);
        }
    }
    
    
    public static Utilisateur verifierConnexion(String email, String mdp) {
        Utilisateur unUtilisateur = null;
        String requete = "SELECT * FROM utilisateur WHERE MailUtilisateur = ? AND MdpUtilisateur = ? AND BloqueUtilisateur = 0";
        
        try {
            String mdpHache = hacherMdp(mdp); 
            
            uneBdd.seConnecter();
            Connection maConnexion = uneBdd.getMaConnexion();
            PreparedStatement unStat = maConnexion.prepareStatement(requete);
            unStat.setString(1, email);
            unStat.setString(2, mdpHache);
            ResultSet unRes = unStat.executeQuery();
            
            if (unRes.next()) {
                unUtilisateur = new Utilisateur(
                    unRes.getInt("IdUtilisateur"), unRes.getString("PseudonymeUtilisateur"),
                    unRes.getString("MailUtilisateur"), unRes.getString("MdpUtilisateur"),
                    unRes.getBoolean("ActiviteUtilisateur"), unRes.getBoolean("BloqueUtilisateur"), 
                    unRes.getString("RolesUtilisateur"), unRes.getBoolean("ExpireUtilisateur"), 
                    unRes.getString("PrenomUtilisateur"), unRes.getString("NomUtilisateur"), 
                    unRes.getString("SexeUtilisateur"), unRes.getString("AdresseUtilisateur"), 
                    unRes.getString("VilleUtilisateur"), unRes.getString("CodePostalUtilisateur")
                );
            }
            unStat.close();
            unRes.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur Connexion : " + exp.getMessage());
        }
        return unUtilisateur;
    }

    // ========================================================================
    // 1. LES SELECTIONS (SELECT ALL) POUR CHAQUE TABLE
    // ========================================================================

    public static ArrayList<Centre> selectAllCentres() {
        ArrayList<Centre> lesCentres = new ArrayList<>();
        String requete = "SELECT * FROM centre;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesCentres.add(new Centre(unRes.getInt("IdCentre"), unRes.getString("NomCentre"), 
                    unRes.getString("AdresseCentre"), unRes.getString("CodePostalCentre"), 
                    unRes.getString("VilleCentre"), unRes.getString("TelephoneCentre"), 
                    unRes.getDate("DateAjoutCentre"), unRes.getInt("IdUtilisateur")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Centres : " + exp.getMessage()); }
        return lesCentres;
    }

    public static ArrayList<Client> selectAllClients() {
        ArrayList<Client> lesClients = new ArrayList<>();
        String requete = "SELECT * FROM client;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesClients.add(new Client(unRes.getInt("IdClient"), unRes.getString("NomClient"), 
                    unRes.getString("PrenomClient"), unRes.getString("AdresseClient"), 
                    unRes.getString("CodePostalClient"), unRes.getString("TelephoneClient"), 
                    unRes.getString("VilleClient"), unRes.getDate("DateAjoutClient")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Clients : " + exp.getMessage()); }
        return lesClients;
    }

    public static ArrayList<Cheval> selectAllChevaux() {
        ArrayList<Cheval> lesChevaux = new ArrayList<>();
        String requete = "SELECT * FROM cheval;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesChevaux.add(new Cheval(unRes.getInt("IdCheval"), unRes.getString("NomCheval"), 
                    unRes.getString("SexeCheval"), unRes.getString("RaceCheval"), 
                    unRes.getDate("DateAjoutCheval"), unRes.getInt("IdCentre"), unRes.getInt("IdClient")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Chevaux : " + exp.getMessage()); }
        return lesChevaux;
    }

    public static ArrayList<Pature> selectAllPatures() {
        ArrayList<Pature> lesPatures = new ArrayList<>();
        String requete = "SELECT * FROM pature;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesPatures.add(new Pature(unRes.getInt("IdPature"), unRes.getString("NomPature"), 
                    unRes.getFloat("TaillePature"), unRes.getDate("DateAjoutPature"), unRes.getInt("IdCentre")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Patures : " + exp.getMessage()); }
        return lesPatures;
    }

    public static ArrayList<TypeLogement> selectAllTypeLogements() {
        ArrayList<TypeLogement> lesTL = new ArrayList<>();
        String requete = "SELECT * FROM type_logement;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesTL.add(new TypeLogement(unRes.getInt("IdTL"), unRes.getString("NomTL"), 
                    unRes.getFloat("TailleTL"), unRes.getFloat("PrixTL"), 
                    unRes.getDate("DateAjoutTL"), unRes.getInt("IdCentre")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT TypeLogement : " + exp.getMessage()); }
        return lesTL;
    }

    public static ArrayList<Box> selectAllBox() {
        ArrayList<Box> lesBox = new ArrayList<>();
        String requete = "SELECT * FROM box;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesBox.add(new Box(unRes.getInt("IdBox"), unRes.getInt("NumBox"), 
                    unRes.getDate("DateAjoutBox"), unRes.getInt("IdCheval"), 
                    unRes.getInt("IdTL"), unRes.getInt("IdCentre")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Box : " + exp.getMessage()); }
        return lesBox;
    }

    public static ArrayList<Equipement> selectAllEquipements() {
        ArrayList<Equipement> lesEquipements = new ArrayList<>();
        String requete = "SELECT * FROM equipement;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesEquipements.add(new Equipement(unRes.getInt("IdEquipement"), unRes.getString("NomEquipement"), 
                    unRes.getDate("DateAjoutEquipement"), unRes.getInt("IdCentre"), unRes.getInt("IdProprietaire")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Equipements : " + exp.getMessage()); }
        return lesEquipements;
    }

    public static ArrayList<Aliment> selectAllAliments() {
        ArrayList<Aliment> lesAliments = new ArrayList<>();
        String requete = "SELECT * FROM aliment;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesAliments.add(new Aliment(unRes.getInt("IdAliment"), unRes.getString("NomAliment"), unRes.getInt("IdCentre")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Aliments : " + exp.getMessage()); }
        return lesAliments;
    }

    public static ArrayList<Facture> selectAllFactures() {
        ArrayList<Facture> lesFactures = new ArrayList<>();
        String requete = "SELECT * FROM facture;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesFactures.add(new Facture(unRes.getInt("IdFacture"), unRes.getDate("DateBail"), 
                    unRes.getFloat("MontantTotal"), unRes.getDate("DateAjoutFacture"), 
                    unRes.getInt("IdClient"), unRes.getInt("IdCheval"), unRes.getInt("IdTL"), unRes.getInt("IdCentre")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Factures : " + exp.getMessage()); }
        return lesFactures;
    }

    public static ArrayList<Supplement> selectAllSupplements() {
        ArrayList<Supplement> lesSupplements = new ArrayList<>();
        String requete = "SELECT * FROM supplement;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesSupplements.add(new Supplement(unRes.getInt("IdSupplement"), unRes.getString("NomSupplement"), 
                    unRes.getFloat("PrixSupplement"), unRes.getInt("IdFacture")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Supplements : " + exp.getMessage()); }
        return lesSupplements;
    }

    public static ArrayList<TypePaiement> selectAllTypePaiements() {
        ArrayList<TypePaiement> lesTP = new ArrayList<>();
        String requete = "SELECT * FROM type_paiement;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesTP.add(new TypePaiement(unRes.getInt("IdTP"), unRes.getString("NomTP")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT TypePaiement : " + exp.getMessage()); }
        return lesTP;
    }

    public static ArrayList<Paiement> selectAllPaiements() {
        ArrayList<Paiement> lesPaiements = new ArrayList<>();
        String requete = "SELECT * FROM paiement;";
        try {
            uneBdd.seConnecter();
            Statement unStat = uneBdd.getMaConnexion().createStatement();
            ResultSet unRes = unStat.executeQuery(requete);
            while (unRes.next()) {
                lesPaiements.add(new Paiement(unRes.getInt("IdPaiement"), unRes.getDate("DatePaiement"), 
                    unRes.getDate("DateEncaissement"), unRes.getFloat("Montant"), 
                    unRes.getInt("IdFacture"), unRes.getInt("IdTP")));
            }
            unStat.close(); unRes.close(); uneBdd.seDeconnecter();
        } catch (SQLException exp) { System.out.println("Erreur SELECT Paiements : " + exp.getMessage()); }
        return lesPaiements;
    }
    
 // ========================================================================
    // 2. LE CRUD (CREATE, UPDATE, DELETE) - ENTITÉ CLIENT
    // ========================================================================

    public static void insertClient(Client unClient) {
        String requete = "INSERT INTO client (NomClient, PrenomClient, AdresseClient, CodePostalClient, TelephoneClient, VilleClient, DateAjoutClient) VALUES (?, ?, ?, ?, ?, ?, CURDATE());";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setString(1, unClient.getNom());
            unStat.setString(2, unClient.getPrenom());
            unStat.setString(3, unClient.getAdresse());
            unStat.setString(4, unClient.getCodePostal());
            unStat.setString(5, unClient.getTelephone());
            unStat.setString(6, unClient.getVille());
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur INSERT Client : " + exp.getMessage());
        }
    }

    public static void updateClient(Client unClient) {
        String requete = "UPDATE client SET NomClient = ?, PrenomClient = ?, AdresseClient = ?, CodePostalClient = ?, TelephoneClient = ?, VilleClient = ? WHERE IdClient = ?;";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setString(1, unClient.getNom());
            unStat.setString(2, unClient.getPrenom());
            unStat.setString(3, unClient.getAdresse());
            unStat.setString(4, unClient.getCodePostal());
            unStat.setString(5, unClient.getTelephone());
            unStat.setString(6, unClient.getVille());
            unStat.setInt(7, unClient.getIdClient()); 
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur UPDATE Client : " + exp.getMessage());
        }
    }

    public static void deleteClient(int idClient) {
        String requete = "DELETE FROM client WHERE IdClient = ?;";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setInt(1, idClient);
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur DELETE Client : " + exp.getMessage());
        }
    }
    
 // ========================================================================
    // 3. LE CRUD (CREATE, UPDATE, DELETE) - ENTITÉ CHEVAL
    // ========================================================================

    public static void insertCheval(Cheval unCheval) {
        String requete = "INSERT INTO cheval (NomCheval, SexeCheval, RaceCheval, IdCentre, IdClient) VALUES (?, ?, ?, ?, ?);";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setString(1, unCheval.getNom());
            unStat.setString(2, unCheval.getSexe());
            unStat.setString(3, unCheval.getRace());
            unStat.setInt(4, unCheval.getIdCentre()); 
            unStat.setInt(5, unCheval.getIdClient()); 
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur INSERT Cheval : " + exp.getMessage());
        }
    }

    public static void updateCheval(Cheval unCheval) {
        String requete = "UPDATE cheval SET NomCheval = ?, SexeCheval = ?, RaceCheval = ?, IdCentre = ?, IdClient = ? WHERE IdCheval = ?;";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setString(1, unCheval.getNom());
            unStat.setString(2, unCheval.getSexe());
            unStat.setString(3, unCheval.getRace());
            unStat.setInt(4, unCheval.getIdCentre());
            unStat.setInt(5, unCheval.getIdClient());
            unStat.setInt(6, unCheval.getIdCheval()); 
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur UPDATE Cheval : " + exp.getMessage());
        }
    }

    public static void deleteCheval(int idCheval) {
        String requete = "DELETE FROM cheval WHERE IdCheval = ?;";
        try {
            uneBdd.seConnecter();
            PreparedStatement unStat = uneBdd.getMaConnexion().prepareStatement(requete);
            unStat.setInt(1, idCheval);
            
            unStat.executeUpdate(); 
            
            unStat.close();
            uneBdd.seDeconnecter();
        } catch (SQLException exp) {
            System.out.println("Erreur DELETE Cheval : " + exp.getMessage());
        }
    }
}