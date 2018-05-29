package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.Constructor.Like_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/24/2018.
 */

public class Like_Adapter  extends ArrayAdapter<Like_Constructor> {
    Context ct;
    ArrayAdapter<Dr_Constructor> array;
    String key;
    public Like_Adapter(Context context, int resource, ArrayList<Like_Constructor> items, String key) {

        super(context, resource, items);
        this.ct = context;
        this.key = key;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Like_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if(key.equals("pill")){
                v =  inflater.inflate(R.layout.item_pill, null);
                TextView title = v.findViewById(R.id.title_pill);
                ImageView img = v.findViewById(R.id.img_pill);
                TextView decri = v.findViewById(R.id.decrip_pill);
                TextView like = v.findViewById(R.id.txt_like);
                TextView cmt = v.findViewById(R.id.txt_comment);

                title.setText(pill.getName());
                Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.getImage(),img);
                decri.setText(pill.getDescri());
                like.setText(pill.getLike());
                cmt.setText(pill.getComment());
            }else{
                v =  inflater.inflate(R.layout.item_sick, null);
                TextView title = v.findViewById(R.id.name_sick);
                ImageView img = v.findViewById(R.id.img_sick);
                TextView decri = v.findViewById(R.id.decrip_sick);
                TextView like = v.findViewById(R.id.txt_like);
                TextView cmt = v.findViewById(R.id.txt_comment);
                TextView date = v.findViewById(R.id.date_sick);

                title.setText(pill.getName());
//                Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.get);
                decri.setText(pill.getDescri());
                like.setText(pill.getLike());
                cmt.setText(pill.getComment());
                date.setText(Utils.convertTimestampToDate(pill.getTime()));
            }

        }
        init();

        return v;
    }

    private void init() {

    }
}

