package com.fotoup;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;

public class MainFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag1, container, false);
    }
}
