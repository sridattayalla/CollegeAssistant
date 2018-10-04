package com.stepCone.sridatta.CollegeAssistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by HP-PC on 31-12-2017.
 */

public class FacultyViewAdapter extends FragmentPagerAdapter {
    public FacultyViewAdapter(FragmentManager fm) {
        super(fm);
    }


    //titles

    String[] titles={"Sign In","Sign Up"};
    @Override
    public CharSequence getPageTitle(int position) {return titles[position];}

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new FacultySignIn();
            case 1:return new FacultySignup();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
