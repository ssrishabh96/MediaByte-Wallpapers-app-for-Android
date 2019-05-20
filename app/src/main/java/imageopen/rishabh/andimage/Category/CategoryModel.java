package imageopen.rishabh.andimage.Category;

/**
 * Created by RISHABH on 2/26/2017.
 */

public class CategoryModel {

    private String CategoryName, BackGroundURL;

    public CategoryModel(String categoryName, String backGroundURL) {
        CategoryName = categoryName;
        BackGroundURL = backGroundURL;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getBackGroundURL() {
        return BackGroundURL;
    }
}
