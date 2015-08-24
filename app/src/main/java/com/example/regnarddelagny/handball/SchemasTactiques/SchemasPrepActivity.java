package com.example.regnarddelagny.handball.SchemasTactiques;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.regnarddelagny.handball.VuesDemiTerrain.Schema.VueDemiTerrainPrep;

import java.io.File;

public class SchemasPrepActivity extends Activity {

    VueDemiTerrainPrep vue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String nomFichier = intent.getStringExtra("FICHIER");
        String nomComplet = "SchemasTactiques/Handball_" + nomFichier;
        getActionBar().hide();
        File fichier = new File(getFilesDir().getAbsolutePath(), nomComplet);
        Log.d("nom fichier", fichier.getAbsolutePath());
        vue = new VueDemiTerrainPrep(this, nomFichier, fichier);
        setContentView(vue);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vue.save();
    }
}
