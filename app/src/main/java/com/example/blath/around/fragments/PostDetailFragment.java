package com.example.blath.around.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.IPostListener;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.models.Post;

public class PostDetailFragment extends Fragment {

    private IPostListener mCallback;
    private Post mPost;
    private View mView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        TextView postTitleContent = (TextView) mView.findViewById(R.id.post_title_content);
        postTitleContent.setText(getPostTitle(mPost));
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UIUtils.showToolbar(mView, (TextView) mView.findViewById(R.id.toolbar_title), getString(R.string.post_detail), "", R.drawable.back_icon_white, true, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        },  R.id.toolbar_title);
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
                titleContent = post.getSubtitle().equals("") ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                title = resources.getString(R.string.studying, titleContent);
                break;
            case Post.KEY_TYPE_TRAVEL:
                title = resources.getString(R.string.from_source_to_destination_post_text, post.getTitle(), post.getSubtitle());
                break;
            case Post.KEY_TYPE_CONCERT:
                title = resources.getString(R.string.name_concert, post.getTitle());
                break;
            case Post.KEY_TYPE_OTHER:
                title = post.getSubtitle().equals("") ? post.getTitle() : post.getTitle() + " (" + post.getSubtitle() + ")";
                break;
        }
        return title;
    }
}
