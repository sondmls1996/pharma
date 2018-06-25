package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Pill_Order_Adapter extends
        RecyclerView.Adapter<Pill_Order_Adapter.RecyclerViewHolder> {
    SimpleDateFormat format,formatHours;
    List<Order_List_Constructor> list;
    String key = "";
    View v;
    private List<Order_List_Constructor> listData = new ArrayList<>();
    Context context;

    public Pill_Order_Adapter(Context context, List<Order_List_Constructor> listData) {
        formatHours = new SimpleDateFormat();
        this.context = context;
        this.listData = listData;

    }

    public void updateData(ArrayList<Order_List_Constructor> arr) {
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

            v = inflater.inflate(R.layout.item_order_pill, null);



        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Pill_Order_Adapter.RecyclerViewHolder viewHolder, int position) {
        Order_List_Constructor pill = listData.get(position);

        viewHolder.tv_title.setText(pill.getName());
        viewHolder.day_order.setText(Utils.convertTimestampToDate(pill.getDate()));
        viewHolder.total_price.setText(Constant.format.format(pill.getPrice())+"VND");
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(pill.getStatus().equals("new")){

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.strock_green) );
            } else {
                viewHolder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.strock_green));
            }
            viewHolder.status.setText("Mới");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.green));

        }

        if(pill.getStatus().equals("paid")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.stroke_gray) );
            } else {
                viewHolder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.stroke_gray));
            }
            viewHolder.status.setText("Đã thanh toán");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.gray));
        }
        if(pill.getStatus().equals("disable")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.status.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.stroke_red) );
            } else {
                viewHolder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.stroke_red));
            }
            viewHolder.status.setText("Đã hủy");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.dark_red));
        }
        Picasso.with(context).load(ServerPath.ROOT_URL+pill.getImage()).into(viewHolder.img);

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

        public TextView tv_title, day_order, total_price, status;
        public TextView date;
        public ImageView img;
        public LinearLayout insertPoint;
        public RecyclerViewHolder(View v) {
            super(v);
             tv_title = v.findViewById(R.id.title_pill);
             day_order = v.findViewById(R.id.day_order);
             total_price = v.findViewById(R.id.total_price);
             status = v.findViewById(R.id.text_status);
             img = v.findViewById(R.id.img_pill);
        }
    }
}