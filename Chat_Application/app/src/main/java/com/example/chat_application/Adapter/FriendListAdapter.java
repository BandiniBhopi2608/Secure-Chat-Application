package com.example.chat_application.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import java.util.List;

/**
 * Created by BANDINI on 30-04-2017.
 */

public class FriendListAdapter extends ArrayAdapter<User> {

    public FriendListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_friend, parent, false);
            holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.textview_name); //Check
            holder.phone = (TextView) view.findViewById(R.id.textview_phone); //Check
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        User objUser = getItem(position);
        holder.name.setText(objUser.getFirstName() + " " + objUser.getLastName());
        holder.phone.setText("Phone: " + objUser.getPhoneNumber());

        return view;
    }

    class Holder {
        TextView name, phone;
    }
}
