package ginger.connexus.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;

public final class AccountUtils {

    private AccountUtils() {
    }

    public static String[] getAccountNames(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < accounts.length; ++i) {
            names[i] = accounts[i].name;
        }
        return names;
    }

}
