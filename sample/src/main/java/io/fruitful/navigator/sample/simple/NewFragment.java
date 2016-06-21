package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import io.fruitful.navigator.NavigatorFragment;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/21/16.
 */

public class NewFragment extends NavigatorFragment {

    public static NewFragment newInstance() {
        return new NewFragment();
    }

    int backgroundColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        if (backgroundColor == 0) {
            if (savedInstanceState != null) {
                backgroundColor = savedInstanceState.getInt("backgroundColor");
            } else {
                backgroundColor = 0xff000000 | new Random().nextInt(0xffffff);
            }
        }
        view.setBackgroundColor(backgroundColor);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("backgroundColor", backgroundColor);
    }
}
