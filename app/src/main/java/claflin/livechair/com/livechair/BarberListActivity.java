package claflin.livechair.com.livechair;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class BarberListActivity extends ListActivity {
    protected ArrayList<String> mBarberList;
    public String TAG = "tag from " + getClass().getSimpleName();
//    public static final int NUMBER_OF_BARBERS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_list_activity);

//        if (networkIsAvailable()) {
//            GetBarbersTask getBarbersTask = new GetBarbersTask();
//            getBarbersTask.execute();
//        }
//        else
//        {
//            Toast.makeText(this, "Network is unavailable.", Toast.LENGTH_LONG).show();
//
//        }


          if (networkIsAvailable())
          {
              getBarberData();
          }







//        Resources resources = getResources();
//        mBarberList = resources.getStringArray(R.array.fake_barbers);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mBarberList);
//        setListAdapter(adapter);
    }

    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_barbers, menu);
        return true;
    }

    public void getBarberData() {

        String barberUrl = "https://fathomless-temple-1065.herokuapp.com/api/v1/users/barbers";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(barberUrl).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                try {
                    final String jsonData = response.body().string();
                    Log.i(TAG, jsonData);

                    if (response.isSuccessful()) {
                        JSONArray jsonArray = new JSONArray(jsonData);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (IOException e) {
                    Log.e("TAG", "Exception caught " + e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // data given, progress data and return data from background operation
    private class GetBarbersTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object[] params) {
//            int responseCode = -1;
//
//            // try getting the barber data
//            try {
//                URL barberListUrl = new URL("https://api.forecast.io/forecast/5534ffc4c4805ecb5a7800e9be7bbe27/37.8267,-122.423");
//                HttpURLConnection connection = (HttpURLConnection) barberListUrl.openConnection();
//
//                connection.connect();
//                responseCode = connection.getResponseCode();
//
//
//                if (responseCode == HttpURLConnection.HTTP_OK)
//                {
//                    InputStream inputStream = connection.getInputStream();
//
//                    Log.i(TAG, "InputStream " + inputStream);
//
//                    Reader reader = new InputStreamReader(inputStream);
//
//                    int contentLength = connection.getContentLength();
//
//                    Log.i(TAG, contentLength + "");
//                    char[] charArray = new char[contentLength];
//
//
//
//                    reader.read(charArray);
//                    String responseData = new String(charArray);
//
//
//                    JSONArray jsonResponse = new JSONArray(responseData);
//                    Log.v(TAG, jsonResponse.toString());
//
//                }
//                else {
//                    Log.i(TAG, "Unsuccessful HTTP Response Code" + responseCode);
//                }
//            } catch (MalformedURLException e) {
//                Log.e(TAG, "Exection caught: " + e);
//            }
//            catch (IOException e)
//            {
//                Log.e(TAG, "Exception caught: " + e);
//            }
//            catch (Exception e)
//            {
//                Log.e(TAG, "Exception caught: " + e);
//            }
//
//            return "Code " + responseCode;
//        }
//    }

            String barberUrl = "https://fathomless-temple-1065.herokuapp.com/api/v1/users/barbers";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(barberUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    try {
                        final String jsonData = response.body().string();
                        Log.i(TAG, jsonData);

                        if (response.isSuccessful()) {
                            JSONArray jsonArray = new JSONArray(jsonData);

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } catch (IOException e) {
                        Log.e("TAG", "Exception caught " + e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return "";
        }
    }
}



