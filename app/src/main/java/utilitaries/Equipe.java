package utilitaries;



import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import utilitaries.Joueurs.Player;


/**
 * Created by RegnarddeLagny on 25/04/2015.
 */
public class Equipe implements Serializable {

    private static final long serialVersionUID = 0L;
    private ArrayList<Player> listeJoueurs;
    private String nomEquipe;

    public Equipe(String nom) {
        nomEquipe = nom;
        listeJoueurs = new ArrayList<Player> ();
    }

    public Equipe(Equipe equipe) {
        nomEquipe = equipe.getNomEquipe();
        listeJoueurs = equipe.getListeJoueurs();
    }

    public void addPlayer (Player joueur) {
        if (listeJoueurs.isEmpty())
            listeJoueurs.add(joueur);
        else {
            if (!(listeJoueurs.contains(joueur))) {
                int numero = joueur.getNumero();
                int ind = 0;
                while (ind < listeJoueurs.size() && listeJoueurs.get(ind).getNumero() < numero ) {
                    ind++;
                }
                listeJoueurs.add(ind, joueur);
            }
        }
    }

    public void addPlayer (String nom, String prenom, int numero, boolean gardien) {
        Player joueur = new Player(nom, prenom, numero, gardien);
        addPlayer(joueur);
    }

    public void removePlayer (Player joueur) {
        if(joueur != null) {
            listeJoueurs.remove(joueur);
        }
    }

    public ArrayList<Player> getListeJoueurs() {
        return listeJoueurs;
    }

    public String getNomEquipe() {
        return nomEquipe;
    }

    public Player findPlayer (String nom, String prenom) {
        int ind;

        for (ind = 0; ind < listeJoueurs.size(); ind++) {
            Player joueur = listeJoueurs.get(ind);
            if (joueur.getNom().equalsIgnoreCase(nom) && joueur.getPrenom().equalsIgnoreCase(prenom)) {
                return joueur;
            }
        }
        return null;
    }

    public void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        // write 'this' to 'out'
        out.writeObject(this);
    }
}
