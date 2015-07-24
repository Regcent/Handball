package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class NewAnalyseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_analyse);
    }

    public void createNewAnalyse (View v) {
        Toast demandeNom;
        File dossier = new File (getFilesDir().getAbsolutePath(), "AnalyseTirs");
        EditText edit = (EditText) findViewById(R.id.nomFiche);
        String finNom = edit.getText().toString();
        if ((finNom.equals(""))) {
            demandeNom = Toast.makeText(this, "Entrez un nom de fichier", Toast.LENGTH_SHORT);
            demandeNom.show();
        }
        else {
            String nomFichier = "Handball_" + finNom;
            File fichier = new File(dossier.getAbsolutePath(), nomFichier);
            try {
              fichier.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, AnalyseTirsActivity.class);
            startActivity(intent);
        }
    }
}
