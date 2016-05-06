package com.tinu.android.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private String mForecastStr;

        public DetailFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            String name = "";
            String email = "";
            String body = "";

            Intent intent = getActivity().getIntent();
            if (null != intent) {
                //Get the data from the intent
                name = intent.getStringExtra(Constants.NAME);
                email = intent.getStringExtra(Constants.EMAIL);
                body = intent.getStringExtra(Constants.BODY);
            }

            TextView nameTxt = (TextView) rootView.findViewById(R.id.textName);
            nameTxt.setText(name);

            TextView emailTxt = (TextView) rootView.findViewById(R.id.textEmail);
            emailTxt.setText(email);

            TextView bodyTxt = (TextView) rootView.findViewById(R.id.textBody);
            bodyTxt.setText(body);

            return rootView;
        }

    }
}
