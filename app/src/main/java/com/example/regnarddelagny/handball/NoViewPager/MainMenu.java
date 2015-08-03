package com.example.regnarddelagny.handball.NoViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.regnarddelagny.handball.R;
import com.example.regnarddelagny.handball.ViewPager.ViewPagerActivity;


public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPlayersView (View view) {
        Intent intent = new Intent(this, PlayersActivity.class);
        startActivity(intent);
        Log.d("click on Button", "successful");
    }

    public void openAnalyseTirsActivity (View view) {
        Intent intent = new Intent (this, ListeAnalyseTirs.class);
        startActivity(intent);
    }

    public void openTestViewPager (View view) {
        Intent intent = new Intent(this, ViewPagerActivity.class);
        startActivity(intent);
    }

}
