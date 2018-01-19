package com.example.blath.around.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.NewPostActivity;
import com.example.blath.around.activities.PostActivity;
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.GetPostsEvent;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HomePostsFragment extends Fragment {

    public static final String KEY_POST = "key_post";
    public static final String KEY_POST_ACTION = "key_post_action";
    public static final String KEY_POST_DETAIL = "key_post_detail";
    public static final String KEY_POST_COMMENTS = "key_post_comments";
    public static final String KEY_POST_REPLY = "key_post_reply";

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

        SharedPreferences userDetails = getActivity().getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
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

        class DashboardPostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final LinearLayout mContentArea;
            final ImageView postUserIcon;
            final TextView postedDate;
            final TextView postDay;
            final TextView postMonthYear;
            final TextView postWeekDay;
            final TextView postUsername;
            final TextView postTitleSubtitle;
            final TextView ageRange;
            final TextView genderPreference;
            final TextView postAddress;
            final Button postReply;
            final Button postComments;

            DashboardPostsViewHolder(View v) {
                super(v);
                mContentArea = (LinearLayout) v.findViewById(R.id.post_content_area);
                postUserIcon = (ImageView) v.findViewById(R.id.post_user_icon);
                postUsername = (TextView) v.findViewById(R.id.post_user_name);
                postedDate = (TextView) v.findViewById(R.id.posted_date);
                postDay = (TextView) v.findViewById(R.id.post_day);
                postMonthYear = (TextView) v.findViewById(R.id.post_month_year);
                postWeekDay = (TextView) v.findViewById(R.id.post_weekday);
                postTitleSubtitle = (TextView) v.findViewById(R.id.post_subtype);
                postAddress = (TextView) v.findViewById(R.id.post_address);
                ageRange = (TextView) v.findViewById(R.id.post_age_range);
                genderPreference = (TextView) v.findViewById(R.id.post_gender_preference);
                postReply = (Button) v.findViewById(R.id.post_reply);
                postComments = (Button) v.findViewById(R.id.post_comments);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                Intent intent = new Intent(getActivity(), PostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_POST_ACTION, KEY_POST_DETAIL);
                bundle.putSerializable(KEY_POST, mDataSet.get(itemPosition));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        DashboardPostsAdapter(List<Post> dataset) {
            mDataSet = dataset;
        }

        @Override
        public DashboardPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.posts_list_item, parent, false);
            return new DashboardPostsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DashboardPostsViewHolder holder, final int position) {
            Resources resources = getResources();
            final Post post = mDataSet.get(position);
            setContentAreaBackground(post.getType(), holder.mContentArea);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(post.getDates().getStartDate());
            holder.postUserIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.baseball));
            holder.postedDate.setText(DateUtils.dateFormatterFromString(post.getPostedDate().toString()));
            holder.postDay.setText(DateUtils.twoDigitDayOfMonth(calendar));
            holder.postMonthYear.setText(DateUtils.monthName(calendar) + ", " + DateUtils.lastTwoDigitYear(calendar));
            holder.postWeekDay.setText(DateUtils.weekDayName(calendar));
            holder.postUsername.setText(post.getUser().getUserPersonalInformation().getFirstName() + " " +
                    post.getUser().getUserPersonalInformation().getLastName());
            holder.postTitleSubtitle.setText(getPostTitle(post));
            holder.ageRange.setText(resources.getString(R.string.age_range_post_text, post.getAgeRange().getMinAge(), post.getAgeRange().getMaxAge()));
            holder.genderPreference.setText(resources.getString(R.string.gender_preference_post_text, post.getGenderPreference()));
            holder.postAddress.setText(post.getLocation().getAddress());
            holder.postReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.showLongToast("This Is me " + String.valueOf(position) + post.getTitle(), getActivity());
                }
            });

            holder.postComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_POST_ACTION, KEY_POST_COMMENTS);
                    bundle.putSerializable(KEY_POST, mDataSet.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
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

        private String getPostTitle(Post post) {
            Resources resources = getResources();
            String title = "";
            String titleContent = "";
            switch (post.getType()) {
                case Post.KEY_TYPE_SPORTS:
                    title = resources.getString(R.string.buddy_for, resources.getString(R.string.playing, post.getTitle()));
                    break;
                case Post.KEY_TYPE_STUDY:
                    titleContent = post.getSubtitle().equals("") ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                    title = resources.getString(R.string.buddy_for, resources.getString(R.string.studying, titleContent));
                    break;
                case Post.KEY_TYPE_TRAVEL:
                    title = resources.getString(R.string.buddy_for, resources.getString(R.string.from_source_to_destination_post_text, post.getTitle(), post.getSubtitle()));
                    break;
                case Post.KEY_TYPE_CONCERT:
                    title = resources.getString(R.string.buddy_for, resources.getString(R.string.name_concert, post.getTitle()));
                    break;
                case Post.KEY_TYPE_OTHER:
                    titleContent = post.getSubtitle().equals("") ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                    title = resources.getString(R.string.buddy_for, titleContent);
                    break;
            }
            return title;
        }
    }
}
