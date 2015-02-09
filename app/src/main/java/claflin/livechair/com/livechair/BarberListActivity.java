package claflin.livechair.com.livechair;

import android.app.ListActivity;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import java.util.List;


public class BarberListActivity extends ListActivity {
    protected Barber[] mBarberList;
    public String TAG = "tag from " + getClass().getSimpleName();
    protected JSONArray mBarberData;
    //ListView barberListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

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
            //barberId = barber.getInt("id");
            JSONObject profile = barber.getJSONObject("profile");
            barberId = profile.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Toast.makeText(this, barberId + " ", Toast.LENGTH_LONG).show();

        // start the BarberProfile Intent
        Intent barberProf = new Intent(BarberListActivity.this, BarberProfile.class);
        barberProf.putExtra("id", barberId);
        Log.i("profile id ", barberId + "");
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
                  mBarberList = new Barber[mBarberData.length()];
                  for (int i = 0; i < mBarberData.length(); i++)
                  {
                      JSONObject barbers = mBarberData.getJSONObject(i);
                      JSONObject barberInfo = barbers.getJSONObject("user");

                      JSONObject profile = barberInfo.getJSONObject("profile");

                      mBarberList[i] = new Barber(barberInfo.getString("fname") + " " + barberInfo.getString("lname"), profile.getInt("id"));
                  }

                    final BarberAdaptor mBarberAdaptor = new BarberAdaptor();
                    //Log.i(TAG, Arrays.toString(mBarberList));

                    setListAdapter(mBarberAdaptor);
                    //barberListView = (ListView)findViewById(android.R.id.list);

//
//                  barberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                      @Override
//                      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                              long arg3) {
//
//                          Log.i("message", "i got clicked");
//
//                          Barber barber = mBarberAdaptor.getBarber(arg2);
//
//                          int barberId = barber.mProfileId;
//
//                          Intent barberProf = new Intent(BarberListActivity.this, BarberProfile.class);
//                          barberProf.putExtra("id", barberId);
//
//                          startActivity(barberProf);
//                      }
//                  });

            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught: " + e);
            }
        }
    }



    class BarberAdaptor extends BaseAdapter{
        List<Barber> barberList = Arrays.asList(mBarberList);

        @Override
        public int getCount() {
            return barberList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row, parent, false);


            TextView barberName = (TextView) convertView.findViewById(R.id.list_text_view);
            RatingBar barberRating = (RatingBar) convertView.findViewById(R.id.list_rating_bar);
            ImageView barberImage = (ImageView) convertView.findViewById(R.id.list_image_view);
            Barber barber = barberList.get(position);

            barberName.setText(barber.mName);
            barberRating.setRating(barber.mRating);

            int id = getResources().getIdentifier("claflin.livechair.com.livechair:drawable/" + "default_barber_image", null, null);
           // barberImage.setImageResource(getResources().getDrawable(R.drawable.default_barber_image));
            barberImage.setImageResource(id);

            return convertView;

        }

        public Barber getBarber(int position)
        {
            return barberList.get(position);
        }
    }


    // data given, progress data and return data from background operation
    class GetBarbersTask extends AsyncTask<Object, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Object[] params) {



            JSONArray jsonResponse = null;
            int responseCode;
            String barberUrl = "https://livechairapp.herokuapp.com/api/v1/users/barbers";
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
                Log.e(TAG, "Exception caught: " + e);
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