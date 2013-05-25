package info.a7madev.myCourses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * User: A7maDev
 */
public class EmptyFragment extends Fragment {

    public EmptyFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get data from Bundle
        if (getArguments() != null) {

            TextView emptyuTextView = (TextView) getActivity().findViewById(R.id.emptyui_noData_textView);
            emptyuTextView.setText(getArguments().getString("NoData Message"));

        }
    }
}