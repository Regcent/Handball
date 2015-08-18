package com.example.regnarddelagny.handball.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.regnarddelagny.handball.R;
import com.example.regnarddelagny.handball.SchemasTactiques.SchemasPrepActivity;

/**
 * Created by RegnarddeLagny on 07/08/2015.
 */
public class SchemasFragment extends android.support.v4.app.Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_schemas, container, false);
        addListeners(rootView);
        return rootView;
    }

    public void addListeners (View rootView) {
        Button bouton = (Button) rootView.findViewById(R.id.gototest);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), SchemasPrepActivity.class);
                startActivity(intent);
            }
        });
    }
}
