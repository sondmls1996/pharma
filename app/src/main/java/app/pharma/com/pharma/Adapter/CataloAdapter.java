package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 4/19/2018.
 */

public class CataloAdapter   extends ArrayAdapter<CataloModel> {
    private Context context;
    private List<CataloModel> items;
    public CataloAdapter(Context context, int resource, List<CataloModel> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    public int getCount(){
        return items.size();
    }

    public CataloModel getItem(int position){
        return items.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_text_spiner, null);
        }
        CataloModel p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri

            TextView txt = (TextView) view.findViewById(R.id.txt_spin);
            txt.setText(p.getName());


        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.custom_text_spiner_2, null);
        }
        TextView txt = (TextView) view.findViewById(R.id.txt_spin2);
        txt.setText(items.get(position).getName());
        return view;
    }
}