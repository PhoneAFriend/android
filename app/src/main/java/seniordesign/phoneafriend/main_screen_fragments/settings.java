package seniordesign.phoneafriend.main_screen_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seniordesign.phoneafriend.R;

public class settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.main_selections_layout, container, false);
        return inflater.inflate(R.layout.settings_layout, container, false);
        //return v;
    }
}