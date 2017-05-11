package io.fruitful.navigator;

import android.os.Bundle;

/**
 * Created by hieuxit on 5/11/17.
 */

public class NavigatorRootFragment extends NavigatorFragment {
    @Override
    public boolean isRootFragment() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getNavigator().setDefaultContentId(R.id.navigator_content);
    }
}
