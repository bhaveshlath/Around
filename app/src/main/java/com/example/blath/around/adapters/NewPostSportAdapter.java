package com.example.blath.around.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.INewPostListener;

/**
 * Created by blath on 4/17/17.
 */

public class NewPostSportAdapter extends BaseAdapter {
    Context mContext;
    int[] mSportNames;
    int[] mSportIcons;
    INewPostListener mINewPostListener;
    TextView mView;

    private static LayoutInflater inflater = null;

    public NewPostSportAdapter(Context context, int[] sportNames, int[] sportIcons, TextView view) {
        mContext = context;
        mSportNames = sportNames;
        mSportIcons = sportIcons;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mINewPostListener = (INewPostListener) mContext;
        mView = view;
    }

    @Override
    public int getCount() {
        return mSportNames.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Holder holder = new Holder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sport_item, null);
        }
        holder.sportName = (TextView) convertView.findViewById(R.id.sport_name);
        holder.sportIcon = (ImageView) convertView.findViewById(R.id.sport_icon);

        holder.sportName.setText(mSportNames[position]);
        holder.sportIcon.setImageResource(mSportIcons[position]);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.setText(mSportNames[position]);
            }
        });

        return convertView;
    }

    public class Holder {
        TextView sportName;
        ImageView sportIcon;
    }
}
