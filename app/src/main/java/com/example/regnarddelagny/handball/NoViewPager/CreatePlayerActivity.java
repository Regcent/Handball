package com.example.regnarddelagny.handball.NoViewPager;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.regnarddelagny.handball.NoViewPager.PlayersActivity;
import com.example.regnarddelagny.handball.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import utilitaries.Equipe;


public class CreatePlayerActivity extends Activity {

    private Equipe equipeAUtiliser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
        try {
            File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            equipeAUtiliser = new Equipe ((Equipe) ois.readObject());
        }
        catch (Exception e) {
            equipeAUtiliser = new Equipe("equipe1");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void createNewPlayer(View view) {
        EditText nom = (EditText) findViewById(R.id.playerName);
        EditText prenom = (EditText) findViewById(R.id.playerFirstName);
        EditText numero = (EditText) findViewById(R.id.playerNumber);
        CheckBox checkGardien = (CheckBox) findViewById(R.id.checkGardien);
        String nomS = nom.getText().toString();
        String prenomS = prenom.getText().toString();
        String numS = numero.getText().toString();

        if (verifierRemplissage(nomS, prenomS, numS)) {
            nomS = nomS.substring(0, 1).toUpperCase() + nomS.substring(1);
            prenomS = prenomS.substring(0, 1).toUpperCase() + prenomS.substring(1);
            int numeroI = Integer.parseInt(numS);

            try {
                File file = new File(getFilesDir().getAbsolutePath(), "joueurs");
                FileOutputStream fos = new FileOutputStream(file, false);
                if (checkGardien.isChecked())
                    equipeAUtiliser.addPlayer(nomS, prenomS, numeroI, true);
                else
                    equipeAUtiliser.addPlayer(nomS, prenomS, numeroI, false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                equipeAUtiliser.writeObject(oos);
                Log.d("Ecriture fichier", "réussie");
                Intent intent = new Intent(this, PlayersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast demandeRemplissage = Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT);
            demandeRemplissage.show();
        }
    }

    public boolean verifierRemplissage(String nomS, String prenomS, String numS) {
        return ((!nomS.equals("")) &&
                (!prenomS.equals("")) &&
                (!numS.equals("")));
    }
}


