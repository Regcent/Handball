package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.Equipe;
import utilitaries.Joueurs.Player;


public class PlayersActivity extends Activity {

    private Equipe equipeAUtiliser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        addPlayersToView();
    }


    public void addNewPlayer(View view) {
        Intent intent = new Intent(this, com.example.regnarddelagny.handball.CreatePlayerActivity.class);
        startActivity(intent);
    }

    public void deleteFile(View view) {
        File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
        if (file.delete()) {
            recreate();
        }
    }

   public void addPlayersToView() {
        final File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            equipeAUtiliser = new Equipe((Equipe) ois.readObject());
            if (equipeAUtiliser != null) {
                ArrayList<Player> listeJoueurs = equipeAUtiliser.getListeJoueurs();
                if (listeJoueurs.size() != 0) {
                    for (int ind = 0; ind < listeJoueurs.size(); ind++) {
                        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = vi.inflate(R.layout.player_view, null);
                        Log.d("inflate", "successful");

// fill in any details dynamically here
                        Player joueur = listeJoueurs.get(ind);
                        TextView number = (TextView) v.findViewById(R.id.playerView_playerNumber);
                        Log.d("number id", "" + number.getId());
                        number.setText("" + joueur.getNumero());
                        Log.d("ecriture Number", "reussi");

                        TextView nom = (TextView) v.findViewById(R.id.playerView_playerName);
                        final String nomJoueur = joueur.getNom();
                        final String prenomJoueur = joueur.getPrenom();
                        if (joueur.isGardien())
                            nom.setText(nomJoueur + " " + prenomJoueur + "(G)");
                        else
                            nom.setText(nomJoueur + " " + prenomJoueur);

                        Log.d("Lectures et écritures", "ok");

                        Button infos = (Button) v.findViewById(R.id.playerView_playerInfos);
                        infos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), com.example.regnarddelagny.handball.StatsGardienActivity.class);
                                intent.putExtra("NOM", nomJoueur);
                                intent.putExtra("PRENOM", prenomJoueur);
                                startActivity(intent);
                            }
                        });
                        Button suppr = (Button) v.findViewById(R.id.playerView_suppr);
                        suppr.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                equipeAUtiliser.removePlayer(equipeAUtiliser.findPlayer(nomJoueur, prenomJoueur));
                                try {
                                    FileOutputStream fos = new FileOutputStream(file, false);
                                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                                    equipeAUtiliser.writeObject(oos);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                recreate();
                            }
                        });


// insert into main vie
                        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
                        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        Log.d("Lecture joueur", "ok");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
   }

   public void getInfos (View view) {

       TextView joueur = (TextView) findViewById(R.id.playerView_playerName);
       Log.d("Nom joueur cliqué", joueur.toString());
   }
}

