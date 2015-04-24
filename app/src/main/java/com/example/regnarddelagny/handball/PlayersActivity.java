package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import utilitaries.Player;
import utilitaries.PlayerView;


public class PlayersActivity extends Activity {

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
        File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Player joueur = new Player((Player) ois.readObject());
            while (joueur != null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.player_view, null);
                Log.d("inflate", "successful");

// fill in any details dynamically here
                TextView number = (TextView) v.findViewById(R.id.playerView_playerNumber);
                Log.d("number id", "" + number.getId());
                number.setText("" + joueur.getNumero());
                Log.d("ecriture Number", "reussi");

                TextView nom = (TextView) v.findViewById(R.id.playerView_playerName);
                nom.setText(joueur.getNom() + " " + joueur.getPrenom());

                TextView nbButs = (TextView) v.findViewById(R.id.playerView_playerGoals);
                nbButs.setText("" + joueur.getNbButs());

                Log.d("Lectures et écritures", "ok");

// insert into main vie
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
                insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                joueur = new Player((Player) ois.readObject());
            }
            Log.d("Lecture joueur", "ok");

        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
}

