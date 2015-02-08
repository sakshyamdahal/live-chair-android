package claflin.livechair.com.livechair;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class BarberListActivity extends ListActivity {
    protected String[] mBarberList;
    public String TAG = "tag from " + getClass().getSimpleName();
    protected JSONArray mBarberData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_list_activity);

        if (networkIsAvailable()) {
            GetBarbersTask getBarbers = new GetBarbersTask();
            getBarbers.execute();

        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l,v,position,id);
        JSONObject[] jsonBarbers = new JSONObject[mBarberData.length()];

        for (int i = 0; i < jsonBarbers.length; i++)
        {
            try {
                jsonBarbers[i] = mBarberData.getJSONObject(i).getJSONObject("user");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       JSONObject barber = jsonBarbers[position];
       Log.i(TAG, jsonBarbers[2] + "barbers");

        Log.i(TAG, barber.toString() + "at position " + position);
        //Toast.makeText(this,barber.toString(),Toast.LENGTH_SHORT).show();
        int barberId = -1;
        try {
            barberId = barber.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this, barberId + " ", Toast.LENGTH_LONG).show();

        // start the BarberProfile Intent
        Intent barberProf = new Intent(BarberListActivity.this, BarberProfile.class);
        barberProf.putExtra("id", barberId);

        startActivity(barberProf);

    }




    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }





    private void updateList() {
        if (mBarberData == null) {
            Log.i(TAG, "not valid");
        } else {
              try {
                  mBarberList = new String[mBarberData.length()];
                  for (int i = 0; i < mBarberData.length(); i++)
                  {
                      JSONObject barbers = mBarberData.getJSONObject(i);
                      JSONObject barberInfo = barbers.getJSONObject("user");

                      mBarberList[i] = barberInfo.getString("fname") + " " + barberInfo.getString("lname");
                  }

                    Log.i(TAG, Arrays.toString(mBarberList));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mBarberList);
                    setListAdapter(adapter);

            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught: " + e);
            }
        }
    }


    // data given, progress data and return data from background operation
    class GetBarbersTask extends AsyncTask<Object, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Object[] params) {



            JSONArray jsonResponse = null;
            int responseCode;
            String barberUrl = "https://fathomless-temple-1065.herokuapp.com/api/v1/users/barbers";
            // try getting the barber data
            try {
                URL barberListUrl = new URL(barberUrl);
                HttpURLConnection connection = (HttpURLConnection) barberListUrl.openConnection();

                connection.connect();
                responseCode = connection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();

                    Log.i(TAG, "InputStream " + inputStream);

                    StringBuilder data = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while( (line = br.readLine()) != null)
                    {
                        Log.i("lines i am getting", line);
                        data.append(line);
                    }

                    String responseData = new String(data);


                    jsonResponse = new JSONArray(responseData);
                    Log.v(TAG, jsonResponse.toString());

                } else {
                    Log.i(TAG, "Unsuccessful HTTP Response Code" + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "Exection caught: " + e);
            } catch (IOException e) {
                Log.e(TAG, "Exception caught: " + e);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONArray result)
        {
            mBarberData = result;
            updateList();
        }

    }

}



