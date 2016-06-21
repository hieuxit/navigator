package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorFragment;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/21/16.
 */

public class ViewPagerItemFragment extends NavigatorFragment implements View.OnClickListener {

    public static ViewPagerItemFragment newInstance(String text) {
        ViewPagerItemFragment instance = new ViewPagerItemFragment();
        Bundle args = new Bundle();
        args.putString("text", text);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_item, container, false);
        ((TextView) view.findViewById(R.id.text)).setText(getArguments().getString("text"));
        view.findViewById(R.id.bt_add_inside).setOnClickListener(this);
        view.findViewById(R.id.bt_replace_inside).setOnClickListener(this);
        view.findViewById(R.id.bt_add_outside).setOnClickListener(this);
        view.findViewById(R.id.bt_replace_outside).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        NewFragment fragment = NewFragment.newInstance();
        final int animEnter = R.anim.slide_in_right;
        final int animExit = R.anim.slide_out_left;
        final int animPopEnter = R.anim.slide_in_left;
        final int animPopExit = R.anim.slide_out_right;
        switch (id) {
            case R.id.bt_add_inside:
                // open inside current fragment must be use a container in fragment layout
                // here we use R.id.sub_content
                getOwnNavigator().openFragment(fragment, R.id.sub_content, true, LayoutType.ADD,
                        animEnter, animExit, animPopEnter, animPopExit);
                break;

            case R.id.bt_replace_inside:
                getOwnNavigator().openFragment(fragment, R.id.sub_content, true, LayoutType.REPLACE,
                        animEnter, animExit, animPopEnter, animPopExit);
                break;

            case R.id.bt_add_outside:
                getRootNavigator().openFragment(fragment, R.id.content, true, LayoutType.ADD,
                        animEnter, animExit, animPopEnter, animPopExit);
                break;

            case R.id.bt_replace_outside:
                getRootNavigator().openFragment(fragment, R.id.content, true, LayoutType.REPLACE,
                        animEnter, animExit, animPopEnter, animPopExit);
                break;
        }
    }
}
