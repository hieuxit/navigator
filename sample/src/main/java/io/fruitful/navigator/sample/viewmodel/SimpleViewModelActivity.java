package io.fruitful.navigator.sample.viewmodel;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.fruitful.navigator.HasNavigator;
import io.fruitful.navigator.NavigatorFragmentActivity;
import io.fruitful.navigator.sample.R;
import io.fruitful.navigator.sample.databinding.ActivitySimpleViewModelBinding;

/**
 * Created by hieuxit on 6/21/16.
 */

public class SimpleViewModelActivity extends NavigatorFragmentActivity {

    @HasNavigator
    ActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // ViewModel must be initialized before call super.onCreate(savedInstanceState)
        viewModel = new ActivityViewModel();
        super.onCreate(savedInstanceState);
        ActivitySimpleViewModelBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_view_model);
        binding.setVm(viewModel);
        viewModel.addFragment();
    }
}
