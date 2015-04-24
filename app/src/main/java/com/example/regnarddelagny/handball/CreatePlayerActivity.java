package com.example.regnarddelagny.handball;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import utilitaries.AppendingObjectOutputStream;
import utilitaries.Player;


public class CreatePlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
    }

    public void createNewPlayer(View view) {
        EditText nom = (EditText) findViewById(R.id.playerName);
        EditText prenom = (EditText) findViewById(R.id.playerFirstName);
        EditText numero = (EditText) findViewById(R.id.playerNumber);
        String nomS = nom.getText().toString();
        String prenomS = prenom.getText().toString();
        nomS = nomS.substring(0,1).toUpperCase() + nomS.substring(1);
        prenomS = prenomS.substring(0,1).toUpperCase() + prenomS.substring(1);
        int numeroI = Integer.parseInt(numero.getText().toString());
        Player joueur1 = new Player (nomS, prenomS, numeroI);
        Log.d("Nom joueur1", joueur1.getNom());
        Log.d("Prenom joueur1", joueur1.getPrenom());
        Log.d("Numero joueur1", "" + joueur1.getNumero());
        Log.d("Nombre buts1", "" + joueur1.getNbButs());
        try {
            File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
            FileOutputStream fos = new FileOutputStream(file, true);
            Log.d("Ouverture fichier", "réussie");
            Log.d("chemin fichier", file.getAbsolutePath() );
            if (file.length() == 0) {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                joueur1.writeObject(oos);
                Log.d("Ecriture fichier", "réussie");
                oos.close();

            }
            else {
                AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(fos);
                joueur1.writeObject(aoos);
                Log.d("Ecriture fichier", "réussie");
                aoos.close();
            }
            fos.close();
            Intent intent = new Intent (this, com.example.regnarddelagny.handball.PlayersActivity.class);
            startActivity(intent);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
