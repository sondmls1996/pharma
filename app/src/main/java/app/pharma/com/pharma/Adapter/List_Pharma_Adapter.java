package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Pharma_Constructor;
import app.pharma.com.pharma.Model.Pill_Constructor;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Pharma_Adapter extends ArrayAdapter<Pharma_Constructor> {
    Context ct;
    ArrayAdapter<Pill_Constructor> array;

    public List_Pharma_Adapter(Context context, int resource, ArrayList<Pharma_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Pharma_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_pharma, null);
        }
        init();

        return v;
    }

    private void init() {

    }
}
