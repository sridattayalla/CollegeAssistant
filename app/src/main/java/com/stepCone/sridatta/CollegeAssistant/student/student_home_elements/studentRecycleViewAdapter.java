package com.stepCone.sridatta.CollegeAssistant.student.student_home_elements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.imageContainer;
import com.stepCone.sridatta.CollegeAssistant.linkContainer;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP-PC on 10-01-2018.
 */

public class studentRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Bitmap bitmap;
    String bottomText;
    String url;
    String topText;
    private List<Object> catalouge;
    //constructor
    public studentRecycleViewAdapter(Context context, List<Object> catalouge) {
        this.catalouge=catalouge;
        this.context=context;
        this.bitmap=bitmap;
        this.bottomText=bottomText;
        this.topText="Get Ready For The RACE";
        this.url="http://www.stepcone.gmrit.org/registration.html";
    }

    @Override
    public int getItemViewType(int position) {
        if(catalouge.get(position) instanceof imageContainer){
            return R.layout.feed_image_layout;
        }
        if(catalouge.get(position) instanceof linkContainer) {
            return R.layout.link_container;
        }
        else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder;
        View view;
        switch (viewType){

            case R.layout.feed_image_layout:view= LayoutInflater.from(context).inflate(R.layout.feed_image_layout,parent,false);
            holder=new imageViewHolder(view);
            //on view click
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
            break;

            case R.layout.link_container:view=LayoutInflater.from(context).inflate(R.layout.link_container,parent,false);
            holder=new linkViewHolder(view);
                break;


            //default
            default:view= LayoutInflater.from(context).inflate(R.layout.feed_image_layout,parent,false);
            holder=new imageViewHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof imageViewHolder){
            final imageContainer imageContiner=(imageContainer) catalouge.get(position);
            ((imageViewHolder)holder).s.setText(imageContiner.getDescription());
            ((imageViewHolder)holder).imageView.setImageDrawable(imageContiner.getImage());
            ((imageViewHolder)holder).t.setText(imageContiner.getTopText());
        }
        if(holder instanceof linkViewHolder){
            final linkContainer linkContainer=(linkContainer) catalouge.get(position);
            //loader
            //image
                ((linkViewHolder)holder).facultyImage.setImageBitmap(linkContainer.getBitmap());
            //name
            ((linkViewHolder)holder).faculty_name.setText(linkContainer.getFacultyName());
            ((linkViewHolder)holder).description.setText(linkContainer.getContent());
            ((linkViewHolder)holder).urlSend.setText(linkContainer.getUrl());
            ((linkViewHolder)holder).linkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    url=linkContainer.getUrl();
                    Intent i=new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
            ((linkViewHolder)holder).linkDate.setText(linkContainer.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return catalouge.size();
    }

    //imageView holder
    class imageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView s;
        public TextView t;
        public imageViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView)itemView.findViewById(R.id.feed_image);
            s=(TextView)itemView.findViewById(R.id.botton_text);
            t=(TextView)itemView.findViewById(R.id.feed_image_top_text);
        }
    }

    //link view holder
    class linkViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView facultyImage;
        public TextView faculty_name;
        public TextView urlSend;
        public TextView description;
        public ConstraintLayout linkLayout;
        public TextView linkDate;
        public linkViewHolder(View view) {
            super(view);
            facultyImage=view.findViewById(R.id.link_faculty_image);
            faculty_name=view.findViewById(R.id.link_faculty_name);
            urlSend=view.findViewById(R.id.link);
            description=view.findViewById(R.id.description);
            linkLayout=view.findViewById(R.id.link_container_layout);
            linkDate=view.findViewById(R.id.link_date);
        }
    }

    class getFacultyImage extends AsyncTask<String,String,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_faculty_image.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode("123","UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                //converting bitmap to string
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp= Base64.encodeToString(b, Base64.DEFAULT);
                //saving imageString
                SharedPreferences sf= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("facultyImage",temp);
                editor.commit();
                //converting string to image
                byte [] encodeByte=Base64.decode(temp,Base64.DEFAULT);
                bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){}
            return null;
        }

    }



}
