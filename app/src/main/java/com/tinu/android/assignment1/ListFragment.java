package com.tinu.android.assignment1;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tinu on 05-05-2016.
 */
public class ListFragment extends Fragment {
    private CustomListAdapter adapter;
    private ArrayList<ListItems> listItemArray = new ArrayList<ListItems>();
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        //The adapter will take the data and populate the list view.
        adapter = new CustomListAdapter(getActivity(), listItemArray);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView itemList = (ListView) rootView.findViewById(R.id.listItems);
        itemList.setAdapter(adapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListItems item = (ListItems) adapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.NAME, item.getName());
                intent.putExtra(Constants.EMAIL, item.getEmail());
                intent.putExtra(Constants.BODY, item.getBody());
                startActivity(intent);
            }
        });


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //Check if the device connected to the internet.
        if (haveNetworkConnection()) {
            updateList();
        } else {
            Toast.makeText(getActivity(), "You are not connected to the internet!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateList() {
        FetchListTask listTask = new FetchListTask();
        listTask.execute();
    }

    // Fetch the data from the URL.
    public class FetchListTask extends AsyncTask<String, Void, ArrayList<ListItems>> {

        private final String LOG_TAG = FetchListTask.class.getSimpleName();

        //Get the data from the JSON Array.
        private ArrayList<ListItems> getListDataFromJson(String listJsonStr)
                throws JSONException {

            String name;
            String email;
            String body;

            JSONArray jsonArray = new JSONArray(listJsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject listItems = jsonArray.getJSONObject(i);
                name = listItems.getString(Constants.NAME);
                email = listItems.getString(Constants.EMAIL);
                body = listItems.getString(Constants.BODY);


                ListItems items = new ListItems();
                items.setName(name);
                items.setEmail(email);
                items.setBody(body);
                listItemArray.add(items);
            }
            return listItemArray;


        }

        @Override
        protected void onPreExecute() {
            //Show the progress dialog while fetching the data.
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.please_wait));
            progressDialog.setMessage(getResources().getString(R.string.fetching_data));
            progressDialog.show();
        }

        @Override
        protected ArrayList<ListItems> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            try {

                URL url = new URL(Constants.URL);
                //Create the request to the URL and open the connections.
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the  data.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getListDataFromJson(forecastJsonStr);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This  happen if there was an error getting or parsing the data.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ListItems> listItemses) {
            super.onPostExecute(listItemses);
            //Dismiss the progress dialog after fetching the data.
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            adapter.notifyDataSetChanged();
        }
    }

    //Method for checking the internet connection.
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}