package com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;


import com.example.regnarddelagny.handball.VuesDemiTerrain.Tirs.VueDemiTerrainTirWriting;

import java.io.File;


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
        setContentView(new VueDemiTerrainTirWriting(this, nomFichier, fichier));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
