package com.example.regnarddelagny.handball.SchemasTactiques;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.example.regnarddelagny.handball.VuesDemiTerrain.Schema.VueDemiTerrainPrep;

public class SchemasPrepActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new VueDemiTerrainPrep(this));
        ActionBar actionBar = getActionBar();
        actionBar.hide();

    }

}
