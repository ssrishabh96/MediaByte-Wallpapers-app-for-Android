package imageopen.rishabh.andimage.MyPixabayVideos;

/**
 * Created by Rishabh on 2/22/2017.
 */

public class PixabayVideoModel {

    String views,likes,comments,id;
    String duration,tags,type,pageURL;
    String url_small, url_medium, url_large;


    public PixabayVideoModel(String id, String tags, String type, String url_small, String url_medium, String url_large, String likes, String views, String comments, String duration, String pageURL) {
        this.tags = tags;
        this.type = type;
        this.url_small = url_small;
        this.url_medium = url_medium;
        this.url_large = url_large;
        this.likes = likes;
        this.views = views;
        this.comments = comments;
        this.duration = duration;
        this.pageURL=pageURL;
    }

    public String getPageURL() {
        return pageURL;
    }

    public String getViews() {
        return views;
    }

    public String getLikes() {
        return likes;
    }

    public String getComments() {
        return comments;
    }

    public String getId() {
        return id;
    }

    public String getDuration() {
        return duration;
    }

    public String getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }

    public String getUrl_small() {
        return url_small;
    }

    public String getUrl_medium() {
        return url_medium;
    }

    public String getUrl_large() {
        return url_large;
    }
}
