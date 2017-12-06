/*
 * @author Bosznai et Lopes
 */

package test.modele.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import modele.dao.DaoVille;
import modele.dao.Jdbc;
import modele.metier.Ville;

public class TestDaoVille { 
    public static void main(String[] args) {
        java.sql.Connection cnx = null;
        try {
            test0_Connexion();
            System.out.println("Test0 effectué : connexion\n");
            test1_SelectVille("44450");
            System.out.println("Test1 effectué : sélection des villes\n");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de pilote JDBC : " + e);
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e);
        } finally {
            try {
                if (cnx != null) {
                    cnx.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture de la connexion JDBC : " + e);
            }
        }
    }
    
    /**
     * Vérifie qu'une connexion peut être ouverte sur le SGBD
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void test0_Connexion() throws ClassNotFoundException, SQLException {
        Jdbc.creer("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:", "@localhost:1521:XE", "", "btssio", "btssio");
        Jdbc.getInstance().connecter();
        Connection cnx = Jdbc.getInstance().getConnexion();
    }

    /**
     * Affiche les villes d'après un codePostale
     * @throws SQLException
     */
    public static void test1_SelectVille(String codePostal) throws SQLException {
        List<Ville> lesVilles = DaoVille.selectVilles(codePostal);
        System.out.println("Villes : "+lesVilles.toString());
    }
}