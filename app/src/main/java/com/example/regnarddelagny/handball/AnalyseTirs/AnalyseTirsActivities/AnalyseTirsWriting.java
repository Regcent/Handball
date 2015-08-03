package com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.regnarddelagny.handball.AnalyseTirs.VuesDemiTerrain.VueDemiTerrainWriting;

import java.io.File;

/*TODO: Cr�er une classe h�ritant d'Activity, appel�e AnalyseTirs et �tant abstraite, donc pas instantiable. Puis faire h�riter Reading et Writing de celles-ci pour �viter la redondance des fonctions*/
/*TODO : G�rer la suppression des analyses dynamiquement*/
public class AnalyseTirsWriting extends Activity {

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
        setContentView(new VueDemiTerrainWriting(this, nomFichier, fichier));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
