package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PostLinkActivity extends AppCompatActivity {
    EditText link;
    EditText description;
    Button postLink;
    String linktoPost;
    String descriptionToPost;
    Toolbar tb;
    AVLoadingIndicatorView loader;
    TextView submitting,hint;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_link);
        //toolbar
        tb=findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //loading
        loader=findViewById(R.id.link_post_loader);
        submitting=findViewById(R.id.link_submitting);
        loader.hide();

        //card
        cardView=findViewById(R.id.post_link_card);
        hint=findViewById(R.id.postlink_hint);

        link =findViewById(R.id.post_link_link);
        description=findViewById(R.id.post_link_description);
        postLink=findViewById(R.id.post_link_button);
        postLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInternetConnection()){
                    TastyToast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT,TastyToast.CONFUSING).show();
                }
                else {
                    linktoPost = link.getText().toString();
                    descriptionToPost = description.getText().toString();
                    if (linktoPost == null || linktoPost.equals("")) {
                        TastyToast.makeText(getApplicationContext(),"Invalied Url",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                    } else if (descriptionToPost.equals("") || descriptionToPost == null) {
                        TastyToast.makeText(getApplicationContext(),"Please Enter Description",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                    } else {
                        //visibility
                        cardView.setVisibility(View.GONE);
                        hint.setVisibility(View.GONE);
                        loader.show();
                        submitting.setVisibility(View.VISIBLE);
                        new postLink().execute();
                    }
                }
            }
        });
    }

    class postLink extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/post_link.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                SharedPreferences sf= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id=sf.getString("id",null);
                br.write(URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(linktoPost, "UTF-8") + "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(descriptionToPost, "UTF-8") + "&" + URLEncoder.encode("roll_no", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String s = bufferedReader.readLine();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            link.setText("");
            description.setText("");
            loader.hide();
            submitting.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            TastyToast.makeText(getApplicationContext(),"Complete",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
        }
    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}
