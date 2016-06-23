package io.fruitful.navigator.sample.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.Navigator;
import io.fruitful.navigator.RootNavigator;
import io.fruitful.navigator.sample.R;
import io.fruitful.navigator.sample.simple.CharacterFragment;

/**
 * Created by hieuxit on 5/27/16.
 */
public class ActivityViewModel extends BaseObservable{

    @RootNavigator
    Navigator mRootNavigator;

    @Bindable
    private boolean isAnimation;

    private char ch = 'A';

    private void addFragment(LayoutType type) {
        boolean enableAnimation = isAnimation;
        int animEnter = enableAnimation ? R.anim.navigator_slide_in_right : 0;
        int animExit = enableAnimation ? R.anim.navigator_slide_out_left : 0;
        int animPopEnter = enableAnimation ? R.anim.navigator_slide_in_left : 0;
        int animPopExit = enableAnimation ? R.anim.navigator_slide_out_right : 0;

        mRootNavigator.openFragment(CharacterFragment.newInstance(ch), R.id.content, true,
                type, animEnter, animExit, animPopEnter, animPopExit);
        if (ch < 'Z') ch++;
        else ch = 'A';
    }

    public void addFragment() {
        addFragment(LayoutType.ADD);
    }

    public void replaceFragment() {
        addFragment(LayoutType.REPLACE);
    }

    public void back() {
        mRootNavigator.goBack(false);
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }
}
