package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import utilitaries.Player;


public class PlayersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        TextView tv1 = (TextView) findViewById(R.id.joueur1);
        TextView tv2 = (TextView) findViewById(R.id.joueur2);
        File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Player joueur1 = new Player((Player) ois.readObject());
            Log.d("Lecture joueur1", "ok");
            tv1.setText(joueur1.getNom() + " " + joueur1.getPrenom());
            Player joueur2 = new Player ((Player) ois.readObject());
            Log.d(joueur2.getNom(), joueur2.getPrenom());
            tv2.setText(joueur2.getNom() + " " + joueur2.getPrenom());
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void addNewPlayer(View view) {
        Intent intent = new Intent(this, com.example.regnarddelagny.handball.CreatePlayerActivity.class);
        startActivity(intent);
    }

}
