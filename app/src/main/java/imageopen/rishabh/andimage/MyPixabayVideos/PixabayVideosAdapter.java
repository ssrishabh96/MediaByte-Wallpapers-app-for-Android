package imageopen.rishabh.andimage.MyPixabayVideos;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import imageopen.rishabh.andimage.MyPixabay.MyPixaBayRecyclerAdapter;
import imageopen.rishabh.andimage.MyPixabay.OnLoadMoreListener;
import imageopen.rishabh.andimage.Preview;
import imageopen.rishabh.andimage.R;

/**
 * Created by Rishabh on 2/22/2017.
 */

public class PixabayVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<PixabayVideoModel> dataSet;
    private Context context;
    private static final String TAG = PixabayVideosAdapter.class.getSimpleName();

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;


    public PixabayVideosAdapter(RecyclerView recyclerView, Context context, ArrayList<PixabayVideoModel> dataSet) {
        Log.i(TAG, "PixabayVideosAdapter() method fired ");
        this.context = context;
        this.dataSet = dataSet;

        final StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleThreshold=recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                try {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPositions(null)[0];//; linearLayoutManager.findLastVisibleItemPosition();

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

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        Log.i(TAG, "setOnLoadMoreListener: fired ");

        this.onLoadMoreListener = mOnLoadMoreListener;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "onCreateViewHolder() method fired ");

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pixabay_videos_row, viewGroup, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        Log.i(TAG, "onBindViewHolder: fired ");
        Log.i(TAG, "onBindViewHolder() method fired");
        final int index = position;

        if (viewHolder instanceof ViewHolder) {
            long duration = Long.parseLong(dataSet.get(index).getDuration());
            String dur = String.format("%d:%d",
                    TimeUnit.SECONDS.toMinutes(duration),
                    duration % 60);

            ((ViewHolder) viewHolder).durationnShow.setText(dur);
            ((ViewHolder) viewHolder).videoView.setData(dataSet.get(index).getPageURL());

            ((ViewHolder) viewHolder).videoView.setListener(new Preview.PreviewListener() {
                @Override
                public void onDataReady(Preview preview) {
                    ((ViewHolder) viewHolder).videoView.setMessage(preview.getLink());
                }
            });

            final int indexed = position;
            ((ViewHolder) viewHolder).videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Image opened, onClick() method fired ");
                    Intent intent = new Intent(context, PixabayVideoActivity.class);
                    intent.putExtra("small_url", dataSet.get(indexed).getUrl_small());
                    intent.putExtra("medium_url", dataSet.get(indexed).getUrl_medium());
                    intent.putExtra("large_url", dataSet.get(indexed).getUrl_large());
                    intent.putExtra("likes", dataSet.get(indexed).getLikes());
                    intent.putExtra("comments", dataSet.get(indexed).getComments());
                    intent.putExtra("views", dataSet.get(indexed).getViews());
                    intent.putExtra("duration", dataSet.get(indexed).getDuration());
                    intent.putExtra("type", dataSet.get(indexed).getType());
                    intent.putExtra("tags", dataSet.get(indexed).getTags());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, ((ViewHolder) viewHolder).videoView, "video");

                    context.startActivity(intent, optionsCompat.toBundle());

                }
            });
        }
        else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }



    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType: fired ");
        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount() method fired, size is: " + dataSet.size());
        return dataSet.size();
    }

    public void setLoaded() {

        Log.i(TAG, "setLoaded: fired ");
        isLoading = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  durationnShow;
        Preview videoView=new Preview(context){


        };


        public ViewHolder(View view) {
            super(view);
            Log.i(TAG, "ViewHolder() method fired");

            videoView = (Preview) view.findViewById(R.id.pixabay_VideoView);
            durationnShow=(TextView) view.findViewById(R.id.durationShow);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            Log.i(TAG, "LoadingViewHolder: fired ");

            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


}
