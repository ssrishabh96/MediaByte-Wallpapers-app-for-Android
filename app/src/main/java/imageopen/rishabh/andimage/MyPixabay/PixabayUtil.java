package imageopen.rishabh.andimage.MyPixabay;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

/**
 * Created by Rishabh on 2/20/2017.
 */

public class PixabayUtil {

    private static final String TAG=PixabayUtil.class.getSimpleName();

    public static ArrayList<MyPixabayModel> retrieveRepositoriesFromResponse(String response) throws
            JSONException {

        Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, response received is: "+response);
        if (null == response) {
            return new ArrayList<>();
        }
        JSONObject jsonObject1 = new JSONObject(response);
        JSONArray jsonArray=jsonObject1.getJSONArray("hits");

        ArrayList<MyPixabayModel> repositories = new ArrayList<>();

        String views,comments,likes,user,largeImageurl, previewURL,id;
        String fullhd_url, weburl;
        views=comments=likes=largeImageurl=previewURL=user=id=fullhd_url=weburl="";
        MyPixabayModel repository;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (null != jsonObject) {

                if (jsonObject.has("id")) {
                    id = jsonObject.getString("id");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, id is "+id);
                }

                if (jsonObject.has("likes")) {
                    likes = jsonObject.getString("likes");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, likes are "+likes);
                }

                if (jsonObject.has("comments")) {
                    comments = jsonObject.getString("comments");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, comments are: "+comments);
                }

                if (jsonObject.has("views")) {
                    views = jsonObject.getString("views");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, views are  "+views);
                }

                if (jsonObject.has("user")) {
                    user = jsonObject.getString("user");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, user is: "+user);
                }

                if (jsonObject.has("largeImageURL")) {
                    largeImageurl = jsonObject.getString("largeImageURL");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, largeimageurl is "+largeImageurl);
                }


                  if (jsonObject.has("previewURL")) {
                   previewURL  = jsonObject.getString("previewURL");
                      Log.i(TAG, "retrieveRepositoriesFromResponse: fired, previewurl is "+previewURL);

                  }

                if(jsonObject.has("fullHDURL")){
                    fullhd_url= jsonObject.getString("fullHDURL");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, previewURL is "+previewURL);

                }

                repository=new MyPixabayModel(id,views,user,comments,likes,previewURL,fullhd_url,largeImageurl);

                repositories.add(repository);
            }
        } // for loop ends here
        Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, image data extracted "+repositories.size());
        return repositories;
    }


}
