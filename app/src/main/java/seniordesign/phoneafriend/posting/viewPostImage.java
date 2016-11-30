package seniordesign.phoneafriend.posting;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import seniordesign.phoneafriend.R;

public class viewPostImage extends AppCompatActivity {

    private ImageView postImage;
    private Bundle recvExtras;
    private String imageUrl;
    private ProgressDialog myProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_image);

        //Recover the url sent from the previos activity
        recvExtras = getIntent().getExtras();
        imageUrl = recvExtras.getString("imageURL");

        postImage = (ImageView) findViewById(R.id.post_image_view);

        //A progress dialog while we load the image
        myProgress = new ProgressDialog(this);
        myProgress.setCancelable(false);
        myProgress.setMessage("Loading image, this may take some time...");
        myProgress.show();

        //Use Glide to get image into image view, listen for failure or resourceready and dismiss the progress dialog
        Glide.with(viewPostImage.this).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                myProgress.dismiss();
                Toast.makeText(viewPostImage.this,"Error:Failed to load image!",Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                myProgress.dismiss();
                return false;
            }
        }).into(postImage);


    }
}
