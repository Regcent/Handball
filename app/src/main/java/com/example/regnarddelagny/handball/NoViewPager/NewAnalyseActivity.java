package com.example.regnarddelagny.handball.NoViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.regnarddelagny.handball.NoViewPager.ListeAnalyseTirs;
import com.example.regnarddelagny.handball.R;

import java.io.File;


public class NewAnalyseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_analyse);
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
            Intent intent = new Intent(this, ListeAnalyseTirs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
