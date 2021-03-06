package com.example.blath.around.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.DateUtils;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.events.SubmitPostEvent;
import com.example.blath.around.models.Post;

import de.greenrobot.event.EventBus;


public class NewPostReviewFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = NewPostReviewFragment.class.getSimpleName();
    private View mView;
    private Post mPost;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_post_review, container, false);
        mActivity = getActivity();
        mPost = (Post) getArguments().getSerializable(NewPostMainFragment.KEY_NEW_POST);
        TextView titleContent = (TextView) mView.findViewById(R.id.post_title_content);
        titleContent.setText(getPostTitle(mPost));
        TextView dateText = (TextView) mView.findViewById(R.id.post_date_content);
        dateText.setText(DateUtils.dateFormatterFromString(mPost.getDates().getStartDate().toString()));
        TextView timeText = (TextView) mView.findViewById(R.id.post_time_content);
        timeText.setText(DateUtils.timeFormatter(mPost.getTime()));
        TextView locationText = (TextView) mView.findViewById(R.id.post_location_content);
        locationText.setText(mPost.getLocation().getAddress());
        TextView ageRangeText = (TextView) mView.findViewById(R.id.post_age_range_content);
        ageRangeText.setText(mPost.getAgeRange().getMinAge() + " " + getString(R.string.to) + " " + mPost.getAgeRange().getMaxAge() + " " + getString(R.string.years));
        TextView genderPreferenceText = (TextView) mView.findViewById(R.id.post_gender_preference_content);
        genderPreferenceText.setText(mPost.getGenderPreference());
        TextView descriptionText = (TextView) mView.findViewById(R.id.post_description_content);
        descriptionText.setText(mPost.getDescription());

        View confirmButton = mView.findViewById(R.id.submit_button);
        confirmButton.setOnClickListener(this);

        UIUtils.hideKeyboard(getActivity());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.review_submit), "", R.drawable.back_icon_black, true, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        },  R.id.toolbar_title);
        UIUtils.animateStatusBarColorTransition(mActivity, R.color.dropdown_blue, R.color.dropdown_blue);
    }

    public void onEventMainThread(SubmitPostEvent result) {
        mView.findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
        if (!ResponseOperations.isError(result.getResponseObject())) {
            NewPostSuccessFragment newPostSuccessFragment = new NewPostSuccessFragment();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.new_post_container, newPostSuccessFragment, TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            UIUtils.showLongToast(result.getResponseObject().getMessage(), mActivity);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                mView.findViewById(R.id.progress_overlay_container).setVisibility(View.VISIBLE);
                Operations.submitPost(mPost);
        }
    }

    private String getPostTitle(Post post) {
        Resources resources = getResources();
        String title = "";
        switch (post.getType()) {
            case Post.KEY_TYPE_SPORTS:
                title = resources.getString(R.string.playing, post.getTitle());
                break;
            case Post.KEY_TYPE_STUDY:
                String titleContent = "";
                titleContent = post.getSubtitle().isEmpty() ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                title = resources.getString(R.string.studying, titleContent);
                break;
            case Post.KEY_TYPE_TRAVEL:
                title = resources.getString(R.string.from_source_to_destination_post_text, post.getTitle(), post.getSubtitle());
                break;
            case Post.KEY_TYPE_CONCERT:
                title = resources.getString(R.string.name_concert, post.getTitle());
                break;
            case Post.KEY_TYPE_OTHER:
                title = post.getSubtitle().isEmpty() ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                break;
        }
        return title;
    }
}
