package imageopen.rishabh.andimage;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import imageopen.rishabh.andimage.MyPixabay.DownloadCompleteListener;
import imageopen.rishabh.andimage.MyPixabay.EndlessRecyclerViewScrollListener;
import imageopen.rishabh.andimage.MyPixabay.MyPixaBayRecyclerAdapter;
import imageopen.rishabh.andimage.MyPixabay.MyPixabayModel;
import imageopen.rishabh.andimage.MyPixabay.OnLoadMoreListener;
import imageopen.rishabh.andimage.MyPixabay.PixabayUtil;
import imageopen.rishabh.andimage.MyPixabay.Settings;
import imageopen.rishabh.andimage.MyPixabayVideos.PixabayVIdeosActivity;

public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener, DownloadCompleteListener, View.OnKeyListener, View.OnTouchListener {

    private ProgressBar downloadProgress;
    MyPixaBayRecyclerAdapter myPixaBayRecyclerAdapter;
    private  RecyclerView recyclerview_Pixabay;
    private static final String TAG=MainActivity.class.getSimpleName();
    private EditText query_ET;
    private int default_page=1;
    private static int current_page=1;
    private SnapHelper snapHelper;
    private ArrayList<MyPixabayModel> imageRepos;
    private TextView errorView;
    private String category="",order="",image_type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate() method fired: fired");
        super.onCreate(savedInstanceState);
           requestWindowFeature(Window.FEATURE_NO_TITLE);
           this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Bundle bundle=getIntent().getExtras();
        Log.i(TAG, "onCreate: fired cateory received is "+bundle.getString("category"));
        category=bundle.getString("category");

        Log.i(TAG, "onCreate: fired, category is: "+category);

        if(category.equals("Videos")) {
            startActivity(new Intent(this, PixabayVIdeosActivity.class));
            finish();
        }

        errorView=(TextView) findViewById(R.id.resultsError);
        query_ET=(EditText) findViewById(R.id.main_query_edittext);

        query_ET.setOnKeyListener(this);
        query_ET.setOnTouchListener(this);

        initALL();

        if(category.isEmpty()!=true) {
            loadWebImages();
            getSupportActionBar().setTitle(category);
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo.isConnected()) {
            Log.i(TAG, "onCreate() method fired: network is connected");

        }

        if (networkInfo.isAvailable()){
            Log.i(TAG, "onCreate() method fired: network is avaiblable to connect");
        }

        if(networkInfo.isConnectedOrConnecting())
        {
            Log.i(TAG, "onCreate() method fired: isConnecting");
        }

        if(networkInfo.isRoaming())
        {
            Log.i(TAG, "onCreate() method fired: isRoaming true");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu() method fired ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected() method fired ");
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.myaccount:
                intent=new Intent(this,UserAccount.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent=new Intent(this, Settings.class);

                startActivity(intent);
                break;
            case R.id.video :
                Log.i(TAG, "onOptionsItemSelected() method fired, Video option Chosen");
                intent=new Intent(this, PixabayVIdeosActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_us:
                Log.i(TAG, "onOptionsItemSelected() method fired, Contact Us chosen");
                intent=new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                break;

            case R.id.faqs:
                Log.i(TAG, "onOptionsItemSelected() method fired, FAQs chosen");
                //startService(new Intent(this,NetworkChangeReceiver.class));


                break;

            case R.id.searchitem:
                Log.i(TAG, "onOptionsItemSelected() method fired, Search Item clicked");

                if(query_ET.getVisibility()==View.GONE)
                    query_ET.setVisibility(View.VISIBLE);
                else  if(query_ET.getVisibility()==View.VISIBLE)
                    query_ET.setVisibility(View.GONE);


                break;

            case R.id.exit:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                else {
                    finish();
                }
                break;
        }

        return true;
    }


   private void initALL()    {
        Log.i(TAG, "initALL: fired");
        recyclerview_Pixabay=(RecyclerView) findViewById(R.id.myPixabayRecyclerView);
        imageRepos=new ArrayList<MyPixabayModel>();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            recyclerview_Pixabay.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        }
       else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerview_Pixabay.setLayoutManager(linearLayoutManager);
        }

        if(snapHelper==null) {
            snapHelper = new GravitySnapHelper(Gravity.TOP);
            snapHelper.attachToRecyclerView(recyclerview_Pixabay);
        }

        downloadProgress=(ProgressBar) findViewById(R.id.downloadProgressBar);

        // Adds the scroll listener to RecyclerView
        //recyclerview_Pixabay.addOnScrollListener(scrollListener);
        myPixaBayRecyclerAdapter= new MyPixaBayRecyclerAdapter(recyclerview_Pixabay,this,imageRepos);
        recyclerview_Pixabay.setAdapter(myPixaBayRecyclerAdapter);
        myPixaBayRecyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                //myPixaBayRecyclerAdapter.notifyItemInserted(imageRepos.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        current_page++;
                        getDatafromJSON(current_page);
                        myPixaBayRecyclerAdapter.setLoaded();
                    }
                }, 300);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired");
    }

    public void getDatafromJSON(int page) {
        Log.i(TAG, "getDatafromJSON() method fired, page number is "+page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        image_type=sharedPreferences.getString(getString(R.string.photo_type_key),"all");
        order=sharedPreferences.getString(getResources().getString(R.string.sort_results_key),"popular");
        String query=query_ET.getText().toString().trim();
//        https://pixabay.com/api/?key=4608907-a773e1287a88b0498389c4523&q=yellow+flowers&image_type=photo
        query.replace(' ','+');
        final String URL=getResources().getString(R.string.pixabay_base_URL)+ getResources().getString(R.string.my_pixabay_API) +"&order="+order+"&image_type="+image_type+"&category="+category.toLowerCase()+"&response_group=high_resolution"+"&per_page=8&page="+page+"&q="+query+"&image_type=photo";
        //final String URL=getResources().getString(R.string.pixabay_base_URL)+ getResources().getString(R.string.my_pixabay_API)+"&q="+query +"&response_group=high_resolution"+"&page="+page+"&image_type=photo";
        Log.i(TAG, "getDatafromJSON() method fired, URL is "+URL);
        downloadProgress.setVisibility(View.VISIBLE);

        new getJSONAsyncTask(this).execute(URL);
    }


    public void loadWebImages() {

        Log.i(TAG, "loadWebImages() method fired ");

        if(query_ET!=null && query_ET.getText().toString().length()>=0){
            Log.i(TAG, "loadWebImages() method fired, query is valid");
            getDatafromJSON(default_page);
        }

    }


    @Override
    public void downloadComplete(ArrayList<MyPixabayModel> imagerepositories) {
        Log.i(TAG, "downloadComplete() method fired ");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       // myPixaBayRecyclerAdapter= new MyPixaBayRecyclerAdapter(this,imageRepos);
       // recyclerview_Pixabay.setAdapter(myPixaBayRecyclerAdapter);
        int pos=imageRepos.size();

        imageRepos.addAll(imagerepositories);

        recyclerview_Pixabay.getAdapter().notifyItemChanged(pos+1,imageRepos.size());

        //myPixaBayRecyclerAdapter.notifyDataSetChanged();

        downloadProgress.setVisibility(View.GONE);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(imagerepositories.size() > 0) {
            errorView.setVisibility(View.GONE);
        }
        else if(imagerepositories.size() == 0)
        {
            Log.i(TAG, "downloadComplete() method fired, no data received.");
            errorView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Log.i(TAG, "onKey() method fired ");
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (i)
            {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    imageRepos.clear();
                    myPixaBayRecyclerAdapter.notifyDataSetChanged();
                    loadWebImages();

                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (query_ET.getRight() - query_ET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                query_ET.setText("");

                //myPixaBayRecyclerAdapter.notifyItemRangeRemoved(); // or notifyItemRangeRemoved
                // 3. Reset endless scroll listener when performing a new search
                return true;
            }
        }
        return false;
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.i(TAG, "onNetworkConnectionChanged() method connection changed, "+isConnected);

    }

    public class getJSONAsyncTask extends AsyncTask<String,Void,String>{


        DownloadCompleteListener mDownloadCompleteListener;

        public getJSONAsyncTask(DownloadCompleteListener downloadCompleteListener) {
            this.mDownloadCompleteListener = downloadCompleteListener;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "doInBackground() method fired ");
            try {
                return downloadData(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private String downloadData(String urlString) throws IOException {
            Log.i(TAG, "downloadData() method fired ");
            InputStream is = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                is = conn.getInputStream();
                return convertToString(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        private String convertToString(InputStream is) throws IOException {
            Log.i(TAG, "convertToString() method fired ");
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return new String(total);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "onPostExecute() method fired ");
            try {
                mDownloadCompleteListener.downloadComplete(PixabayUtil.retrieveRepositoriesFromResponse(result));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
