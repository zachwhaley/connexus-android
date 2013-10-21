/*
 * Copyright (C) 2013 Zach Whaley, Trevor Latson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ginger.connexus.fragment;

import ginger.connexus.R;
import ginger.connexus.util.AccountUtils;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChooseAccountFragment extends DialogFragment {

    public interface ChooseAccountListener {
        public void onAccountChosen(final Account accountEmail);
    }

    private ChooseAccountListener mListener;
    private List<Account> mAccounts;

    public ChooseAccountFragment() {
    }

    public static ChooseAccountFragment newInstance(ChooseAccountListener listener) {
        ChooseAccountFragment accountChooser = new ChooseAccountFragment();
        accountChooser.mListener = listener;
        return accountChooser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccounts = AccountUtils.getAccounts(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_account_text);
        ArrayList<String> emails = new ArrayList<String>();
        for (Account account : mAccounts) {
            emails.add(account.name);
        }
        builder.setItems(emails.toArray(new String[emails.size()]), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onAccountChosen(mAccounts.get(which));
            }
        });
        return builder.create();
    }

}
