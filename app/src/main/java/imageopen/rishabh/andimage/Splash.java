package imageopen.rishabh.andimage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import imageopen.rishabh.andimage.Category.CategoryTiles;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_TIME = 2000;
    public static final String TAG=Splash.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.i(TAG, "onCreate: fired ");
        new BackgroundTask().execute();


    }


    private class BackgroundTask extends AsyncTask {
        Intent intent;
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute: fired ");
            super.onPreExecute();
            intent = new Intent(Splash.this, LoginSIgnup.class);
        }
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(TAG, "doInBackground: fired ");
            /*  Use this method to load background
            * data that your app needs. */
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i(TAG, "onPostExecute: fired ");
//            Pass your loaded data here using Intent
//            intent.putExtra("data_key", "");
            startActivity(intent);
            finish();
        }
    }


}
