/*
 * @author Bosznai et Lopes
 */

package modele.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modele.metier.Ville;

public class DaoVille {
    
    public static List<Ville> selectVilles(String codePostal) throws SQLException {
        
        List<Ville> lesVilles = new ArrayList<>();
        Ville uneVille = null;
        ResultSet rs;
        PreparedStatement pstmt;
        Jdbc jdbc = Jdbc.getInstance();
        
        String requete = "SELECT * FROM VILLES_FRANCE WHERE VILLE_CODE_POSTAL LIKE ? ORDER BY VILLE_NOM";
        pstmt = jdbc.getConnexion().prepareStatement(requete);
        //% permet de prendre dans la requête tout ce qu'il y a après par exemple 44% 
        //Prend tous les cp commençant par 44
        pstmt.setString(1, codePostal+'%');
        rs = pstmt.executeQuery();
        
        while(rs.next()) {
            int id = rs.getInt("VILLE_ID");
            String ville = rs.getString("VILLE_NOM");
            String cp = rs.getString("VILLE_CODE_POSTAL");
            uneVille = new Ville(id, cp, ville);
            lesVilles.add(uneVille);
        }
        return lesVilles;
    }
}