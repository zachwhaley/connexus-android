package ginger.connexus.activity;

import ginger.connexus.R;
import ginger.connexus.auth.GetNameInForeground;
import ginger.connexus.fragment.AccountChooserFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AuthActivity extends FragmentActivity implements AccountChooserFragment.AccountChooserListener {

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    private static final String CHOOSE_ACCOUNT_TAG = "account_chooser";

    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
        setTitle("Sign in");
        createSignInButton();
    }

    private void createSignInButton() {
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AccountChooserFragment.newInstance(AuthActivity.this)
                        .show(getSupportFragmentManager(), CHOOSE_ACCOUNT_TAG);
            }
        });
    }

    @Override
    public void onAccountChosen(String accountEmail) {
        new GetNameInForeground(AuthActivity.this, accountEmail, SCOPE, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            Toast.makeText(this, "Unknown sign in error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            new GetNameInForeground(AuthActivity.this, mEmail, SCOPE, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        Toast.makeText(this, "Unknown sign in error", Toast.LENGTH_SHORT).show();
    }

}
