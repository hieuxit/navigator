package io.fruitful.navigator.internal;

import io.fruitful.navigator.Navigator;
import io.fruitful.navigator.NavigatorActivityInterface;
import io.fruitful.navigator.NavigatorFragmentInterface;

/**
 * Created by hieuxit on 6/1/16.
 */

public enum NavigatorOwnerKind {

    NAVIGATOR_FRAGMENT {
        @Override
        public Navigator getRootNavigator(Object owner) {
            return ((NavigatorFragmentInterface) owner).getRootNavigator();
        }

        @Override
        public Navigator getChildNavigator(Object owner) {
            return ((NavigatorFragmentInterface) owner).getChildNavigator();
        }

        @Override
        public Navigator getNavigator(Object owner) {
            return ((NavigatorFragmentInterface) owner).getNavigator();
        }
    },
    NAVIGATOR_ACTIVITY {
        @Override
        public Navigator getRootNavigator(Object owner) {
            return ((NavigatorActivityInterface) owner).getRootNavigator();
        }

        @Override
        public Navigator getChildNavigator(Object owner) {
            return ((NavigatorActivityInterface) owner).getRootNavigator();
        }

        @Override
        public Navigator getNavigator(Object owner) {
            return null;
        }
    };

    public abstract Navigator getRootNavigator(Object owner);

    public abstract Navigator getChildNavigator(Object owner);

    public abstract Navigator getNavigator(Object owner);
}
