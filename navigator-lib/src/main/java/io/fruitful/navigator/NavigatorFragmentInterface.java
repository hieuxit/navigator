package io.fruitful.navigator;

/**
 * Created by hieuxit on 6/16/16.
 */

public interface NavigatorFragmentInterface {

    /**
     * @return True if fragment need 'back pressed' otherwise return False
     */
    boolean handleBackIfNeeded();

    /**
     * @return Navigator for fragment to open another fragment inside
     */
    Navigator getChildNavigator();

    /**
     * @return Navigator of root fragment - main navigator to open chain of Fragment
     */
    Navigator getNavigator();

    /**
     * @return Navigator of Activity
     */
    Navigator getRootNavigator();

    /**
     * @return True if fragment is root fragment that use as a container
     * and open a chain of other fragments. It means, this fragment own {@link }
     */
    boolean isRootFragment();

    NavigatorFragmentDispatcher getNavigatorDispatcher();
}