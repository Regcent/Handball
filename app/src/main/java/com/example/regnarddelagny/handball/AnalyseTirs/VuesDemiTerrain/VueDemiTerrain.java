package com.example.regnarddelagny.handball.AnalyseTirs.VuesDemiTerrain;

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

import java.util.ArrayList;

import utilitaries.dessins.Tir;

/**
 * Created by RegnarddeLagny on 03/08/2015.
 * VueDemiTerrain
 */
public abstract class VueDemiTerrain extends View {

    protected String mNomFichier;

    // Les rectangles utilises pour former les dessins
    protected RectF demiTerrain;
    protected RectF ligne9m;
    protected RectF ligne6m;
    protected RectF ligne7m;
    protected RectF rectBut;
    protected RectF tir = new RectF(0, 0, 0, 0);

    //Les peintures utilisees pour le dessin
    protected Paint peintureLignesTouche = new Paint();
    protected Paint peinture9m = new Paint();
    protected Paint peintureZone = new Paint();
    protected Paint peinture6m = new Paint();
    protected Paint peintureRectBut = new Paint();
    protected Paint peintureTirGagne = new Paint();
    protected Paint peintureTirRate = new Paint();
    protected Paint peintureOmbre = new Paint();
    protected Paint peintureContours = new Paint();
    protected Paint peinturePoint = new Paint();
    protected Paint peintureTexte = new Paint();
    protected Paint peintureTitre = new Paint();

    //Les chaines de caracteres utilisees pour la legende
    protected String nbButsMarques;
    protected String nbTirsManques;
    protected String pourcentageReussite;

    //Les compteurs de buts/tirs manques
    protected int buts;
    protected int tirsManques;

    //Les dimensions de reference en DP
    protected float[] dimPxTerrain = {0, 0, 0, 0};
    protected float[] dimPxLigne9m = {0, 0, 0, 0};
    protected float[] dimPxLigne6m = {0, 0, 0, 0};
    protected float[] dimPxLigne7m = {0, 0, 0, 0};
    protected float[] dimPxRectBut = {0, 0, 0, 0};

    //Les valeurs utilisées pour dimensionner les points de mesure
    protected float tirPx = 0;

    //Les valeurs utilisées pour les écritures
    protected float abscisseCpx = 0;  //cercles
    protected float abscisseTpx = 0;  //texte
    protected float ordonneeTitrepx = 0;
    protected float ordonneeCGpx = 0;    //cercles "gageiés"
    protected float ordonneeCLpx = 0;   //cercles "loupes"
    protected float ordonneePpx = 0;   //pourcentage
    protected float ordonneeTGpx = 0;
    protected float ordonneeTLpx = 0;
    protected float textSizePx = 0;

    public VueDemiTerrain(Context context) {
        super(context);
    }

    @Override
    /**
     * Gestion des evenements de toucher
     */
    public abstract boolean onTouchEvent (@NonNull MotionEvent e);

    protected abstract void touch_start(float x, float y);

    @Override
    protected abstract void onDraw(Canvas canvas);

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

        peintureTexte.setAntiAlias(true);
        peintureTexte.setColor(Color.BLACK);
        peintureTexte.setTextAlign(Paint.Align.CENTER);

        peintureTitre.setAntiAlias(true);
        peintureTitre.setColor(Color.BLACK);
        peintureTitre.setTextAlign(Paint.Align.CENTER);
    }

    protected void prepareDessins(Resources resources) {

        final float[] dimDpTerrain = {(float) 13.33, (float) 13.33, (float) 464.67, (float) 320};
        final float[] dimDpLigne9m = {(float) -33.33, (float) -132, (float) 511.33, (float) 132};
        final float[] dimDpLigne6m = {(float) 56.93, (float) -146.07, (float) 407.73, (float) 94.66};
        final float[] dimDpLigne7m = {(float) 232.33, (float) 108.66, (float) 245.66, (float) 109.33};
        final float[] dimDpRectBut = {(float) 205.66, (float) 17.33, (float) 272.33, (float) 70.66};


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

        demiTerrain = new RectF(dimPxTerrain[0], dimPxTerrain[1], dimPxTerrain[2], dimPxTerrain[3]);
        Log.d ("dim1 + dim3 = ", dimPxTerrain[1] + dimPxTerrain[3] + "");
        ligne9m = new RectF(dimPxLigne9m[0], dimPxLigne9m[1], dimPxLigne9m[2], dimPxLigne9m[3]);
        ligne6m = new RectF(dimPxLigne6m[0], dimPxLigne6m[1], dimPxLigne6m[2], dimPxLigne6m[3]);
        ligne7m = new RectF(dimPxLigne7m[0], dimPxLigne7m[1], dimPxLigne7m[2], dimPxLigne7m[3]);
        rectBut = new RectF(dimPxRectBut[0], dimPxRectBut[1], dimPxRectBut[2], dimPxRectBut[3]);
        tir = new RectF(0, 0, 0, 0);

        nbButsMarques = " buts marques";
        nbTirsManques = " tirs manques";
        pourcentageReussite = "% buts = ";
    }
    protected boolean verificationPosition (float x, float y) {
        return ((x < (dimPxTerrain[2])) &&
                (x > dimPxTerrain[0]) &&
                (y > dimPxTerrain[1]) &&
                (y < dimPxTerrain[3]));
    }

    protected void dessinTerrain (Canvas canvas) {
        canvas.drawRect(demiTerrain, peintureLignesTouche);     //Dessin du demi-terrain
        canvas.drawArc(ligne9m, 34, 112, false, peinture9m);    //Dessin de la ligne des 9m
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peintureZone);   //Dessin de la zone
        canvas.drawArc(ligne6m,(float) 19.5, 141, false, peinture6m);     //Dessin de la ligne des 6m
        canvas.drawRect(ligne7m, peinture6m);                   //Dessin de la ligne des 7m
        canvas.drawRect(rectBut, peintureRectBut);              //Dessin du rectangle But
    }

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

    protected abstract void comptageTirs (ArrayList<Tir> listeTirs);

    protected void convertDpToPx(float[] dimPx, float[] dimDp, DisplayMetrics metrics) {
        for(int ind = 0; ind < dimDp.length; ind++) {
            dimPx[ind] =  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimDp[ind], metrics));
        }
    }

    protected boolean verificationTerrain(int hauteur) {
        return ((dimPxTerrain[3] + dimPxTerrain[1]) * 1.05 < hauteur);
    }

    protected void correctionTaille(int hauteur) {
        float ratio = hauteur / ((dimPxTerrain[3] + dimPxTerrain[1]) * (float) 1.10);
        correctionTerrain(ratio);
        correctionLigne9m(ratio);
        correctionLigne7m(ratio);
        correctionLigne6m(ratio);
        correctionRectBut(ratio);
        correctionEcriture(ratio);
    }

    protected void correctionTerrain(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxTerrain[ind] *= ratio;
        }
    }

    protected void correctionLigne9m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne9m[ind] *= ratio;
        }
    }

    protected void correctionLigne7m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne7m[ind] *= ratio;
        }
    }

    protected void correctionLigne6m(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxLigne6m[ind] *= ratio;
        }
    }

    protected void correctionRectBut(float ratio) {
        for (int ind = 0; ind < 4; ind++) {
            dimPxRectBut[ind] *= ratio;
        }
    }

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
