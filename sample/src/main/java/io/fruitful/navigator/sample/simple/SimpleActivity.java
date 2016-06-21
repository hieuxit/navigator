package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorFragmentActivity;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/12/16.
 */

public class SimpleActivity extends NavigatorFragmentActivity implements View.OnClickListener {

    char ch = 'A';

    CheckBox mCheckboxAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_replace).setOnClickListener(this);
        findViewById(R.id.bt_back).setOnClickListener(this);
        mCheckboxAnimation = (CheckBox) findViewById(R.id.cb_animation);
        // perform add click
        findViewById(R.id.bt_add).performClick();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_add:
            case R.id.bt_replace:
                boolean enableAnimation = mCheckboxAnimation.isChecked();
                int animEnter = enableAnimation ? R.anim.slide_in_right : 0;
                int animExit = enableAnimation ? R.anim.slide_out_left: 0;
                int animPopEnter = enableAnimation ? R.anim.slide_in_left : 0;
                int animPopExit = enableAnimation ? R.anim.slide_out_right : 0;

                getNavigator().openFragment(CharacterFragment.newInstance(ch), R.id.content, true,
                        id == R.id.bt_add ? LayoutType.ADD : LayoutType.REPLACE,
                        animEnter, animExit, animPopEnter, animPopExit);
                if (ch < 'Z') ch++;
                else ch = 'A';
                break;

            case R.id.bt_back:
                getNavigator().goBack(false);
                break;
        }
    }
}
