package com.example.regnarddelagny.handball;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.dessins.Point;


public class TrackGardActivity extends Activity {

    private static String nomFichier;
    private boolean isOnlyReadable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String finNom = intent.getStringExtra("FICHIER");
        nomFichier = "AnalyseTirs/Handball_" + finNom;
        String opening_state = intent.getStringExtra("STATE");
        if (opening_state.equals("UNLOCKED")) {
            isOnlyReadable = false;
        }
        else if (opening_state.equals("LOCKED")) {
            isOnlyReadable = true;
        }
        setContentView(new VueDemiTerrain(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class VueDemiTerrain extends View {

        //La liste de points utilisee pour sauvegarder les mesures
        protected ArrayList<Point> listePoints;

        // Utilises pour la mesure de chaque point, afin de s'assurer de ne pas prendre une mesure a chaque evenement de toucher
        private float mX, mY;
        private Point pointMesure;
        private boolean mesureEnCours = false;

        // Les rectangles utilises pour former les dessins
        /*TODO : Voir si les valeurs sont bonnes pour "tout" ecran ou uniquement le mien*/
        private RectF demiTerrain;
        private RectF ligne9m;
        private RectF ligne6m;
        private RectF ligne7m;
        private RectF rectBut;
        private RectF tir = new RectF(0, 0, 0, 0);

        //Les peintures utilisees pour le dessin
        private Paint peintureLignesTouche = new Paint();
        private Paint peinture9m = new Paint();
        private Paint peintureZone = new Paint();
        private Paint peinture6m = new Paint();
        private Paint peintureRectBut = new Paint();
        private Paint peintureTirGagne = new Paint();
        private Paint peintureTirRate = new Paint();
        private Paint peintureTexte = new Paint();

        //Le fichier et les flux d'ecriture et de lecture utilises
        private File fichier;
        private FileInputStream fis;
        private FileOutputStream fos;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        //Les chaines de caracteres utilisees pour la legende
        private String nbButsMarques;
        private String nbTirsManques;
        private String pourcentageReussite;

        //Les compteurs de buts/tirs manques
        private int buts;
        private int tirsManques;

        //Les dimensions de reference en DP
        private final float[] dimDpTerrain = {(float) 13.33, (float) 13.33, (float) 464.67, (float) 320};
        private final float[] dimDpLigne9m = {(float) -33.33, (float) -132, (float) 511.33, (float) 132};
        private final float[] dimDpLigne6m = {(float) 56.93, (float) -146.07, (float) 407.73, (float) 94.66};
        private final float[] dimDpLigne7m = {(float) 232.33, (float) 108.66, (float) 245.66, (float) 109.33};
        private final float[] dimDpRectBut = {(float) 205.66, (float) 17.33, (float) 272.33, (float) 70.66};

        private float[] dimPxTerrain = {0, 0, 0, 0};
        private float[] dimPxLigne9m = {0, 0, 0, 0};
        private float[] dimPxLigne6m = {0, 0, 0, 0};
        private float[] dimPxLigne7m = {0, 0, 0, 0};
        private float[] dimPxRectBut = {0, 0, 0, 0};

        //Les valeurs utilisées pour dimensionner les points de mesure
        private float tirDp = (float) 1.5;
        private float tirPx = 0;

        //Les valeurs utilisées pour les écritures
        private float abscisseCdp = (float) 478;  //cercles
        private float abscisseTdp = (float) 539;  //texte
        private float ordonneeCGdp = (float) 40;    //cercles "gageiés"
        private float ordonneeCLdp = (float) 66.67;   //cercles "loupes"
        private float ordonneePdp = (float) 100;   //pourcentage
        private float ordonneeTGdp = (float) 46.66;
        private float ordonneeTLdp = (float) 73.33;
        private float textSizeDp = (float) 13.33;

        private float abscisseCpx = 0;  //cercles
        private float abscisseTpx = 0;  //texte
        private float ordonneeCGpx = 0;    //cercles "gageiés"
        private float ordonneeCLpx = 0;   //cercles "loupes"
        private float ordonneePpx = 0;   //pourcentage
        private float ordonneeTGpx = 0;
        private float ordonneeTLpx = 0;
        private float textSizePx = 0;

        // CONSTRUCTOR
        public VueDemiTerrain(Context context) {

            super(context);
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


            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            convertDpToPx(dimPxTerrain, dimDpTerrain, metrics);
            convertDpToPx(dimPxLigne9m, dimDpLigne9m, metrics);
            convertDpToPx(dimPxLigne7m, dimDpLigne7m, metrics);
            convertDpToPx(dimPxLigne6m, dimDpLigne6m, metrics);
            convertDpToPx(dimPxRectBut, dimDpRectBut, metrics);
            convertTextValues(metrics);
            peintureTexte.setTextSize(textSizePx);

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

            listePoints = new ArrayList<>();
            fichier = new File(getFilesDir().getAbsolutePath(), nomFichier);
            try {
                fis = new FileInputStream(fichier);
                ois = new ObjectInputStream(fis);
                listePoints = new ArrayList<>((ArrayList<Point>) ois.readObject());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            comptageTirs(listePoints);

        }

        @Override
        /**
         * Gestion des evenements de toucher
         */
        public boolean onTouchEvent (@NonNull MotionEvent e) {
            if (!(isOnlyReadable)) {
                float x = e.getX();
                float y = e.getY();
                if (verificationPosition(x, y)) {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touch_start(x, y);
                            break;
                        /* case MotionEvent.ACTION_MOVE:
                            touch_move(x, y);
                            break;*/
                        case MotionEvent.ACTION_UP:
                            touch_up(x, y);
                            invalidate();
                            break;
                    }
                }
            }
            return true;
        }


        private void touch_start(float x, float y) {
            mX = x;
            mY = y;
            if (!mesureEnCours) {
                Log.d("TouchStartEvent on", "mX=" + mX + "; mY=" + mY);
                pointMesure = new Point(mX, mY);
                mesureEnCours = true;   //mesureEnCours sert a eviter la creation de points multiples
            }
        }


        private void touch_up(float x, float y) {
            if (y <= dimPxRectBut[3] && y >= dimPxRectBut[1]) {
                if (x <= dimPxRectBut[2] && x >= dimPxRectBut[0]) {
                    pointMesure.setBut_valide();
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
            listePoints.add(pointMesure);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            //canvas.drawColor(Color.rgb(255, 255, 153));
            canvas.setDensity(1);
            canvas.save();

            //canvas.scale(rapportX, rapportY);
            dessinTerrain(canvas);

            dessinLegende(canvas);

            int nbPoints = listePoints.size();

            if (nbPoints > 0) {
                for (int ind = 0; ind < nbPoints; ind++) {
                    Point point_mesure = listePoints.get(ind);
                    float abscisse = point_mesure.getX();
                    float ordonnee = point_mesure.getY();
                    tir.set(abscisse - tirPx, ordonnee - tirPx, abscisse + tirPx, ordonnee + tirPx);
                    if (point_mesure.getBut_valide()) { //Tir Reussi
                        canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
                    }
                    else { //Tir Rate
                        canvas.drawArc(tir, 0, 360, false, peintureTirRate);
                    }
                }

                try {
                    fos = new FileOutputStream(fichier);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(listePoints);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            canvas.restore();
        }

        private boolean verificationPosition (float x, float y) {
            return ((x < (dimPxTerrain[2])) &&
                    (x > dimPxTerrain[0]) &&
                    (y > dimPxTerrain[1]) &&
                    (y < dimPxTerrain[3]));
        }

        public void dessinTerrain (Canvas canvas) {
            canvas.drawRect(demiTerrain, peintureLignesTouche);     //Dessin du demi-terrain
            canvas.drawArc(ligne9m, 34, 112, false, peinture9m);    //Dessin de la ligne des 9m
            canvas.drawArc(ligne6m,(float) 19.5, 141, false, peintureZone);   //Dessin de la zone
            canvas.drawArc(ligne6m,(float) 19.5, 141, false, peinture6m);     //Dessin de la ligne des 6m
            canvas.drawRect(ligne7m, peinture6m);                   //Dessin de la ligne des 7m
            canvas.drawRect(rectBut, peintureRectBut);              //Dessin du rectangle But
        }

        public void dessinLegende(Canvas canvas) {

            tir.set(abscisseCpx - tirPx, ordonneeCGpx - tirPx, abscisseCpx + tirPx, ordonneeCGpx + tirPx);
            canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
            tir.set(abscisseCpx - tirPx, ordonneeCLpx - tirPx, abscisseCpx + tirPx, ordonneeCLpx + tirPx);
            canvas.drawArc(tir, 0, 360, false, peintureTirRate);
            canvas.drawText(buts + nbButsMarques, abscisseTpx, ordonneeTGpx, peintureTexte);
            canvas.drawText(tirsManques + nbTirsManques, abscisseTpx, ordonneeTLpx, peintureTexte);
            if ((buts + tirsManques) != 0) {
                int pourcentage = buts * 100 / (buts + tirsManques);
                canvas.drawText(pourcentageReussite + pourcentage + "%", abscisseTpx, ordonneePpx, peintureTexte);
            } else {
                canvas.drawText(pourcentageReussite + "--%", abscisseTpx, ordonneePpx, peintureTexte);
            }
        }

        private void comptageTirs (ArrayList<Point> listePoints) {

            for (Point p : listePoints) {
                if (p.getBut_valide()) {
                    buts += 1;
                }
                else {
                    tirsManques += 1;
                }
            }

        }

        private void convertDpToPx(float[] dimPx, float[] dimDp, DisplayMetrics metrics) {
            for(int ind = 0; ind < dimDp.length; ind++) {
                dimPx[ind] =  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimDp[ind], metrics));
            }
        }

        private boolean verificationTerrain(int hauteur) {
            return ((dimPxTerrain[3] + dimPxTerrain[1]) * 1.05 < hauteur);
        }

        private void correctionTaille(int hauteur) {
            float ratio = hauteur / ((dimPxTerrain[3] + dimPxTerrain[1]) * (float) 1.10);
            correctionTerrain(ratio);
            correctionLigne9m(ratio);
            correctionLigne7m(ratio);
            correctionLigne6m(ratio);
            correctionRectBut(ratio);
            correctionEcriture(ratio);
        }

        private void correctionTerrain(float ratio) {
            for (int ind = 0; ind < 4; ind++) {
                dimPxTerrain[ind] *= ratio;
            }
        }

        private void correctionLigne9m(float ratio) {
            for (int ind = 0; ind < 4; ind++) {
                dimPxLigne9m[ind] *= ratio;
            }
        }

        private void correctionLigne7m(float ratio) {
            for (int ind = 0; ind < 4; ind++) {
                dimPxLigne7m[ind] *= ratio;
            }
        }

        private void correctionLigne6m(float ratio) {
            for (int ind = 0; ind < 4; ind++) {
                dimPxLigne6m[ind] *= ratio;
            }
        }

        private void correctionRectBut(float ratio) {
            for (int ind = 0; ind < 4; ind++) {
                dimPxRectBut[ind] *= ratio;
            }
        }

        private void convertTextValues(DisplayMetrics metrics) {

            abscisseCpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, abscisseCdp, metrics));
            abscisseTpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, abscisseTdp, metrics));
            ordonneeCGpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeCGdp, metrics));
            ordonneeCLpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeCLdp, metrics));
            ordonneePpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneePdp, metrics));
            ordonneeTGpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeTGdp, metrics));
            ordonneeTLpx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ordonneeTLdp, metrics));
            textSizePx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSizeDp, metrics));
            tirPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tirDp, metrics);
        }

        private void correctionEcriture(float ratio) {

            abscisseCpx *= ratio;
            abscisseTpx *= ratio;
            ordonneeCGpx *= ratio;
            ordonneeCLpx *= ratio;
            ordonneePpx *= ratio;
            ordonneeTGpx *= ratio;
            ordonneeTLpx *= ratio;
            textSizePx *= ratio;
        }
    }
}
