package csce576.suggestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import csce576.beans.CuisineBean;
import csce576.beans.EstablishmentBean;

/**
 * Created by tsarkar on 22/04/17.
 */
public class EstablishmentChildAdapter extends EstablishmentAdapter{

    AdapterClickItems ac_items;
    Context mContext;
    ArrayList<EstablishmentBean> establishment_list;


    public EstablishmentChildAdapter(Context context, ArrayList items, AdvancedScreenActivity activity) {
        super(context, items, activity);
        this.mContext = context;
        this.establishment_list = items;
        ac_items = activity;
    }
    public void updateEstablishmentsList(ArrayList<EstablishmentBean> newlist) {

        establishment_list = newlist;

        this.notifyDataSetChanged();
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.establishment_adapter, null);
            // viewHolder = new ViewHolderItem(convertView);
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
        System.out.println("checked >> "+checked);
        if(checked.equals("true"))
        {
            System.out.println("in true condition");
            viewHolder.est_tick
                    .setBackgroundResource(R.drawable.green_tick);

        }
        if(checked.equals("false"))
        {
            System.out.println("in false condition");
            viewHolder.est_tick
                    .setBackgroundResource(R.drawable.gray_tick);

        }

        viewHolder.tv_estname.setText(establishment_list.get(position).getEst_name());


        viewHolder.layout_est_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(establishment_list.get(position).getIs_checked().equals("true")) {
                    System.out.println("in get checked true condition");

                    viewHolder.est_tick
                            .setBackgroundResource(R.drawable.gray_tick);
                    establishment_list.get(position).setIs_checked("false");

                    EstablishmentBean eb = (EstablishmentBean)establishment_list.get(position);
                    Iterator itr =  establishment_list.iterator(); // remove all even numbers while (itr.hasNext()) { Integer number = itr.next(); if (number % 2 == 0) { numbers.remove(number); } }

                    while (itr.hasNext())
                    {
                        EstablishmentBean bean = (EstablishmentBean)itr.next();
                        if (bean.getEst_name().equals(eb.getEst_name()))
                        {
                            itr.remove();
                        }
                    }
                    updateEstablishmentsList(establishment_list);
//                    ac_items.removeCuisines(cuisine_list.get(position));
                }
                else if(establishment_list.get(position).getIs_checked().equals("false")) {
                    System.out.println("in get checked false condition");

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
