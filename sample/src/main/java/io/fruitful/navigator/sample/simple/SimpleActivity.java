package io.fruitful.navigator.sample.simple;

import android.os.Bundle;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorFragmentActivity;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/12/16.
 */

public class SimpleActivity extends NavigatorFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        if (getRootNavigator().isBackStackEmpty()) {
            getRootNavigator().openFragment(new RootFragment(), LayoutType.ADD, false);
        }
    }
}
