package vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controleur.*;
import modele.Modele;

public class VuePrincipale extends JFrame implements ActionListener {

    // --- Couleurs de la charte ---
    private Color vertFonce = new Color(34, 100, 50);
    private Color blanc = Color.WHITE;

    // --- Conteneurs principaux ---
    private JPanel panelMenu = new JPanel();
    private JPanel panelContenu = new JPanel();
    private CardLayout affichageCartes = new CardLayout();

    // --- Boutons du menu ---
    private JButton btAccueil = new JButton("Accueil");
    private JButton btCentres = new JButton("Centres");
    private JButton btClients = new JButton("Clients");
    private JButton btChevaux = new JButton("Chevaux");
    private JButton btEquipements = new JButton("Équipements");
    private JButton btFactures = new JButton("Factures");
    private JButton btQuitter = new JButton("Se déconnecter");

    public VuePrincipale() {
        this.setTitle("Caballio - Tableau de bord Administrateur");
        this.setBounds(100, 100, 1100, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // -----------------------------------------
        // 1. MENU LATÉRAL (GAUCHE)
        // -----------------------------------------
        this.panelMenu.setPreferredSize(new Dimension(220, 650));
        this.panelMenu.setBackground(vertFonce);
        this.panelMenu.setLayout(new GridLayout(10, 1, 10, 10));

        JLabel lblTitre = new JLabel("CABALLIO", SwingConstants.CENTER);
        lblTitre.setForeground(blanc);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 26));
        this.panelMenu.add(lblTitre);

        styliserBoutonMenu(btAccueil);
        styliserBoutonMenu(btCentres);
        styliserBoutonMenu(btClients);
        styliserBoutonMenu(btChevaux);
        styliserBoutonMenu(btEquipements);
        styliserBoutonMenu(btFactures);
        
        this.panelMenu.add(new JLabel("")); // Espace vide pour aérer
        styliserBoutonMenu(btQuitter);

        // -----------------------------------------
        // 2. ZONE DE CONTENU (CENTRE)
        // -----------------------------------------
        this.panelContenu.setLayout(affichageCartes);
        this.panelContenu.setBackground(blanc);

        // --- Création des différentes vues ---
        // Accueil
        JPanel panelAccueil = new JPanel(new BorderLayout());
        panelAccueil.setBackground(blanc);
        JLabel lblAccueil = new JLabel("Bienvenue sur l'espace d'administration Caballio", SwingConstants.CENTER);
        lblAccueil.setFont(new Font("Arial", Font.PLAIN, 22));
        lblAccueil.setForeground(vertFonce);
        panelAccueil.add(lblAccueil, BorderLayout.CENTER);

        // Ajout des panneaux au CardLayout
        this.panelContenu.add(panelAccueil, "Accueil");
        this.panelContenu.add(construirePanelCentres(), "Centres");
        this.panelContenu.add(construirePanelClients(), "Clients");
        this.panelContenu.add(construirePanelChevaux(), "Chevaux");
        this.panelContenu.add(construirePanelEquipements(), "Equipements");
        this.panelContenu.add(construirePanelFactures(), "Factures");

        // -----------------------------------------
        // 3. ASSEMBLAGE
        // -----------------------------------------
        this.add(this.panelMenu, BorderLayout.WEST);
        this.add(this.panelContenu, BorderLayout.CENTER);

        affichageCartes.show(this.panelContenu, "Accueil");
        this.setVisible(true);
    }

    // =========================================
    // MÉTHODES DE CONSTRUCTION DES DONNÉES
    // =========================================

    private JPanel construirePanelCentres() {
        String[] colonnes = {"ID", "Nom du Centre", "Adresse", "Code Postal", "Ville", "Téléphone"};
        ArrayList<Centre> liste = Modele.selectAllCentres();
        Object[][] donnees = new Object[liste.size()][colonnes.length];
        
        for (int i = 0; i < liste.size(); i++) {
            donnees[i][0] = liste.get(i).getIdCentre();
            donnees[i][1] = liste.get(i).getNomCentre();
            donnees[i][2] = liste.get(i).getAdresseCentre();
            donnees[i][3] = liste.get(i).getCodePostalCentre();
            donnees[i][4] = liste.get(i).getVilleCentre();
            donnees[i][5] = liste.get(i).getTelephoneCentre();
        }
        return creerPanelTableau("Gestion des Centres Équestres", colonnes, donnees);
    }

    private JPanel construirePanelClients() {
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Ville", "Date d'ajout"};
        ArrayList<Client> liste = Modele.selectAllClients();
        Object[][] donnees = new Object[liste.size()][colonnes.length];
        
        for (int i = 0; i < liste.size(); i++) {
            donnees[i][0] = liste.get(i).getIdClient();
            donnees[i][1] = liste.get(i).getNom();
            donnees[i][2] = liste.get(i).getPrenom();
            donnees[i][3] = liste.get(i).getTelephone();
            donnees[i][4] = liste.get(i).getVille();
            donnees[i][5] = liste.get(i).getDateAjout();
        }
        return creerPanelTableau("Gestion des Clients", colonnes, donnees);
    }

    private JPanel construirePanelChevaux() {
        String[] colonnes = {"ID", "Nom", "Sexe", "Race", "ID Centre", "ID Propriétaire"};
        ArrayList<Cheval> liste = Modele.selectAllChevaux();
        Object[][] donnees = new Object[liste.size()][colonnes.length];
        
        for (int i = 0; i < liste.size(); i++) {
            donnees[i][0] = liste.get(i).getIdCheval();
            donnees[i][1] = liste.get(i).getNom();
            donnees[i][2] = liste.get(i).getSexe();
            donnees[i][3] = liste.get(i).getRace();
            donnees[i][4] = liste.get(i).getIdCentre();
            donnees[i][5] = liste.get(i).getIdClient();
        }
        return creerPanelTableau("Gestion des Chevaux", colonnes, donnees);
    }

    private JPanel construirePanelEquipements() {
        String[] colonnes = {"ID", "Nom de l'équipement", "Date d'ajout", "ID Centre", "ID Propriétaire"};
        ArrayList<Equipement> liste = Modele.selectAllEquipements();
        Object[][] donnees = new Object[liste.size()][colonnes.length];
        
        for (int i = 0; i < liste.size(); i++) {
            donnees[i][0] = liste.get(i).getIdEquipement();
            donnees[i][1] = liste.get(i).getNomEquipement();
            donnees[i][2] = liste.get(i).getDateAjoutEquipement();
            donnees[i][3] = liste.get(i).getIdCentre();
            donnees[i][4] = liste.get(i).getIdProprietaire();
        }
        return creerPanelTableau("Gestion des Équipements", colonnes, donnees);
    }

    private JPanel construirePanelFactures() {
        String[] colonnes = {"ID Facture", "Date", "Montant Total (€)", "ID Client", "ID Centre"};
        ArrayList<Facture> liste = Modele.selectAllFactures();
        Object[][] donnees = new Object[liste.size()][colonnes.length];
        
        for (int i = 0; i < liste.size(); i++) {
            donnees[i][0] = liste.get(i).getIdFacture();
            donnees[i][1] = liste.get(i).getDateAjoutFacture();
            donnees[i][2] = liste.get(i).getMontantTotal();
            donnees[i][3] = liste.get(i).getIdClient();
            donnees[i][4] = liste.get(i).getIdCentre();
        }
        return creerPanelTableau("Historique des Factures", colonnes, donnees);
    }

    // =========================================
    // MÉTHODES UTILITAIRES (GÉNÉRATEURS D'UI)
    // =========================================

    private JPanel creerPanelTableau(String titre, String[] colonnes, Object[][] donnees) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(blanc);
        
        JLabel lblTitre = new JLabel(titre, SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitre.setForeground(vertFonce);
        panel.add(lblTitre, BorderLayout.NORTH);

        DefaultTableModel modeleTable = new DefaultTableModel(donnees, colonnes);
        JTable table = new JTable(modeleTable);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(vertFonce);
        table.getTableHeader().setForeground(blanc);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void styliserBoutonMenu(JButton bouton) {
        bouton.setBackground(blanc);
        bouton.setForeground(vertFonce);
        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.setFocusPainted(false);
        bouton.addActionListener(this);
        this.panelMenu.add(bouton);
    }

    // =========================================
    // GESTION DES CLICS BOUTONS
    // =========================================

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btAccueil) {
            affichageCartes.show(this.panelContenu, "Accueil");
        } else if (e.getSource() == btCentres) {
            affichageCartes.show(this.panelContenu, "Centres");
        } else if (e.getSource() == btClients) {
            affichageCartes.show(this.panelContenu, "Clients");
        } else if (e.getSource() == btChevaux) {
            affichageCartes.show(this.panelContenu, "Chevaux");
        } else if (e.getSource() == btEquipements) {
            affichageCartes.show(this.panelContenu, "Equipements");
        } else if (e.getSource() == btFactures) {
            affichageCartes.show(this.panelContenu, "Factures");
        } else if (e.getSource() == btQuitter) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                this.dispose(); 
                new VueConnexion(); 
            }
        }
    }
}