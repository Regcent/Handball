package utilitaries.dessins.dessinsJoueurs;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by RegnarddeLagny on 20/08/2015.
 */
public class PointSerial implements Serializable {

    protected int x;
    protected int y;

    public PointSerial (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
