/*
 * @author Bosznai et Lopes
 */

package test.modele.metier;

import modele.metier.Ville;

public class TestVille {
    public static void main(String[] args) {
        Ville ville;
        System.out.println("\nTest N°1: Instanciation et accesseurs");
        ville= new Ville(1,"44230", "Saint-Sébastien-sur-Loire");
        System.out.print(ville);
        System.out.print("\nTest N°2: Mutateurs");
        ville.setCp("44265");
        ville.setVille("Nantes");
        System.out.println(ville);
    }
}