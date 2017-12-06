/*
 * Ajoute, supprime et modifie des adresses
 * 
 * @author Bosznai et Lopes
 * @version 5
 * Date de la version: 18/05/2017
 * Note de version:
 * - Fusion de la mission 3 et 4
 * - Corection de beug
 */

package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modele.dao.DaoAdresse;
import modele.dao.DaoVille;
import modele.dao.Jdbc;
import modele.metier.Adresse;
import modele.metier.Ville;

public class VueAdresse_M5 extends javax.swing.JFrame {

    private int etat; // 1 affichage, 2 ajout, 3 suppression, 4 modification, 
    // 5 recherche, possibilité d'utiliser enum

    private Adresse adresseCourante;    // l'adresse courante
    
    private List<Adresse> adressesTotale; //Nouvelle liste pour lié vue et DaoAdresse

    private Ecouteur ecouteur;
    
    private int compteur; //l'itérateur pour parcourir la liste
    
    private Ville villeCourante;

    /**
     * Creates new form VueAdresse
     */
    public VueAdresse_M5() {
        // initialisation des composants grahiques de l'interface
        initComponents();
        
        compteur = 0;
        
        // instanciation d'un listener
        ecouteur = new Ecouteur();
        // ajout du listener au différents contrôles écoutés
        jButtonRechercher.addActionListener(ecouteur);
        jButtonAjouter.addActionListener(ecouteur);
        jButtonModifier.addActionListener(ecouteur);
        jButtonSupprimer.addActionListener(ecouteur);
        jButtonValider.addActionListener(ecouteur);
        jButtonAnnuler.addActionListener(ecouteur);
        jButtonQuitter.addActionListener(ecouteur);
        jButtonFlecheD.addActionListener(ecouteur);
        jButtonFlecheG.addActionListener(ecouteur);
        jButtonOrderVille.addActionListener(ecouteur);
        jButtonOrderInsert.addActionListener(ecouteur);
        jButtonOrderID.addActionListener(ecouteur);
        jButtonChoisirVille.addActionListener(ecouteur);
        jTextFieldId.addKeyListener(ecouteur);

        // création du singleton Jdbc
        Jdbc.creer("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:", "@localhost:1521:XE", "", "btssio", "btssio");
        // passage en mode affichage
        modeAffichage();

        afficherPremiereAdresse();         

        try {
            Jdbc.getInstance().connecter();
            adressesTotale = new ArrayList(DaoAdresse.selectAllbyId());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Pilote absent");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Echec de connexion");
        }        
    }      
    /* Affiche la première adresse
    */
    
    private void afficherPremiereAdresse() {
        // affichage de la première adresse
        
        try {
            Jdbc.getInstance().connecter();
            adresseCourante = DaoAdresse.selectFirst();
            afficherAdresse(adresseCourante);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Pilote absent");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Echec de connexion");
        }  
    }
        
    
    
    private void afficherAdresseVille() {
        // affichage des adresses dans l'ordre des villes
        try {
            adressesTotale = new ArrayList(DaoAdresse.selectAllbyVilles());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "pilote absent");
        }
    }
    
    private void afficherAdresseID() {
        try {
            adressesTotale = new ArrayList(DaoAdresse.selectAllbyId());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "pilote absent");
        }
    }
    
    private void afficherAdresseInsert() {
        try {
            adressesTotale = new ArrayList(DaoAdresse.selectAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "pilote absent");
        }
    }
   

    /**
     * Fixe les contrôles actifs en mode Affichage. les zones de saisie sont
     * actives les boutons Ajouter, Modifier, supprimer sont actifs les boutons
     * Annuler et Valider sont inactifs le bouton Quitter est toujours actif
     * etat vaut 1
     */
    private void modeAffichage() {
        etat = 1;
        jTextFieldId.setEnabled(false);
        jTextFieldRue.setEnabled(false);
        jTextFieldCdp.setEnabled(false);
        jTextFieldVille.setEnabled(false);
        jButtonAjouter.setEnabled(true);
        jButtonModifier.setEnabled(true);
        jButtonSupprimer.setEnabled(true);
        jButtonRechercher.setEnabled(true);
        jButtonAnnuler.setEnabled(false);
        jButtonValider.setEnabled(false);
        jButtonQuitter.setEnabled(true);
        jButtonFlecheD.setEnabled(true);
        jButtonFlecheG.setEnabled(true);
        jButtonChoisirVille.setEnabled(false);
    }

    /**
     * Fixe les contrôles actifs en mode Ajout. les zones de saisie sont actives
     * les boutons Ajouter, Modifier, supprimer sont inactifs les boutons
     * Annuler et Valider sont actifs le bouton Quitter est toujours actif etat
     * vaut 2
     */
    private void modeAjout() {
        etat = 2;
        jTextFieldId.setEnabled(true);
        jTextFieldRue.setEnabled(true);
        jTextFieldCdp.setEnabled(false);
        jTextFieldVille.setEnabled(false);
        jButtonAjouter.setEnabled(false);
        jButtonModifier.setEnabled(false);
        jButtonSupprimer.setEnabled(false);
        jButtonRechercher.setEnabled(false);
        jButtonAnnuler.setEnabled(true);
        jButtonValider.setEnabled(true);
        jButtonQuitter.setEnabled(true);
        jButtonFlecheD.setEnabled(true);
        jButtonFlecheG.setEnabled(true);
        jButtonChoisirVille.setEnabled(true);
    }

    /**
     * Fixe les contrôles actifs en mode Suppression. les zones de saisie sont
     * inactives les boutons Ajouter, Modifier, supprimer sont inactifs les
     * boutons Annuler et Valider sont inactifs le bouton Quitter est toujours
     * actif etat vaut 3
     */
    private void modeSuppression() {
        etat = 3;
        jTextFieldId.setEnabled(false);
        jTextFieldRue.setEnabled(false);
        jTextFieldCdp.setEnabled(false);
        jTextFieldVille.setEnabled(false);
        jButtonAjouter.setEnabled(false);
        jButtonModifier.setEnabled(false);
        jButtonSupprimer.setEnabled(false);
        jButtonRechercher.setEnabled(false);
        jButtonAnnuler.setEnabled(false);
        jButtonValider.setEnabled(false);
        jButtonQuitter.setEnabled(true);
        jButtonFlecheD.setEnabled(true);
        jButtonFlecheG.setEnabled(true);
        jButtonChoisirVille.setEnabled(false);
    }

    /**
     * Fixe les contrôles actifs en mode Modification. les zones de saisie sont
     * inactives, sauf celle de l'id les boutons Ajouter, Modifier, supprimer
     * sont inactifs les boutons Annuler et Valider sont actifs le bouton
     * Quitter est toujours actif etat vaut 4
     */
    private void modeModification() {
        etat = 4;
        jTextFieldId.setEnabled(false);
        jTextFieldRue.setEnabled(true);
        jTextFieldCdp.setEnabled(false);
        jTextFieldVille.setEnabled(false);
        jButtonAjouter.setEnabled(false);
        jButtonModifier.setEnabled(false);
        jButtonSupprimer.setEnabled(false);
        jButtonRechercher.setEnabled(false);
        jButtonAnnuler.setEnabled(true);
        jButtonValider.setEnabled(true);
        jButtonQuitter.setEnabled(true);
        jButtonFlecheD.setEnabled(true);
        jButtonFlecheG.setEnabled(true);
        jButtonChoisirVille.setEnabled(true);
    }

    /**
     * Fixe les contrôles actifs en mode Recherche. La zone de saisie de l'id
     * est active, les autres inactives les boutons Ajouter, Modifier, supprimer
     * sont inactifs les boutons Annuler et Valider sont actifs le bouton
     * Quitter est toujours actif etat vaut 5
     */
    private void modeRecherche() {
        etat = 5;
        jTextFieldId.setEnabled(true);
        jTextFieldRue.setEnabled(false);
        jTextFieldCdp.setEnabled(false);
        jTextFieldVille.setEnabled(false);
        jButtonAjouter.setEnabled(false);
        jButtonModifier.setEnabled(false);
        jButtonSupprimer.setEnabled(false);
        jButtonRechercher.setEnabled(false);
        jButtonAnnuler.setEnabled(true);
        jButtonValider.setEnabled(true);
        jButtonQuitter.setEnabled(true);
        jButtonFlecheD.setEnabled(true);
        jButtonFlecheG.setEnabled(true);
        jButtonChoisirVille.setEnabled(false);
    }

    /**
     * Parcour la liste des adresse pour afficher la suivant
     *
     * @param l'adresse à afficher
     */
    
    /**
     * Affiche dans l'interface une adresse passée en paramètre.
     *
     * @param l'adresse à afficher
     */
    private void afficherAdresse(Adresse uneAdresse) {
        this.jTextFieldId.setText(Integer.toString(uneAdresse.getId()));
        this.jTextFieldRue.setText(uneAdresse.getRue());
        this.jTextFieldCdp.setText(uneAdresse.getCp());
        this.jTextFieldVille.setText(uneAdresse.getVille());
    }

    /**
     * Remet à vide les zones de saisie.
     */
    private void effacerAdresse() {
        this.jTextFieldId.setText("");
        this.jTextFieldRue.setText("");
        this.jTextFieldCdp.setText("");
        this.jTextFieldVille.setText("");
    }

    
    private void fenetreSelectionner() {

        String codePostal = (String) JOptionPane.showInputDialog(null, "Saisissez un code postal", "Code postal",
                JOptionPane.PLAIN_MESSAGE);

        List<Ville> lesVilles = new ArrayList<>();
        boolean erreurSaisie = false;

        //On vérifie que le code postal n'est pas vide
        if (codePostal != null && !codePostal.equals("")) {
            try {
                Jdbc.getInstance().connecter();
                lesVilles = DaoVille.selectVilles(codePostal);
                //On vérifie que la liste est supérieure à 0
                if (lesVilles.size() > 0) {
                    Object[] possibilities = lesVilles.toArray();

                    //On affiche les possibilitées liés au code postal
                    Ville laVilleChoisie = (Ville) JOptionPane.showInputDialog(
                            null,
                            "Liste des Villes",
                            "Ville",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            possibilities,
                            "");
                    
                    //On évite les erreurs quand on annule la sélection de la ville
                    if (laVilleChoisie != null) {
                        jTextFieldCdp.setText(laVilleChoisie.getCp());
                        jTextFieldVille.setText(laVilleChoisie.getVille());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Le Code Postal n'existe pas dans la base de données");
                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Pilote BDD absent");
                System.exit(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Problème avec la Base de données");
            } catch (NumberFormatException ex) {
                erreurSaisie = true;
                JOptionPane.showMessageDialog(this, "il faut saisir un entier dans l'dentifiant");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Le champ code postal est vide");
            jTextFieldId.requestFocus();
        }
        if (erreurSaisie) {
            jTextFieldId.requestFocus();
            jTextFieldId.selectAll();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldRue = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldCdp = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldVille = new javax.swing.JTextField();
        jButtonAjouter = new javax.swing.JButton();
        jButtonSupprimer = new javax.swing.JButton();
        jButtonRechercher = new javax.swing.JButton();
        jButtonModifier = new javax.swing.JButton();
        jButtonValider = new javax.swing.JButton();
        jButtonAnnuler = new javax.swing.JButton();
        jButtonQuitter = new javax.swing.JToggleButton();
        jButtonFlecheD = new javax.swing.JToggleButton();
        jButtonFlecheG = new javax.swing.JToggleButton();
        jButtonOrderVille = new javax.swing.JToggleButton();
        jLabelOrderBy = new javax.swing.JLabel();
        jButtonOrderID = new javax.swing.JToggleButton();
        jButtonOrderInsert = new javax.swing.JToggleButton();
        jButtonChoisirVille = new javax.swing.JButton();
        jLabelOrderBy1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion des adresses version M2");

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gestion des adresses Mission 5");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Id : ");
        jLabel2.setToolTipText("");

        jTextFieldId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldIdKeyPressed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Rue : ");
        jLabel3.setToolTipText("");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Cdp :");
        jLabel4.setToolTipText("");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ville : ");
        jLabel5.setToolTipText("");

        jButtonAjouter.setText("Ajouter");

        jButtonSupprimer.setText("Supprimer");

        jButtonRechercher.setText("Rechercher");

        jButtonModifier.setText("Modifier");

        jButtonValider.setText("Valider");

        jButtonAnnuler.setText("Annuler");

        jButtonQuitter.setText("Quitter");
        jButtonQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitterActionPerformed(evt);
            }
        });

        jButtonFlecheD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vue/images/fleche_droite.png"))); // NOI18N

        jButtonFlecheG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vue/images/fleche_gauche.png"))); // NOI18N

        jButtonOrderVille.setText("Ville");
        jButtonOrderVille.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderVilleActionPerformed(evt);
            }
        });

        jLabelOrderBy.setText("Organiser la liste dans l'ordre :");

        jButtonOrderID.setText("ID");

        jButtonOrderInsert.setText("Insertion");

        jButtonChoisirVille.setText("Choisir la ville");

        jLabelOrderBy1.setText("Choisir la ville avec le code postal:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButtonFlecheG, javax.swing.GroupLayout.PREFERRED_SIZE, 59, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonAjouter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSupprimer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonModifier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonAnnuler, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonValider, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRechercher)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButtonQuitter, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonFlecheD)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3))
                                .addComponent(jLabel4))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldVille, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(jTextFieldCdp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldRue, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldId, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(jButtonChoisirVille))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabelOrderBy1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelOrderBy)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButtonOrderVille)
                                        .addGap(12, 12, 12)
                                        .addComponent(jButtonOrderID)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonOrderInsert))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelOrderBy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldRue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOrderVille)
                    .addComponent(jButtonOrderID)
                    .addComponent(jButtonOrderInsert))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldCdp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelOrderBy1)
                        .addGap(3, 3, 3)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldVille, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonChoisirVille))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAjouter)
                            .addComponent(jButtonSupprimer)
                            .addComponent(jButtonRechercher)
                            .addComponent(jButtonModifier))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonValider)
                            .addComponent(jButtonAnnuler)
                            .addComponent(jButtonQuitter)))
                    .addComponent(jButtonFlecheG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonFlecheD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitterActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonQuitterActionPerformed

    private void jTextFieldIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (etat) {
                case 5:
                    recherche();
                    break;
                default:
            }
        }
    }//GEN-LAST:event_jTextFieldIdKeyPressed

    private void jButtonOrderVilleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderVilleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonOrderVilleActionPerformed

    private void recherche() {
        boolean erreurSaisie = false;
        Adresse cetteAdresse = null;
        if (!jTextFieldId.getText().equals("")) {
            try {
                int idAdresse = Integer.valueOf(jTextFieldId.getText());
                Jdbc.getInstance().connecter();
                cetteAdresse = DaoAdresse.selectOne(idAdresse);
                if (cetteAdresse != null) {
                    adresseCourante = cetteAdresse;
                    afficherAdresse(adresseCourante);
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiant inconnu");
                    jTextFieldId.selectAll();
                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Pilote BDD absent");
                System.exit(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Problème avec la Base de données");
            } catch (NumberFormatException ex) {
                erreurSaisie = true;
                JOptionPane.showMessageDialog(this, "il faut saisir un entier dans l'dentifiant");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Le champ Id est vide");
            jTextFieldId.requestFocus();
        }
        if (erreurSaisie) {
            jTextFieldId.requestFocus();
            jTextFieldId.selectAll();
        } else {
            modeAffichage();
        }
    }

    private void modification() {
        Adresse cetteAdresse = null;
        int idAdresse = Integer.valueOf(jTextFieldId.getText());
        String rueAdresse = this.jTextFieldRue.getText();
        String cdpAdresse = this.jTextFieldCdp.getText();
        String villeAdresse = this.jTextFieldVille.getText();
        cetteAdresse = new Adresse(idAdresse, rueAdresse, cdpAdresse, villeAdresse);
        try {
            Jdbc.getInstance().connecter();
            DaoAdresse.update(idAdresse, cetteAdresse);
            JOptionPane.showMessageDialog(this, "Modification effectuée");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Echec de la mise à jour de la base de données");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Pilote BDD absent");
            System.exit(0);
        }
        modeAffichage();
    }

    private void ajout() {
        boolean erreurBDD = false;
        boolean erreurSaisie = false;
        if (!jTextFieldId.getText().equals("")) {
            try {
                int idAdresse = Integer.valueOf(jTextFieldId.getText());
                Adresse cetteAdresse;
                try {
                    Jdbc.getInstance().connecter();
                    // on vérifie q'une adresse de même id n'existe pas dans la BDD
                    cetteAdresse = DaoAdresse.selectOne(idAdresse);
                    // l'adresse a été ajoutée à la BDD, elle devient l'adresse courante
                    // et on l'affiche
                    if (cetteAdresse == null) {
                        cetteAdresse = new Adresse(idAdresse, jTextFieldRue.getText(), jTextFieldCdp.getText(), jTextFieldVille.getText());
                        DaoAdresse.insert(idAdresse, cetteAdresse);
                        adresseCourante = cetteAdresse;
                        afficherAdresse(adresseCourante);
                        JOptionPane.showMessageDialog(this, "Ajout effectué");
                    } else {
                        erreurSaisie = true;
                        JOptionPane.showMessageDialog(this, "L'identifiant existe déjà");
                    }
                } catch (SQLException ex) {
                    erreurBDD = true;
                    JOptionPane.showMessageDialog(this, "Echec de l'ajout dans la base de données");
                } catch (ClassNotFoundException ex) {
                    erreurBDD = true;
                    JOptionPane.showMessageDialog(this, "Pilote BDD absent");
                    System.exit(0);
                }
            } catch (NumberFormatException ex) {
                erreurSaisie = true;
                JOptionPane.showMessageDialog(this, "il faut saisir un entier dans l'dentifiant");
            }
        } else {
            erreurSaisie = true;
            JOptionPane.showMessageDialog(this, "L'identifiant ne peut pas être vide");
        }
        // s'il y a eu une erreur avec la BDD, on réaffiche la première adresse.
        if (erreurBDD) {
            afficherPremiereAdresse();
            // s'il y a une erreur de saisie sur l'id, on place le cursuer sur la zone de saisie de l'id
        } else if (erreurSaisie) {
            jTextFieldId.requestFocus();
            jTextFieldId.selectAll();
        } else {
            modeAffichage();
        }
    }

    private void suppression() {
        int idAdresse = Integer.valueOf(jTextFieldId.getText());
        if (!jTextFieldId.getText().equals("")) {
            Adresse cetteAdresse;
            try {
                Jdbc.getInstance().connecter();
                cetteAdresse = DaoAdresse.selectOne(idAdresse);
                if (cetteAdresse != null) {
                    int rep = JOptionPane.showConfirmDialog(this, "Etes-vous sûr(e) de vouloir supprimer l'adresse d'id " + idAdresse + " ?", "Suppression", JOptionPane.YES_NO_OPTION);
                    if (rep == JOptionPane.YES_OPTION) {
                        DaoAdresse.delete(idAdresse);
                        effacerAdresse();
                        JOptionPane.showMessageDialog(this, "Suppression effectuée");
                        adresseCourante = DaoAdresse.selectFirst();
                        afficherAdresse(adresseCourante);
                    }
                } else {
                    effacerAdresse();
                    JOptionPane.showMessageDialog(this, "Identifiant inconnu");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Echec de la suppression dans la BDD");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Pilote absent");
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Le champ Id est vide");
        }
        modeAffichage();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VueAdresse_M5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VueAdresse_M5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VueAdresse_M5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VueAdresse_M5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VueAdresse_M5().setVisible(true);
            }
        });
    }

    private class Ecouteur implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jButtonRechercher) {
                modeRecherche();
                jTextFieldId.requestFocus();
                jTextFieldId.selectAll();
                effacerAdresse();
            } else if (e.getSource() == jButtonModifier) {
                modeModification();
                jTextFieldRue.requestFocus();
            } else if (e.getSource() == jButtonAjouter) {
                modeAjout();
                effacerAdresse();
                jTextFieldId.requestFocus();
            } else if (e.getSource() == jButtonSupprimer) {
                modeSuppression();
                suppression();
            } else if (e.getSource() == jButtonValider) {
                switch (etat) {
                    case 1:
                        break;
                    case 2:
                        ajout();
                        break;
                    case 4:
                        modification();
                        break;
                    case 5:
                        recherche();
                        break;
                    default:
                }
            } else if (e.getSource() == jButtonAnnuler) {
                switch (etat) {
                    case 1:
                        break;
                    case 2:     // annulation ajout
                    case 5:     // annulation recherche
                        afficherPremiereAdresse();
                        modeAffichage();
                        break;
                    case 4:     // annulation modification
                        afficherAdresse(adresseCourante);
                        modeAffichage();
                        break;
                    default:
                }
            } else if (e.getSource() == jButtonQuitter) {
                System.exit(0);
            } else if (e.getSource() == jButtonChoisirVille) {
                fenetreSelectionner();
            } else if (e.getSource() == jButtonFlecheD) {   
                compteur++;
                if (compteur >= adressesTotale.size()-1) {
                    compteur=0;
                }
                adresseCourante = adressesTotale.get(compteur);
                afficherAdresse(adresseCourante);
                
            } else if (e.getSource() == jButtonFlecheG) {
                compteur--;
                if (compteur < 0) {
                    compteur=adressesTotale.size()-1;
                }
                adresseCourante = adressesTotale.get(compteur);
                afficherAdresse(adresseCourante);
                
            } else if (e.getSource() == jButtonOrderVille) {               
                afficherAdresseVille();
                compteur = 0;
                afficherAdresse(adressesTotale.get(compteur)); 
                
            } else if (e.getSource() == jButtonOrderID) {               
                afficherAdresseID();
                compteur = 0;
                afficherAdresse(adressesTotale.get(compteur));     
                
            } else if (e.getSource() == jButtonOrderInsert) {               
                afficherAdresseInsert();
                compteur = 0;
                afficherAdresse(adressesTotale.get(compteur));               
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                switch (etat) {
                    case 5:
                        recherche();
                        break;
                    default:
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAjouter;
    private javax.swing.JButton jButtonAnnuler;
    private javax.swing.JButton jButtonChoisirVille;
    private javax.swing.JToggleButton jButtonFlecheD;
    private javax.swing.JToggleButton jButtonFlecheG;
    private javax.swing.JButton jButtonModifier;
    private javax.swing.JToggleButton jButtonOrderID;
    private javax.swing.JToggleButton jButtonOrderInsert;
    private javax.swing.JToggleButton jButtonOrderVille;
    private javax.swing.JToggleButton jButtonQuitter;
    private javax.swing.JButton jButtonRechercher;
    private javax.swing.JButton jButtonSupprimer;
    private javax.swing.JButton jButtonValider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelOrderBy;
    private javax.swing.JLabel jLabelOrderBy1;
    private javax.swing.JTextField jTextFieldCdp;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldRue;
    private javax.swing.JTextField jTextFieldVille;
    // End of variables declaration//GEN-END:variables
}