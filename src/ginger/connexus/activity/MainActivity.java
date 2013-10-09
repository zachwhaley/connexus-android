package ginger.connexus.activity;

import ginger.connexus.R;
import ginger.connexus.fragment.StreamGridFragment;
import ginger.connexus.util.AccountUtils;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class MainActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.toString();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stream_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        startLocationClient();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            switch (position) {
                case StreamGridFragment.REQUEST_ALL:
                    arguments.putInt(StreamGridFragment.REQUEST, StreamGridFragment.REQUEST_ALL);
                    break;
                case StreamGridFragment.REQUEST_SUBSCRIBED:
                    arguments.putInt(StreamGridFragment.REQUEST, StreamGridFragment.REQUEST_SUBSCRIBED);
                    final String email = AccountUtils.getChosenAccountName(MainActivity.this);
                    arguments.putString(StreamGridFragment.EMAIL, email);
                    break;
                case StreamGridFragment.REQUEST_NEARBY:
                    arguments.putInt(StreamGridFragment.REQUEST, StreamGridFragment.REQUEST_NEARBY);
                    arguments.putParcelable(StreamGridFragment.LOCATION, getLocation());
                    break;
            }
            Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
            return StreamGridFragment.newInstance(intent, arguments);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case StreamGridFragment.REQUEST_ALL:
                    return getString(R.string.title_all);
                case StreamGridFragment.REQUEST_SUBSCRIBED:
                    return getString(R.string.title_subscriptions);
                case StreamGridFragment.REQUEST_NEARBY:
                    return getString(R.string.title_nearby);
            }
            return null;
        }
    }

}
