package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;
import com.example.blath.around.fragments.PostCommentsFragment;
import com.example.blath.around.fragments.PostDetailFragment;
import com.example.blath.around.models.Post;

public class PostActivity extends AppCompatActivity implements IPostListener{

    String mPostAction;
    Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_end_color, R.color.around_background_end_color);
        mPostAction = getIntent().getExtras().getString(Post.KEY_POST_ACTION);
        mPost = (Post) getIntent().getExtras().getSerializable(Post.KEY_POST);

        switch (mPostAction){
            case Post.KEY_POST_DETAIL:
                PostDetailFragment postDetailFragment = new PostDetailFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.post_container, postDetailFragment).commit();
                break;
            case Post.KEY_POST_COMMENTS:
                PostCommentsFragment postCommentsFragment = new PostCommentsFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.post_container, postCommentsFragment).commit();
                break;
            case Post.KEY_POST_REPLY:
                break;
        }
    }

    @Override
    public Post getPostObject() {
        return mPost;
    }
}
