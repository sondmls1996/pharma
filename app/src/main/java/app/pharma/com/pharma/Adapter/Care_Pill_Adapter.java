package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.Constructor.Like_Constructor;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 4/1/2018.
 */

public class Care_Pill_Adapter  extends ArrayAdapter<Like_Constructor> {
    Context ct;
    ArrayAdapter<Dr_Constructor> array;

    public Care_Pill_Adapter(Context context, int resource, ArrayList<Like_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Like_Constructor pill = getItem(position);
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

