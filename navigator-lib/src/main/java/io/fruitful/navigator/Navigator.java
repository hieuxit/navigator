package io.fruitful.navigator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.List;

/**
 *
 */
public class Navigator<ActivityType extends FragmentActivity & NavigatorActivityInterface> {
    private ActivityType activity;
    private FragmentManager fragmentManager;

    @IdRes
    private int contentId;

    @AnimRes
    private int animEnter, animExit, animPopEnter, animPopExit;

    public Navigator(ActivityType activity, FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        setDefaultAnim(R.anim.navigator_slide_in_right, R.anim.navigator_slide_out_left,
                R.anim.navigator_slide_in_left, R.anim.navigator_slide_out_right);
    }

    public static <ActivityType extends FragmentActivity & NavigatorActivityInterface> Navigator fromActivity(ActivityType activity) {
        return new Navigator(activity, activity.getSupportFragmentManager());
    }

    public static <FragmentType extends Fragment & NavigatorFragmentInterface> Navigator fromFragment(FragmentType fragment) {
        return new Navigator(fragment.getActivity(), fragment.getChildFragmentManager());
    }

    public static void openGooglePlay(Context context, String packageApp) {
        openGooglePlay(context, packageApp, false);
    }

    public static void openGooglePlay(Context context, String packageApp, boolean flagStartNewActivity) {
        if (context == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageApp));
            if (flagStartNewActivity) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageApp)));
        }
    }

    public static void openOtherApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public static void openUrlOnWebApp(String url, Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void openAppToSendText(Context context, String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(i);
    }

    public static void openEmail(Context context, String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        context.startActivity(Intent.createChooser(emailIntent, "Open with"));
    }

    public static void openDial(Context context, String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

    /**
     * set default layout id whenever using a short version of {@link #openFragment(Fragment, int, boolean, LayoutType, int, int, int, int)}
     * @param contentId your layout id to host fragment
     */
    public void setDefaultContentId(@IdRes int contentId) {
        this.contentId = contentId;
    }

    /**
     * set default animation when opening fragment
     */
    public void setDefaultAnim(@AnimRes int animEnter, @AnimRes int animExit,
                               @AnimRes int animPopEnter, @AnimRes int animPopExit) {
        this.animEnter = animEnter;
        this.animExit = animExit;
        this.animPopEnter = animPopEnter;
        this.animPopExit = animPopExit;
    }

    /**
     * @param fragment a fragment you next open
     * @param contentId a layout id hosts the fragment
     * @param backToCurrentFragment if you want after open new fragment and never back
     *                              to current fragment set it false. default we will back to current fragment
     * @param layoutType Add or Replace
     * @param enter enter animation
     * @param exit exit animation
     * @param popEnter pop enter animation
     * @param popExit pop exit animation
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, @AnimRes int enter, @AnimRes int exit,
                             @AnimRes int popEnter, @AnimRes int popExit) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (enter > 0 || exit > 0 || popEnter > 0 || popExit > 0) {
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
        }
        if (layoutType == LayoutType.ADD) {
            ft.add(contentId, fragment);
        } else {
            ft.replace(contentId, fragment);
        }

        if (!backToCurrentFragment) {
            popBackStack();
        }
        ft.addToBackStack(Integer.toString((int) (2147483646.0D * Math.random())));
        ft.commit();
    }

    /**
     * a bit shorter of full version with enable or disable animation when changing fragment
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, boolean animation) {
        if (animation) {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType,
                    animEnter, animExit, animPopEnter, animPopExit);
        } else { // disable animation by call anim params as 0 all;
            openFragment(fragment, contentId, backToCurrentFragment, layoutType,
                    0, 0, 0, 0);
        }
    }

    /**
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType) {
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, true);
    }

    /**
     * Open a fragment on a default container id
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType,
                             boolean animation) {
        if(this.contentId == 0){
            throw new IllegalStateException("call setDefaultContentId first");
        }
        openFragment(fragment, this.contentId, backToCurrentFragment, layoutType, animation);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType) {
        if(this.contentId == 0){
            throw new IllegalStateException("call setDefaultContentId first");
        }
        openFragment(fragment, this.contentId, backToCurrentFragment, layoutType, true);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     * backToCurrent is not declared equivalent will back
     */
    public void openFragment(Fragment fragment, LayoutType layoutType) {
        openFragment(fragment, true, layoutType);
    }

    /**
     * get current fragment laid out on layout with contentId
     */
    public NavigatorFragment getCurrentFragment(@IdRes int contentId) {
        return (NavigatorFragment) fragmentManager.findFragmentById(contentId);
    }

    public boolean goBack(boolean forceBack) {
        boolean isNavigateFromActivity = activity.getSupportFragmentManager() == fragmentManager;
        if (!isBackStackEmpty() && !forceBack) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                Fragment lastInFragment = fragments.get(fragments.size() - 1);
                if (lastInFragment != null && lastInFragment instanceof NavigatorFragmentInterface) {
                    NavigatorFragmentInterface currentFragment = (NavigatorFragmentInterface) lastInFragment;
                    // Check if current fragment need back
                    if (currentFragment.handleBackIfNeeded()) {
                        return true;
                    }
                }
            }
        }

        boolean pop = (!isNavigateFromActivity && !isBackStackEmpty()) || (isNavigateFromActivity && !isRoot());

        if (pop) {
            popBackStack();
            return true;
        }
        return false;
    }

    public void backToRoot() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if (!isRoot()) {
            for (int i = backStackCount - 1; i >= 1; i--) {
                popBackStack();
            }
        }
    }

    public boolean isBackStackEmpty() {
        return fragmentManager.getBackStackEntryCount() == 0;
    }

    public boolean isRoot() {
        return fragmentManager.getBackStackEntryCount() <= 1;
    }

    public void clean() {
        activity = null;
        fragmentManager = null;
    }

    public void popBackStack() {
        if (activity == null || activity.isStateSaved()) {
            return;
        }
        fragmentManager.popBackStack();
    }

    public void openActivity(Class<? extends Activity> activityClassToOpen, Bundle args, boolean finishCurrentActivity) {
        Intent intent = new Intent(activity, activityClassToOpen);
        if (args != null) {
            intent.putExtras(args);
        }
        activity.startActivity(intent);
        if (finishCurrentActivity) {
            activity.finish();
        }
    }
}
