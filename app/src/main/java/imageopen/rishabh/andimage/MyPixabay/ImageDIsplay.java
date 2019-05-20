package imageopen.rishabh.andimage.MyPixabay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import imageopen.rishabh.andimage.CustomDialog;
import imageopen.rishabh.andimage.FileName;
import imageopen.rishabh.andimage.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDIsplay extends AppCompatActivity {

    private Toast toast;
    private  PhotoViewAttacher mAttacher;
    private PhotoView imageView;
    private static final String TAG = ImageDIsplay.class.getSimpleName();
    private String likes, views, comments, id;
    private String largeurl, fullhdurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_display);
        Log.i(TAG, "onCreate() method fired ");
        likes = views = comments = id = largeurl=fullhdurl="";

        // initilialize the views
        init();
        // extarct URL from the intent
        retreiveIntentData();
        // load Image into the View
        loadImagefromURL();
    }

    private void loadImagefromURL() {
        Log.i(TAG, "loadImagefromURL() method fired ");
        if (largeurl != null)
            Glide.with(this).load(largeurl).into(imageView);
        else {
            Log.i(TAG, "loadImagefromURL() method fired, could not load image, url is EMPTY");
        }

    }

    private void retreiveIntentData() {
        Log.i(TAG, "retreiveIntentData() method fired ");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            largeurl = bundle.getString("largeurl");
            fullhdurl = bundle.getString("fullhdurl");
            likes = bundle.getString("likes", "NA");
            Log.i(TAG, "retreiveIntentData: fired, likes are "+likes);
            views = bundle.getString("views", "NA");
            Log.i(TAG, "retreiveIntentData: fired, views are: "+views);

            comments = bundle.getString("comments", "NA");
            Log.i(TAG, "retreiveIntentData: fired, comments are "+comments);
            id = bundle.getString("id", "NA");
            Log.i(TAG, "retreiveIntentData: fired, ID is  "+id);

            Log.i(TAG, "retreiveDataFromIntent(), urls are:  "+largeurl+fullhdurl);

        }
        else {
            Log.i(TAG, "retreiveDataFromIntent() method fired. Null bundle, no data received");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        if(toast!=null )toast.cancel();
    }

    private void init() {
        Log.i(TAG, "init() method fired ");
        imageView = (PhotoView) findViewById(R.id.imagePreview);
        //top = (LinearLayout) findViewById(R.id.topBarLinear);

        largeurl = fullhdurl = "";
        mAttacher = new PhotoViewAttacher(imageView);

    }

    public void download(View view) {
        Log.i(TAG, "download() method fired, url is " + largeurl);
        String url = "";
        url = getCorrectURL();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Downloading the file");
			// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        FileName fn = new FileName();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fn.getName() + ".jpg");
			// get download service and enqueue file
    		DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        toast=new Toast(this);
        toast.makeText(this,"Download Started, ;)",Toast.LENGTH_LONG).show();
    }

    private String getCorrectURL() {
        Log.i(TAG, "getCorrectURL: fired");
        String url = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sharedPreferences.getString(getResources().getString(R.string.image_download_key), "")) {

            case "FullHD":
                url = fullhdurl;
                Log.i(TAG, "getCorrectURL: Downloading full HD URL, url: " + url);
                break;

            default:
                url = largeurl;
                Log.i(TAG, "getCorrectURL: Downloading Default HD resolution, url: " + url);
                break;
        }
        return url;
    }

    public void share(View view) {
        Log.i(TAG, "share() method fired, link to share is: "+getCorrectURL());
        Intent intent;
        intent= new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check this Awesome Picture");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Check out this awesome image here: "+ getCorrectURL());

        startActivity(Intent.createChooser(intent, "Share link!"));


    }

    public void information(View view) {
        Log.i(TAG, "information() method fired ");
       //AlertDialog.Builder builder = new AlertDialog.Builder(this);
       //builder.setTitle("Info");
       //builder.setMessage("yolo");
       //builder.setNegativeButton("OK", null);
       //AlertDialog dialog = builder.create();
       //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
       //dialog.show();

CustomDialog customizeDialog = new CustomDialog(this);
        customizeDialog.setTitle("Picture Info");
        customizeDialog.setMessage(
                "\nLikes:" +likes+ "\nViews:" +views +
                "\nComments:" +comments+
                        "\nID:" +id
        );
        customizeDialog.show();
    }

    public void goback(View view) {
        Log.i(TAG, "goback() method fired ");
        onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() method fired ");
    }
}