package com.example.blath.around.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.NewPostActivity;
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundApplication;
import com.example.blath.around.events.GetPostsEvent;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HomePostsFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<Post> mDataSet;
    private DashboardPostsAdapter mAdapter;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load mDataSet by making a get call and getting the data from server.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_posts, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.posts_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DashboardPostsAdapter(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton dashboardNewPostFAB = (FloatingActionButton) mView.findViewById(R.id.dashboard_newpost_fab);
        dashboardNewPostFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences userDetails = getActivity().getSharedPreferences(AroundApplication.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Operations.getPosts(new LatLng(Double.parseDouble(userDetails.getString(User.KEY_USER_LATITUDE, "37.399345")),
                Double.parseDouble(userDetails.getString(User.KEY_USER_LONGITUTDE, "-121.919924"))));

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(GetPostsEvent result) {
//        mView.findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
        if (!ResponseOperations.isError(result.getResponseObject())) {
            try {
                List<Post> posts = (List<Post>) result.getResponseObject().getBody();
                mAdapter.updateData(posts);
            } catch (Exception e) {

            }
        } else {
            UIUtils.showLongToast(result.getResponseObject().getMessage(), getActivity());
        }
    }


    class DashboardPostsAdapter extends RecyclerView.Adapter<DashboardPostsAdapter.DashboardPostsViewHolder> {

        class DashboardPostsViewHolder extends RecyclerView.ViewHolder {
            final LinearLayout mContentArea;
            final ImageView mPostUserIcon;
            final TextView mPostDay;
            final TextView mPostMonthYear;
            final TextView mPostWeekDay;
            final TextView mPostUsername;
            final TextView mPostSubtype;
            final TextView mAgeRange;
            final TextView mGenderPreference;
            final TextView mPostAddress;

            DashboardPostsViewHolder(View v) {
                super(v);
                mContentArea = (LinearLayout) v.findViewById(R.id.post_content_area);
                mPostUserIcon = (ImageView) v.findViewById(R.id.post_user_icon);
                mPostDay = (TextView) v.findViewById(R.id.post_day);
                mPostMonthYear = (TextView) v.findViewById(R.id.post_month_year);
                mPostWeekDay = (TextView) v.findViewById(R.id.post_weekday);
                mPostUsername = (TextView) v.findViewById(R.id.post_user_name);
                mPostSubtype = (TextView) v.findViewById(R.id.post_subtype);
                mPostAddress = (TextView) v.findViewById(R.id.post_address);
                mAgeRange = (TextView) v.findViewById(R.id.post_age_range);
                mGenderPreference = (TextView) v.findViewById(R.id.post_gender_preference);
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
            setContentAreaBackground(post.getType(), holder.mContentArea);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(post.getDates().getStartDate());
            holder.mPostUserIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.baseball));
            holder.mPostDay.setText(DateUtils.twoDigitDayOfMonth(calendar));
            holder.mPostMonthYear.setText(DateUtils.monthName(calendar) + ", " + DateUtils.lastTwoDigitYear(calendar));
            holder.mPostWeekDay.setText(DateUtils.weekDayName(calendar));
            holder.mPostUsername.setText(post.getUser().getUserPersonalInformation().getFirstName() + " " +
                    post.getUser().getUserPersonalInformation().getLastName());
            holder.mPostSubtype.setText(post.getSubType());
            holder.mAgeRange.setText(post.getAgeRange().getMinAge() + "-" + post.getAgeRange().getMaxAge());
            holder.mGenderPreference.setText(post.getGenderPreference());
            holder.mPostAddress.setText(post.getLocation().getAddress());
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

        private void setContentAreaBackground(String type, LinearLayout contentArea) {
            switch (type) {
                case Post.KEY_TYPE_SPORTS:
                    contentArea.setBackgroundResource(R.drawable.post_sport_background);
                    break;
                case Post.KEY_TYPE_STUDY:
                    contentArea.setBackgroundResource(R.drawable.post_study_background);
                    break;
                case Post.KEY_TYPE_TRAVEL:
                    contentArea.setBackgroundResource(R.drawable.post_travel_background);
                    break;
                case Post.KEY_TYPE_CONCERT:
                    contentArea.setBackgroundResource(R.drawable.post_concert_background);
                    break;
                case Post.KEY_TYPE_OTHER:
                    contentArea.setBackgroundResource(R.drawable.post_other_background);
                    break;
                default:
                    break;
            }
        }
    }
}
