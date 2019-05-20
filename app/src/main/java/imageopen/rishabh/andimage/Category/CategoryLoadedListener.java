package imageopen.rishabh.andimage.Category;

import java.util.ArrayList;

import imageopen.rishabh.andimage.MyPixabay.MyPixabayModel;

/**
 * Created by RISHABH on 2/26/2017.
 */

public interface CategoryLoadedListener {

    void downloadComplete(ArrayList<CategoryModel> categories);
}

