package ginger.connexus.fragment;

import ginger.connexus.service.ConnexusService;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

public abstract class SpiceFragment extends Fragment {

    private final SpiceManager spiceManager = new SpiceManager(ConnexusService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        // Please review https://github.com/octo-online/robospice/issues/96 for
        // the reason of that ugly if statement.
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
