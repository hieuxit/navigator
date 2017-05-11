package io.fruitful.navigator.sample.viewmodel;

import io.fruitful.navigator.annotation.ChildNavigator;
import io.fruitful.navigator.annotation.Navigator;
import io.fruitful.navigator.annotation.RootNavigator;

/**
 * Created by hieuxit on 5/27/16.
 */
public class FragmentViewModel {

    @RootNavigator
    io.fruitful.navigator.Navigator mRootNavigator;

    @Navigator
    io.fruitful.navigator.Navigator mNavigator;

    @ChildNavigator
    io.fruitful.navigator.Navigator mOwnNavigator;

}
