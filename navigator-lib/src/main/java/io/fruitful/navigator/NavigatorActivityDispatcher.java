package io.fruitful.navigator;

import android.support.v4.app.FragmentActivity;

import io.fruitful.navigator.internal.NavigatorManager;
import io.fruitful.navigator.internal.NavigatorOwnerKind;

/**
 * Created by hieuxit on 6/16/16.
 */

public class NavigatorActivityDispatcher<ActivityType extends FragmentActivity & NavigatorActivityInterface> {

    private boolean isStateSaved;
    private Navigator rootNavigator;
    private ActivityType activity;

    public void onCreate(ActivityType activity) {
        this.activity = activity;
        this.rootNavigator = Navigator.fromActivity(activity);
        this.rootNavigator.setDefaultContentId(R.id.navigator_root_content);
        NavigatorManager.emitBindHasNavigator(activity, NavigatorOwnerKind.NAVIGATOR_ACTIVITY);
    }

    public void onResume() {
        isStateSaved = false;
    }

    public void onSaveInstanceState() {
        isStateSaved = true;
    }

    public void onDestroy() {
        if (rootNavigator != null) {
            rootNavigator.clean();
            rootNavigator = null;
        }
    }

    public void onBackPressed() {
        if (rootNavigator != null && !rootNavigator.goBack(false) && rootNavigator.isRoot()) {
            activity.finish();
        }
    }

    public boolean isStateSaved() {
        return this.isStateSaved;
    }

    public Navigator getRootNavigator() {
        return rootNavigator;
    }
}
