package io.fruitful.navigator.sample;

import android.os.Bundle;

import io.fruitful.navigator.HasNavigator;
import io.fruitful.navigator.NavigatorActivity;
import io.fruitful.navigator.sample.viewmodel.ActivityViewModel;

public class MainActivity extends NavigatorActivity {

    @HasNavigator
    ActivityViewModel mActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityViewModel = new ActivityViewModel();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
