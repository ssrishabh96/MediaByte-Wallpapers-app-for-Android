package imageopen.rishabh.andimage.Category;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import imageopen.rishabh.andimage.GravitySnapHelper;
import imageopen.rishabh.andimage.R;

public class CategoryTiles extends AppCompatActivity implements CategoryLoadedListener{

    public static final String TAG=CategoryTiles.class.getSimpleName();
    private String[] categories;
    private String current_CAT;
    private RecyclerView recyclerView;
    private CategoryImageAdapter categoryAdapter;
    private ArrayList<CategoryModel> categoryArray;
    private TextView errorView;
    private SnapHelper snapHelper;
    private LinearLayout errorlayout;
    Button btnRetry;
    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_tiles);
        Log.i(TAG, "onCreate: fired ");

        getSupportActionBar().setTitle("Categories");

        init();

        requestDatafromJSON();



    }


    private void init() {
        Log.i(TAG, "init: fired ");
        current_CAT="";
        categories=getResources().getStringArray(R.array.category_titles);
        categoryArray=new ArrayList<>();
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);
        errorView=(TextView) findViewById(R.id.categoryError);
        errorlayout=(LinearLayout) findViewById(R.id.error_layout);
        Log.i(TAG, "init: fired, eroorlayout is  "+errorlayout.isShown());
        recyclerView=(RecyclerView) findViewById(R.id.categoryRecycler);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        categoryAdapter= new CategoryImageAdapter(recyclerView,this,categoryArray);
        recyclerView.setAdapter(categoryAdapter);



        if(snapHelper==null) {
            snapHelper = new GravitySnapHelper(Gravity.TOP);
            snapHelper.attachToRecyclerView(recyclerView);
        }

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDatafromJSON();
            }

        });

        // Adds the scroll listener to RecyclerView
        //recyclerview_Pixabay.addOnScrollListener(scrollListener);

    }

    public void requestDatafromJSON() {
        Log.i(TAG, "getDatafromJSON() method fired, ");
        int i=R.drawable.noimage;
        categoryArray.add(new CategoryModel("Videos","file:///android_asset/noimage1.png"));

        for(i=0; i<categories.length;i++) {
            Log.i(TAG, "requestDatafromJSON: fired, current Index: "+i);
            String URL = getResources().getString(R.string.pixabay_base_URL) + getResources().getString(R.string.my_pixabay_API) + "&per_page=3&response_group=high_resolution" +"&q=" + categories[i] + "&image_type=photo";
            Log.i(TAG, "getDatafromJSON() method fired, URL is " + URL);
            current_CAT=categories[i];
            new getImageData(this).execute(URL,current_CAT);


                  }

    }


    @Override
    public void downloadComplete(ArrayList<CategoryModel> categories) {
        Log.i(TAG, "downloadComplete: fired");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // myPixaBayRecyclerAdapter= new MyPixaBayRecyclerAdapter(this,imageRepos);
        // recyclerview_Pixabay.setAdapter(myPixaBayRecyclerAdapter);

        int pos=categoryArray.size();

        categoryArray.addAll(categories);
        recyclerView.getAdapter().notifyItemChanged(pos+1,categoryArray.size());



        if(categories.size() > 0) {
            errorView.setVisibility(View.GONE);
            errorlayout.setVisibility(View.GONE);
        }
        else if(categories.size() == 0)
        {
            Log.i(TAG, "downloadComplete() method fired, no data received.");
            //errorView.setVisibility(View.VISIBLE);

        }
    }

    public class getImageData extends AsyncTask<String,Void,String> {


        private CategoryLoadedListener categoryLoadedListener;
        private String mycategory;

        public getImageData(CategoryLoadedListener categoryLoadedListener) {
            this.categoryLoadedListener = categoryLoadedListener;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "doInBackground() method fired, category is "+strings[1]);
            try {
                mycategory=strings[1];
                if(!isNetworkConnected())
                    showInternetError();
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
                if(is==null)
                    showInternetError();
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
            Log.i(TAG, "onPostExecute() method fired, result is "+result);
            try {
                if(result!=null)
                categoryLoadedListener.downloadComplete(CategoryUtilsClass.retrieveRepositoriesFromResponse(result,mycategory));
                else {
                    errorView.setVisibility(View.VISIBLE);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void showInternetError() {
        Log.i(TAG, "showInternetError: fired ");

        if (errorlayout.getVisibility() == View.GONE || errorlayout.getVisibility() == View.INVISIBLE) {
            errorlayout.setVisibility(View.VISIBLE);
            // display appropriate error message
            // Handling 3 generic fail cases.

            if (!isNetworkConnected())
            txtError.setText("No Internet Connection");

        }

        }

    private boolean isNetworkConnected() {
        Log.i(TAG, "isNetworkConnected: fired ");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    }