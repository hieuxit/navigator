package io.fruitful.navigator.internal;

import io.fruitful.navigator.Navigator;
import io.fruitful.navigator.NavigatorActivity;
import io.fruitful.navigator.NavigatorFragment;

/**
 * Created by hieuxit on 6/1/16.
 */

public enum NavigatorOwnerKind {

    NAVIGATOR_FRAGMENT {
        @Override
        public Navigator getRootNavigator(Object owner) {
            return ((NavigatorFragment) owner).getRootNavigator();
        }

        @Override
        public Navigator getOwnNavigator(Object owner) {
            return ((NavigatorFragment) owner).getOwnNavigator();
        }

        @Override
        public Navigator getParentNavigator(Object owner) {
            return ((NavigatorFragment) owner).getParentNavigator();
        }
    },
    NAVIGATOR_ACTIVITY {
        @Override
        public Navigator getRootNavigator(Object owner) {
            return ((NavigatorActivity) owner).getNavigator();
        }

        @Override
        public Navigator getOwnNavigator(Object owner) {
            return ((NavigatorActivity) owner).getNavigator();
        }

        @Override
        public Navigator getParentNavigator(Object owner) {
            return null;
        }
    };

    public abstract Navigator getRootNavigator(Object owner);

    public abstract Navigator getOwnNavigator(Object owner);

    public abstract Navigator getParentNavigator(Object owner);
}
