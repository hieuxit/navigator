package io.fruitful.navigator.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.fruitful.navigator.NavigatorFragmentActivity;
import io.fruitful.navigator.sample.simple.SimpleActivity;
import io.fruitful.navigator.sample.simple.ViewPagerActivity;
import io.fruitful.navigator.sample.viewmodel.SimpleViewModelActivity;

public class MainActivity extends NavigatorFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_simple).setOnClickListener(this);
        findViewById(R.id.bt_view_pager).setOnClickListener(this);
        findViewById(R.id.bt_annotations).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_simple: {
                Intent intent = new Intent(this, SimpleActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.bt_view_pager: {
                Intent intent = new Intent(this, ViewPagerActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.bt_annotations: {
                Intent intent = new Intent(this, SimpleViewModelActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
