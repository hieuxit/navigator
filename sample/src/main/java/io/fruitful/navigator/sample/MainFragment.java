package io.fruitful.navigator.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.fruitful.navigator.HasNavigator;
import io.fruitful.navigator.NavigatorFragment;
import io.fruitful.navigator.sample.viewmodel.FragmentViewModel;

/**
 * Created by hieuxit on 5/27/16.
 */
public class MainFragment extends NavigatorFragment {

    @HasNavigator
    FragmentViewModel mFragmentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentViewModel = new FragmentViewModel();
    }
}
