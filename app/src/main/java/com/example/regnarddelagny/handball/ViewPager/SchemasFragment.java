package com.example.regnarddelagny.handball.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsWriting;
import com.example.regnarddelagny.handball.R;
import com.example.regnarddelagny.handball.SchemasTactiques.SchemasPrepActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RegnarddeLagny on 07/08/2015.
 */
public class SchemasFragment extends android.support.v4.app.Fragment{

    EditText nomFiche;
    ArrayList<View> schemaViews;
    boolean isInDeletingMode = false;
    LinearLayout.LayoutParams addMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int marginValueDp = 12;
        int marginValuePx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginValueDp, getResources().getDisplayMetrics()));
        addMargins.setMargins(0, 0, 0, marginValuePx);
        schemaViews = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.activity_schemas, container, false);
        addSchemasToView(rootView);
        addListeners(rootView);
        return rootView;
    }

    public void addListeners (final View rootView) {
        Button ajout = (Button) rootView.findViewById(R.id.bouton_ajout);
        final Button suppr = (Button) rootView.findViewById(R.id.bouton_suppr);
        final Button back = (Button) rootView.findViewById(R.id.bouton_back);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog analyseDialog = createSchemaDialog(rootView);
                analyseDialog.show();
            }
        });

        suppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppr.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                isInDeletingMode = true;
                for (View view : schemaViews) {
                    view.setBackgroundColor(Color.rgb(213, 77, 86));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                suppr.setVisibility(View.VISIBLE);
                isInDeletingMode = false;
                for (View view : schemaViews) {
                    view.setBackgroundColor(Color.rgb(33, 150, 242));
                }
            }
        });
    }

    public void addSchemasToView(View rootView) {

        final File dossier = new File(getActivity().getFilesDir().getAbsolutePath(), "SchemasTactiques");

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

    public void ajouterFichier(View rootView, final File file) {
        String nomFichier = file.getName();
        Log.d ("fichier", file.getAbsolutePath());
        if (nomFichier.substring(0, 8).equals("Handball")) {
            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View anaView = vi.inflate(R.layout.analyse_tirs_view, null);
            // fill in any details dynamically here
            TextView nomFiche = (TextView) anaView.findViewById(R.id.analyse_view_text);
            final String nomFichierCourt = nomFichier.substring(9);
            nomFiche.setText(nomFichierCourt);
            anaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isInDeletingMode) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), SchemasPrepActivity.class);
                        intent.putExtra("FICHIER", nomFichierCourt);
                        startActivity(intent);
                    }
                    else {
                        while(file.delete() && file.isFile()) {
                            Log.e("Deleting error", "Fichier impossible à effacer");
                        }
                        v.setVisibility(View.GONE);
                    }
                }
            });
            ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.insert_point_at);
            insertPoint.addView(anaView, 0, addMargins);
            schemaViews.add(anaView);
        }
    }

    public AlertDialog createSchemaDialog(final View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.create_analyse_dialog, null);
        nomFiche = (EditText) dialog_view.findViewById(R.id.analyseName);

        builder.setView(dialog_view);
        builder.setTitle(R.string.creation_fiche_tactique);
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
                        File dossier = new File(getActivity().getFilesDir().getAbsolutePath(), "SchemasTactiques");
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
}
