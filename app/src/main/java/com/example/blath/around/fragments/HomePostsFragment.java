package com.example.blath.around.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.NewPostActivity;
import com.example.blath.around.models.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomePostsFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<Post> mDataSet;
    private DashboardPostsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Load mDataSet by making a get call and getting the data from server.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_posts, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.posts_recycler);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new DashboardPostsAdapter(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton dashboardNewPostFAB = (FloatingActionButton) view.findViewById(R.id.dashboard_newpost_fab);
        dashboardNewPostFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    class DashboardPostsAdapter extends RecyclerView.Adapter<DashboardPostsAdapter.DashboardPostsViewHolder> {

        class DashboardPostsViewHolder extends RecyclerView.ViewHolder {
            final ImageView mPostUserIcon;
            final TextView mPostDay;
            final TextView mPostMonthYear;
            final TextView mPostWeekDay;
            final TextView mPostUsername;
            final TextView mPostSportName;
            final TextView mPostCity;
            final TextView mPostDescription;

            DashboardPostsViewHolder(View v) {
                super(v);
                mPostUserIcon = (ImageView) v.findViewById(R.id.post_user_icon);
                mPostDay = (TextView) v.findViewById(R.id.post_day);
                mPostMonthYear = (TextView) v.findViewById(R.id.post_month_year);
                mPostWeekDay = (TextView) v.findViewById(R.id.post_weekday);
                mPostUsername = (TextView) v.findViewById(R.id.post_user_name);
                mPostSportName = (TextView) v.findViewById(R.id.post_sport_name);
                mPostCity = (TextView) v.findViewById(R.id.post_city);
                mPostDescription= (TextView) v.findViewById(R.id.post_description);
            }
        }

        DashboardPostsAdapter(List<Post> dataset) {
            mDataSet = dataset;
        }

        @Override
        public DashboardPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_row_item, parent, false);
            return new DashboardPostsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DashboardPostsViewHolder holder, int position) {
            Post post = mDataSet.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(post.getDates().getStartDate());
            holder.mPostDay.setText(calendar.get(Calendar.DAY_OF_MONTH));
            holder.mPostMonthYear.setText(calendar.get(Calendar.MONTH) + calendar.get(Calendar.YEAR));
            holder.mPostWeekDay.setText(calendar.get(Calendar.DAY_OF_WEEK));
            holder.mPostUsername.setText(post.getUser().getUserPersonalInformation().getFirstName() + " " +
                    post.getUser().getUserPersonalInformation().getLastName());
            holder.mPostSportName.setText(post.getSport().getName());
            holder.mPostCity.setText(post.getLocation().getCity());
            holder.mPostDescription.setText(post.getDescription());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        void updateData(List<Post> dataSet) {
            mDataSet.clear();
            mDataSet.addAll(dataSet);
            notifyDataSetChanged();
        }
    }
}
