package ch.swissonid.sample;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SampleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SampleFragment extends Fragment {

    private SampleFragmentListener mListener;

    private final static String HISTORY_KEY = "HistoryKey";
    private int mHistory;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param historySize The size of the current history (BackStack)
     * @return A new instance of fragment HelloFragment.
     *
     */
    public static SampleFragment newInstance(int historySize) {
        SampleFragment fragment = new SampleFragment();
        Bundle args = new Bundle();
        args.putInt(HISTORY_KEY, historySize);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the default value 0 of the history (BackStack) size.
     *
     * @return A new instance of fragment HelloFragment.
     *
     */
    public static SampleFragment newInstance() {
        return newInstance(0);
    }

    public SampleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHistory = getArguments().getInt(HISTORY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        setOnClickListener(rootView);
        if(mHistory == 0){
            ((TextView) rootView.findViewById(R.id.sampleText)).setText(getString(R.string.no_history));
        }else {
            ((TextView) rootView.findViewById(R.id.sampleText)).setText(String.valueOf(mHistory));
        }
        rootView.findViewById(R.id.backButton).setEnabled(mHistory >=1);
        return rootView;
    }

    private void setOnClickListener(final View rootView) {
        rootView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onNextClicked();
            }
        });
        rootView.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onBackClicked();
            }
        });
    }


    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SampleFragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SampleFragmentListener");
        }

    }

    public interface SampleFragmentListener{
       void onNextClicked();
       void onBackClicked();
    }
}
