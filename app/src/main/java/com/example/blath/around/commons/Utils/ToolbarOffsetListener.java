package com.example.blath.around.commons.Utils;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.view.View;

import com.example.blath.around.R;

import java.lang.ref.WeakReference;

/**
 * Created by blath on 4/15/17.
 */

public class ToolbarOffsetListener implements OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;

    private boolean mIsTheTitleVisible = false;
    private int mToolbarTitleContentId;
    private WeakReference<View> mView = null;

    public ToolbarOffsetListener(View appBarViewContainer, int toolbarTitleContentId) {
        mView = new WeakReference(appBarViewContainer);
        mToolbarTitleContentId = toolbarTitleContentId;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        View view = mView.get();
        if (null == view) {
            return;
        }
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = Math.abs(offset) / (float) Math.abs(maxScroll);
        handleToolbarTitleVisibility(view, percentage);
    }

    private void handleToolbarTitleVisibility(@NonNull View view, float percentage) {
        View titleContent = view.findViewById(R.id.toolbar_title);
        View appbarContent = view.findViewById(R.id.appbar_content);
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                titleContent.setVisibility(View.VISIBLE);
                appbarContent.setVisibility(View.INVISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                titleContent.setVisibility(View.INVISIBLE);
                appbarContent.setVisibility(View.VISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }
}
