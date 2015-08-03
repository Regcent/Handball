package com.example.regnarddelagny.handball.AnalyseTirs.VuesDemiTerrain;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;

import android.view.MotionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.dessins.Tir;

/**
 * Created by RegnarddeLagny on 03/08/2015.
 * VueDemiTerrainWriting
 */
public class VueDemiTerrainWriting extends VueDemiTerrain {

    //La liste de points utilisee pour sauvegarder les mesures
    protected ArrayList<Tir> listeTirs;

    // Utilises pour la mesure de chaque point, afin de s'assurer de ne pas prendre une mesure a chaque evenement de toucher
    private Tir tirMesure;
    private boolean mesureEnCours = false;

    //Le fichier et les flux d'ecriture et de lecture utilises
    private ObjectOutputStream oos;

    public VueDemiTerrainWriting(Context context) {
        super(context);
    }
    // CONSTRUCTOR
    public VueDemiTerrainWriting(Context context, String nomFichier, File fichier) {

        super(context);

        mNomFichier = nomFichier;

        setPeintures();

        Resources resources = context.getResources();
        prepareDessins(resources);

        listeTirs = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listeTirs = new ArrayList<>((ArrayList<Tir>) ois.readObject());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(fichier);
            oos = new ObjectOutputStream(fos);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        comptageTirs(listeTirs);

    }

    @Override
    /**
     * Gestion des evenements de toucher
     */
    public boolean onTouchEvent (@NonNull MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        if (verificationPosition(x, y)) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up(x, y);
                    invalidate();
                    break;
            }
        }
        return true;
    }


    protected void touch_start(float x, float y) {
        if (!mesureEnCours) {
            Log.d("TouchStartEvent on", "mX=" + x + "; mY=" + y);
            tirMesure = new Tir(x, y);
            mesureEnCours = true;   //mesureEnCours sert a eviter la creation de points multiples
        }
    }


    private void touch_up(float x, float y) {
        if (y <= dimPxRectBut[3] && y >= dimPxRectBut[1]) {
            if (x <= dimPxRectBut[2] && x >= dimPxRectBut[0]) {
                tirMesure.setBut_valide();
                buts += 1;
                Log.d("TouchUpEvent", "x=" + x + "; y=" + y);

            }
            else {
                tirsManques += 1;
            }
        }
        else {
            tirsManques  += 1;
        }

        mesureEnCours = false;          //On attend l'evenement touchUp pour prendre un point
        listeTirs.add(tirMesure);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        dessinTerrain(canvas);
        dessinLegende(canvas);

        int nbPoints = listeTirs.size();

        if (nbPoints > 0) {
            for (int ind = 0; ind < nbPoints; ind++) {
                Tir tir_mesure = listeTirs.get(ind);
                float abscisse = tir_mesure.getX();
                float ordonnee = tir_mesure.getY();
                tir.set(abscisse - tirPx, ordonnee - tirPx, abscisse + tirPx, ordonnee + tirPx);
                if (tir_mesure.getBut_valide()) { //Tir Reussi
                    canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
                } else { //Tir Rate
                    canvas.drawArc(tir, 0, 360, false, peintureTirRate);
                }
            }
        }
    }

    protected void comptageTirs (ArrayList<Tir> listeTirs) {

        for (Tir p : listeTirs) {
            if (p.getBut_valide()) {
                buts += 1;
            }
            else {
                tirsManques += 1;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            Log.d("Ecriture de la liste", "ok");
            oos.writeObject(listeTirs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
