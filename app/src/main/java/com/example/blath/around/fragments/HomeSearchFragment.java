package com.example.blath.around.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.PostActivity;
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.events.GetSearchPostsEvent;
import com.example.blath.around.models.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HomeSearchFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private String mSearchPostType;
    private LinearLayout mSearchArea;
    private ImageView mArrowImage;
    private RecyclerView mRecyclerView;
    private List<Post> mDataSet;
    private SearchPostsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home_search, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_posts_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchPostsAdapter(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        Button sportsButton = (Button) mView.findViewById(R.id.sports_type);
        Button studyButton = (Button) mView.findViewById(R.id.study_type);
        Button travelButton = (Button) mView.findViewById(R.id.travel_type);
        Button concertButton = (Button) mView.findViewById(R.id.concert_type);
        Button otherButton = (Button) mView.findViewById(R.id.other_type);
        Button searchButton = (Button) mView.findViewById(R.id.search_button);
        mSearchArea = (LinearLayout) mView.findViewById(R.id.search_area);
        mArrowImage = (ImageView) mView.findViewById(R.id.arrow_image);
        sportsButton.setOnClickListener(this);
        studyButton.setOnClickListener(this);
        travelButton.setOnClickListener(this);
        concertButton.setOnClickListener(this);
        otherButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        mArrowImage.setOnClickListener(this);

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

    public void onEventMainThread(GetSearchPostsEvent result) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sports_type:
                selectAndUpdateBackground(v, mSearchPostType);
                mSearchPostType = Post.KEY_TYPE_SPORTS;
                break;
            case R.id.study_type:
                selectAndUpdateBackground(v, mSearchPostType);
                mSearchPostType = Post.KEY_TYPE_STUDY;
                break;
            case R.id.travel_type:
                selectAndUpdateBackground(v, mSearchPostType);
                mSearchPostType = Post.KEY_TYPE_TRAVEL;
                break;
            case R.id.concert_type:
                selectAndUpdateBackground(v, mSearchPostType);
                mSearchPostType = Post.KEY_TYPE_CONCERT;
                break;
            case R.id.other_type:
                selectAndUpdateBackground(v, mSearchPostType);
                mSearchPostType = Post.KEY_TYPE_OTHER;
                break;
            case R.id.search_button:
                fetchPostAndUpdateUI();
                break;
            case R.id.arrow_image:
                updateUI();
                break;

        }
    }

    private void selectAndUpdateBackground(View view, String prevSearchPostType) {
        if (prevSearchPostType != null && !prevSearchPostType.isEmpty()) {
            Button prevSearchPostTypeButton = (Button) mView.findViewById(R.id.sports_type);
            switch (prevSearchPostType) {
                case Post.KEY_TYPE_SPORTS:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.sports_type);
                    break;
                case Post.KEY_TYPE_STUDY:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.study_type);
                    break;
                case Post.KEY_TYPE_TRAVEL:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.travel_type);
                    break;
                case Post.KEY_TYPE_CONCERT:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.concert_type);
                    break;
                case Post.KEY_TYPE_OTHER:
                    prevSearchPostTypeButton = (Button) mView.findViewById(R.id.other_type);
                    break;
            }
            prevSearchPostTypeButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        }
        view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.post_other_background_start_color));
    }

    private void fetchPostAndUpdateUI(){
        //handle fetching of posts based on search criteria
        mSearchArea.setVisibility(View.GONE);
        mArrowImage.setImageResource(R.drawable.ic_down_arrow_blue);
    }

    private void updateUI(){
        if(View.VISIBLE == mSearchArea.getVisibility()){
            mSearchArea.setVisibility(View.GONE);
            mArrowImage.setImageResource(R.drawable.ic_down_arrow_blue);
        }else{
            mSearchArea.setVisibility(View.VISIBLE);
            mArrowImage.setImageResource(R.drawable.ic_up_arrow_blue);
        }
    }

    class SearchPostsAdapter extends RecyclerView.Adapter<SearchPostsAdapter.SearchPostsViewHolder> {

        class SearchPostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final RelativeLayout postHeaderLayout;
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

            SearchPostsViewHolder(View v) {
                super(v);
                postHeaderLayout = (RelativeLayout) v.findViewById(R.id.post_header_layout);
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
                bundle.putString(Post.KEY_POST_ACTION, Post.KEY_POST_DETAIL);
                bundle.putSerializable(Post.KEY_POST, mDataSet.get(itemPosition));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        SearchPostsAdapter(List<Post> dataset) {
            mDataSet = dataset;
        }

        @Override
        public SearchPostsAdapter.SearchPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.posts_list_item, parent, false);
            return new SearchPostsAdapter.SearchPostsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchPostsAdapter.SearchPostsViewHolder holder, final int position) {
            Resources resources = getResources();
            final Post post = mDataSet.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(post.getDates().getStartDate());
            holder.postUserIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.baseball));
            holder.postedDate.setText(DateUtils.dateFormatterFromString(post.getPostedDate().toString()));
            holder.postDay.setText(DateUtils.twoDigitDayOfMonth(calendar));
            holder.postMonthYear.setText(DateUtils.monthName(calendar) + ", " + DateUtils.lastTwoDigitYear(calendar));
            holder.postWeekDay.setText(DateUtils.weekDayName(calendar));
            setBackgroundAndTextColor(post.getType(), holder.postHeaderLayout, holder.postDay, holder.postMonthYear, holder.postWeekDay);
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
                    bundle.putString(Post.KEY_POST_ACTION, Post.KEY_POST_COMMENTS);
                    bundle.putSerializable(Post.KEY_POST, mDataSet.get(position));
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

        private void setBackgroundAndTextColor(String type, RelativeLayout headerLayout, TextView postDay, TextView postMonthYear, TextView postWeekDay) {
            Resources resources = getResources();
            switch (type) {
                case Post.KEY_TYPE_SPORTS:
                    headerLayout.setBackgroundResource(R.color.post_sport_background_start_color);
                    postDay.setTextColor(resources.getColor(R.color.post_sport_background_end_color));
                    postMonthYear.setTextColor(resources.getColor(R.color.post_sport_background_end_color));
                    postWeekDay.setTextColor(resources.getColor(R.color.post_sport_background_end_color));
                    break;
                case Post.KEY_TYPE_STUDY:
                    headerLayout.setBackgroundResource(R.color.around_background_end_color);
                    postDay.setTextColor(resources.getColor(R.color.around_background_start_color));
                    postMonthYear.setTextColor(resources.getColor(R.color.around_background_start_color));
                    postWeekDay.setTextColor(resources.getColor(R.color.around_background_start_color));
                    break;
                case Post.KEY_TYPE_TRAVEL:
                    headerLayout.setBackgroundResource(R.color.post_travel_background_end_color);
                    postDay.setTextColor(resources.getColor(R.color.post_travel_background_start_color));
                    postMonthYear.setTextColor(resources.getColor(R.color.post_travel_background_start_color));
                    postWeekDay.setTextColor(resources.getColor(R.color.post_travel_background_start_color));
                    break;
                case Post.KEY_TYPE_CONCERT:
                    headerLayout.setBackgroundResource(R.color.post_concert_background_end_color);
                    postDay.setTextColor(resources.getColor(R.color.post_concert_background_start_color));
                    postMonthYear.setTextColor(resources.getColor(R.color.post_concert_background_start_color));
                    postWeekDay.setTextColor(resources.getColor(R.color.post_concert_background_start_color));
                    break;
                case Post.KEY_TYPE_OTHER:
                    headerLayout.setBackgroundResource(R.color.post_other_background_end_color);
                    postDay.setTextColor(resources.getColor(R.color.post_other_background_start_color));
                    postMonthYear.setTextColor(resources.getColor(R.color.post_other_background_start_color));
                    postWeekDay.setTextColor(resources.getColor(R.color.post_other_background_start_color));
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


