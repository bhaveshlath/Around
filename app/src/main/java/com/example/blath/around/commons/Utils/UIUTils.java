package com.example.blath.around.commons.Utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.*;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.blath.around.R;

/**
 * Created by blath on 4/15/17.
 */

public class UIUTils{

    public static void showToolbar(@NonNull View view,
                                   @IdRes int toolBarTitleId,
                                   @StringRes int titleTextId,
                                   @StringRes int subTitleTextId,
                                   @DrawableRes int navigationIconResId,
                                   boolean showHomeAsUpEnabled,
                                   int toolbarTitleContentId) {
        TextView tv = (TextView) view.findViewById(toolBarTitleId);
        Resources r = tv.getResources();
        String titleText = 0 == titleTextId ? null : r.getString(titleTextId);
        String subTitleText = 0 == subTitleTextId ? null : r.getString(subTitleTextId);
        showToolbar(view, tv, titleText, subTitleText, navigationIconResId, showHomeAsUpEnabled, toolbarTitleContentId);
    }

    /**
     * Helper method to show Toolbar on the fragment.
     *
     * @param view Root View
     * @param title Title to display in the toolbar
     * @param toolbarTitle
     * @param subTitle Subtitle to display in the toolbar
     * @param navigationIconResId Resource ID of a drawable to set. Can be 0 to clear the icon.
     * @param showHomeAsUpEnabled Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     */
    public static void showToolbar(final View view,
                                   TextView toolbarTitle,
                                   String title,
                                   String subTitle,
                                   int navigationIconResId,
                                   boolean showHomeAsUpEnabled,
                                   int toolbarTitleContentId) {
        if (view == null) {
            return;
        }
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView expandedToolbarTitle = (TextView) view.findViewById(R.id.title);
        TextView expandedToolbarSubTitle = (TextView) view.findViewById(R.id.subtitle);

        if (toolbar != null) {
            if (navigationIconResId != 0) {
                toolbar.setNavigationIcon(navigationIconResId);
            }
            //set expanded toolbar title if its a tall toolbar
            if (expandedToolbarTitle != null && !android.text.TextUtils.isEmpty(title)) {
                expandedToolbarTitle.setText(title);
            }
            //set expanded toolbar subtitle if its a tall toolbar
            if (expandedToolbarSubTitle != null) {
                if (!android.text.TextUtils.isEmpty(subTitle)) {
                    expandedToolbarSubTitle.setText(subTitle);
                } else {
                    expandedToolbarSubTitle.setVisibility(View.GONE);
                }
            }
            //set title of the toolbar/actionbar
            if (toolbarTitle != null) {
                toolbarTitle.setText(title);
            }

            Context context = view.getContext();
            if (context instanceof ContextThemeWrapper) {
                context = ((ContextThemeWrapper)context).getBaseContext();
            }

            if (AppCompatActivity.class.isAssignableFrom(context.getClass())) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.setSupportActionBar(toolbar);
                ActionBar bar = activity.getSupportActionBar();
                if (bar != null) {
                    bar.setDisplayHomeAsUpEnabled(showHomeAsUpEnabled);
                    bar.setDisplayShowTitleEnabled(true);
                }
            }

            AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
            if (appBarLayout != null) {
                ToolbarOffsetListener offsetListener = new ToolbarOffsetListener(view, toolbarTitleContentId);
                appBarLayout.addOnOffsetChangedListener(offsetListener);
                toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        FragmentActivity activity = (FragmentActivity) view.getContext();
                        activity.onBackPressed();
                    }
                });
            }
        }
    }


    public static void animateStatusBarColorTransition(Context context, @ColorRes int startColor, @ColorRes int endColor) {
        int statusBarColorStart = ContextCompat.getColor(context, startColor);
        int statusBarColorEnd = ContextCompat.getColor(context, endColor);
        ValueAnimator statusBarColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), statusBarColorStart, statusBarColorEnd);
        final Activity activity = (Activity)context;

        statusBarColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
                }
            }
        });

        statusBarColorAnimation.setDuration(300);
        statusBarColorAnimation.setInterpolator(new AccelerateInterpolator());
        statusBarColorAnimation.start();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
