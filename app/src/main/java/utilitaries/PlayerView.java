package utilitaries;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by RegnarddeLagny on 24/04/2015.
 */
public class PlayerView extends View {

    private Player joueur;

    public PlayerView(Context context, AttributeSet attrs, Player joueur) {
        super(context, attrs);
        this.joueur = joueur;


    }


}
