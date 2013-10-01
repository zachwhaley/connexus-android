package ginger.connexus.activity;

import ginger.connexus.R;
import ginger.connexus.auth.GetNameInForeground;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

public class AuthActivity extends Activity {

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    public static final String EXTRA_ACCOUNTNAME = "extra_accountname";

    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    private AccountManager mAccountManager;
    private List<String> mAccountNames;
    private Spinner mAccountsSpinner;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountNames = getAccountNames();

        setContentView(R.layout.activity_auth);
        setTitle("Sign in");

        mAccountsSpinner = createAccountsSpinner();
        createSignInButton();
    }

    private Spinner createAccountsSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                mAccountNames);
        Spinner accountsSpinner = (Spinner) findViewById(R.id.accounts_spinner);
        accountsSpinner.setAdapter(adapter);
        return accountsSpinner;
    }

    private void createSignInButton() {
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int accountIndex = mAccountsSpinner.getSelectedItemPosition();
                if (accountIndex < 0) {
                    return;
                }
                mEmail = mAccountNames.get(accountIndex);
                new GetNameInForeground(AuthActivity.this, mEmail, SCOPE, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR)
                        .execute();
            }
        });
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

    private List<String> getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        ArrayList<String> names = new ArrayList<String>(accounts.length);
        for (Account account : accounts) {
            names.add(account.name);
        }
        return names;
    }
}
