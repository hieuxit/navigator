package io.fruitful.navigator.sample.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import io.fruitful.navigator.LayoutType;
import io.fruitful.navigator.NavigatorFragment;
import io.fruitful.navigator.sample.R;

/**
 * Created by hieuxit on 6/12/16.
 */

public class CharacterFragment extends NavigatorFragment implements View.OnClickListener {

    int backgroundColor;

    public static CharacterFragment newInstance(char ch) {
        CharacterFragment instance = new CharacterFragment();
        Bundle args = new Bundle();
        args.putChar("character", ch);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);
        char ch = getArguments().getChar("character");
        ((TextView) view.findViewById(R.id.text)).setText(String.valueOf(ch));
        if (backgroundColor == 0) {
            if (savedInstanceState != null) {
                backgroundColor = savedInstanceState.getInt("backgroundColor");
            } else {
                backgroundColor = 0xff000000 | new Random().nextInt(0xffffff);
            }
        }
        view.setBackgroundColor(backgroundColor);
        view.findViewById(R.id.bt_add).setOnClickListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("backgroundColor", backgroundColor);
    }

    @Override
    public void onClick(View v) {
        getNavigator().openFragment(CharacterFragment.newInstance('w'), LayoutType.ADD, true);
    }
}
