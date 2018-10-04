package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepCone.sridatta.CollegeAssistant.R;

/**
 * Created by HP-PC on 29-01-2018.
 */

public class AttendenceDialog extends DialogFragment implements View.OnClickListener {
    attendenceDialogHandle adh;
    TextView confirm;
    TextView cancel;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.attendence_submit_confirm_dialog,container,false);
        confirm=v.findViewById(R.id.attendence_post_confirm);
        confirm.setOnClickListener(this);
        cancel=v.findViewById(R.id.attendence_post_cancle);
        cancel.setOnClickListener(this);
        return v;

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.attendence_post_confirm){
            adh.clicked("confirm");
        }
        if(view.getId()== R.id.attendence_post_cancle){
            adh.clicked("cancel");
        }
    }

    public interface attendenceDialogHandle{
        public void clicked(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adh=(attendenceDialogHandle)activity;
    }
}
