package io.fruitful.navigator.sample.viewmodel;

import io.fruitful.navigator.Navigator;
import io.fruitful.navigator.OwnNavigator;
import io.fruitful.navigator.ParentNavigator;
import io.fruitful.navigator.RootNavigator;

/**
 * Created by hieuxit on 5/27/16.
 */
public class FragmentViewModel {

    @RootNavigator
    Navigator mRootNavigator;

    @ParentNavigator
    Navigator mParentNavigator;

    @OwnNavigator
    Navigator mOwnNavigator;

}
