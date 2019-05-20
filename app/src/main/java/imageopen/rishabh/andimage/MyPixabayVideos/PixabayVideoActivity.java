package imageopen.rishabh.andimage.MyPixabayVideos;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

import imageopen.rishabh.andimage.CustomDialog;
import imageopen.rishabh.andimage.FileName;
import imageopen.rishabh.andimage.R;

public class PixabayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaController mediaControls;
    private VideoView videoplayer;
    private Toast toast;
    private Intent intent;
    private Boolean isVisible;
    private ImageView orientation, info, download;
    private String tags,type,likes,views,comments, duration, small_url, medium_url,large_url;
    CardView cardView;
    private String selected_url="", current_quality;
    private ToggleButton toggleButton;

    private static final String TAG=PixabayVideoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pixabay_video);

        Log.i(TAG, "onCreate() method fired ");

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            selected_url=savedInstanceState.getString("quality");
        } else {
            // Probably initialize members with default values for a new instance
            selected_url="";
        }

        // initialize the views
        init();
        // Get the video data from the Intent
        retreiveDataFromIntent();
        // LOAD Video in the player
        loadVideo(selected_url);
    }

    private void loadVideo(String url) {
        selected_url=url;
        Log.i(TAG, "loadVideo() method fired, url is: "+selected_url);
        try {
            //set the uri of the video to be played
            videoplayer.setVideoURI(Uri.parse(url));
        } catch (Exception e) {
            Log.e(TAG,"Error could not set media controls. "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void downloadVideo() throws NullPointerException  {
        Log.i(TAG, "downloadVideo() method fired ");
        selected_url = getCorrectURL();

        if(selected_url.isEmpty()==false && selected_url!=null) {
            Log.i(TAG, "downloadVideo() method fired, url is: "+selected_url);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(selected_url));
            request.setDescription("Downloading file in the background.");
            request.setTitle("Download Started");
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            FileName fn = new FileName();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fn.getName()+".mp4");

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            toast=new Toast(this);
            toast.makeText(this,"Download Started, ;)",Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.i(TAG, "downloadVideo() method fired, invalid URL. Could not Download the video. Url is "+selected_url);
        }

    }

    private String getCorrectURL() {

        Log.i(TAG, "getCorrectURL: fired");
        String url = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sharedPreferences.getString(getResources().getString(R.string.video_download_key), "")) {

            case "FullHD":
                url = large_url;
                Log.i(TAG, "getCorrectURL: Downloading full HD URL, url: " + url);
                break;

            default:
                url = medium_url;
                Log.i(TAG, "getCorrectURL: Downloading Default Medium resolution, url: " + url);
                break;
        }
        return url;
    }

    private void retreiveDataFromIntent() {
        Log.i(TAG, "retreiveDataFromIntent() method fired ");
        intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            small_url = bundle.getString("small_url","NA");
            medium_url = bundle.getString("medium_url","NA");
            large_url = bundle.getString("large_url","NA");
            Log.i(TAG, "retreiveDataFromIntent(), urls are:  "+small_url+medium_url+large_url);

            type = bundle.getString("type","NA");
            tags=bundle.getString("tags","NA");
            duration=bundle.getString("duration","NA");

            likes=(bundle.getString("likes","NA"));
            views=(bundle.getString("views","NA"));
            comments=(bundle.getString("comments","NA"));

            selected_url=medium_url;

        }
        else {
            Log.i(TAG, "retreiveDataFromIntent() method fired. Null bundle, no data received");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void init() {
        Log.i(TAG, "init() method fired ");
        small_url=medium_url=large_url=type=tags=views=likes=comments=type="";
        videoplayer=(VideoView) findViewById(R.id.videoPlay);
        orientation=(ImageView) findViewById(R.id.videoOrientation);
        info=(ImageView) findViewById(R.id.videoInfo);
        download=(ImageView) findViewById(R.id.videoDownload);
        cardView=(CardView) findViewById(R.id.cardBuffer);
        current_quality="sd";


        toggleButton=(ToggleButton) findViewById(R.id.qualityToggle);
        orientation.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        download.setOnClickListener(this);
            info.setOnClickListener(this);
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        videoplayer.setMediaController(mediaControls);
        //mediaControls.setAnchorView(videoplayer);

        videoplayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(TAG, "onError() method fired,  ");
                videoplayer.stopPlayback();
                return false;
            }
        });

        videoplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i(TAG, "onPrepared() method fired ");
                videoplayer.seekTo(100);
                cardView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed() method fired ");
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick() method fired ");
        switch (view.getId()) {
            case R.id.videoPlay:
                Log.i(TAG, "onClick() method fired, inside the videoplay CLick case");
//            if (isVisible) {
//                orientation.setVisibility(View.INVISIBLE);
//                info.setVisibility(View.INVISIBLE);
//                download.setVisibility(View.INVISIBLE);
//
//            } else {
//                orientation.setVisibility(View.VISIBLE);
//                info.setVisibility(View.VISIBLE);
//                download.setVisibility(View.VISIBLE);
//            }
//
//            isVisible = !isVisible;
                break;

                case R.id.videoInfo:
                Log.i(TAG, "onClick() method fired, inside the video Information Clause");

                CustomDialog customizeDialog = new CustomDialog(this);
                customizeDialog.setTitle("Video Info");
                    long i=Long.parseLong(duration);
                    duration=String.format("%d:%d",
                            TimeUnit.SECONDS.toMinutes(i),
                            i % 60);
                customizeDialog.setMessage("Duration: "+duration+
                "\nLikes:" +likes+ "\nViews:" +views +
                "\nComments:" +comments+
                                "\nType:" +type+
                                "\nTags:" +tags
                );
                customizeDialog.show();

                break;

            case R.id.videoOrientation:
                Log.i(TAG, "onClick() method fired, inside the orientation CLick phase ");

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                break;

            case R.id.qualityToggle:
                Log.i(TAG, "onClick() method fired, inside the quality change block");

                videoplayer.stopPlayback();
                cardView.setVisibility(View.VISIBLE);
                if(current_quality.equals("sd"))
                {
                    current_quality="hd";
                    selected_url=large_url;
                }
                else if(current_quality.equals("hd"))
                {
                    current_quality="sd";
                    selected_url=medium_url;
                }
                loadVideo(selected_url);
                break;

            case R.id.videoDownload:
                Log.i(TAG, "onClick() method fired download fired");
                downloadVideo();

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() method fired ");

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onSaveInstanceState() method fired ");

        outState.putInt("position", videoplayer.getCurrentPosition());
        outState.putString("quality", selected_url);
    }

}
