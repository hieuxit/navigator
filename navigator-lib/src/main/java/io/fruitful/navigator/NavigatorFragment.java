package io.fruitful.navigator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

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
    public boolean isRootFragment() {
        return false;
    }

    @Override
    public NavigatorFragmentDispatcher getNavigatorDispatcher() {
        return dispatcher;
    }

    @Override
    public Navigator getNavigator() {
        return dispatcher.getNavigator();
    }

    @Override
    public Navigator getChildNavigator() {
        return dispatcher.getChildNavigator();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return dispatcher.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }
}
