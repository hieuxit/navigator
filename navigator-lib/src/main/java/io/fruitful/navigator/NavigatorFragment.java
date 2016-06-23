package io.fruitful.navigator;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by hieuxit on 5/27/16.
 */
public class NavigatorFragment extends Fragment implements NavigatorFragmentInterface {

    private NavigatorFragmentDispatcher<NavigatorFragment> dispatcher = new NavigatorFragmentDispatcher<>();

    /**
     * @return true if Fragment need interact with back command e.g: hide the popup layout,
     * hide search layout,... and more
     */
    public boolean handleBackIfNeeded() {
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dispatcher.onActivityCreated(this);
    }

    @Override
    public Navigator getRootNavigator() {
        return dispatcher.getRootNavigator();
    }

    @Override
    public Navigator getParentNavigator() {
        return dispatcher.getParentNavigator();
    }

    @Override
    public Navigator getOwnNavigator() {
        return dispatcher.getOwnNavigator();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }
}
