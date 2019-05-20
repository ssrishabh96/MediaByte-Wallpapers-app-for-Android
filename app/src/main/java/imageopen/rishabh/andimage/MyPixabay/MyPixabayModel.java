package imageopen.rishabh.andimage.MyPixabay;

/**
 * Created by Rishabh on 2/20/2017.
 */

public class MyPixabayModel {

    private String id, comments,likes,views,user, preview_URL;
    private String fullhd_url, largeurl;


    public MyPixabayModel(String ID, String views, String user,  String comments, String likes, String preview_URL, String fullhd_url, String largeurl) {
        this.id = ID;
        this.comments = comments;
        this.likes= likes;
        this.views= views;
        this.user = user;
        this.preview_URL = preview_URL;
        this.fullhd_url = fullhd_url;
        this.largeurl = largeurl;
    }

    public String getFullhd_url() {
        return fullhd_url;
    }



    public String getLargeurl() {
        return largeurl;
    }

    public String getPreview_URL() {
        return preview_URL;
    }

    public MyPixabayModel() {
    }

    public String getUser() {
        return user;
    }

    public String getID() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public String getLikes() {
        return likes;
    }

    public String getViews() {
        return views;
    }

}
