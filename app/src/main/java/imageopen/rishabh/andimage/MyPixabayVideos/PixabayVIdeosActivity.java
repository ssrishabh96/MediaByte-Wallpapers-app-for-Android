package imageopen.rishabh.andimage.MyPixabayVideos;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import imageopen.rishabh.andimage.Category.CategoryTiles;
import imageopen.rishabh.andimage.MainActivity;
import imageopen.rishabh.andimage.MyPixabay.MyPixaBayRecyclerAdapter;
import imageopen.rishabh.andimage.MyPixabay.OnLoadMoreListener;
import imageopen.rishabh.andimage.MyPixabay.PixabayUtil;
import imageopen.rishabh.andimage.R;

public class PixabayVIdeosActivity extends AppCompatActivity implements VideosDownloadCompleteListener {

    private static final String TAG=PixabayVIdeosActivity.class.getSimpleName();

    private ProgressBar downloadProgress;
    private ArrayList<PixabayVideoModel> videos;
    private RecyclerView recyclerview;
    private PixabayVideosAdapter pixabayVideosAdapter;
    private TextView errorView;
    private  int start_page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixabay_videos);

        Log.i(TAG, "onCreate() method fired ");
        getSupportActionBar().setTitle("Videos");

        // Initialize the views
        initViews();
        // Check for Internet Information
        checkInternetAccesInfo();
        // Prepare url for requesting
        requestResponseFromURL(start_page);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed: fired ");
        startActivity(new Intent(this, CategoryTiles.class));
        finish();
    }

    private void requestResponseFromURL(int page) {
        Log.i(TAG, "requestResponseFromURL() method fired ");
        // String query=query_ET.getText().toString().trim();
        // https://pixabay.com/api/videos/?key=4608907-a773e1287a88b0498389c4523&q=yellow+flowers
        // query.replace(' ','+');
        final String URL=getResources().getString(R.string.pixabay_videos_base_URL)+ getResources().getString(R.string.my_pixabay_API) +"&per_page=8&page="+page+"&q=";
        //final String URL=getResources().getString(R.string.pixabay_base_URL)+ getResources().getString(R.string.my_pixabay_API)+"&q="+query +"&response_group=high_resolution"+"&page="+page+"&image_type=photo";
        Log.i(TAG, "getDatafromJSON() method, URL is "+URL);
        new StartAsyncTask(this).execute(URL);

    }

    private void checkInternetAccesInfo() {

        Log.i(TAG, "checkInternetAccesInfo() method fired ");
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo.isConnected()) {
            Log.i(TAG, "checkInternetAccesInfo() method fired: network is connected");
        }

        if (networkInfo.isAvailable()){
            Log.i(TAG, "checkInternetAccesInfo() method fired: network is avaiblable to connect");
        }

        if(networkInfo.isConnectedOrConnecting())
        {
            Log.i(TAG, "checkInternetAccesInfo() method fired: isConnecting");
        }

        if(networkInfo.isRoaming())
        {
            Log.i(TAG, "checkInternetAccesInfo() method fired: isRoaming true");
        }

    }

    private void initViews() {
        Log.i(TAG, "initViews() method fired ");


        errorView=(TextView) findViewById(R.id.videosError);
        downloadProgress=(ProgressBar) findViewById(R.id.videosProgressBar);
        recyclerview=(RecyclerView) findViewById(R.id.pixabayVideosRecyclerView);
        videos=new ArrayList<>();

        RecyclerView.LayoutManager manager= new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); //LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(manager);//2, StaggeredGridLayoutManager.VERTICAL));
        pixabayVideosAdapter= new PixabayVideosAdapter(recyclerview,this,videos);
        //recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerview.setAdapter(pixabayVideosAdapter);

        pixabayVideosAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                //myPixaBayRecyclerAdapter.notifyItemInserted(imageRepos.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start_page++;
                        requestResponseFromURL(start_page);
                        pixabayVideosAdapter.setLoaded();
                    }
                }, 300);

            }
        });
    }

    @Override
    public void videosdownloadComplete(ArrayList<PixabayVideoModel> videoRepositories) {
        Log.i(TAG, "videosdownloadComplete() method fired, data size is "+videoRepositories.size());

        int cur=videos.size();
        videos.addAll(videoRepositories);
        recyclerview.getAdapter().notifyItemChanged(cur+1,videos.size());
        downloadProgress.setVisibility(View.GONE);


        if(videoRepositories.size() > 0) {

            errorView.setVisibility(View.GONE);


        }
        else if(videoRepositories.size() == 0)
        {
            errorView.setVisibility(View.VISIBLE);
            Log.i(TAG, "downloadComplete() method fired, no data received.");

        }

    }




    public class StartAsyncTask extends AsyncTask<String,Void,String>{

        private final String TAG=StartAsyncTask.class.getSimpleName();
        private VideosDownloadCompleteListener videosDownloadCompleteListener;

        public StartAsyncTask(VideosDownloadCompleteListener videosDownloadCompleteListener) {
            this.videosDownloadCompleteListener=videosDownloadCompleteListener;
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
                videosDownloadCompleteListener.videosdownloadComplete(PixabayVideoUtils.retrieveRepositoriesFromResponse(result));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
