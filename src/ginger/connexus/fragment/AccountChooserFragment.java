package ginger.connexus.fragment;

import ginger.connexus.R;
import ginger.connexus.util.AccountUtils;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AccountChooserFragment extends DialogFragment {

    public interface AccountChooserListener {
        public void onAccountChosen(String accountEmail);
    }

    private AccountChooserListener mListener;
    private String[] mAccountNames;

    public AccountChooserFragment() {
    }

    public static AccountChooserFragment newInstance(AccountChooserListener listener) {
        AccountChooserFragment accountChooser = new AccountChooserFragment();
        accountChooser.mListener = listener;
        return accountChooser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountNames = AccountUtils.getAccountNames(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_account_text);
        builder.setItems(mAccountNames, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onAccountChosen(mAccountNames[which]);
            }
        });
        return builder.create();
    }

}
