package ginger.connexus.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class SearchableActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SearchableActivity.class.toString();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
        Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
    }
}
