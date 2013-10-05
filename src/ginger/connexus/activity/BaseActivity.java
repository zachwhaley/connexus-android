package ginger.connexus.activity;

import ginger.connexus.R;
import ginger.connexus.util.AccountUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!AccountUtils.isAuthenticated(this)) {
            AccountUtils.startAuthenticationFlow(this, getIntent());
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.logout:
        	AccountUtils.signOut(this);
        	AccountUtils.startAuthenticationFlow(this, getIntent());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
