package imageopen.rishabh.andimage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {

    private Intent intent;

    private static final String TAG= AboutUsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hey "+sharedPreferences.getString(getResources().getString(R.string.username_key),getResources().getString(R.string.default_username)));
        setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("Hey "+sharedPreferences.getString(getResources().getString(R.string.username_key),getResources().getString(R.string.default_username)));

    }

    public void showAllApps(View view) {
        Log.i(TAG, "showAllApps: fired");
        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=rishabh agrawal"));
        startActivity(Intent.createChooser(intent,"Play Store"));
    }

    public void showPhysics(View view) {
        Log.i(TAG, "showPhysics: fired");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=inspireme.rishabh.physicssiunitdimensions"));
        startActivity(Intent.createChooser(intent,"Play Store"));

    }

    public void showSplits(View view) {
        Log.i(TAG, "showSplits: fired");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=tipcalc.rishabh.mytipplease"));
        startActivity(Intent.createChooser(intent,"Play Store"));

    }

    public void linkedin(View view) {
        Log.i(TAG, "linkedin: fired");
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/rishabh-agrawal-085541113/"));

        startActivity(Intent.createChooser(intent, "Connect through Linkedin"));
    }

    public void email(View view) {
        Log.i(TAG, "email: fired");

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "rishabh0148@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "From ClipBoard App: ");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public void twitter(View view) {
        Log.i(TAG, "twitter: fired");
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=ssrishabh96"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ssrishabh96"));
        }
        startActivity(Intent.createChooser(intent,"Twitter"));

    }

    public void googlelle(View view) {
        Log.i(TAG, "googlelle: fired");
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://googlelle.com"));
        startActivity(Intent.createChooser(intent,"Web Broswer"));
    }
}
