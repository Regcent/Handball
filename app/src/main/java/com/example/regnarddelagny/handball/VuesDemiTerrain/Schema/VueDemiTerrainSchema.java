package com.example.regnarddelagny.handball.VuesDemiTerrain.Schema;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.regnarddelagny.handball.R;

/**
 * Created by RegnarddeLagny on 07/08/2015.
 * abcd
 */
public abstract class VueDemiTerrainSchema extends View {

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
     * A rect used to draw the first button
     */
    protected Rect Button1;

    /**
     * A rect used to draw the second button
     */
    protected Rect Button2;

    /**
     * A rect used to draw the third button
     */
    protected Rect Button3;

    /**
     * A rect used to draw the fourth button
     */
    protected Rect Button4;

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
     * A paint used to draw attackers
     */
    protected Paint peintureAttaquants = new Paint();

    /**
     * A paint used to draw defenders
     */
    protected Paint peintureDefenseurs = new Paint();

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
     * A float table containing the base values for intPlayer RectF
     */
    protected float[] dimIntPlayerPx = {0, 0, 0, 0};

    /**
     * A float table containing the top value for extPlayer RectF
     */
    protected float[] dimExtPlayerPx = {0, 0, 0, 0};

    /**
     * A float table containing the abscissas values for the buttons
     */
    protected float[] buttonAbscissasPx = {0, 0, 0, 0};

    /**
     * A float table containing the ordinate values for the buttons
     */
    protected float[] buttonOrdinatesPx = {0, 0, 0, 0};

    /**
     * A float used during creation of the view, to place the views
     */
    protected float ordonneeBasePx = 0;

    /**
     * A float used to find whcih point was clicked
     */
    protected float margeClicPx = 0;

    /**
     * A float containing the minimum distance to be done to save another point
     */
    protected float tolerancePx = 0;

    /**
     * A drawable containing the drawing icon
     */
    protected Drawable drawing;

    /**
     * A drawable containing the selecting/moving icon
     */
    protected Drawable moving;

    /**
     * A drawable containing the animating icon
     */
    protected Drawable animate;

    /**
     * A drawable containing the erase icon
     */
    protected Drawable erase;

    /**
     * A drawable containing the button shape and style
     */
    protected Drawable boutonBleu;

    /**
     * Default constructor, never used in the application (VueDemiTerrain is abstract), but only there to make it possible to call View constructor through inheritance
     * @param context a context in which the view should be drawn.
     */
    public VueDemiTerrainSchema(Context context) {
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

        peintureAttaquants.setAntiAlias(true);
        peintureAttaquants.setColor(Color.BLUE);
        peintureAttaquants.setStyle(Paint.Style.STROKE);
        //peintureAttaquants.setStyle(Paint.Style.FILL_AND_STROKE); //Versionr ronds
        peintureAttaquants.setStrokeWidth(4f);

        peintureDefenseurs.setAntiAlias(true);
        peintureDefenseurs.setColor(Color.GREEN);
        peintureDefenseurs.setStyle(Paint.Style.STROKE);
        //peintureDefenseurs.setStyle(Paint.Style.FILL_AND_STROKE); //Version ronds
        peintureDefenseurs.setStrokeWidth(4f);
    }

    /**
     * Method used to set up all the dimension values, according to the available resources.
     * All the DP values have been found experimentally
     * @param resources the available resources for the view, ie the resources containing the width and height of the screen
     */
    protected void prepareDessins(Resources resources) {

        final float[] dimDpTerrain = {(float) 13.33, (float) 13.33, (float) 464.67, (float) 320};
        final float[] dimDpLigne9m = {(float) 3.33, (float) -182, (float) 475.33, (float) 159};
        final float[] dimDpLigne6m = {(float) 35, (float) -196.07, (float) 443, (float) 119.67};
        final float[] dimDpLigne7m = {(float) 232.33, (float) 132.67, (float) 245.67, (float) 134.33};
        final float[] dimIntPlayerDp = {(float) -9, (float) -9, (float) 9, (float) 9}; //Version joueurs;
        //final float[] dimIntPlayerDp={-9, -9, 9, 9}; //Version ronds
        final float[] dimExtPlayerDp = {(float) -16, (float) -16, (float) 16, (float) 16};
        final float[] buttonAbscissaDp = {(float) 489.33, (float) 522.67, (float) 542.67, (float) 576};
        final float[] buttonOrdinateDp = {(float) 13.33, 44, (float) 57.33, 88};
        final float ordonneeBaseDp = 40;
        final float margeClicDp = (float) 30;
        final float toleranceDp = (float) 6.67;

        //Calculation of the Px values, using the predetermined Dp values
        DisplayMetrics metrics = resources.getDisplayMetrics();
        convertDpToPx(dimPxTerrain, dimDpTerrain, metrics);
        convertDpToPx(dimPxLigne9m, dimDpLigne9m, metrics);
        convertDpToPx(dimPxLigne7m, dimDpLigne7m, metrics);
        convertDpToPx(dimPxLigne6m, dimDpLigne6m, metrics);
        convertDpToPx(dimIntPlayerPx, dimIntPlayerDp, metrics);
        convertDpToPx(dimExtPlayerPx, dimExtPlayerDp, metrics);
        convertDpToPx(buttonAbscissasPx, buttonAbscissaDp, metrics);
        convertDpToPx(buttonOrdinatesPx, buttonOrdinateDp, metrics);
        ordonneeBasePx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeBaseDp, metrics));
        margeClicPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margeClicDp, metrics));
        tolerancePx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toleranceDp, metrics));
        int hauteur = metrics.heightPixels;

        if (!verificationTerrain(hauteur)) {
            Log.d("Correction", "en cours");
            correctionTaille(hauteur);
        }

        //Setup of the RectF values
        demiTerrain = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[2], dimPxTerrain[3]);
        Log.d("dim1 + dim3 = ", dimPxTerrain[1] + dimPxTerrain[3] + "");
        ligne9m = new RectF(dimPxLigne9m[0], dimPxLigne9m[1], dimPxLigne9m[2], dimPxLigne9m[3]);
        ligne6m = new RectF(dimPxLigne6m[0], dimPxLigne6m[1], dimPxLigne6m[2], dimPxLigne6m[3]);
        ligne7m = new RectF(dimPxLigne7m[0], dimPxLigne7m[1], dimPxLigne7m[2], dimPxLigne7m[3]);
        Button1 = new Rect(Math.round(buttonAbscissasPx[0]), Math.round(buttonOrdinatesPx[0]), Math.round(buttonAbscissasPx[1]), Math.round(buttonOrdinatesPx[1]));
        Button2 = new Rect(Math.round(buttonAbscissasPx[2]), Math.round(buttonOrdinatesPx[0]), Math.round(buttonAbscissasPx[3]), Math.round(buttonOrdinatesPx[1]));
        Button3 = new Rect(Math.round(buttonAbscissasPx[0]), Math.round(buttonOrdinatesPx[2]), Math.round(buttonAbscissasPx[1]), Math.round(buttonOrdinatesPx[3]));
        Button4 = new Rect(Math.round(buttonAbscissasPx[2]), Math.round(buttonOrdinatesPx[2]), Math.round(buttonAbscissasPx[3]), Math.round(buttonOrdinatesPx[3]));
    }

    /**
     * Method used to load all drawables
     */
    protected void prepareDrawables(Resources resources) {
        drawing = resources.getDrawable(R.drawable.ic_edit_white_48dp);
        moving = resources.getDrawable(R.drawable.ic_open_with_white_48dp);
        animate = resources.getDrawable(R.drawable.ic_play_arrow_white_48dp);
        erase = resources.getDrawable(R.drawable.ic_clear_white_48dp);
        boutonBleu = resources.getDrawable(R.drawable.boutonbleu);
    }

    /**
     * Method used to draw the playground
     * @param canvas a canvas in which the playground should be drawn
     */
    protected void dessinTerrain (Canvas canvas) {
        canvas.drawArc(ligne9m, 19, 142, false, peinture9m);    //Dessin de la ligne des 9m
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peintureZone);   //Dessin de la zone
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peinture6m);     //Dessin de la ligne des 6m
        canvas.drawRect(ligne7m, peinture6m);                   //Dessin de la ligne des 7m
        canvas.drawRect(demiTerrain, peintureLignesTouche);     //Dessin du demi-terrain
    }

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
        correctionTaillePlayers(ratio);
        correctionButtons(ratio);
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
     * Method changing the size of the players
     * @param ratio a float containing the ratio used to change the size
     */
    protected void  correctionTaillePlayers(float ratio) {
        for (int ind = 0; ind < 4; ind ++) {
            dimExtPlayerPx[ind] *= ratio;
            dimIntPlayerPx[ind] *= ratio;
        }
        ordonneeBasePx *= ratio;
    }

    /**
     * Method changing the size of the buttons
     * @param ratio a float containing the ratio used to change the size
     */
    protected void correctionButtons(float ratio) {
        for (int ind = 0; ind < 4; ind ++){
            buttonAbscissasPx[ind] *= ratio;
            buttonOrdinatesPx[ind] *= ratio;
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
