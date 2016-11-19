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
import android.view.View;

import java.util.List;

/**
 *
 */
public class Navigator<ActivityType extends FragmentActivity & NavigatorActivityInterface> {

    private ActivityType activity;
    private FragmentManager fragmentManager;
    /**
     * if true: No animation will return on {@link Fragment#onCreateAnimation(int, boolean, int)}
     * So no animation for transition.
     */
    private boolean disableAnimation;

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

    public boolean isDisableAnimation() {
        return disableAnimation;
    }

    public void setDisableAnimation(boolean disableAnimation) {
        this.disableAnimation = disableAnimation;
    }

    /**
     * set default layout id whenever using a short version of {@link #openFragment(Fragment, int, boolean, LayoutType, int, int, int, int, View, String)}
     *
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
     * @param fragment              a fragment you next open
     * @param contentId             a layout id hosts the fragment
     * @param backToCurrentFragment if you want after open new fragment and never back
     *                              to current fragment set it false. default we will back to current fragment
     * @param layoutType            Add or Replace
     * @param enter                 enter animation
     * @param exit                  exit animation
     * @param popEnter              pop enter animation
     * @param popExit               pop exit animation
     * @param sharedElement         For android 21 and above sharedElement is a view which is disappearing and appearing
     * @param transitionName        A unique name for this transition
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, @AnimRes int enter, @AnimRes int exit,
                             @AnimRes int popEnter, @AnimRes int popExit,
                             View sharedElement, String transitionName) {
        if (activity == null || activity.isFinishing() || activity.isStateSaved()) return;
        ensureAnimationForFragment(fragment);
        ensureAnimationForFragmentsInBackstack(1);
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
        if (sharedElement != null) {
            if (transitionName == null)
                throw new NullPointerException("transitionName must be set and unique");
            ft.addSharedElement(sharedElement, transitionName);
        }
        ft.addToBackStack(Integer.toString((int) (2147483646.0D * Math.random())));
        ft.commit();
    }

    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, @AnimRes int enter, @AnimRes int exit,
                             @AnimRes int popEnter, @AnimRes int popExit) {
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, enter, exit, popEnter, popExit, null, null);
    }

    /**
     * a bit shorter of full version with enable or disable animation when changing fragment
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, boolean animation) {
        if (animation) {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType,
                    animEnter, animExit, animPopEnter, animPopExit, null, null);
        } else { // disable animation by call anim params as 0 all;
            openFragment(fragment, contentId, backToCurrentFragment, layoutType,
                    0, 0, 0, 0, null, null);
        }
    }

    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType, View sharedElement, String transitionName) {
        if (sharedElement != null) {
            if (transitionName == null)
                throw new NullPointerException("transitionName must be set and unique");
            openFragment(fragment, contentId, backToCurrentFragment, layoutType, 0, 0, 0, 0, sharedElement, transitionName);
        }
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, true);
    }

    /**
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
                             LayoutType layoutType) {
        openFragment(fragment, contentId, backToCurrentFragment, layoutType, true);
    }

    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType,
                             View sharedElement, String transitionName) {
        if (this.contentId == 0) {
            throw new IllegalStateException("call setDefaultContentId first");
        }
        if (sharedElement != null) {
            if (transitionName == null)
                throw new NullPointerException("transitionName must be set and unique");
            openFragment(fragment, contentId, backToCurrentFragment, layoutType, 0, 0, 0, 0, sharedElement, transitionName);
        }
        openFragment(fragment, backToCurrentFragment, layoutType, true);
    }

    /**
     * Open a fragment on a default container id
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType,
                             boolean animation) {
        if (this.contentId == 0) {
            throw new IllegalStateException("call setDefaultContentId first");
        }
        openFragment(fragment, this.contentId, backToCurrentFragment, layoutType, animation);
    }

    /**
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     */
    public void openFragment(Fragment fragment, boolean backToCurrentFragment, LayoutType layoutType) {
        if (this.contentId == 0) {
            throw new IllegalStateException("call setDefaultContentId first");
        }
        openFragment(fragment, this.contentId, backToCurrentFragment, layoutType, true);
    }

    public void openFragment(Fragment fragment, LayoutType layoutType,
                             View sharedElement, String transitionName) {
        openFragment(fragment, true, layoutType, sharedElement, transitionName);
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
     * id is not declared equivalent default content id is used
     * animation is not declared equivalent animation is enabled
     * backToCurrent is not declared equivalent will back
     */
    public void openFragment(Fragment fragment, LayoutType layoutType, boolean animation) {
        openFragment(fragment, this.contentId, true, layoutType, animation);
    }

    /**
     * get current fragment laid out on layout with contentId
     */
    public Fragment getCurrentFragment(@IdRes int contentId) {
        return fragmentManager.findFragmentById(contentId);
    }

    public boolean goBack(boolean forceBack) {
        boolean isNavigateFromActivity = activity.getSupportFragmentManager() == fragmentManager;
        if (!isBackStackEmpty() && !forceBack) {
            Fragment lastInFragment = getLastFragmentInBackStack();

            if (lastInFragment != null && lastInFragment instanceof NavigatorFragmentInterface) {
                NavigatorFragmentInterface currentFragment = (NavigatorFragmentInterface) lastInFragment;
                // Check if current fragment need back
                if (currentFragment.handleBackIfNeeded()) {
                    return true;
                }
            }
        }

        boolean pop = (!isNavigateFromActivity && !isBackStackEmpty()) || (isNavigateFromActivity && !isRoot());

        if (pop) {
            ensureAnimationForFragmentsInBackstack(2);
            popBackStack();
            return true;
        }
        return false;
    }

    /**
     * @param fragment: ensure disable animation for this fragment or not(from navigator)
     * @return true: if fragment implement {@link NavigatorFragmentInterface} and we can set
     * disableAnimation from Navigator. other wise return false
     */
    private boolean ensureAnimationForFragment(Fragment fragment) {
        if (fragment instanceof NavigatorFragmentInterface) {
            ((NavigatorFragmentInterface) fragment).getNavigatorDispatcher().setDisableAnimation(disableAnimation);
            return true;
        }
        return false;
    }

    /**
     * @param count: number of fragment if backstack will apply disableAnimation flag from
     *               Navigator. count is always 1 or 2.
     *               When 1: when user open a new fragment.
     *               Why and when 2: when we pop backstack the "last in" and the previous one
     *               will create animation for popAnimIn and exitAnimOut effect.
     *               So ensure enable or disable animation from navigator here must be apply to at least for 2 fragments.
     *               <p>
     *               -1: for all
     *               positive: for count fragment
     */
    private void ensureAnimationForFragmentsInBackstack(int count) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0) return;
        int sum = 0;
        for (int size = fragments.size(), i = size - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment == null) continue;
            if (ensureAnimationForFragment(fragment)) {
                sum++;
                if (sum == count) return;
            }
        }
    }

    private Fragment getLastFragmentInBackStack() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0) return null;
        for (int size = fragments.size(), i = size - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment == null) continue;
            if (fragment.isVisible()) return fragment;
        }
        return null;
    }

    public void backToRoot() {
        ensureAnimationForFragmentsInBackstack(-1);
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

    public void finishActivity() {
        activity.finish();
    }

    public void openActivityForResult(Class<? extends Activity> activityClassToOpen, Bundle args, int requestCode) {
        Intent intent = new Intent(activity, activityClassToOpen);
        if (args != null) {
            intent.putExtras(args);
        }
        activity.startActivityForResult(intent, requestCode);
    }
}
