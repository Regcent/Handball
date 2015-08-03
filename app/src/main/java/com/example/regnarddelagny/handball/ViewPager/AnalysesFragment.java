package com.example.regnarddelagny.handball.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsReading;
import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsWriting;
import com.example.regnarddelagny.handball.R;

import java.io.File;

/**
 * Created by RegnarddeLagny on 02/08/2015.
 */
public class AnalysesFragment extends android.support.v4.app.Fragment {

    EditText nomFiche;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_analyse_tirs, container, false);
        addTestsToView(rootView);
        addListeners(rootView);
        return rootView;
    }

    public void addTestsToView(View rootView) {

        String nomFichier;
        final File dossier = new File(getActivity().getFilesDir().getAbsolutePath(), "AnalyseTirs");

        if (dossier.mkdir() || dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();
            if (fichiers.length == 0) {
                Log.d("Erreur", "pas de fichiers!!!");
            }
            for (int i = 0; i < fichiers.length; i++) {
                Log.d("Entree for", "ok");
                nomFichier = fichiers[i].getName();
                if (nomFichier.substring(0, 8).equals("Handball")) {
                    LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.analyse_tirs_view, null);
                    // fill in any details dynamically here
                    TextView nomFiche = (TextView) v.findViewById(R.id.analyse_view_text);
                    final String nomFichierCourt = nomFichier.substring(9);
                    nomFiche.setText(nomFichierCourt);
                    Button boutonW = (Button) v.findViewById(R.id.analyse_button_w);
                    boutonW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), AnalyseTirsWriting.class);
                            intent.putExtra("FICHIER", nomFichierCourt);
                            startActivity(intent);
                        }
                    });
                    Button boutonR = (Button) v.findViewById(R.id.analyse_button_r);
                    boutonR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), AnalyseTirsReading.class);
                            intent.putExtra("FICHIER", nomFichierCourt);
                            startActivity(intent);
                        }
                    });

                    ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.insert_point_at);
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

    public void addListeners (View rootView) {
        Button bouton = (Button) rootView.findViewById(R.id.bouton_ajout);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog playerDialog = createAnalyseDialog();
                playerDialog.show();
            }
        });
    }

    public AlertDialog createAnalyseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.create_analyse_dialog, null);
        nomFiche = (EditText) dialog_view.findViewById(R.id.analyseName);

        builder.setView(dialog_view);
        builder.setTitle(R.string.creation_fiche_tirs);
        builder.setPositiveButton(R.string.validate, null);
        builder.setNegativeButton("Annuler", null);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomSFiche = nomFiche.getText().toString();

                        Toast demandeNom;
                        File dossier = new File (getActivity().getFilesDir().getAbsolutePath(), "AnalyseTirs");
                        if ((nomSFiche.equals(""))) {
                            demandeNom = Toast.makeText(getActivity(), "Entrez un nom de fichier", Toast.LENGTH_SHORT);
                            demandeNom.show();
                        }
                        else {
                            String nomFichier = "Handball_" + nomSFiche;
                            File fichier = new File(dossier.getAbsolutePath(), nomFichier);
                            try {
                                fichier.createNewFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                });
            }
        });
        return dialog;
    }

}
