package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorFragmentActivity;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/21/16.
 */

public class ViewPagerActivity extends NavigatorFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        getRootNavigator().openFragment(ViewPagerFragment.newInstance(), R.id.content, true, LayoutType.ADD,
                0, 0, 0, 0);
    }
}
