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
import java.util.ArrayList;

/**
 * Created by RegnarddeLagny on 02/08/2015.
 */
public class AnalysesFragment extends android.support.v4.app.Fragment {

    EditText nomFiche;
    ArrayList<View> analyseViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        analyseViews = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.activity_analyse_tirs, container, false);
        addTestsToView(rootView);
        addListeners(rootView);
        return rootView;
    }

    public void addTestsToView(View rootView) {

        final File dossier = new File(getActivity().getFilesDir().getAbsolutePath(), "AnalyseTirs");

        if (dossier.mkdir() || dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();
            if (fichiers.length == 0) {
                Log.d("Erreur", "pas de fichiers!!!");
            }
            for (final File file : fichiers) {
                Log.d("Entree for", "ok");
                ajouterFichier(rootView, file);
            }
        }
    }

    public void addListeners (final View rootView) {
        Button ajout = (Button) rootView.findViewById(R.id.bouton_ajout);
        final Button suppr = (Button) rootView.findViewById(R.id.bouton_suppr);
        final Button back = (Button) rootView.findViewById(R.id.bouton_back);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog analyseDialog = createAnalyseDialog(rootView);
                analyseDialog.show();
            }
        });

        suppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppr.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                for (View view : analyseViews) {
                    Button boutonR = (Button) view.findViewById(R.id.analyse_button_r);
                    Button boutonW = (Button) view.findViewById(R.id.analyse_button_w);
                    Button boutonDel = (Button) view.findViewById(R.id.analyse_button_del);
                    boutonR.setVisibility(View.GONE);
                    boutonW.setVisibility(View.GONE);
                    boutonDel.setVisibility(View.VISIBLE);

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                suppr.setVisibility(View.VISIBLE);
                for (View view : analyseViews) {
                    Button boutonR = (Button) view.findViewById(R.id.analyse_button_r);
                    Button boutonW = (Button) view.findViewById(R.id.analyse_button_w);
                    Button boutonDel = (Button) view.findViewById(R.id.analyse_button_del);
                    boutonR.setVisibility(View.VISIBLE);
                    boutonW.setVisibility(View.VISIBLE);
                    boutonDel.setVisibility(View.GONE);
                }
            }
        });
    }

    public AlertDialog createAnalyseDialog(final View rootView) {
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
                        File dossier = new File(getActivity().getFilesDir().getAbsolutePath(), "AnalyseTirs");
                        if ((nomSFiche.equals(""))) {
                            demandeNom = Toast.makeText(getActivity(), "Entrez un nom de fichier", Toast.LENGTH_SHORT);
                            demandeNom.show();
                        } else {
                            String nomFichier = "Handball_" + nomSFiche;
                            File fichier = new File(dossier.getAbsolutePath(), nomFichier);
                            Toast creaFile;
                            try {
                                if (fichier.createNewFile()) {
                                    dialog.dismiss();
                                    ajouterFichier(rootView, fichier);
                                } else {
                                    creaFile = Toast.makeText(getActivity(), getResources().getString(R.string.nom_deja_utilise), Toast.LENGTH_SHORT);
                                    creaFile.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });
            }
        });
        return dialog;
    }

   /* public static ArrayList<View> getViewsByTag(ViewGroup root, String tag) {
        ArrayList<View> views = new ArrayList<>();
        final int childCount = root.getChildCount();
        for (int ind = 0; ind < childCount; ind++) {
            final View child = root.getChildAt(ind);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }
        }
        return views;
    }*/

    public void ajouterFichier(View rootView, final File file) {
        String nomFichier = file.getName();
        if (nomFichier.substring(0, 8).equals("Handball")) {
            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View anaView = vi.inflate(R.layout.analyse_tirs_view, null);
            // fill in any details dynamically here
            TextView nomFiche = (TextView) anaView.findViewById(R.id.analyse_view_text);
            final String nomFichierCourt = nomFichier.substring(9);
            nomFiche.setText(nomFichierCourt);
            Button boutonW = (Button) anaView.findViewById(R.id.analyse_button_w);
            boutonW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), AnalyseTirsWriting.class);
                    intent.putExtra("FICHIER", nomFichierCourt);
                    startActivity(intent);
                }
            });
            Button boutonR = (Button) anaView.findViewById(R.id.analyse_button_r);
            boutonR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), AnalyseTirsReading.class);
                    intent.putExtra("FICHIER", nomFichierCourt);
                    startActivity(intent);
                }
            });
            Button boutonDel = (Button) anaView.findViewById(R.id.analyse_button_del);
            boutonDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    while (!file.delete() && file.isFile()) {
                        Log.d("echec", "effacement");
                    }
                    anaView.setVisibility(View.GONE);
                }
            });
            ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.insert_point_at);
            insertPoint.addView(anaView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            analyseViews.add(anaView);
        }
    }
}
