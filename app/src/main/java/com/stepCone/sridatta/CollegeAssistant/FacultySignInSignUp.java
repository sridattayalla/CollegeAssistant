package com.stepCone.sridatta.CollegeAssistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HP-PC on 31-12-2017.
 */

public class FacultySignInSignUp extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.faculty_signin_signup,container,false);
        TabLayout tl=v.findViewById(R.id.faculty_tab_layout);
        ViewPager vp=v.findViewById(R.id.faculty_viewPager);
        tl.setupWithViewPager(vp);
        vp.setAdapter(new FacultyViewAdapter(getChildFragmentManager()));
        return v;
    }
}
