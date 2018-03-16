package br.com.davidlemos.ezpointmobile.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.davidlemos.ezpointmobile.R;
import br.com.davidlemos.ezpointmobile.models.Ponto;

public class PontoAdapter extends BaseAdapter {
    private final Activity activity;
    private List<Ponto> pontos;

    public PontoAdapter(Activity activity, List<Ponto> pontos) {
        this.activity = activity;
        this.pontos = pontos;
    }

    @Override
    public int getCount() {
        return pontos.size();
    }

    @Override
    public Object getItem(int position) {
        return pontos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pontos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(
                R.layout.espelho_item, null);

        Ponto ponto = pontos.get(position);

        TextView item_counter_f = (TextView) view.findViewById(R.id.item_counter_f);
        item_counter_f.setText(ponto.getFa());

        TextView item_counter_e = (TextView) view.findViewById(R.id.item_counter_e);
        item_counter_e.setText(ponto.getEx());

        TextView item_title = (TextView) view.findViewById(R.id.item_title);
        item_title.setText(ponto.getDate()+" - "+ponto.getWeekDay());

        return view;
    }
}
