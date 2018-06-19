package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Constructor.Like_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/24/2018.
 */

public class Like_Adapter extends
        RecyclerView.Adapter<Like_Adapter.RecyclerViewHolder> {
    SimpleDateFormat format,formatHours;
    List<Like_Constructor> list;
    String key = "";
    View v;
    private List<Like_Constructor> listData = new ArrayList<>();
    Context context;

    public Like_Adapter(Context context, List<Like_Constructor> listData,String key) {
        formatHours = new SimpleDateFormat();
        this.context = context;
        this.listData = listData;
        this.key = key;
    }

    public void updateData(ArrayList<Like_Constructor> arr) {
        this.listData = arr;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                  int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if(key.equals("pill")) {
            v = inflater.inflate(R.layout.item_pill, null);

        }else{
            v = inflater.inflate(R.layout.item_sick, null);
        }
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Like_Adapter.RecyclerViewHolder viewHolder, int position) {
        Like_Constructor pill = listData.get(position);

        if(key.equals("pill")){
            viewHolder.title.setText(pill.getName());
            Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.getImage(),viewHolder.img);
            viewHolder.decri.setText(pill.getDescri());
            viewHolder.like.setText(pill.getLike());
            viewHolder.cmt.setText(pill.getComment());
        }else{
            viewHolder.title.setText(pill.getName());
            Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.getImage(),viewHolder.img);
            viewHolder.decri.setText(pill.getDescri());
            viewHolder.like.setText(pill.getLike());
            viewHolder.cmt.setText(pill.getComment());
            viewHolder.date.setText(Utils.convertTimestampToDate(pill.getTime()));
        }


    }


    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for item view of list
     */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView title, descr, decri, like,cmt;
        public TextView date;
        public ImageView img;
        public LinearLayout insertPoint;
        public RecyclerViewHolder(View v) {
            super(v);
            if(key.equals("pill")) {

                 title = v.findViewById(R.id.title_pill);
                 img = v.findViewById(R.id.img_pill);
                 decri = v.findViewById(R.id.decrip_pill);
                 like = v.findViewById(R.id.txt_like);
                 cmt = v.findViewById(R.id.txt_comment);
            }else{
                 title = v.findViewById(R.id.name_sick);
                 img = v.findViewById(R.id.img_sick);
                 decri = v.findViewById(R.id.decrip_sick);
                 like = v.findViewById(R.id.txt_like);
                 cmt = v.findViewById(R.id.txt_comment);
                 date = v.findViewById(R.id.date_sick);

            }
        }
    }
}