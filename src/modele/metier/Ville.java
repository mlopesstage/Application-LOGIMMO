/*
 * @author Bosznai et Lopes
 */

package modele.metier;

public class Ville {
    private int id;
    private String cp;
    private String ville;
    
    public Ville(int id, String cp, String ville) {
        this.id = id;
        this.cp = cp;
        this.ville = ville;
    }
    
    public String getCp() {
        return cp;
    }
    
    public void setCp(String cp) {
        this.cp = cp;
    }
    
    public String getVille() {
        return ville;
    }
    
    public void setVille(String ville) {
        this.ville = ville;
    }
    
    @Override
    public String toString() {
        return "Ville{" + "id=" + id + ", cp=" + cp + ", ville=" + ville + '}';
    }
}