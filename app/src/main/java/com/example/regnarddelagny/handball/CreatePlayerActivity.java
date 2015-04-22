package com.example.regnarddelagny.handball;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

            /*FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Player joueur2 = new Player("c","d", 2);
            joueur2.readObject(ois);

            Log.d("Nom copie joueur1", joueur2.getNom());
            Log.d("Prenom copie joueur1", joueur2.getPrenom());
            Log.d("Numero copie joueur1", "" + joueur2.getNumero());
            Log.d("Nombre copie buts1", "" + joueur2.getNbButs());*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
