package com.stepCone.sridatta.CollegeAssistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by HP-PC on 30-12-2017.
 */

public class StudentViewAdapter extends FragmentPagerAdapter {

    //constructor
    public StudentViewAdapter(FragmentManager fm) {super(fm);}

    //titles

    String[] titles={"Sign In","Sign Up"};
    @Override
    public CharSequence getPageTitle(int position) {return titles[position];}

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new StudentLogin();
            case 1:return new StudentSignup();
            default:return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {return 2;}
}
