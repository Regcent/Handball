package com.example.regnarddelagny.handball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class PlayersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
    }

    public void addNewPlayer(View view) {
        Intent intent = new Intent(this, com.example.regnarddelagny.handball.CreatePlayerActivity.class);
        startActivity(intent);
    }

}
