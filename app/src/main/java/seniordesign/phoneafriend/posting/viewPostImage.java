package seniordesign.phoneafriend.posting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import seniordesign.phoneafriend.R;

public class viewPostImage extends AppCompatActivity {

    private ImageView postImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_image);
    }
}
