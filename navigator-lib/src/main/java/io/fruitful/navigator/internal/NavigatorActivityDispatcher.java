package io.fruitful.navigator.internal;

import android.support.v4.app.FragmentActivity;

import io.fruitful.navigator.Navigator;

/**
 * Created by hieuxit on 6/16/16.
 */

public class NavigatorActivityDispatcher<ActivityType extends FragmentActivity & NavigatorActivityInterface> {

    private boolean isStateSaved;
    private Navigator navigator;
    private ActivityType activity;

    public void onCreate(ActivityType activity) {
        this.activity = activity;
        this.navigator = Navigator.fromActivity(activity);
        NavigatorManager.emitBindHasNavigator(activity, NavigatorOwnerKind.NAVIGATOR_ACTIVITY);
    }

    public void onResume() {
        isStateSaved = false;
    }

    public void onSaveInstanceState() {
        isStateSaved = true;
    }

    public void onDestroy() {
        if (navigator != null) {
            navigator.clean();
            navigator = null;
        }
    }

    public void onBackPressed() {
        if (navigator != null && !navigator.goBack(false) && navigator.isRoot()) {
            activity.finish();
        }
    }

    public boolean isStateSaved() {
        return this.isStateSaved;
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
