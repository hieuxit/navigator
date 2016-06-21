package io.fruitful.navigator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.fruitful.navigator.internal.NavigatorActivityDispatcher;
import io.fruitful.navigator.internal.NavigatorActivityInterface;

/**
 * Created by hieuxit on 5/27/16.
 */
public class NavigatorAppCompatActivity extends AppCompatActivity implements NavigatorActivityInterface {

    private NavigatorActivityDispatcher<NavigatorAppCompatActivity> dispatcher = new NavigatorActivityDispatcher<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher.onCreate(this);
    }

    @Override
    public boolean isStateSaved() {
        return dispatcher.isStateSaved();
    }

    @Override
    public Navigator getNavigator() {
        return dispatcher.getNavigator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dispatcher.onBackPressed();
    }
}
