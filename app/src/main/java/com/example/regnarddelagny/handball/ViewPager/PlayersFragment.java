package com.example.regnarddelagny.handball.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.regnarddelagny.handball.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.Equipe;
import utilitaries.Joueurs.Player;


public class PlayersFragment extends android.support.v4.app.Fragment {

    Equipe equipeAUtiliser;

    EditText nom;
    EditText prenom;
    EditText numero;
    CheckBox gardien;

    LinearLayout.LayoutParams addMargins = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_players, container, false);
        final File file = new File(getActivity().getFilesDir().getAbsolutePath(), "joueurs");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            equipeAUtiliser = new Equipe((Equipe) ois.readObject());
        }
        catch (Exception e) {
            equipeAUtiliser = new Equipe("equipe1");
            e.printStackTrace();
        }
        addPlayersToView(rootView);
        addListeners(rootView);
        return rootView;
    }

    public void addPlayersToView(View rootView) {

        addMargins.setMargins(0, 0, 0, 18);
        if (equipeAUtiliser != null) {
            ArrayList<Player> listeJoueurs = equipeAUtiliser.getListeJoueurs();
            if (listeJoueurs.size() != 0) {
                for (int ind = 0; ind < listeJoueurs.size(); ind++) {
                    LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.player_view, (ViewGroup) getView());

// fill in any details dynamically here
                    Player joueur = listeJoueurs.get(ind);
                    TextView number = (TextView) v.findViewById(R.id.playerView_playerNumber);
                    number.setText("" + joueur.getNumero());

                    TextView nom = (TextView) v.findViewById(R.id.playerView_playerName);
                    final String nomJoueur = joueur.getNom();
                    final String prenomJoueur = joueur.getPrenom();
                    if (joueur.isGardien())
                        nom.setText(nomJoueur + " " + prenomJoueur + "(G)");
                    else
                        nom.setText(nomJoueur + " " + prenomJoueur);

                    ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.insert_point);
                    insertPoint.addView(v, 0, addMargins);
                }
            }
        }
    }

    public void addListeners (View rootView) {
        Button bouton = (Button) rootView.findViewById(R.id.bouton_ajout);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog playerDialog = createPlayerDialog();
                playerDialog.show();
            }
        });
    }

    public AlertDialog createPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.create_player_dialog, null);
        nom = (EditText) dialog_view.findViewById(R.id.playerName);
        prenom = (EditText) dialog_view.findViewById(R.id.playerFirstName);
        numero = (EditText) dialog_view.findViewById(R.id.playerNumber);
        gardien = (CheckBox) dialog_view.findViewById(R.id.checkGardien);
        builder.setView(dialog_view);
        builder.setTitle(R.string.creation_joueur);
        builder.setPositiveButton(R.string.validate, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nomS = nom.getText().toString();
                        String prenomS = prenom.getText().toString();
                        String numS = numero.getText().toString();

                        if (verifierRemplissage(nomS, prenomS, numS)) {
                            nomS = nomS.substring(0, 1).toUpperCase() + nomS.substring(1);
                            prenomS = prenomS.substring(0, 1).toUpperCase() + prenomS.substring(1);
                            int numeroI = Integer.parseInt(numS);

                            try {
                                File file = new File(getActivity().getFilesDir().getAbsolutePath(), "joueurs");
                                FileOutputStream fos = new FileOutputStream(file, false);
                                if (gardien.isChecked())
                                    equipeAUtiliser.addPlayer(nomS, prenomS, numeroI, true);
                                else
                                    equipeAUtiliser.addPlayer(nomS, prenomS, numeroI, false);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                equipeAUtiliser.writeObject(oos);
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast demandeRemplissage = Toast.makeText(getActivity(), "Remplissez tous les champs", Toast.LENGTH_SHORT);
                            demandeRemplissage.show();
                        }
                    }
                });
            }
        });
        return dialog;
    }


    public boolean verifierRemplissage(String nomS, String prenomS, String numS) {
        return ((!nomS.equals("")) &&
        (!prenomS.equals("")) &&
        (!numS.equals("")));
    }
}

