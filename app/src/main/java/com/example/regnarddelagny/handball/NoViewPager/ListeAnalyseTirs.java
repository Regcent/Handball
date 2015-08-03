package com.example.regnarddelagny.handball.NoViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsReading;
import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsWriting;
import com.example.regnarddelagny.handball.R;

import java.io.File;

public class ListeAnalyseTirs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_tirs);
        addTestsToView();
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

    public void addTestsToView() {

        String nomFichier;
        final File dossier = new File(getFilesDir().getAbsolutePath(), "AnalyseTirs");
        Log.d("AbsolutePath", dossier.getAbsolutePath());

        if (dossier.mkdir() || dossier.isDirectory()) {
            Log.d ("Entree if", "ok");
            File[] fichiers = dossier.listFiles();
            if (fichiers.length == 0) {
                Log.d("Erreur", "pas de fichiers!!!");
            }
            for (int i = 0; i < fichiers.length; i++) {
                Log.d("Entree for", "ok");
                nomFichier = fichiers[i].getName();
                Log.d("Nom fichier", nomFichier);
                Log.d("Nom fichier substringed", nomFichier.substring(0, 8));
                if (nomFichier.substring(0, 8).equals("Handball")) {
                    Log.d("Fichiertest", "trouve");
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.analyse_tirs_view, null);
                    // fill in any details dynamically here
                    TextView nomFiche = (TextView) v.findViewById(R.id.analyse_view_text);
                    final String nomFichierCourt = nomFichier.substring(9);
                    nomFiche.setText(nomFichierCourt);
                    Button boutonW = (Button) v.findViewById(R.id.analyse_button_w);
                    boutonW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), AnalyseTirsWriting.class);
                            intent.putExtra("FICHIER", nomFichierCourt);
                            startActivity(intent);
                        }
                    });
                    Button boutonR = (Button) v.findViewById(R.id.analyse_button_r);
                    boutonR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), AnalyseTirsReading.class);
                            intent.putExtra("FICHIER", nomFichierCourt);
                            startActivity(intent);
                        }
                    });

                    ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point_at);
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    Log.d("Lecture joueur", "ok");
                }
            }
        }
    }

    public void deleteAll (View v) {
        final File dossier = new File(getFilesDir().getAbsolutePath(), "AnalyseTirs");
        File[] fichiers = dossier.listFiles();

        for (File f : fichiers)
            f.delete();
        Intent intent = new Intent (this, ListeAnalyseTirs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void openNewAnalyse (View v) {
        Intent intent = new Intent (this, NewAnalyseActivity.class);
        startActivity(intent);
    }
}