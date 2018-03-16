package br.com.davidlemos.ezpointmobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.davidlemos.ezpointmobile.R;
import br.com.davidlemos.ezpointmobile.model.Model;

public class Adapter extends ArrayAdapter<Model> {

    private final Context context;
    private final ArrayList<Model> modelsArrayList;

    public Adapter(Context context, ArrayList<Model> modelsArrayList) {
        super(context, R.layout.espelho_item, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
            if(!modelsArrayList.get(position).isGroupHeader()){
                rowView = inflater.inflate(R.layout.espelho_item, parent, false);
                if (position % 2 == 1) {
                    rowView.setBackgroundColor(Color.parseColor("#b9b9b9"));
                }
                else {
                    rowView.setBackgroundColor(Color.parseColor("#a4a4a4"));
                }

                // 3. Get icon,title & counter views from the rowView
                ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView counterView_f = (TextView) rowView.findViewById(R.id.item_counter_f);
                TextView counterView_e = (TextView) rowView.findViewById(R.id.item_counter_e);

                // 4. Set the text for textView
                imgView.setImageResource(modelsArrayList.get(position).getIcon());
                titleView.setText(modelsArrayList.get(position).getTitle());
                counterView_f.setText(modelsArrayList.get(position).getCounterF());
                counterView_e.setText(modelsArrayList.get(position).getCounterE());
            }
            else{
                rowView = inflater.inflate(R.layout.group_header_espelho_item, parent, false);
                //TextView titleView = (TextView) rowView.findViewById(R.id.header);
                //titleView.setText(modelsArrayList.get(position).getTitle());

            }

        return rowView;
    }
}
