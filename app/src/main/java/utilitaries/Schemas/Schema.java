package utilitaries.Schemas;

import java.io.Serializable;
import java.util.ArrayList;

import utilitaries.dessins.dessinsJoueurs.Attacker;
import utilitaries.dessins.dessinsJoueurs.Defender;
import utilitaries.dessins.dessinsJoueurs.PointSerial;

/**
 * Created by RegnarddeLagny on 20/08/2015.
 */
public class Schema implements Serializable {

    private ArrayList<PointSerial> listeJoueurs;
    private ArrayList<ArrayList<PointSerial>> lines;
    private String nomSchema;

    public Schema (String nomFichier) {
        nomSchema = nomFichier;
        listeJoueurs = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public ArrayList<PointSerial> getListeJoueurs() {
        return listeJoueurs;
    }

    public void setListeJoueurs(ArrayList<PointSerial> listeJoueurs) {
        this.listeJoueurs = listeJoueurs;
    }


    public void setLines(ArrayList<ArrayList<PointSerial>> lines) {
        this.lines = lines;
    }

    public String getNomSchema() {
        return nomSchema;

    }

    public Schema (Schema toCopy) {
        lines = toCopy.getLines();
        nomSchema = toCopy.getNomSchema();
        listeJoueurs = toCopy.getListeJoueurs();
    }

    public ArrayList<ArrayList<PointSerial>> getLines() {
        return lines;
    }
}
