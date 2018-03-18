package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Pill_Constructor;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/18/2018.
 */

public class List_Pill_Adapter extends ArrayAdapter<Pill_Constructor> {
    Context ct;
    ArrayAdapter<Pill_Constructor> array;

    public List_Pill_Adapter(Context context, int resource, ArrayList<Pill_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Pill_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_pill, null);
        }
        init();

        return v;
    }

    private void init() {

    }
}
