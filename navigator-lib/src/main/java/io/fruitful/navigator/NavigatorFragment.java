package io.fruitful.navigator;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.fruitful.navigator.internal.NavigatorManager;
import io.fruitful.navigator.internal.NavigatorOwnerKind;

/**
 * Created by hieuxit on 5/27/16.
 */
public class NavigatorFragment extends Fragment {
    private Navigator mNavigator;

    /**
     * @return true if Fragment need interact with back command e.g: hide the popup layout,
     * hide search layout,... and more
     */
    public boolean handleBackIfNeeded() {
        return false;
    }

    public NavigatorActivity getBaseActivity() {
        return (NavigatorActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NavigatorActivity activity = getBaseActivity();
        mNavigator = new Navigator(activity, getChildFragmentManager());
        NavigatorManager.emitBindHasNavigator(this, NavigatorOwnerKind.NAVIGATOR_FRAGMENT);
    }

    public Navigator getRootNavigator() {
        return getBaseActivity().getNavigator();
    }

    public Navigator getParentNavigator() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) return getRootNavigator();
        return ((NavigatorFragment) parentFragment).getOwnNavigator();
    }

    public Navigator getOwnNavigator() {
        return mNavigator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNavigator != null) {
            mNavigator.destroy();
        }
    }
}
