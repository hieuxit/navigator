package io.fruitful.navigator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by hieuxit on 4/13/16.
 */
public class Navigator {
    private NavigatorActivity activity;
    private FragmentManager fragmentManager;

    public Navigator(NavigatorActivity activity, FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
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

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
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

//    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
//                             LayoutType layoutType, boolean enableAnimation) {
//        if (enableAnimation) {
//            openFragment(fragment, contentId, backToCurrentFragment, layoutType, R.anim.slide_in_left,
//                    R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
//        } else {
//            openFragment(fragment, contentId, backToCurrentFragment, layoutType, 0, 0, 0, 0);
//        }
//    }

//    public void openFragment(Fragment fragment, @IdRes int contentId, boolean backToCurrentFragment,
//                             LayoutType layoutType) {
//        openFragment(fragment, contentId, backToCurrentFragment, layoutType, true);
//    }


    public NavigatorFragment getCurrentFragment(@IdRes int contentId) {
        return (NavigatorFragment) fragmentManager.findFragmentById(contentId);
    }

    public boolean goBack(boolean forceBack) {
        boolean isNavigateFromActivity = activity.getSupportFragmentManager() == fragmentManager;
        if (!isBackStackEmpty() && !forceBack) {
            List<Fragment> fragments = fragmentManager.getFragments();
            Fragment lastInFragment = null;
            if (fragments != null && fragments.size() > 0) {
                // maybe this is not true, because fragment manager may add a new fragment to the last of list
                lastInFragment = fragments.get(fragments.size() - 1);
                if (lastInFragment != null && lastInFragment instanceof NavigatorFragment) {
                    NavigatorFragment currentFragment = (NavigatorFragment) lastInFragment;
                    Navigator parentNavigator = currentFragment.getParentNavigator();
                    if (parentNavigator.isBackStackEmpty() || parentNavigator == this) {
                        if (currentFragment.handleBackIfNeeded()) return true;
                    } else if (!isNavigateFromActivity || !isRoot()) {
                        parentNavigator.popBackStack();
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

    public void destroy() {
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
