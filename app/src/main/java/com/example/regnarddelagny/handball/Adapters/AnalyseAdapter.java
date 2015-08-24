package com.example.regnarddelagny.handball.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsReading;
import com.example.regnarddelagny.handball.AnalyseTirs.AnalyseTirsActivities.AnalyseTirsWriting;
import com.example.regnarddelagny.handball.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RegnarddeLagny on 19/08/2015.
 */
public class AnalyseAdapter extends ArrayAdapter<String> {

    private final Context m_context;
    private final ArrayList<File> m_values;

    public AnalyseAdapter(Context context, ArrayList<File> values) {
        super(context, -1);
        m_context = context;
        m_values = values;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.analyse_tirs_view, parent, false);

        /*final File file = m_values.get(position);
        final String nomFichierCourt = file.getName().substring(9);
        TextView nomFiche = (TextView) convertView.findViewById(R.id.analyse_view_text);
        nomFiche.setText(nomFichierCourt);
        Button boutonW = (Button) convertView.findViewById(R.id.analyse_button_w);
        boutonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(m_context, AnalyseTirsWriting.class);
                intent.putExtra("FICHIER", nomFichierCourt);
                m_context.startActivity(intent);
            }
        });
        Button boutonR = (Button) convertView.findViewById(R.id.analyse_button_r);
        boutonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(m_context, AnalyseTirsReading.class);
                intent.putExtra("FICHIER", nomFichierCourt);
                m_context.startActivity(intent);
            }
        });
        Button boutonDel = (Button) convertView.findViewById(R.id.analyse_button_del);
        boutonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (!file.delete() && file.isFile()) {
                    Log.d("echec", "effacement");
                }
                //convertView.setVisibility(View.GONE);
            }
        });
*/
        return convertView;
    }
}
