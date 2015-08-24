package com.example.regnarddelagny.handball.VuesDemiTerrain.Tirs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import android.view.MotionEvent;

import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsWriting;

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
public class VueDemiTerrainTirWriting extends VueDemiTerrainTir {

    //La liste de points utilisee pour sauvegarder les mesures
    protected ArrayList<Tir> listeTirs;

    // Utilises pour la mesure de chaque point, afin de s'assurer de ne pas prendre une mesure a chaque evenement de toucher
    private Tir tirMesure;
    private boolean mesureEnCours = false;

    //Le fichier et les flux d'ecriture et de lecture utilises
    private ObjectOutputStream oos;

    //Le point s�lectionn� lors du premier �v�nement de toucher
    private Point selectedPoint;

    //Les quatre points utilis�s pour cr�er le rectangle dynamiquement
    private Point topLeft = new Point();
    private Point bottomLeft = new Point();
    private Point topRight = new Point();
    private Point bottomRight = new Point();

    //Les rectangles utilis�s pour cr�er l'ombre
    private RectF RectLeftTop;
    private RectF RectLeftBot;
    private RectF RectRightTop;
    private RectF RectRightBot;

    //Les peintures sp�cifiques � la vue Reading
    /**
     * A paint used to draw the shadow
     */
    protected Paint peintureOmbre = new Paint();
    /**
     * A paint used to draw the silver linings of the shadow
     */
    protected Paint peintureContours = new Paint();
    /**
     * A paint used to draw the limitating points of the shadow
     */
    protected Paint peinturePoint = new Paint();

    private boolean isInReadingMode = false;
    private boolean isInWritingMode = true;
    private boolean stopListening = false;
    public VueDemiTerrainTirWriting(Context context) {
        super(context);
    }

    // CONSTRUCTOR
    public VueDemiTerrainTirWriting(Context context, String nomFichier, File fichier) {

        super(context);

        mNomFichier = nomFichier;

        setPeintures();

        peintureOmbre.setAntiAlias(true);
        peintureOmbre.setColor(Color.argb(122, 0, 0, 0));
        peintureOmbre.setStyle(Paint.Style.FILL);

        peintureContours.setAntiAlias(true);
        peintureContours.setColor(Color.argb(220, 172, 180, 189));
        peintureContours.setStyle(Paint.Style.STROKE);
        peintureContours.setStrokeWidth(2f);

        peinturePoint.setAntiAlias(true);
        peinturePoint.setColor(Color.argb(220, 172, 180, 189));
        peinturePoint.setStyle(Paint.Style.STROKE);
        peinturePoint.setStrokeWidth(15f);

        Resources resources = context.getResources();
        prepareDessins(resources);

        setPointsInit();

        RectLeftBot = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[0], dimPxTerrain[1]);
        RectLeftTop = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[2], dimPxTerrain[1]);
        RectRightBot = new RectF(dimPxTerrain[0], dimPxTerrain[3], dimPxTerrain[0], dimPxTerrain[3]);
        RectRightTop = new RectF(dimPxTerrain[2], dimPxTerrain[3], dimPxTerrain[2], dimPxTerrain[3]);

        Log.d("taille fichier", fichier.length() + "");

        listeTirs = new ArrayList<>();
        try {
            if (fichier.length() > 100) {
                FileInputStream fis = new FileInputStream(fichier);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listeTirs = new ArrayList<>((ArrayList<Tir>) ois.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(fichier);
            oos = new ObjectOutputStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        comptageTirs(listeTirs);

    }

    @Override
    /**
     * Gestion des evenements de toucher
     */
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        int action = e.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            checkButton(x, y);
        }

        if (!stopListening) {

            if (isInWritingMode) {
                if (verificationPosition(x, y)) {
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            touch_start(x, y);
                            break;
                        case MotionEvent.ACTION_UP:
                            touch_up(x, y);
                            invalidate();
                    }
                }
            } else if (isInReadingMode) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        setPoint_start(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (verificationPosition(x, y)) {
                            setPoint_move(x, y);
                            invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        selectedPoint = null;
                }
            }

            if (action == MotionEvent.ACTION_DOWN) {
                checkButton(x, y);
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            stopListening = false;
        }

        return true;
    }


    protected void setPoint_start(float x, float y) {
        findNearestPoint((int) x, (int) y);
        if(stopListening) {
            selectedPoint = null;
        }
        changeRectCoordinates();
        comptageTirsRect(listeTirs);
    }


    private void setPoint_move(float x, float y) {
        int abscisse = Math.round(x);
        int ordonnee = Math.round(y);

        if (selectedPoint.equals(topRight)) {
            changePointTopRight(abscisse, ordonnee);
        } else if (selectedPoint.equals(topLeft)) {
            changePointTopLeft(abscisse, ordonnee);
        } else if (selectedPoint.equals(bottomLeft)) {
            changePointBottomLeft(abscisse, ordonnee);
        } else if (selectedPoint.equals(bottomRight)) {
            changePointBottomRight(abscisse, ordonnee);
        }
        changeRectCoordinates();
        comptageTirsRect(listeTirs);
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

        canvas.drawRect(abscisseCpx, ordonneePpx + 60, abscisseCpx + 200, ordonneePpx + 200, peintureOmbre);

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
        if (isInReadingMode) {
            dessinOmbre(canvas);
        }
    }

    protected void comptageTirs (ArrayList<Tir> listeTirs) {

        buts = 0;
        tirsManques = 0;

        for (Tir p : listeTirs) {
            if (p.getBut_valide()) {
                buts += 1;
            }
            else {
                tirsManques += 1;
            }
        }
    }

    protected void comptageTirsRect (ArrayList<Tir> listeTirs) {

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

    private void checkButton(float x, float y) {
        if((x < (abscisseCpx + 200)) &&
                (x > abscisseCpx) &&
                (y > ordonneePpx + 60) &&
                (y < ordonneePpx + 200)) {
            if (isInReadingMode) {
                isInReadingMode = false;
                isInWritingMode = true;
                comptageTirs(listeTirs);
            }
            else if (isInWritingMode) {
                isInReadingMode = true;
                isInWritingMode = false;
                comptageTirsRect(listeTirs);
            }
            stopListening = true;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        save();
    }

    public void save() {
        try {
            Log.d("Ecriture de la liste", "ok");
            oos.writeObject(listeTirs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
