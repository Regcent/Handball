package com.example.regnarddelagny.handball;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class CreatePlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);
    }

    public void createNewPlayer(View view) {
        EditText nom = (EditText) findViewById(R.id.playerName);
        EditText prenom = (EditText) findViewById(R.id.playerFirstName);
        EditText numero = (EditText) findViewById(R.id.playerNumber);
        Log.d("Nom joueur",nom.getText().toString());
        Log.d("Prenom joueur", prenom.getText().toString());
        Log.d("Numero joueur", numero.getText().toString());
    }
}
