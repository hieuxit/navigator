package io.fruitful.navigator.internal;

import android.app.Activity;
import android.support.v4.app.Fragment;

import io.fruitful.navigator.Navigator;

/**
 * Created by hieuxit on 6/16/16.
 */

public class NavigatorFragmentDispatcher<FragmentType extends Fragment & NavigatorFragmentInterface> {

    private Navigator navigator;
    private FragmentType fragment;

    public void onActivityCreated(FragmentType fragment) {
        this.fragment = fragment;
        this.navigator = Navigator.fromFragment(fragment);
        NavigatorManager.emitBindHasNavigator(fragment, NavigatorOwnerKind.NAVIGATOR_FRAGMENT);
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
}
