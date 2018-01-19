package com.example.blath.around.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.IPostListener;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.ResponseOperations;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.PostCommentEvent;
import com.example.blath.around.models.Comment;
import com.example.blath.around.models.Post;
import com.example.blath.around.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class PostCommentsFragment extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<Comment> mDataSet;
    private PostCommentsAdapter mAdapter;
    private IPostListener mCallback;
    private Post mPost;
    private View mView;
    private Comment mComment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (IPostListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IPostListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPost = mCallback.getPostObject();
        mDataSet = mPost.getComments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_comments, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.comments_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PostCommentsAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mView.findViewById(R.id.new_comment_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText commentContentView = (EditText) mView.findViewById(R.id.new_comment_content);
                SharedPreferences userDetails = getActivity().getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
                mComment = new Comment(userDetails.getString(User.KEY_USERNAME, "Bhavesh Lath"), userDetails.getString(User.KEY_USER_ID, "001100110011"), commentContentView.getText().toString());
                Operations.postCommentOfPost(mComment, mPost.getId());
                commentContentView.setText("");
            }
        });
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.comments), null, R.drawable.back_icon_white, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        }, R.id.toolbar_title);
        UIUtils.animateStatusBarColorTransition(getActivity(), R.color.around_background_end_color, R.color.around_background_end_color);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        hideShowNoCommentsMessage();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(PostCommentEvent result) {
//        mView.findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
        if (!ResponseOperations.isError(result.getResponseObject())) {
            try {
                mDataSet.add(mComment);
                mAdapter.notifyDataSetChanged();
                hideShowNoCommentsMessage();
            } catch (Exception e) {

            }
        } else {
            UIUtils.showLongToast(result.getResponseObject().getMessage(), getActivity());
        }
    }

    class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.PostCommentViewHolder> {

        class PostCommentViewHolder extends RecyclerView.ViewHolder {
            final TextView mUserName;
            final TextView mContent;

            PostCommentViewHolder(View v) {
                super(v);
                mUserName = (TextView) v.findViewById(R.id.comment_username);
                mContent = (TextView) v.findViewById(R.id.comment_content);
            }
        }

        PostCommentsAdapter(ArrayList<Comment> dataSet) {
            mDataSet = dataSet;
        }

        @Override
        public PostCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.comments_list_item, parent, false);
            return new PostCommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostCommentViewHolder holder, int position) {
            final Comment comment = mDataSet.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendar.getTime());
            holder.mUserName.setText(comment.getDisplayName());
            holder.mContent.setText(comment.getContent());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    void hideShowNoCommentsMessage() {
        if (mDataSet.size() <= 0) {
            mView.findViewById(R.id.no_comments_heading).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.no_comments_message).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.no_comments_heading).setVisibility(View.GONE);
            mView.findViewById(R.id.no_comments_message).setVisibility(View.GONE);
        }
    }
}
