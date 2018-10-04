package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

public class sectionDropDown implements OnItemClickListener {

    String TAG = "DogsDropdownOnItemClickListener.java";

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

        // get the context and main activity to access variables
        Context mContext = v.getContext();
        AttendenceRegister mainActivity = ((AttendenceRegister) mContext);
        ((AttendenceRegister) mContext).loadingIndicatorView.show();
        for(int i=0;i<31;i++){
            ((AttendenceRegister) mContext).customCheckBoxes.get(i).setVisibility(View.GONE);
            ((AttendenceRegister) mContext).rollnoText.get(i).setVisibility(View.GONE);
        }

        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);

        // dismiss the pop up
        mainActivity.popupWindowSections.dismiss();

        // get the text and set it as the button text
        mainActivity.section_name= ((TextView) v).getText().toString();
        mainActivity.buttonShowDropDown.setText(mainActivity.section_name);
        mainActivity.section_no=arg2;

        //getting rollnos
        mainActivity.setAndgetRollno();
       // mainActivity.makeThemVisible();

        //setting subject
        mainActivity.subject=mainActivity.HandlingSubjectsList.get(arg2);
       TastyToast.makeText(mainActivity.context,mainActivity.subject,Toast.LENGTH_LONG,TastyToast.DEFAULT).show();


    }

}
