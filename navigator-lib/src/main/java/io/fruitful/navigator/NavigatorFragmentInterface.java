package io.fruitful.navigator;

import io.fruitful.navigator.Navigator;

/**
 * Created by hieuxit on 6/16/16.
 */

public interface NavigatorFragmentInterface {

    boolean handleBackIfNeeded();

    Navigator getOwnNavigator();

    Navigator getParentNavigator();

    Navigator getRootNavigator();
}
