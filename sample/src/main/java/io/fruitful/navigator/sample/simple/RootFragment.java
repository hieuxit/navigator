package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorRootFragment;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 5/11/17.
 */

public class RootFragment extends NavigatorRootFragment implements View.OnClickListener {

    char ch = 'A';

    CheckBox mCheckboxAnimation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root, container, false);
        view.findViewById(R.id.bt_add).setOnClickListener(this);
        view.findViewById(R.id.bt_replace).setOnClickListener(this);
        view.findViewById(R.id.bt_back).setOnClickListener(this);
        mCheckboxAnimation = (CheckBox) view.findViewById(R.id.cb_animation);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // perform add click
        getView().findViewById(R.id.bt_add).performClick();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_add:
            case R.id.bt_replace:
                boolean enableAnimation = mCheckboxAnimation.isChecked();
                int animEnter = enableAnimation ? R.anim.navigator_slide_in_right : 0;
                int animExit = enableAnimation ? R.anim.navigator_slide_out_left : 0;
                int animPopEnter = enableAnimation ? R.anim.navigator_slide_in_left : 0;
                int animPopExit = enableAnimation ? R.anim.navigator_slide_out_right : 0;

                getNavigator().openFragment(CharacterFragment.newInstance(ch),
                        id == R.id.bt_add ? LayoutType.ADD : LayoutType.REPLACE, enableAnimation);
                if (ch < 'Z') ch++;
                else ch = 'A';
                break;

            case R.id.bt_back:
                getNavigator().goBack(false);
                break;
        }
    }
}
