package imageopen.rishabh.andimage.Category;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import imageopen.rishabh.andimage.MyPixabay.MyPixabayModel;
import imageopen.rishabh.andimage.MyPixabay.PixabayUtil;

/**
 * Created by RISHABH on 2/26/2017.
 */

public class CategoryUtilsClass {

    private static final String TAG = CategoryUtilsClass.class.getSimpleName();

    public static ArrayList<CategoryModel> retrieveRepositoriesFromResponse(String response, String category) throws
            JSONException {
        Log.i(TAG, "retrieveRepositoriesFromResponse: fired ");

        if (null == response) {
            return new ArrayList<>();
        }
        JSONObject jsonObject1 = new JSONObject(response);
        JSONArray jsonArray=jsonObject1.getJSONArray("hits");

        ArrayList<CategoryModel> categoryModel=new ArrayList<>();
        String imageurl="";

        for (int i = 0; i < 1; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (null != jsonObject) {


                if (jsonObject.has("largeImageURL")) {
                    imageurl = jsonObject.getString("largeImageURL");
                    Log.i(TAG, "retrieveRepositoriesFromResponse: fired, url is:  "+imageurl);
                }

                categoryModel.add(new CategoryModel(category,imageurl));

            }
        } // for loop ends here
        Log.i(TAG, "retrieveRepositoriesFromResponse() method fired, image data extracted "+categoryModel.size());

        return categoryModel;
    }


}