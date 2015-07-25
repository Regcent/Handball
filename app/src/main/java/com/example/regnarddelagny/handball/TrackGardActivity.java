package com.example.regnarddelagny.handball;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utilitaries.dessins.Point;


public class TrackGardActivity extends Activity {

    private static int hauteur;
    private static int largeur;
    private static String nomFichier;
    private static boolean isOnlyReadable;

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
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        hauteur = metrics.heightPixels;
        largeur = metrics.widthPixels;
        Log.d("hauteur", "" + hauteur);
        Log.d("largeur", "" + largeur);
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
        private RectF demiTerrain = new RectF(20, 20, largeur - 200, hauteur - 60);
        private RectF ligne9m = new RectF(- 50, - (9 * (hauteur - 100) / 20), largeur - 130, 9 * (hauteur - 100) / 20);
        private RectF ligne6m = new RectF(- 90 + (largeur - 20) / 5, 20 - (3 * (hauteur - 100) / 10), - 90 + 4 * (largeur - 20) / 5, 10 + 3 * (hauteur - 100) / 10);
        private RectF ligne7m = new RectF(largeur / 2 - 100, 9 + (7 * (hauteur - 100) / 20), largeur / 2 - 80, 10 + (7 * (hauteur - 100) / 20));
        private RectF rectBut = new RectF(largeur / 2 - 140, - 40 + (3 * (hauteur - 100) / 20), largeur / 2 - 40, 40 + (3 * (hauteur - 100) / 20));
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
            peintureTirGagne.setStyle(Paint.Style.STROKE);
            peintureTirGagne.setStrokeWidth(3f);

            peintureTirRate.setAntiAlias(true);
            peintureTirRate.setColor(Color.RED);
            peintureTirRate.setStyle(Paint.Style.STROKE);
            peintureTirRate.setStrokeWidth(3f);

            peintureTexte.setAntiAlias(true);
            peintureTexte.setColor(Color.BLACK);
            peintureTexte.setTextAlign(Paint.Align.CENTER);
            peintureTexte.setTextSize(20);

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
            if (y <= 40 + (3 * (hauteur - 100) / 20) && y >= - 40 + (3 * (hauteur - 100) / 20)) {
                if (x <= largeur / 2 - 40 && x >= largeur / 2 - 140) {
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

            dessinTerrain(canvas);

            dessinLegende(canvas);

            int nbPoints = listePoints.size();

            if (nbPoints > 0) {
                for (int ind = 0; ind < nbPoints; ind++) {
                    Point point_mesure = listePoints.get(ind);
                    float abscisse = point_mesure.getX();
                    float ordonnee = point_mesure.getY();
                    if (point_mesure.getBut_valide()) { //Tir Reussi
                        tir.set(abscisse - 4, ordonnee - 4, abscisse + 4, ordonnee + 4);
                        canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
                    }
                    else { //Tir Rate
                        tir.set(abscisse - 2, ordonnee - 2, abscisse + 2, ordonnee + 2);
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
        }

        private boolean verificationPosition (float x, float y) {
            return ((x < (largeur - 200)) &&
                    (x > 20) &&
                    (y > 20) &&
                    (y < hauteur - 60));
        }

        public void dessinTerrain (Canvas canvas) {
            canvas.drawRect(demiTerrain, peintureLignesTouche);     //Dessin du demi-terrain
            canvas.drawArc(ligne9m, 34, 112, false, peinture9m);    //Dessin de la ligne des 9m
            canvas.drawArc(ligne6m, 3, 174, false, peintureZone);   //Dessin de la zone
            canvas.drawArc(ligne6m, 3, 174, false, peinture6m);     //Dessin de la ligne des 6m
            canvas.drawRect(ligne7m, peinture6m);                   //Dessin de la ligne des 7m
            canvas.drawRect(rectBut, peintureRectBut);              //Dessin du rectangle But
        }

        public void dessinLegende(Canvas canvas) {

            int abscisseC = largeur - 180;  //cercles
            int abscisseT = largeur - 100;  //texte
            int ordonneeCG = 60;    //cercles "gageiés"
            int ordonneeCL = 100;   //cercles "loupes"
            int ordonneeP = 150;   //pourcentage
            int ordonneeTG = 70;
            int ordonneeTL = 110;
            tir.set(abscisseC - 4, ordonneeCG - 4, abscisseC + 4, ordonneeCG + 4);
            canvas.drawArc(tir, 0, 360, false, peintureTirGagne);
            tir.set(abscisseC - 2, ordonneeCL - 2, abscisseC + 2, ordonneeCL + 2);
            canvas.drawArc(tir, 0, 360, false, peintureTirRate);
            canvas.drawText(buts + nbButsMarques, abscisseT, ordonneeTG, peintureTexte);
            canvas.drawText(tirsManques + nbTirsManques, abscisseT, ordonneeTL, peintureTexte);
            if ((buts + tirsManques) != 0) {
                int pourcentage = buts * 100 / (buts + tirsManques);
                canvas.drawText(pourcentageReussite + pourcentage + "%", abscisseT, ordonneeP, peintureTexte);
            } else {
                canvas.drawText(pourcentageReussite + "--%", abscisseT, ordonneeP, peintureTexte);
            }
        }

        public void comptageTirs (ArrayList<Point> listePoints) {

            for (Point p : listePoints) {
                if (p.getBut_valide()) {
                    buts += 1;
                }
                else {
                    tirsManques += 1;
                }
            }

        }
    }
}
