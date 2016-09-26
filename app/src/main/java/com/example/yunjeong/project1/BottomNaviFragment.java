package com.example.yunjeong.project1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by YunJeong on 2016-08-12.
 */
public class BottomNaviFragment extends Fragment {

    private static final String STARTING_TEXT = "Four Buttons Bottom Navigation";

    public BottomNaviFragment() {
    }

    public static BottomNaviFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(STARTING_TEXT, text);

        BottomNaviFragment bottomNaviFragment = new BottomNaviFragment();
        bottomNaviFragment.setArguments(args);
        return  bottomNaviFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(getArguments().getString(STARTING_TEXT));
        return textView;
    }
}