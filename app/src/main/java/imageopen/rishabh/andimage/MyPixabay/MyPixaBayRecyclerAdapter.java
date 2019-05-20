package imageopen.rishabh.andimage.MyPixabay;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import imageopen.rishabh.andimage.R;

/**
 * Created by Rishabh on 2/20/2017.
 */

public class MyPixaBayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MyPixabayModel> android_versions;
    private Context context;
    private static final String TAG = MyPixaBayRecyclerAdapter.class.getSimpleName();

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;

    public MyPixaBayRecyclerAdapter(RecyclerView recyclerView,Context context, ArrayList<MyPixabayModel> android_versions) {

        Log.i(TAG, "MyPixaBayRecyclerAdapter() method fired:");
        this.context = context;
        this.android_versions = android_versions;
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    } else{
            final StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    visibleThreshold=recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    try {
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPositions(null)[0];

                    }catch (NullPointerException ne){
                        Log.i(TAG, "onScrolled: fired, EXCEPTION FIRED ");
                        lastVisibleItem= RecyclerView.NO_POSITION;
                    }
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });

        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "onCreateViewHolder() method fired");
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pixabay_row, viewGroup, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
            return new LoadingViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof ViewHolder) {

            ((ViewHolder) viewHolder).views_android.setText(android_versions.get(i).getViews());
            ((ViewHolder) viewHolder).likes_android.setText(android_versions.get(i).getLikes());
            ((ViewHolder) viewHolder).comment_android.setText(android_versions.get(i).getComments());
            // Picasso.with(context).load(android_versions.get(i).getAndroid_url()).resize(100,100).centerCrop().into(viewHolder.img_android);
            Glide.with(context).load(android_versions.get(i).getPreview_URL()).into(((ViewHolder) viewHolder).img_android);

            ((ViewHolder) viewHolder).img_android.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Image opened, onClick() method fired ");
                    Intent in=new Intent(context,ImageDIsplay.class);


                    in.putExtra("preview_url", android_versions.get(i).getPreview_URL());
                    in.putExtra("largeurl", android_versions.get(i).getLargeurl());
                    in.putExtra("fullhdurl", android_versions.get(i).getFullhd_url());
                    in.putExtra("likes", android_versions.get(i).getLikes());
                    in.putExtra("id", android_versions.get(i).getID());
                    in.putExtra("comments", android_versions.get(i).getComments());
                    in.putExtra("views", android_versions.get(i).getViews());
                    Log.i(TAG, "onClick: fired, views are: "+android_versions.get(i).getViews());
                    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,((ViewHolder) viewHolder).img_android,"loadPicture");

                    context.startActivity(in,optionsCompat.toBundle());
                }
            });


        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return android_versions.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    //    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//        Log.i(TAG, "onBindViewHolder() method fired");
//        final int index = i;
//        viewHolder.views_android.setText(android_versions.get(i).getViews());
//        viewHolder.likes_android.setText(android_versions.get(i).getLikes());
//        viewHolder.comment_android.setText(android_versions.get(i).getComments());
//        // Picasso.with(context).load(android_versions.get(i).getAndroid_url()).resize(100,100).centerCrop().into(viewHolder.img_android);
//        Picasso.with(context).load(android_versions.get(i).getPreview_URL()).into(viewHolder.img_android);
//
//        viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(TAG, "Image opened, onClick() method fired ");
//                Intent intent=new Intent(context,ImageDIsplay.class);
//                intent.putExtra("preview_url", android_versions.get(i).getPreview_URL());
//                intent.putExtra("largeurl", android_versions.get(i).getLargeurl());
//                intent.putExtra("fullhdurl", android_versions.get(i).getFullhd_url());
//                intent.putExtra("likes", android_versions.get(i).getLikes());
//                intent.putExtra("id", android_versions.get(i).getID());
//                intent.putExtra("comments", android_versions.get(i).getComments());
//                intent.putExtra("views", android_versions.get(i).getViews());
//
//                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,viewHolder.img_android,"loadPicture");
//
//                context.startActivity(intent,optionsCompat.toBundle());
//            }
//        });
//        viewHolder.download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // new DownloadPictureFromURL().execute(android_versions.get(index).getURL());
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(android_versions.get(index).getPreview_URL()));
//                request.setDescription("Downloading file in the background.");
//                request.setTitle("Download Started");
//// in order for this if to run, you must use the android 3.2 to compile your app
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    request.allowScanningByMediaScanner();
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                }
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");
//
//// get download service and enqueue file
//                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                manager.enqueue(request);
//            }
//        });
//    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount() method fired, size is: " + android_versions.size());
        return android_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView views_android, comment_android, likes_android;
        ImageView img_android, download;


        public ViewHolder(View view) {
            super(view);
            Log.i(TAG, "ViewHolder() method fired");
            views_android = (TextView) view.findViewById(R.id.pixabay_Views);
            comment_android = (TextView) view.findViewById(R.id.pixabay_Comments);
            likes_android = (TextView) view.findViewById(R.id.pixabay_Likes);
            img_android = (ImageView) view.findViewById(R.id.pixabay_imageView);
            download = (ImageView) view.findViewById(R.id.pixabay_Download);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

}