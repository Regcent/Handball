package com.example.regnarddelagny.handball.AnalyseTirs.VuesDemiTerrain;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import utilitaries.dessins.Tir;

/**
 * Created by RegnarddeLagny on 03/08/2015.
 * VueDemiTerrainReading
 */
public class VueDemiTerrainReading extends VueDemiTerrain {

    //La liste de points utilisee pour sauvegarder les mesures
    ArrayList<Tir> listeTirs;

    //Le point sélectionné lors du premier événement de toucher
    private Point selectedPoint;

    //Les quatre points utilisés pour créer le rectangle dynamiquement
    private Point topLeft = new Point();
    private Point bottomLeft = new Point();
    private Point topRight = new Point();
    private Point bottomRight = new Point();

    //Les rectangles utilisés pour créer l'ombre
    private RectF RectLeftTop;
    private RectF RectLeftBot;
    private RectF RectRightTop;
    private RectF RectRightBot;


    public VueDemiTerrainReading(Context context) {
        super(context);
    }

    // CONSTRUCTOR
    public VueDemiTerrainReading(Context context, String nomFichier, File fichier) {

        super(context);

        mNomFichier = nomFichier;

        setPeintures();

        Resources resources = context.getResources();
        prepareDessins(resources);

        setPointsInit();

        RectLeftBot = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[0], dimPxTerrain[1]);
        RectLeftTop = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[2], dimPxTerrain[1]);
        RectRightBot = new RectF(dimPxTerrain[0], dimPxTerrain[3], dimPxTerrain[0], dimPxTerrain[3]);
        RectRightTop = new RectF(dimPxTerrain[2], dimPxTerrain[3], dimPxTerrain[2], dimPxTerrain[3]);

        listeTirs = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listeTirs = new ArrayList<>((ArrayList<Tir>) ois.readObject());
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


        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (verificationPosition(x, y)) {
                    touch_move(x, y);
                    invalidate();
                }
                break;
        }
        return true;
    }

    protected void touch_start(float x, float y) {
        findNearestPoint((int) x, (int) y);
        changeRectCoordinates();
        comptageTirs(listeTirs);
    }


    private void touch_move(float x, float y) {
        int abscisse = Math.round(x);
        int ordonnee = Math.round(y);

        if (selectedPoint.equals(topRight)) {
            changePointTopRight(abscisse, ordonnee);
        }
        else if (selectedPoint.equals(topLeft)) {
            changePointTopLeft(abscisse, ordonnee);
        }
        else if (selectedPoint.equals(bottomLeft)) {
            changePointBottomLeft(abscisse, ordonnee);
        }
        else if (selectedPoint.equals(bottomRight)) {
            changePointBottomRight(abscisse, ordonnee);
        }
        changeRectCoordinates();
        comptageTirs(listeTirs);
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
                }
                else { //Tir Rate
                    canvas.drawArc(tir, 0, 360, false, peintureTirRate);
                }
            }
        }
        dessinOmbre(canvas);
    }

    protected void comptageTirs (ArrayList<Tir> listeTirs) {

        buts = 0;
        tirsManques = 0;

        for (Tir p : listeTirs) {
            if (inCenterRect(p)) {
                if (p.getBut_valide()) {
                    buts += 1;
                } else {
                    tirsManques += 1;
                }
            }
        }

    }

    private void setPointsInit() {
        setPoint(topLeft, Math.round(dimPxTerrain[0]), Math.round(dimPxTerrain[1]));
        setPoint(bottomLeft, Math.round(dimPxTerrain[0]), Math.round(dimPxTerrain[3]));
        setPoint(topRight, Math.round(dimPxTerrain[2]), Math.round(dimPxTerrain[1]));
        setPoint(bottomRight, Math.round(dimPxTerrain[2]), Math.round(dimPxTerrain[3]));
    }

    private void setPoint(Point point, int abscisse, int ordonnee) {
        point.x = abscisse;
        point.y = ordonnee;
    }

    private void dessinOmbre(Canvas canvas) {
        canvas.drawRect(RectLeftTop, peintureOmbre);
        canvas.drawRect(RectLeftBot, peintureOmbre);
        canvas.drawRect(RectRightTop, peintureOmbre);
        canvas.drawRect(RectRightBot, peintureOmbre);
        canvas.drawPoint(topLeft.x, topLeft.y, peinturePoint);
        canvas.drawPoint(topRight.x, topRight.y, peinturePoint);
        canvas.drawPoint(bottomLeft.x, bottomLeft.y, peinturePoint);
        canvas.drawPoint(bottomRight.x, bottomRight.y, peinturePoint);
        canvas.drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y, peintureContours);
        canvas.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, peintureContours);
        canvas.drawLine(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, peintureContours);
        canvas.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y, peintureContours);
    }

    private void findNearestPoint (int x, int y) {
        int distanceTopLeft = (x - topLeft.x) * (x - topLeft.x) + (y - topLeft.y) * (y - topLeft.y);
        int distanceBottomLeft = (x - bottomLeft.x) * (x - bottomLeft.x) + (y - bottomLeft.y) * (y - bottomLeft.y);
        int distanceTopRight = (x - topRight.x) * (x - topRight.x) + (y - topRight.y) * (y - topRight.y);
        int distanceBottomRight = (x - bottomRight.x) * (x - bottomRight.x) + (y - bottomRight.y) * (y - bottomRight.y);
        int mini = Math.min(distanceTopLeft, distanceBottomLeft);
        mini = Math.min (mini, distanceTopRight);
        mini = Math.min (mini, distanceBottomRight);

        if (mini == distanceTopLeft) {
            selectedPoint = topLeft;
            changePointTopLeft(x, y);
        }
        else if (mini == distanceBottomLeft) {
            selectedPoint = bottomLeft;
            changePointBottomLeft(x, y);
        }
        else if (mini == distanceTopRight) {
            selectedPoint = topRight;
            changePointTopRight(x, y);
        }
        else if (mini == distanceBottomRight) {
            selectedPoint = bottomRight;
            changePointBottomRight(x, y);
        }
    }

    private void changePointTopLeft(int x, int y) {
        topLeft.x = x;
        topLeft.y = y;
        bottomLeft.x = x;
        topRight.y = y;
    }

    private void changePointBottomLeft(int x, int y) {
        bottomLeft.x = x;
        bottomLeft.y = y;
        topLeft.x = x;
        bottomRight.y = y;
    }

    private void changePointTopRight(int x, int y) {
        topRight.x = x;
        topRight.y = y;
        bottomRight.x = x;
        topLeft.y = y;
    }

    private void changePointBottomRight(int x, int y) {
        bottomRight.x = x;
        bottomRight.y = y;
        topRight.x = x;
        bottomLeft.y = y;
    }

    private void changeRectCoordinates() {
        changeRectLeftBot();
        changeRectLeftTop();
        changeRectRightTop();
        changeRectRightBot();
    }

    private void changeRectLeftBot() {
        RectLeftBot.right = bottomLeft.x;
        RectLeftBot.bottom = bottomLeft.y;
    }

    private void changeRectLeftTop() {
        RectLeftTop.left = topLeft.x;
        RectLeftTop.bottom = topLeft.y;
    }

    private void changeRectRightTop() {
        RectRightTop.left = topRight.x;
        RectRightTop.top = topRight.y;
    }

    private void changeRectRightBot() {
        RectRightBot.right = bottomRight.x;
        RectRightBot.top = bottomRight.y;
    }

    private boolean inCenterRect(Tir tir) {

        int absTir = Math.round(tir.getX());
        int ordTir = Math.round(tir.getY());
        return ((absTir >= topLeft.x)
                && (absTir <= bottomRight.x)
                && (ordTir <= bottomRight.y)
                && (ordTir >= topLeft.y));
    }
}