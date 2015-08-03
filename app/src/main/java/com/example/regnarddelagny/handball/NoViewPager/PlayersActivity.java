package com.example.regnarddelagny.handball.NoViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.regnarddelagny.handball.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import utilitaries.Equipe;
import utilitaries.Joueurs.Player;

/*TODO: Gestion par équipes avec arrangement sur le filesystem*/

public class PlayersActivity extends Activity {

    private Equipe equipeAUtiliser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        addPlayersToView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("Ouverture", "menu");
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewPlayer(View view) {
        Intent intent = new Intent(this, CreatePlayerActivity.class);
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
       Log.d("FilesDir", getFilesDir().getAbsolutePath());
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
}

