package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Order_List_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Pill_Order_Adapter  extends ArrayAdapter<Order_List_Constructor> {
    Context ct;
    ArrayList<Order_List_Constructor> array;

    public Pill_Order_Adapter(Context context, int resource, ArrayList<Order_List_Constructor> items) {

        super(context, resource, items);
        this.ct = context;
        this.array = items;

    }

    public void addListItem(ArrayList<Order_List_Constructor> itemplust){
        array.addAll(itemplust);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Order_List_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_order_pill, null);
        }

        TextView tv_title = v.findViewById(R.id.title_pill);
        TextView day_order = v.findViewById(R.id.day_order);
        TextView total_price = v.findViewById(R.id.total_price);
        TextView status = v.findViewById(R.id.text_status);
        ImageView img = v.findViewById(R.id.img_pill);
        tv_title.setText(pill.getName());
        day_order.setText(Utils.convertTimestampToDate(pill.getDate()));
        total_price.setText(Constant.format.format(pill.getPrice())+"VND");
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(pill.getStatus().equals("new")){

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                status.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.strock_green) );
            } else {
                status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.strock_green));
            }
            status.setText("Mới");
            status.setTextColor(getContext().getResources().getColor(R.color.green));

        }

        if(pill.getStatus().equals("paid")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                status.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.stroke_gray) );
            } else {
                status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.stroke_gray));
            }
            status.setText("Đã thanh toán");
            status.setTextColor(getContext().getResources().getColor(R.color.gray));
        }
        if(pill.getStatus().equals("disable")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                status.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.stroke_red) );
            } else {
                status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.stroke_red));
            }
            status.setText("Đã hủy");
            status.setTextColor(getContext().getResources().getColor(R.color.dark_red));
        }
        Picasso.with(getContext()).load(ServerPath.ROOT_URL+pill.getImage()).into(img);

        return v;
    }

    private void init(View v) {

        //  TextView t
    }
}
