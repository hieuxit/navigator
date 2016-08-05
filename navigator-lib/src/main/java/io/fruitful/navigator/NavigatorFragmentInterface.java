package io.fruitful.navigator;

/**
 * Created by hieuxit on 6/16/16.
 */

public interface NavigatorFragmentInterface {

    boolean handleBackIfNeeded();

    Navigator getOwnNavigator();

    Navigator getParentNavigator();

    Navigator getRootNavigator();

    NavigatorFragmentDispatcher getNavigatorDispatcher();
}
