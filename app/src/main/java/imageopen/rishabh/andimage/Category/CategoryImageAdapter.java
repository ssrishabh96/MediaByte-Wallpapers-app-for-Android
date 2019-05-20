package imageopen.rishabh.andimage.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
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

import imageopen.rishabh.andimage.MainActivity;
import imageopen.rishabh.andimage.MyPixabay.MyPixaBayRecyclerAdapter;
import imageopen.rishabh.andimage.R;

/**
 * Created by RISHABH on 2/26/2017.
 */

public class CategoryImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<CategoryModel> android_versions;
    private Context context;
    private static final String TAG = MyPixaBayRecyclerAdapter.class.getSimpleName();


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public CategoryImageAdapter(RecyclerView recyclerView,Context context, ArrayList<CategoryModel> android_versions) {
        Log.i(TAG, "CategoryImageAdapter: Constructor fired");
        this.context = context;
        this.android_versions = android_versions;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i(TAG, "onCreateViewHolder() method fired");
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_layout_row, viewGroup, false);
            return new CategoryImageAdapter.ViewHolder(view);
        }
//        } else if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false);
//            return new CategoryImageAdapter.LoadingViewHolder(view);
//        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof ViewHolder) {

            Log.i(TAG, "onBindViewHolder: fired, background url is  "+android_versions.get(i).getBackGroundURL());
            Glide.with(context).load(Uri.parse(android_versions.get(i).getBackGroundURL())).into(((ViewHolder) viewHolder).imageView);

            ((ViewHolder) viewHolder).category.setText(android_versions.get(i).getCategoryName());


            ((ViewHolder) viewHolder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Image opened, onClick() method fired ");
                    Intent intent=new Intent(context,MainActivity.class);
                    intent.putExtra("category", android_versions.get(i).getCategoryName());

                   // //ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,((ViewHolder) viewHolder).imageView,"loadPicture");
//
                   // ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,((ViewHolder) viewHolder).imageView,"loadPicture");

                    context.startActivity(intent);//,optionsCompat.toBundle());
                }
            });


        } else if (viewHolder instanceof CategoryImageAdapter.LoadingViewHolder) {
            CategoryImageAdapter.LoadingViewHolder loadingViewHolder = (CategoryImageAdapter.LoadingViewHolder) viewHolder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return android_versions.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount() method fired, size is: " + android_versions.size());
        return android_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;
        ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            Log.i(TAG, "ViewHolder() method fired");
            category = (TextView) view.findViewById(R.id.categoryTitle);
            imageView = (ImageView) view.findViewById(R.id.categoryImage);


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
