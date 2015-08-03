package utilitaries.dessins;

import java.io.Serializable;

/**
 * Created by RegnarddeLagny on 01/06/2015.
 */
public class Tir implements Serializable {
    private float x;
    private float y;
    private boolean but_valide;

    public Tir(float x, float y) {
        this.x = x;
        this.y = y;
        but_valide = false;
    }

    public Tir(Tir tir) {
        x = tir.getX();
        y = tir.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setBut_valide()  {
        but_valide = true;
    }

    public boolean getBut_valide()
    {
        return but_valide;
    }
}
