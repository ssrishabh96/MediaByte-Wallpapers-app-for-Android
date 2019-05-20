package imageopen.rishabh.andimage.MyPixabayVideos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import imageopen.rishabh.andimage.MyPixabay.MyPixabayModel;

/**
 * Created by Rishabh on 2/22/2017.
 */

public class PixabayVideoUtils {

    private static final String TAG= PixabayVideoUtils.class.getSimpleName();

    public static ArrayList<PixabayVideoModel> retrieveRepositoriesFromResponse(String response) throws
            JSONException {

        Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, response from Server is: "+response);

        if (null == response) {
            return new ArrayList<>();
        }

        JSONObject jsonObject= new JSONObject(response);
        JSONArray jsonArrayHITS=jsonObject.getJSONArray("hits");

        ArrayList<PixabayVideoModel> repositories = new ArrayList<>();
        String views,likes,comments,id;
        String duration,tags,type;
        String url_small, url_medium, url_large,pageURL;
        views=likes=comments=id=duration=tags=type=url_small= url_medium=url_large=pageURL="";


        for(int i=0; i<jsonArrayHITS.length();i++) {
            JSONObject indexed=jsonArrayHITS.getJSONObject(i);
            Log.i(TAG, "retrieveRepositoriesFromResponse: Logging for Index "+i);
            if(indexed.has("id"))
            {
                id=indexed.getString("id");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, id is: "+id);
            }

            if(indexed.has("tags")){
                tags=indexed.getString("tags");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, tags: "+tags);

            }

            if(indexed.has("types")){
                type=indexed.getString("types");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, types: "+type);
            }

            if(indexed.has("likes")){
                likes=indexed.getString("likes");
            }

            if(indexed.has("comments")){
                comments=indexed.getString("comments");
            }

            if(indexed.has("views")){
                views=indexed.getString("views");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, views: "+views);
            }

            if(indexed.has("duration")){
                duration=indexed.getString("duration");
            }

            if(indexed.has("pageURL")){
                pageURL=indexed.getString("pageURL");
            }




            JSONObject urls=indexed.getJSONObject("videos");

            JSONObject temp;
            temp=urls.getJSONObject("small");

            if(temp.has("url")) {
                url_small =temp.getString("url");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, small url: "+url_small);

            }

            temp=urls.getJSONObject("medium");

            if(temp.has("url")) {
                url_medium =temp.getString("url");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, medium url: "+url_medium);

            }

            temp=urls.getJSONObject("large");

            if(temp.has("url")) {
                url_large =temp.getString("url");
                Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, large url: "+url_large);

            }

            repositories.add(new PixabayVideoModel(id,tags,type, url_small,url_medium,url_large, likes, views, comments, duration,pageURL));
            Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, data added. Current size is: "+repositories.size());

        }// for-loop

        return repositories;
    } // retreiveRepositoriesfromResponse ends here


} // class body ends here
