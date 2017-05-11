package io.fruitful.navigator;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import io.fruitful.navigator.internal.NavigatorManager;
import io.fruitful.navigator.internal.NavigatorOwnerKind;

/**
 * Created by hieuxit on 6/16/16.
 */

public class NavigatorFragmentDispatcher<FragmentType extends Fragment & NavigatorFragmentInterface> {

    private Navigator childNavigator;
    private FragmentType fragment;

    /**
     * Disable animation for fragment when transiting
     */
    private boolean disableAnimation;

    public void onActivityCreated(FragmentType fragment) {
        this.fragment = fragment;
        this.childNavigator = Navigator.fromFragment(fragment);
        NavigatorManager.emitBindHasNavigator(fragment, NavigatorOwnerKind.NAVIGATOR_FRAGMENT);
    }

    public boolean isDisableAnimation() {
        return disableAnimation;
    }

    public void setDisableAnimation(boolean disableAnimation) {
        this.disableAnimation = disableAnimation;
    }

    public Navigator getRootNavigator() {
        Activity activity = fragment.getActivity();
        if (activity instanceof NavigatorActivityInterface) {
            return ((NavigatorActivityInterface) activity).getRootNavigator();
        }
        return null;
    }

    /**
     * Find the root fragment and return it's navigator
     *
     * @return Navigator of root fragment
     */
    public Navigator getNavigator() {
        Fragment parentFragment = fragment;
        while (parentFragment != null) {
            if (parentFragment instanceof NavigatorFragmentInterface) {
                NavigatorFragmentInterface navFragment = (NavigatorFragmentInterface) parentFragment;
                if (navFragment.isRootFragment()) {
                    return navFragment.getChildNavigator();
                }
            }
            parentFragment = parentFragment.getParentFragment();
        }
        return null;
    }

    public Navigator getChildNavigator() {
        return childNavigator;
    }

    public void onDestroy() {
        if (childNavigator != null) {
            childNavigator.clean();
        }
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (disableAnimation) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return null;
    }
}
