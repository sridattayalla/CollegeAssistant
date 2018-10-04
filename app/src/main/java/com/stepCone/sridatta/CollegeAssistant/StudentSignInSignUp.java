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
 * Created by HP-PC on 30-12-2017.
 */

public class StudentSignInSignUp extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.student_signin_signup,container,false);
        //tab layout
        TabLayout tl=v.findViewById(R.id.student_tab_layout);
        ViewPager vp=v.findViewById(R.id.student_viewPager);
        tl.setupWithViewPager(vp);
        vp.setAdapter(new StudentViewAdapter(getChildFragmentManager()));
        return v;
    }
}
