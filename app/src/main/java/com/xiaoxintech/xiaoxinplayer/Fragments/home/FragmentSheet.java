package com.xiaoxintech.xiaoxinplayer.Fragments.home;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaoxintech.xiaoxinplayer.R;

public class FragmentSheet extends Fragment {

    private FragmentSheetViewModel mViewModel;

    public static FragmentSheet newInstance() {
        return new FragmentSheet();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FragmentSheetViewModel.class);
        // TODO: Use the ViewModel
    }

}
