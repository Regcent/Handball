package utilitaries.Joueurs;

import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by RegnarddeLagny on 19/04/2015.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nom;
    private String prenom;
    private int numero;
    private int nbButs;
    private int nbArrets;
    private boolean gardien;

    public Player(final String nom, final String prenom, final int numero, final boolean gardien) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.nbButs = 0;
        this.nbArrets = 0;
        this.gardien = gardien;
    }

    public Player(Player player) {
        nom = player.nom;
        prenom = player.prenom;
        numero = player.numero;
        nbButs = player.nbButs;
        nbArrets = player.nbArrets;
        gardien = player.gardien;
    }

    /* Getters */

    public int getNumero() {
        return numero;
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

    public int getNbButs() {
        return nbButs;
    }

    public int getNbArrets() {
        return nbArrets;
    }

    public boolean isGardien() {
        return gardien;
    }

    public void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        // write 'this' to 'out'
        out.writeObject(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (!nom.equalsIgnoreCase(player.nom)) return false;
        if (!prenom.equalsIgnoreCase(player.prenom)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nom.hashCode();
        result = 31 * result + prenom.hashCode();
        return result;
    }
}

