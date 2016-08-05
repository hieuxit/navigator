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

    private Navigator navigator;
    private FragmentType fragment;

    /**
     * Disable animation for fragment when transiting
     */
    private boolean disableAnimation;

    public void onActivityCreated(FragmentType fragment) {
        this.fragment = fragment;
        this.navigator = Navigator.fromFragment(fragment);
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
            return ((NavigatorActivityInterface) activity).getNavigator();
        }
        return null;
    }

    public Navigator getParentNavigator() {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment instanceof NavigatorFragmentInterface) {
            return ((NavigatorFragmentInterface) parentFragment).getOwnNavigator();
        }
        return null;
    }

    public Navigator getOwnNavigator() {
        return navigator;
    }

    public void onDestroy() {
        if (navigator != null) {
            navigator.clean();
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
