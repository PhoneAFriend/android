package seniordesign.phoneafriend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    protected void gotoSignUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
