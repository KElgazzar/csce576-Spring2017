package csce576.suggestapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import csce576.beans.EstablishmentBean;

/**
 * Created by tsarkar on 20/04/17.
 */

public class EstablishmentAdapter extends BaseAdapter {


    AdapterClickItems ac_items;
    Context mContext;
    // List<ProductModel> product_list_items;
    ArrayList<EstablishmentBean> establishment_list;
    String from_screen;

    Typeface type1;


    boolean unchecked;
    boolean checked;

    public EstablishmentAdapter(Context context,
                          ArrayList items,AdvancedScreenActivity activity) {

        this.mContext = context;
        this.establishment_list = items;
        ac_items = activity;
    }

    static class ViewHolderItem {
        TextView tv_estname;

        ImageView est_tick;
        LinearLayout layout_est_tick;

    }
    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getCount() {
        return establishment_list.size();
    }

    @Override
    public Object getItem(int position) {
        return establishment_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolderItem viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.establishment_adapter, null);

            viewHolder = new ViewHolderItem();
            viewHolder.tv_estname = (TextView) convertView
                    .findViewById(R.id.tv_estname);


            viewHolder.est_tick = (ImageView) convertView
                    .findViewById(R.id.est_tick);
            viewHolder.layout_est_tick= (LinearLayout) convertView
                    .findViewById(R.id.layout_est_tick);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


        String checked = establishment_list.get(position).getIs_checked();

        if(checked.equals("true"))
        {

            viewHolder.est_tick
                    .setBackgroundResource(R.drawable.green_tick);
        }
        if(checked.equals("false"))
        {

            viewHolder.est_tick
                    .setBackgroundResource(R.drawable.gray_tick);

        }

        viewHolder.tv_estname.setText(establishment_list.get(position).getEst_name());


        viewHolder.layout_est_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(establishment_list.get(position).getIs_checked().equals("true")) {

                    viewHolder.est_tick
                            .setBackgroundResource(R.drawable.gray_tick);
                    establishment_list.get(position).setIs_checked("false");

                    ac_items.removeEstablishments(establishment_list.get(position));
                }
                else if(establishment_list.get(position).getIs_checked().equals("false")) {

                    viewHolder.est_tick
                            .setBackgroundResource(R.drawable.green_tick);
                    establishment_list.get(position).setIs_checked("true");
                    ac_items.addEstablishments(establishment_list.get(position));
                }
            }
        });


        return convertView;
    }
}