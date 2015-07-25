package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }


    public void openPlayersView (View view) {
        Intent intent = new Intent(this, com.example.regnarddelagny.handball.PlayersActivity.class);
        startActivity(intent);
        Log.d("click on Button", "successful");
    }

    public void openAnalyseTirsActivity (View view) {
        Intent intent = new Intent (this, AnalyseTirsActivity.class);
        startActivity(intent);
    }

}
