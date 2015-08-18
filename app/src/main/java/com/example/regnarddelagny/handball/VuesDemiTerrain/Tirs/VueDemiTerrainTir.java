package com.example.regnarddelagny.handball.VuesDemiTerrain.Tirs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.regnarddelagny.handball.R;

import java.util.ArrayList;

import utilitaries.dessins.Tir;

/**
 * Abstract class used to display a drawing of a half handball playground
 * @author Regnarddelagny
 *
 */
public abstract class VueDemiTerrainTir extends View {

    /**
     * A string containing the file name
     */
    protected String mNomFichier;

    // Les rectangles utilises pour former les dessins
    /**
     * A rectF representing the half playground
     */
    protected RectF demiTerrain;
    /**
     * A rectF used to draw the 9m line
     */
    protected RectF ligne9m;
    /**
     * A rectF used to draw the 6m line
     */
    protected RectF ligne6m;
    /**
     * A rectF used to draw the 7m line
     */
    protected RectF ligne7m;
    /**
     * A rectF used to draw the goal
     */
    protected RectF rectBut;
    /**
     * A rectF used to draw the shots
     */
    protected RectF tir = new RectF(0, 0, 0, 0);

    //Les peintures utilisees pour le dessin
    /**
     * A paint used to draw the lines of the playground
     */
    protected Paint peintureLignesTouche = new Paint();
    /**
     * A paint used to draw the 9m line
     */
    protected Paint peinture9m = new Paint();
    /**
     * A paint used to draw the handball "forbidden zone"
     */
    protected Paint peintureZone = new Paint();
    /**
     * A paint used to draw the 6m line
     */
    protected Paint peinture6m = new Paint();
    /**
     * A paint used to draw the goal
     */
    protected Paint peintureRectBut = new Paint();
    /**
     * A paint used to draw the successful shots
     */
    protected Paint peintureTirGagne = new Paint();
    /**
     * A paint used to draw the failed shots
     */
    protected Paint peintureTirRate = new Paint();
    /**
     * A paint used to draw the text
     */
    protected Paint peintureTexte = new Paint();
    /**
     * A paint used to draw the title
     */
    protected Paint peintureTitre = new Paint();

    //Les chaines de caracteres utilisees pour la legende
    /**
     * A string used in the caption
     */
    protected String nbButsMarques = " " + getResources().getString(R.string.buts_marques);
    /**
     * A string used in the caption
     */
    protected String nbTirsManques = " " + getResources().getString(R.string.tirs_manques);
    /**
     * A string used in the caption
     */
    protected String pourcentageReussite = "% buts = ";

    //Les compteurs de buts/tirs manques
    /**
     * An integer containing the number of successful shots
     */
    protected int buts;
    /**
     * An integer containing the number of failed shots
     */
    protected int tirsManques;

    //Les dimensions de reference en DP
    /**
     * A float table with the 4 "dimensions"(left, top, right, bot) used to design the RectF demiTerrain
     */
    protected float[] dimPxTerrain = {0, 0, 0, 0};
    /**
     * A float table with the 4 "dimensions"(left, top, right, bot) used to design the RectF ligne9m
     */
    protected float[] dimPxLigne9m = {0, 0, 0, 0};
    /**
     * A float table with the 4 "dimensions"(left, top, right, bot) used to design the RectF ligne6m
     */
    protected float[] dimPxLigne6m = {0, 0, 0, 0};
    /**
     * A float table with the 4 "dimensions"(left, top, right, bot) used to design the RectF ligne7m
     */
    protected float[] dimPxLigne7m = {0, 0, 0, 0};
    /**
     * A float table with the 4 "dimensions"(left, top, right, bot) used to design the RectF RectBut
     */
    protected float[] dimPxRectBut = {0, 0, 0, 0};

    //Les valeurs utilisées pour dimensionner les points de mesure
    /**
     * A float used to design the shot drawings
     */
    protected float tirPx = 0;

    //Les valeurs utilisées pour les écritures
    /**
     * A float used to contain the abscissa of the caption circles
     */
    protected float abscisseCpx = 0;  //cercles
    /**
     * A float used to contain the abscissa of the caption text
     */
    protected float abscisseTpx = 0;  //texte
    /**
     * A float used to contain the ordinate of the title
     */
    protected float ordonneeTitrepx = 0;
    /**
     * A float used to contain the ordinate of the successful shots circle in the caption
     */
    protected float ordonneeCGpx = 0;    //cercles "gageiés"
    /**
     * A float used to contain the ordinate of the failed shots circle in the caption
     */
    protected float ordonneeCLpx = 0;   //cercles "loupes"
    /**
     * A float used to contain the ordinate of the percentage in the caption
     */
    protected float ordonneePpx = 0;   //pourcentage
    /**
     * A float used to contain the ordinate of the text for the successful shots in the caption
     */
    protected float ordonneeTGpx = 0;
    /**
     * A float used to contain the ordinate of the text for the failed shots in the caption
     */
    protected float ordonneeTLpx = 0;
    /**
     * A float used to contain the size of the text in the caption
     */
    protected float textSizePx = 0;

    /**
     * Default constructor, never used in the application (VueDemiTerrain is abstract), but only there to make it possible to call View constructor through inheritance
     * @param context a context in which the view should be drawn.
     */
    public VueDemiTerrainTir(Context context) {
        super(context);
    }

    @Override
    /**
     * Method allowing the application to handle the touch events
     * @param e a motionEvent containing all the necessary datas about the touch event
     * @return  a boolean used to know whether the touchevent has been handled
     */
    public abstract boolean onTouchEvent (@NonNull MotionEvent e);

    /**
     * Method used in case the touch_event is a ACTION_DOWN event
     * @param x the abscissa of the touch event
     * @param y the ordinate of the touch event
     */
    protected abstract void touch_start(float x, float y);

    @Override
    /**
     * Method used to draw the view
     * @param canvas a canvas on which the view should be drawn
     */
    protected abstract void onDraw(Canvas canvas);

    /**
     * Method used to set up all the paints used to draw the view
     */
    protected void setPeintures() {
        setBackgroundColor(Color.rgb(255, 255, 153));

        //Initialisation des peintures
        peintureLignesTouche.setAntiAlias(true);
        peintureLignesTouche.setColor(Color.RED);
        peintureLignesTouche.setStyle(Paint.Style.STROKE);
        peintureLignesTouche.setStrokeWidth(4.5f);

        peinture9m.setAntiAlias(true);
        peinture9m.setColor(Color.RED);
        peinture9m.setStyle(Paint.Style.STROKE);
        peinture9m.setStrokeWidth(3f);
        peinture9m.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));

        peintureZone.setAntiAlias(true);
        peintureZone.setColor(Color.CYAN);
        peintureZone.setStyle(Paint.Style.FILL);

        peinture6m.setAntiAlias(true);
        peinture6m.setColor(Color.RED);
        peinture6m.setStyle(Paint.Style.STROKE);
        peinture6m.setStrokeWidth(3f);

        peintureRectBut.setAntiAlias(true);
        peintureRectBut.setColor(Color.BLACK);
        peintureRectBut.setStyle(Paint.Style.STROKE);
        peintureRectBut.setStrokeWidth(1f);

        peintureTirGagne.setAntiAlias(true);
        peintureTirGagne.setColor(Color.GREEN);
        peintureTirGagne.setStyle(Paint.Style.FILL_AND_STROKE);
        peintureTirGagne.setStrokeWidth(3f);

        peintureTirRate.setAntiAlias(true);
        peintureTirRate.setColor(Color.RED);
        peintureTirRate.setStyle(Paint.Style.FILL_AND_STROKE);
        peintureTirRate.setStrokeWidth(3f);

        peintureTexte.setAntiAlias(true);
        peintureTexte.setColor(Color.BLACK);
        peintureTexte.setTextAlign(Paint.Align.CENTER);

        peintureTitre.setAntiAlias(true);
        peintureTitre.setColor(Color.BLACK);
        peintureTitre.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Method used to set up all the dimension values, according to the available resources
     * @param resources the available resources for the view, ie the resources containing the width and height of the screen
     */
    protected void prepareDessins(Resources resources) {

        final float[] dimDpTerrain = {(float) 13.33, (float) 13.33, (float) 464.67, (float) 320};
        final float[] dimDpLigne9m = {(float) -33.33, (float) -132, (float) 511.33, (float) 132};
        final float[] dimDpLigne6m = {(float) 56.93, (float) -146.07, (float) 407.73, (float) 94.66};
        final float[] dimDpLigne7m = {(float) 232.33, (float) 108.66, (float) 245.66, (float) 109.33};
        final float[] dimDpRectBut = {(float) 205.66, (float) 17.33, (float) 272.33, (float) 70.66};

        //Calculation of the Px values, using the predetermined Dp values
        DisplayMetrics metrics = resources.getDisplayMetrics();
        convertDpToPx(dimPxTerrain, dimDpTerrain, metrics);
        convertDpToPx(dimPxLigne9m, dimDpLigne9m, metrics);
        convertDpToPx(dimPxLigne7m, dimDpLigne7m, metrics);
        convertDpToPx(dimPxLigne6m, dimDpLigne6m, metrics);
        convertDpToPx(dimPxRectBut, dimDpRectBut, metrics);
        convertTextValues(metrics);
        peintureTexte.setTextSize(textSizePx);
        peintureTitre.setTextSize((float) 1.5 * textSizePx);

        int hauteur = metrics.heightPixels;

        if (!verificationTerrain(hauteur)) {
            Log.d("Correction", "en cours");
            correctionTaille(hauteur);
        }

        //Setup of the RectF values
        demiTerrain = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[2], dimPxTerrain[3]);
        Log.d ("dim1 + dim3 = ", dimPxTerrain[1] + dimPxTerrain[3] + "");
        ligne9m = new RectF(dimPxLigne9m[0], dimPxLigne9m[1], dimPxLigne9m[2], dimPxLigne9m[3]);
        ligne6m = new RectF(dimPxLigne6m[0], dimPxLigne6m[1], dimPxLigne6m[2], dimPxLigne6m[3]);
        ligne7m = new RectF(dimPxLigne7m[0], dimPxLigne7m[1], dimPxLigne7m[2], dimPxLigne7m[3]);
        rectBut = new RectF(dimPxRectBut[0], dimPxRectBut[1], dimPxRectBut[2], dimPxRectBut[3]);
        tir = new RectF(0, 0, 0, 0);
    }

    /**
     * Method used to check whether the touch event, eg the shot, is in the valid area or not
     * @param x the x-coordinate of the touch event
     * @param y the y-coordinate of the touch event
     * @return  a boolean
     */
    protected boolean verificationPosition (float x, float y) {
        return ((x < (dimPxTerrain[2])) &&
                (x > dimPxTerrain[0]) &&
                (y > dimPxTerrain[1]) &&
                (y < dimPxTerrain[3]));
    }

    /**
     * Method used to draw the playground
     * @param canvas a canvas in which the playground should be drawn
     */
    protected void dessinTerrain (Canvas canvas) {
        canvas.drawRect(demiTerrain, peintureLignesTouche);     //Dessin du demi-terrain
        canvas.drawArc(ligne9m, 34, 112, false, peinture9m);    //Dessin de la ligne des 9m
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peintureZone);   //Dessin de la zone
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peinture6m);     //Dessin de la ligne des 6m
        canvas.drawRect(ligne7m, peinture6m);                   //Dessin de la ligne des 7m
        canvas.drawRect(rectBut, peintureRectBut);              //Dessin du rectangle But
    }

    /**
     * Method used to draw the caption
     * @param canvas a canvas in which the caption should be drawn
     */
    protected void dessinLegende(Canvas canvas) {

        tir.set(abscisseCpx - tirPx, ordonneeCGpx - tirPx, abscisseCpx + tirPx, ordonneeCGpx + tirPx);
        canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
        tir.set(abscisseCpx - tirPx, ordonneeCLpx - tirPx, abscisseCpx + tirPx, ordonneeCLpx + tirPx);
        canvas.drawArc(tir, 0, 360, false, peintureTirRate);
        canvas.drawText(mNomFichier, abscisseTpx, ordonneeTitrepx, peintureTitre);
        canvas.drawText(buts + nbButsMarques, abscisseTpx, ordonneeTGpx, peintureTexte);
        canvas.drawText(tirsManques + nbTirsManques, abscisseTpx, ordonneeTLpx, peintureTexte);
        if ((buts + tirsManques) != 0) {
            int pourcentage = buts * 100 / (buts + tirsManques);
            canvas.drawText(pourcentageReussite + pourcentage + "%", abscisseTpx, ordonneePpx, peintureTexte);
        } else {
            canvas.drawText(pourcentageReussite + "--%", abscisseTpx, ordonneePpx, peintureTexte);
        }
    }

    /**
     * Method used to count the successful shots and the failed shots
     * @param listeTirs an ArrayList containing all the shots
     */
    protected abstract void comptageTirs (ArrayList<Tir> listeTirs);

    /**
     * Method used to convert the predetermined Dp values into Px values, using the metrics of the screen
     * @param dimPx a float table containing the dimensions in px
     * @param dimDp a float table containing the dimensions in dp
     * @param metrics the metrics of the screen
     */
    protected void convertDpToPx(float[] dimPx, float[] dimDp, DisplayMetrics metrics) {
        for(int ind = 0; ind < dimDp.length; ind++) {
            dimPx[ind] =  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimDp[ind], metrics));
        }
    }

    /**
     * Method used to check that the drawing is fitting the screen on which it's displayed
     * @param hauteur the height of the screen
     * @return a boolean indicating whether the drawing is fitting the screen
     */
    protected boolean verificationTerrain(int hauteur) {
        return ((dimPxTerrain[3] + dimPxTerrain[1]) * 1.05 < hauteur);
    }

    /**
     * Method used to change the size of the drawing, in case it's too large for the screen
     * @param hauteur the height of the screen
     */
    protected void correctionTaille(int hauteur) {
        float ratio = hauteur / ((dimPxTerrain[3] + dimPxTerrain[1]) * (float) 1.10);
        correctionTerrain(ratio);
        correctionLigne9m(ratio);
        correctionLigne7m(ratio);
        correctionLigne6m(ratio);
        correctionRectBut(ratio);
        correctionEcriture(ratio);
    }

    /**
     * Method changing the size of the playground drawing
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionTerrain(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxTerrain[ind] *= ratio;
        }
    }

    /**
     * Method changing the size of the 9m line drawing
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionLigne9m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne9m[ind] *= ratio;
        }
    }

    /**
     * Method changing the size of the 7m line drawing
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionLigne7m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne7m[ind] *= ratio;
        }
    }

    /**
     * Method changing the size of the 6m line drawing
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionLigne6m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne6m[ind] *= ratio;
        }
    }

    /**
     * Method changing the size of the goal drawing
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionRectBut(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxRectBut[ind] *= ratio;
        }
    }

    /**
     * Method converting the predetermined dp values for the caption into px values
     * @param metrics the metrics of the screen
     */
    protected void convertTextValues(DisplayMetrics metrics) {

        float abscisseCdp = (float) 478;  //cercles
        float abscisseTdp = (float) 539;  //texte
        float ordonneeTitredp = (float) 40;
        float ordonneeCGdp = (float) 100;    //cercles "gageiés"
        float ordonneeCLdp = (float) 126.67;   //cercles "loupes"
        float ordonneePdp = (float) 163.33;   //pourcentage
        float ordonneeTGdp = (float) 106.66;
        float ordonneeTLdp = (float) 133.33;
        float textSizeDp = (float) 13.33;
        float tirDp = (float) 1.5;

        abscisseCpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, abscisseCdp, metrics));
        abscisseTpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, abscisseTdp, metrics));
        ordonneeTitrepx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeTitredp, metrics));
        ordonneeCGpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeCGdp, metrics));
        ordonneeCLpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeCLdp, metrics));
        ordonneePpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneePdp, metrics));
        ordonneeTGpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeTGdp, metrics));
        ordonneeTLpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeTLdp, metrics));
        textSizePx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSizeDp, metrics));
        tirPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tirDp, metrics);
    }

    /**
     * Method used to change the size of the caption
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionEcriture(float ratio) {

        abscisseCpx *= ratio;
        abscisseTpx *= ratio;
        ordonneeTitrepx *= ratio;
        ordonneeCGpx *= ratio;
        ordonneeCLpx *= ratio;
        ordonneePpx *= ratio;
        ordonneeTGpx *= ratio;
        ordonneeTLpx *= ratio;
        textSizePx *= ratio;
    }

    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
