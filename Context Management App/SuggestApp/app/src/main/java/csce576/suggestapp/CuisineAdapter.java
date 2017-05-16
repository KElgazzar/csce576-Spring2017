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

import csce576.beans.CuisineBean;

/**
 * Created by tsarkar on 19/04/17.
 */
public class CuisineAdapter extends BaseAdapter {


    AdapterClickItems ac_items;
    Context mContext;
    // List<ProductModel> product_list_items;
    ArrayList<CuisineBean> cuisine_list;
    String from_screen;

    Typeface type1;

    String centrally_listed, store_listed;
    boolean unchecked;
    boolean checked;

    public CuisineAdapter(Context context,
                                   ArrayList items,AdvancedScreenActivity activity) {

        this.mContext = context;
        this.cuisine_list = items;
        ac_items = activity;


    }
    static class ViewHolderItem {
        TextView tv_cuisinename;
        /*ImageView cuisine_tick_checked;
        ImageView cuisine_tick_unchecked;
        LinearLayout layout_cuisine_tick_checked;
        LinearLayout layout_cuisine_tick_unchecked;*/
        ImageView cuisine_tick;
        LinearLayout layout_cuisine_tick;

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
        return cuisine_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cuisine_list.get(position);
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
                    R.layout.cuisine_adapter, null);
            // viewHolder = new ViewHolderItem(convertView);
            viewHolder = new ViewHolderItem();
            viewHolder.tv_cuisinename = (TextView) convertView
                    .findViewById(R.id.tv_cuisinename);
           /* viewHolder.cuisine_tick_checked = (ImageView) convertView
                    .findViewById(R.id.cuisine_tick_checked);
            viewHolder.cuisine_tick_unchecked = (ImageView) convertView
                    .findViewById(R.id.cuisine_tick_unchecked);
            viewHolder.layout_cuisine_tick_checked = (LinearLayout) convertView
                    .findViewById(R.id.layout_cuisine_tick_checked);
            viewHolder.layout_cuisine_tick_unchecked= (LinearLayout) convertView
                    .findViewById(R.id.layout_cuisine_tick_unchecked);*/
            viewHolder.cuisine_tick = (ImageView) convertView
                    .findViewById(R.id.cuisine_tick);
            viewHolder.layout_cuisine_tick= (LinearLayout) convertView
                    .findViewById(R.id.layout_cuisine_tick);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


//        System.out.println("cuisins name in adapter >> "+cuisine_list.get(position).toString());
//        final CuisineBean cb = (CuisineBean) cuisine_list.get(position);
        String checked = cuisine_list.get(position).getIs_checked();
        System.out.println("checked >> "+checked);
        if(checked.equals("true"))
        {
            System.out.println("in true condition");
            viewHolder.cuisine_tick
                    .setBackgroundResource(R.drawable.green_tick);
//            viewHolder.layout_cuisine_tick_checked.setVisibility(View.GONE);
//            viewHolder.layout_cuisine_tick_unchecked.setVisibility(View.VISIBLE);
        }
        if(checked.equals("false"))
        {
            System.out.println("in false condition");
            viewHolder.cuisine_tick
                    .setBackgroundResource(R.drawable.gray_tick);
//            viewHolder.layout_cuisine_tick_checked.setVisibility(View.VISIBLE);
//            viewHolder.layout_cuisine_tick_unchecked.setVisibility(View.GONE);
        }

        viewHolder.tv_cuisinename.setText(cuisine_list.get(position).getCuisine_name());


        viewHolder.layout_cuisine_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("tick_checked  adapter >> "+cuisine_list.get(position).getCuisine_name());
//                System.out.println("tick_checked getIs_checked  adapter >> "+cuisine_list.get(position).getIs_checked());
//                viewHolder.layout_cuisine_tick_checked.setVisibility(View.GONE);
//                viewHolder.layout_cuisine_tick_unchecked.setVisibility(View.VISIBLE);
                if(cuisine_list.get(position).getIs_checked().equals("true")) {
                    System.out.println("in get checked true condition");

                    viewHolder.cuisine_tick
                            .setBackgroundResource(R.drawable.gray_tick);
                    cuisine_list.get(position).setIs_checked("false");
                    ac_items.removeCuisines(cuisine_list.get(position));
                }
                else if(cuisine_list.get(position).getIs_checked().equals("false")) {
                    System.out.println("in get checked false condition");

                    viewHolder.cuisine_tick
                            .setBackgroundResource(R.drawable.green_tick);
                    cuisine_list.get(position).setIs_checked("true");
                    ac_items.addCuisines(cuisine_list.get(position));
                }
            }
        });


        return convertView;
    }
}



