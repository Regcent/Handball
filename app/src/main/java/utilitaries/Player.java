package utilitaries;

/**
 * Created by RegnarddeLagny on 19/04/2015.
 */
public class Player {

    private String nom;
    private String prenom;
    private int nbButs;
    private int numero;

    public Player(final String nom, final String prenom, final int numero) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        nbButs = 0;
    }

    /* Getters */

    public int getNumero() {
        return numero;
    }

    public int getNbButs() {
        return nbButs;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNumero(final int newNum) {
        numero = newNum;
    }

    public void incNbButs() {
        nbButs++;
    }
}
