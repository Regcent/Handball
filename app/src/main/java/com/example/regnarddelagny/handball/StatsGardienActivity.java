package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class StatsGardienActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String nomJoueur = intent.getStringExtra("NOM");
        String prenomJoueur = intent.getStringExtra("PRENOM");
        /*setContentView(R.layout.activity_stats_gardien);
        TextView tv =(TextView) findViewById(R.id.st/atsGardien_nom);
        tv.setText(nomJoueur + " " + prenomJoueur);*/
    }

}
