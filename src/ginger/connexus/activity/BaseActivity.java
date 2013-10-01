package ginger.connexus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO check for authentication
        final Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

}
