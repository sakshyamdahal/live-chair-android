package claflin.livechair.com.livechair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BarberProfile extends ActionBarActivity {

    public String TAG = ActionBarActivity.class.getSimpleName();
    protected JSONObject barber;
    int barberProfileId;
    Drawable d;

    private ImageView barberImage;
    private TextView barberName;
    private TextView barberPhone;
    private TextView barberAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_profile);

        barberImage = (ImageView) findViewById(R.id.barber_image);
        barberName = (TextView) findViewById(R.id.barber_name);
        barberPhone = (TextView) findViewById(R.id.barber_phone);
        barberAddress = (TextView) findViewById(R.id.barber_address);

        Intent intent = getIntent();
        barberProfileId = intent.getIntExtra("id", -1);

        Toast.makeText(this, barberProfileId + "", Toast.LENGTH_SHORT);

//      Log.i("activity", idValue + "");

        if (networkIsAvailable()) {
            GetBarberProfile getProfile = new GetBarberProfile();
            getProfile.execute();

        }


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


    private void updateView() throws JSONException {


        JSONObject barberDes = barber.getJSONObject("profile");
        Log.i("this is what baberDes look", barberDes.toString());
        Log.i("barberShop", barberDes.getString("phone"));
       // barberName.setText(barberDes.getInt("id") + "");


         barberName.setText(barberDes.getString("shop_name"));
         barberPhone.setText(barberDes.getString("phone"));

        String complete_address = barberDes.getString("address") + " " + barberDes.getString("city") + " "
                + barberDes.getString("state") + " " + barberDes.getString("zip");

        barberAddress.setText(complete_address);

        //Log.i("imageUrl" , " " + imageUrl );

        if (networkIsAvailable())
        {
            DownloadImageTask getImage = new DownloadImageTask(barberImage);
            getImage.execute("https://fathomless-temple-1065.herokuapp.com" + barberDes.getString("avatar"));
        }


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }


    }

    // data given, progress data and return data from background operation
    class GetBarberProfile extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object[] params) {



            JSONObject jsonResponse = null;
            int responseCode;
            String profileUrl = "https://fathomless-temple-1065.herokuapp.com/api/v1/profiles/" + barberProfileId;
            // try getting the barber data

            try {
                URL barberProfileUrl = new URL(profileUrl);
                HttpURLConnection connection = (HttpURLConnection) barberProfileUrl.openConnection();

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


                    jsonResponse = new JSONObject(responseData);
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
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            barber = result;
            try {
                updateView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }




}
