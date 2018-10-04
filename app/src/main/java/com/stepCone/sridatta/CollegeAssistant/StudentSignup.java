package com.stepCone.sridatta.CollegeAssistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.spec.ECField;

/**
 * Created by HP-PC on 30-12-2017.
 */

public class StudentSignup extends Fragment {
    Activity context;
    TextView userid;
    TextView userpassword,confirmStudentPassword,phoneno;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.student_sign_up,container,false);
        context=getActivity();
        return v;
    }

    //constructor
    public StudentSignup StudentSignup(){
        return new StudentSignup();
    }


    public void onStart(){
        super.onStart();
        //login button
        Button b=context.findViewById(R.id.student_sign_up);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkInternetConnection())
                {
                    TastyToast.makeText(context,"check internet connection",Toast.LENGTH_LONG, TastyToast.CONFUSING).show();
                }
                else if(validated()){
                    userid=context.findViewById(R.id.studetnt_id);
                    String id=userid.getText().toString();
                    userpassword=context.findViewById(R.id.etstudent_signup_password);
                    String password=userpassword.getText().toString();
                    confirmStudentPassword=context.findViewById(R.id.etconfirm_student_password);
                    String confirmPassword=confirmStudentPassword.getText().toString();
                    phoneno=context.findViewById(R.id.student_mobile_number);
                    String mobileno=phoneno.getText().toString();
                    if(phoneno.length()==10){
                    if(password.equals(confirmPassword)){
                    new studentSignUp().execute(id,password,mobileno);}
                    else{userid.setError("Password not Matched");}}
                    else{TastyToast.makeText(getActivity(),"Invalied Mobile Number",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}
                }


                /*if(password.equalsIgnoreCase(confirmPassword)){
                new studentSignUp().execute(id,password,mobileno);}
                else {
                    Toast.makeText(context,"password not matched",Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }


    //async task

    class studentSignUp extends AsyncTask<String,String,String>{
        String line;
        URL url;
        HttpURLConnection conn;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(getContext());
            pd.setMessage("loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            int READ_TIMEOUT=10000;
            int READ_CONNECTION=10000;
            String Id=strings[0];
            String pass=strings[1];
            String phoneno=strings[2];

            //setting url
            try {
                url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/student_register.php");
            }
            catch (Exception e){
                return "url not found";
            }

            //opening connnection
            try{conn=(HttpURLConnection)url.openConnection();}
            catch (Exception e){return "connection not established";}

            //setting connection
            try {
                conn.setRequestMethod("POST");
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(Id,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8")+"&"+URLEncoder.encode("phoneno","UTF-8")+"="+URLEncoder.encode(phoneno,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //input stream
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));

                line=bufferedReader.readLine();
                //return line;
                switch (line){
                    case "Successful, Go back and login": return "Registration Successful";
                    case "User already exist":return "user alredy exit";
                    case "No match in student database":return "No match in student database";
                    default: return "Unsuccesfull";

                }

                /*if(bufferedReader.readLine().equalsIgnoreCase("Registration Successful")){
                    return "succefully registered";
                }
                else if(bufferedReader.readLine().equalsIgnoreCase("user exist")){
                    return "user alredy exist";
                }
                else{return "Unsuccesfull";}*/
            }

            catch (Exception e){
                return e.toString();
            }
            finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            userid.setText("");
            userpassword.setText("");
            confirmStudentPassword.setText("");
            phoneno.setText("");
            if(s.equals("connection problem")){TastyToast.makeText(context,s,Toast.LENGTH_LONG, TastyToast.INFO).show();}
            else if(s.equals("Unsuccesfull")){TastyToast.makeText(context,s,Toast.LENGTH_LONG, TastyToast.ERROR).show();}
            else if(s.equals("Registration Successful")){TastyToast.makeText(context,s,Toast.LENGTH_LONG, TastyToast.SUCCESS).show();}
            else if(s.equals("user alredy exist")){TastyToast.makeText(context,s,Toast.LENGTH_LONG, TastyToast.ERROR).show();}
            else if(s.equals("No match in student database")){TastyToast.makeText(context,s,Toast.LENGTH_LONG, TastyToast.WARNING).show();}

        }
    }

    //validate
    public boolean validated() {
        String error;
        EditText emp_id,emp_password;
        emp_id = (EditText)context.findViewById(R.id.studetnt_id);
        emp_password = (EditText)context.findViewById(R.id.etstudent_signup_password);
        String id = emp_id.getText().toString();
        String password = emp_password.getText().toString();
        Validation validate=new Validation();
        if(!(error=validate.idValidation(id)).equals("true")){
            emp_id.setError(error);
            return false;
        }
        if(!(error=validate.passwordValidation(password)).equals("true")){
            emp_id.setError(error);
            return false;
        }
        return true;
    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}
