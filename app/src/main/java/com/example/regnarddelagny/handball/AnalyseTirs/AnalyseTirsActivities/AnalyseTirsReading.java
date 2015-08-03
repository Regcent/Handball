package com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.regnarddelagny.handball.AnalyseTirs.VuesDemiTerrain.VueDemiTerrainReading;

import java.io.File;


/*TODO: Créer une classe héritant d'Activity, appelée AnalyseTirs et étant abstraite, donc pas instantiable. Puis faire hériter Reading et Writing de celles-ci pour éviter la redondance des fonctions*/
public class AnalyseTirsReading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String nomFichier = intent.getStringExtra("FICHIER");
        String nomComplet = "AnalyseTirs/Handball_" + nomFichier;
        try {
            getActionBar().hide();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        File fichier = new File(getFilesDir().getAbsolutePath(), nomComplet);
        setContentView(new VueDemiTerrainReading(this, nomFichier, fichier));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
