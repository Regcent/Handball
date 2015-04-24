package utilitaries;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by RegnarddeLagny on 19/04/2015.
 */
public class Player implements Serializable{

    private static final long serialVersionUID = 0L;
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

    public Player(Player player) {
        nom = player.nom;
        prenom = player.prenom;
        nbButs = player.nbButs;
        numero = player.numero;
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

    public void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        // write 'this' to 'out'
        out.writeObject(this);
    }


    public void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        // populate the fields of 'this' from the data in 'in'
        Player other = (Player) in.readObject();
        prenom = other.getPrenom();
        nom = other.getNom();
        numero = other.getNumero();
        nbButs = other.getNbButs();
    }
}

