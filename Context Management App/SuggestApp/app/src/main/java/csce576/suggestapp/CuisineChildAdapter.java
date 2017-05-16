package csce576.suggestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import csce576.beans.CuisineBean;
import csce576.beans.EstablishmentBean;

/**
 * Created by tsarkar on 21/04/17.
 */
public class CuisineChildAdapter extends CuisineAdapter {

    AdapterClickItems ac_items;
    Context mContext;
    ArrayList<CuisineBean> cuisine_list;


    public CuisineChildAdapter(Context context, ArrayList items, AdvancedScreenActivity activity) {
        super(context, items, activity);
        this.mContext = context;
        this.cuisine_list = items;
        ac_items = activity;
    }

    public void updatecuisinesList(ArrayList<CuisineBean> newlist) {
//        cuisine_list = newlist;
        System.out.println("size of new list is > "+newlist.size());
        System.out.println(">> notifydatasetchanged");
//        this.cuisine_list.clear();
//        this.cuisine_list.addAll(newlist);
        cuisine_list = newlist;
        System.out.println("size of new list is >> "+cuisine_list.size());
        this.notifyDataSetChanged();
    }


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

            viewHolder.cuisine_tick = (ImageView) convertView
                    .findViewById(R.id.cuisine_tick);
            viewHolder.layout_cuisine_tick= (LinearLayout) convertView
                    .findViewById(R.id.layout_cuisine_tick);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


        String checked = cuisine_list.get(position).getIs_checked();
        System.out.println("checked >> "+checked);
        if(checked.equals("true"))
        {
            System.out.println("in true condition");
            viewHolder.cuisine_tick
                    .setBackgroundResource(R.drawable.green_tick);

        }
        if(checked.equals("false"))
        {
            System.out.println("in false condition");
            viewHolder.cuisine_tick
                    .setBackgroundResource(R.drawable.gray_tick);

        }

        viewHolder.tv_cuisinename.setText(cuisine_list.get(position).getCuisine_name());


        viewHolder.layout_cuisine_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cuisine_list.get(position).getIs_checked().equals("true")) {
                    System.out.println("in get checked true condition");

                    viewHolder.cuisine_tick
                            .setBackgroundResource(R.drawable.gray_tick);
                    cuisine_list.get(position).setIs_checked("false");

                    CuisineBean cb = (CuisineBean)cuisine_list.get(position);
                    Iterator itr = cuisine_list.iterator(); // remove all even numbers while (itr.hasNext()) { Integer number = itr.next(); if (number % 2 == 0) { numbers.remove(number); } }

                    while (itr.hasNext())
                    {
                        CuisineBean bean = (CuisineBean)itr.next();
                        if (bean.getCuisine_name().equals(cb.getCuisine_name()))
                        {
                            itr.remove();
                        }
                    }
                    updatecuisinesList(cuisine_list);
//                    ac_items.removeCuisines(cuisine_list.get(position));
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
