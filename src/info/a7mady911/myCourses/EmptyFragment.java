package info.a7mady911.myCourses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: A7madY911
 */
public class EmptyFragment extends Fragment {

    public EmptyFragment() {
        // Empty constructor required for fragment subclasses
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.empty_ui);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty_ui, container, false);
        return rootView;
    }
}