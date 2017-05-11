package io.fruitful.navigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by hieuxit on 5/27/16.
 */
public class NavigatorFragmentActivity extends FragmentActivity implements NavigatorActivityInterface {

    private NavigatorActivityDispatcher<NavigatorFragmentActivity> dispatcher = new NavigatorActivityDispatcher<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher.onCreate(this);
    }

    @Override
    public boolean isStateSaved() {
        return dispatcher.isStateSaved();
    }

    @Override
    public Navigator getRootNavigator() {
        return dispatcher.getRootNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }

    @Override
    public void onBackPressed() {
        dispatcher.onBackPressed();
    }
}
