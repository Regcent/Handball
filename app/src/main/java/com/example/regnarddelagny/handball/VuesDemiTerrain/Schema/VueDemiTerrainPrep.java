package com.example.regnarddelagny.handball.VuesDemiTerrain.Schema;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import com.example.regnarddelagny.handball.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.Schemas.Schema;
import utilitaries.dessins.dessinsJoueurs.Attacker;
import utilitaries.dessins.dessinsJoueurs.Defender;
import utilitaries.dessins.dessinsJoueurs.PointSerial;


/**
 * Created by RegnarddeLagny on 07/08/2015.
 * Coucou!
 */
public class VueDemiTerrainPrep extends VueDemiTerrainSchema {

    private ArrayList<PointSerial> listeJoueurs;
    private ArrayList<PointSerial> drawnLine;
    private ArrayList<ArrayList<PointSerial>> lines;
    private ArrayList<PointSerial> initialPositions;
    private Schema schemaTactique;

    private ObjectOutputStream oos;

    private String mNomFichier;

    private RectF intPlayer;
    private RectF extPlayer;
    //private RectF playerCircle;

    private PointSerial selectedPoint;
    private PointSerial start;
    private PointSerial finish;

    private boolean isInDrawingMode = false;
    private boolean toAnimate = false;
    private boolean isInMovingMode = true;

    private Paint blackInk = new Paint();
    private Paint blueInk = new Paint();

    private float mX;
    private float mY;

    private int linesIndex = 0;
    private int currentIndex = 0;

    public VueDemiTerrainPrep(Context context) {

        super(context);
    }

    public VueDemiTerrainPrep(Context context, String nomFichier, File fichier) {

        super(context);

        mNomFichier = nomFichier;

        listeJoueurs= new ArrayList<>();
        drawnLine = new ArrayList<>();
        lines = new ArrayList<>();
        initialPositions = new ArrayList<>();
        for (int ind = 0; ind < 12; ind ++) {
            initialPositions.add(new PointSerial(0, 0));
        }

        setPeintures();

        Resources resources = context.getResources();
        prepareDessins(resources);
        prepareDrawables(resources);
        intPlayer = new RectF(dimIntPlayerPx[0], 0, dimIntPlayerPx[2], 0);
        extPlayer = new RectF(dimExtPlayerPx[0], 0, dimExtPlayerPx[2], 0);

        blackInk.setAntiAlias(true);
        blackInk.setColor(Color.BLACK);
        blackInk.setStyle(Paint.Style.STROKE);
        blackInk.setStrokeWidth(2f);

        blueInk.setAntiAlias(true);
        blueInk.setColor(Color.BLUE);
        blueInk.setStyle(Paint.Style.FILL_AND_STROKE);
        blueInk.setStrokeWidth(2f);

        try {
            FileInputStream fis = new FileInputStream(fichier);
            ObjectInputStream ois = new ObjectInputStream(fis);
            schemaTactique = new Schema((Schema) ois.readObject());
            listeJoueurs.addAll(schemaTactique.getListeJoueurs());
            lines = schemaTactique.getLines();
        } catch (Exception e) {
            e.printStackTrace();
            fillListeJoueurs();
            schemaTactique = new Schema(mNomFichier);
        }
        try {
            FileOutputStream fos = new FileOutputStream(fichier);
            oos = new ObjectOutputStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(@NonNull MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        int action = e.getAction();

        if(isInDrawingMode) {
            if (isInPlayground(Math.round(x), Math.round(y))) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        draw_start(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        draw_move(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        draw_finish(x, y);
                }
            }
        }
        else if (isInMovingMode) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    selectedPoint = null;
            }
        }
        invalidate();
        if (!toAnimate) {  //The animation should go to its end before we change mode
            if (action == MotionEvent.ACTION_DOWN) {
                checkDrawingButton(x, y);
                checkAnimatingButton(x, y);
                checkMovingButton(x, y);
                checkEraseButton(x, y);
            }
        }
        return true;
    }

    protected void touch_start(float x, float y) {
        findNearestPlayer((int) x, (int) y);
        changeSelectedPointCoordinates((int) x, (int) y);
    }

    protected void touch_move(float x, float y) {
        changeSelectedPointCoordinates((int) x, (int) y);
    }

    protected void draw_start(float x, float y) {
        drawnLine = new ArrayList<>();
        mX = x;
        mY = y;
        drawnLine.add(new PointSerial(Math.round(x), Math.round(y)));
    }

    protected void draw_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= tolerancePx || dy >= tolerancePx) {
            mX = x;
            mY = y;
            drawnLine.add(new PointSerial(Math.round(x), Math.round(y)));
        }
    }

    protected void draw_finish(float x, float y) {
        drawnLine.add(new PointSerial(Math.round(x), Math.round(y)));
        lines.add(drawnLine);
        Log.d("drawnLine size", drawnLine.size() + "");

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!toAnimate) {
            dessinTerrain(canvas);

           for (PointSerial player : listeJoueurs) {
               drawPlayer(player, canvas);
           }

            dessinBoutons(canvas);
            for (ArrayList<PointSerial> table : lines) {
                for (int ind = 0; ind < table.size() - 1; ind++) {
                    start = table.get(ind);
                    finish = table.get(ind + 1);
                    canvas.drawLine(start.getX(), start.getY(), finish.getX(), finish.getY(), blueInk);
                }
            }

            for (int ind = 0; ind < drawnLine.size() - 1; ind++) {
                start = drawnLine.get(ind);
                finish = drawnLine.get(ind + 1);
                canvas.drawLine(start.getX(), start.getY(), finish.getX(), finish.getY(), blueInk);
            }
            Drawable ballon = getResources().getDrawable(R.drawable.ballon);
            ballon.setBounds(100, 100, 125, 125);
            ballon.draw(canvas);
        }
        else {
            dessinTerrain(canvas);
            dessinBoutons(canvas);

            for (PointSerial player : listeJoueurs) {
                drawPlayer(player, canvas);
            }


            if (linesIndex < lines.size()) {
                ArrayList<PointSerial> table = lines.get(linesIndex);
                if (currentIndex < table.size()) {
                    start = table.get(currentIndex);
                    findNearestAttacker(start.getX(), start.getY());
                    changeSelectedPointCoordinates(start.getX(), start.getY());
                    Log.d("dessin", "point selectionne");
                    if (selectedPoint != null) {
                        drawPlayer(selectedPoint, canvas);
                        currentIndex++;
                    }
                }
                else {
                    drawPlayer(selectedPoint, canvas);
                    selectedPoint = null;
                    linesIndex++;
                    currentIndex = 0;
                }
                invalidate();
            }
            else {
                linesIndex = 0;
                toAnimate = false;
                for (int ind = 0; ind < 12; ind ++) {
                    start = listeJoueurs.get(ind);
                    finish = initialPositions.get(ind);
                    start.setX(finish.getX());
                    start.setY(finish.getY());
                }
                selectedPoint = null;
            }
        }
    }

    protected void drawPlayer (PointSerial player, Canvas canvas) {
        if (player instanceof Attacker) {
            drawAttacker(player.getX(), player.getY(), peintureAttaquants, canvas);
        }
        else if (player instanceof Defender) {
            drawDefender(player.getX(), player.getY(), peintureDefenseurs, canvas);
        }
    }
    protected void drawDefender(int xPlayer, int yPlayer, Paint peinture, Canvas canvas) {

        /*
        The substraction of dimExtPlayerPx[3] is here to recenter the second rect, used to draw the inner circle.
        In fact, for attackers as defenders, the reference point is the center of the extern circle, so we got to "translate" this point to replace the inner circle good.
         */

        if (isInPlayground(xPlayer, yPlayer)) {
            float milieuTerrain = (dimPxTerrain[2] + dimPxTerrain[0]) / 2;
            float translatedAbs = xPlayer - milieuTerrain;
            float translatedOrd = yPlayer - dimPxTerrain[1] + (float) 0.5 * dimPxTerrain[3];
            double angleRot = Math.atan2(translatedAbs, translatedOrd);
            double yRef = translatedAbs * Math.sin(angleRot) + translatedOrd * Math.cos(angleRot);
            intPlayer.top = dimIntPlayerPx[1] - dimExtPlayerPx[3] + (float) yRef;
            intPlayer.bottom = dimIntPlayerPx[3] - dimExtPlayerPx[3] + (float) yRef;
            extPlayer.top = dimExtPlayerPx[1] + (float) yRef;
            extPlayer.bottom = dimExtPlayerPx[3] + (float) yRef;
            canvas.save();
            canvas.translate(milieuTerrain, dimPxTerrain[1] - (float) 0.5 * dimPxTerrain[3]);
            canvas.rotate((float) -(angleRot * 180 / Math.PI));
        } else {
            intPlayer.top = dimIntPlayerPx[1] - dimExtPlayerPx[3];
            intPlayer.bottom = dimIntPlayerPx[3] - dimExtPlayerPx[3];
            extPlayer.top = dimExtPlayerPx[1];
            extPlayer.bottom = dimExtPlayerPx[3];
            canvas.save();
            canvas.translate(xPlayer, yPlayer);
        }
        canvas.drawArc(extPlayer, 180, 180, false, peinture);
        canvas.drawArc(intPlayer, 12, 156, false, peinture);
        canvas.restore();
    }

    protected void drawAttacker(int xPlayer, int yPlayer, Paint peinture, Canvas canvas) {

        /*
        The substraction of dimExtPlayerPx[3] is here to recenter the second rect, used to draw the inner circle.
        In fact, for attackers as defenders, the reference point is the center of the extern circle, so we got to "translate" this point to replace the inner circle good.
         */

        if (isInPlayground(xPlayer, yPlayer)) {
            float milieuTerrain = (dimPxTerrain[2] + dimPxTerrain[0]) / 2;
            float translatedAbs = xPlayer - milieuTerrain;
            float translatedOrd = yPlayer - dimPxTerrain[1];
            double angleRot = Math.atan2(translatedAbs, translatedOrd);
            double yRef = translatedAbs * Math.sin(angleRot) + translatedOrd * Math.cos(angleRot);
            intPlayer.top = dimIntPlayerPx[1] + dimExtPlayerPx[3] + (float) yRef;
            intPlayer.bottom = dimIntPlayerPx[3] + dimExtPlayerPx[3] + (float) yRef;
            extPlayer.top = dimExtPlayerPx[1] + (float) yRef;
            extPlayer.bottom = dimExtPlayerPx[3] + (float) yRef;
            canvas.save();
            canvas.translate(milieuTerrain, dimPxTerrain[1]);
            canvas.rotate((float) -(angleRot * 180 / Math.PI));
        } else {
            intPlayer.top = dimIntPlayerPx[1] + dimExtPlayerPx[3];
            intPlayer.bottom = dimIntPlayerPx[3] + dimExtPlayerPx[3];
            extPlayer.top = dimExtPlayerPx[1];
            extPlayer.bottom = dimExtPlayerPx[3];
            canvas.save();
            canvas.translate(xPlayer, yPlayer);
        }
        canvas.drawArc(extPlayer, 0, 180, false, peinture);
        canvas.drawArc(intPlayer, 192, 156, false, peinture);
        canvas.restore();
    }

    public boolean isInPlayground(int xPlayer, int yPlayer) {
        return ((xPlayer < (dimPxTerrain[2])) &&
                (xPlayer > dimPxTerrain[0]) &&
                (yPlayer > dimPxTerrain[1]) &&
                (yPlayer < dimPxTerrain[3]));
    }

    /*protected void fillListeDef() {

        /*
        1.30 and 0.7 values have been found experimentally, to adjust the players initial positions
         *

        float decalageAbscisse = 2 * dimExtPlayerPx[2];
        for (int ind = 2; ind <= 7; ind++) {
            listeDef.add(new Defender(Math.round(dimPxTerrain[2] + (float) 1.30 *decalageAbscisse), Math.round(dimPxTerrain[1] + ((float) 0.7 + ind) * ordonneeBasePx)));
        }
    }

    protected void fillListeAtt() {

        float ecart = dimExtPlayerPx[3] - dimExtPlayerPx[1];
        float decalageAbscisse = 2 * dimExtPlayerPx[2];
        for (int ind = 2; ind <= 7; ind++) {
            listeAtt.add(new Attacker(Math.round(dimPxTerrain[2] + (float) 2.94 * decalageAbscisse), Math.round(dimPxTerrain[1] + (((float) 0.7 + ind) * ordonneeBasePx) - ecart / 2)));
        }
    }*/

    protected void fillListeJoueurs() {

        float decalageAbscisse = 2 * dimExtPlayerPx[2];
        float ecart = dimExtPlayerPx[3] - dimExtPlayerPx[1];

        for (int ind = 2; ind <= 7; ind++) {
            listeJoueurs.add(new Defender(Math.round(dimPxTerrain[2] + (float) 1.30 *decalageAbscisse), Math.round(dimPxTerrain[1] + ((float) 0.7 + ind) * ordonneeBasePx)));
        }
        for (int ind = 2; ind <= 7; ind++) {
            listeJoueurs.add(new Attacker(Math.round(dimPxTerrain[2] + (float) 2.94 * decalageAbscisse), Math.round(dimPxTerrain[1] + (((float) 0.7 + ind) * ordonneeBasePx) - ecart / 2)));
        }
    }

    protected void findNearestPlayer(int x, int y) {
        float min = margeClicPx;
        findNearestDefender(x, y, min);
        findNearestAttacker(x, y, min);
    }

    protected void findNearestDefender(int x, int y, float min) {
        long norme;
        float deltaX;
        float deltaY;
        for (PointSerial p : listeJoueurs) {
            if (p instanceof Defender) {
                deltaX = p.getX() - x;
                deltaY = p.getY() - y;
                norme = Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
                Log.d("Norme =", norme + "");
                if (norme < min) {
                    min = norme;
                    selectedPoint = p;
                }
            }
        }
    }

    protected void findNearestAttacker(int x, int y) {
        float min = 2 * margeClicPx;
        long norme;
        float deltaX;
        float deltaY;
        for (PointSerial p : listeJoueurs) {
            if (p instanceof Attacker) {
                deltaX = p.getX() - x;
                deltaY = p.getY() - y;
                norme = Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
                Log.d("Norme =", norme + "");
                if (norme < min) {
                    min = norme;
                    selectedPoint = p;
                }
            }
        }
    }

    protected void findNearestAttacker(int x, int y, float min) {
        long norme;
        float deltaX;
        float deltaY;
        for (PointSerial p : listeJoueurs) {
            if (p instanceof Attacker) {
                deltaX = p.getX() - x;
                deltaY = p.getY() - y;
                norme = Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
                Log.d("Norme =", norme + "");
                if (norme < min) {
                    min = norme;
                    selectedPoint = p;
                }
            }
        }
    }

    protected void changeSelectedPointCoordinates(int x, int y) {
        if (selectedPoint != null) {
            selectedPoint.setX(x);
            selectedPoint.setY(y);
        }
    }

    /* protected void drawPlayerCircle(float x, float y, Canvas canvas) {
        playerCircle.left = x - 65;
        playerCircle.top = y - 65;
        playerCircle.right = x + 65;
        playerCircle.bottom = y + 65;
        canvas.drawArc(playerCircle, 0, 360, false, peinture6m);
    }*/

    private void checkDrawingButton(float x, float y) {

        if ((x < buttonAbscissasPx[3] &&
                (x > buttonAbscissasPx[2]) &&
                (y > buttonOrdinatesPx[0]) &&
                (y < buttonOrdinatesPx[1]))) {
            isInDrawingMode = true;
            isInMovingMode = false;
            toAnimate = false;
        }
    }

    private void checkAnimatingButton(float x, float y) {

        PointSerial att;
        PointSerial copie;

        if ((x < buttonAbscissasPx[1] &&
                (x > buttonAbscissasPx[0]) &&
                (y > buttonOrdinatesPx[2]) &&
                (y < buttonOrdinatesPx[3]))) {
            toAnimate = true;
            isInMovingMode = false;
            isInDrawingMode = false;
            for (int ind = 0; ind < 12; ind ++) {
                att = listeJoueurs.get(ind);
                copie = initialPositions.get(ind);
                copie.setX(att.getX());
                copie.setY(att.getY());
            }
        }
    }

    private void checkMovingButton(float x, float y) {

        if ((x < buttonAbscissasPx[1] &&
                (x > buttonAbscissasPx[0]) &&
                (y > buttonOrdinatesPx[0]) &&
                (y < buttonOrdinatesPx[1]))) {
            isInMovingMode = true;
            isInDrawingMode = false;
            toAnimate = false;
        }
    }

    private void checkEraseButton(float x, float y) {

        if ((x < buttonAbscissasPx[3] &&
                (x > buttonAbscissasPx[2]) &&
                (y > buttonOrdinatesPx[2]) &&
                (y < buttonOrdinatesPx[3]))) {
            isInMovingMode = false;
            isInDrawingMode = false;
            toAnimate = false;
            lines.clear();
            drawnLine.clear();
        }
    }



    private void dessinBoutons(Canvas canvas) {
        //Dessin premier bouton
        boutonBleu.setBounds(Button1);
        moving.setBounds(Button1);
        if (isInMovingMode) {
            boutonBleu.setState(PRESSED_ENABLED_STATE_SET);
        }
        boutonBleu.draw(canvas);
        moving.draw(canvas);
        boutonBleu.setState(EMPTY_STATE_SET);

        //Dessin second bouton
        boutonBleu.setBounds(Button2);
        drawing.setBounds(Button2);
        if (isInDrawingMode) {
            boutonBleu.setState(PRESSED_ENABLED_STATE_SET);
        }
        boutonBleu.draw(canvas);
        drawing.draw(canvas);
        boutonBleu.setState(EMPTY_STATE_SET);

        //Dessin troisième bouton
        boutonBleu.setBounds(Button3);
        animate.setBounds(Button3);
        if (toAnimate) {
            boutonBleu.setState(PRESSED_ENABLED_STATE_SET);
        }
        boutonBleu.draw(canvas);
        animate.draw(canvas);
        boutonBleu.setState(EMPTY_STATE_SET);

        //Dessin quatrième bouton
        boutonBleu.setBounds(Button4);
        erase.setBounds(Button4);
        boutonBleu.draw(canvas);
        erase.draw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /*SAVE FILE*/
        //save();
    }

    public void save() {
        schemaTactique.setListeJoueurs(listeJoueurs);
        schemaTactique.setLines(lines);

        try {
            Log.d("Ecriture de la liste", "ok");
            oos.writeObject(schemaTactique);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
