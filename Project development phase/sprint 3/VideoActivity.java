package com.teapink.damselindistress.activities;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.teapink.damselindistress.R;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    int i=0;
    String path;
    VideoView videoView;
    MediaController mediaController;

    File[] files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView =(VideoView)findViewById(R.id.videoView);

        //Creating MediaController
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);


        path=Environment.getExternalStorageDirectory().getPath()+"/Videos";
        //specify the location of media file

        File directory = new File(path);
        files = directory.listFiles();

        Uri uri=Uri.parse(path+"/"+files[i].getName());
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        Toast.makeText(getApplicationContext(),
                files[i].getName()+String.valueOf(files.length), Toast.LENGTH_LONG).show();

        ImageButton img_prev=(ImageButton) findViewById(R.id.imageButton);

        img_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                videoView.stopPlayback();

                if(i>0)
                {

                    i=i-1;
                    Uri uri=Uri.parse(path+"/"+files[i].getName());
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                    videoView.requestFocus();
                    videoView.start();



                }
                Toast.makeText(getApplicationContext(),
                        files[i].getName(), Toast.LENGTH_LONG).show();



            }
        });

        ImageButton img_next=(ImageButton) findViewById(R.id.imageButton2);

img_next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        videoView.stopPlayback();
        if(i<files.length)
        {
            i=i+1;
            Uri uri=Uri.parse(path+"/"+files[i].getName());
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();

        }
        Toast.makeText(getApplicationContext(),
                files[i].getName(), Toast.LENGTH_LONG).show();

    }
});



    }
}
