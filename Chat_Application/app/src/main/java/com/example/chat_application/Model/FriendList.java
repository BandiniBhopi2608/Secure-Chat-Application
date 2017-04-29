package com.example.chat_application.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chat_application.R;

import java.util.List;

/**
 * Created by brin pereira on 28/04/2017.
 */

public class FriendList extends ArrayAdapter<User>
{

    public FriendList(Context context, int resource, List objects)
    {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       View v = convertView;
        Holder h;
        if (v == null)
        {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_screen, parent, false);
            h = new Holder();
            h.name = (TextView) v.findViewById(R.id.textview_name);
            h.phone = (TextView) v.findViewById(R.id.textview_phone);
            v.setTag(h);
        } else
        {
            h = (Holder) v.getTag();
        }

        User user = getItem(position);
     //   h.name.setText(user.getFirst_name() + " " + user.getLast_name());
     //   h.phone.setText("Phone: " + user.getPhone());

        return v;

       // return null;
    }

    class Holder
    {
        TextView name, phone;
    }

}
