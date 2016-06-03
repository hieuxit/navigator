package io.fruitful.navigator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import io.fruitful.navigator.internal.NavigatorManager;
import io.fruitful.navigator.internal.NavigatorOwnerKind;

/**
 * Created by hieuxit on 5/27/16.
 */
public class NavigatorActivity extends FragmentActivity {
    private boolean mStateSaved;
    protected Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigator = new Navigator(this, getSupportFragmentManager());
        NavigatorManager.emitBindHasNavigator(this, NavigatorOwnerKind.NAVIGATOR_ACTIVITY);
    }

    @Override
    protected void onResume() {
        mStateSaved = false;
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateSaved = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNavigator != null) {
            mNavigator.destroy();
            mNavigator = null;
        }
    }

    public Navigator getNavigator() {
        return mNavigator;
    }

    @Override
    public void onBackPressed() {
        if (mNavigator != null && !mNavigator.goBack(false) && mNavigator.isRoot()) {
            finish();
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved;
    }


}
