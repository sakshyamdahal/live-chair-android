package claflin.livechair.com.livechair;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;


public class BarberProfile extends ActionBarActivity {

    public String TAG = ActionBarActivity.class.getSimpleName();
    protected JSONObject barber;
    int barberProfileId;
    // Drawable d;

   // private ImageView barberImage;
    private Button barberName;
//    private TextView barberPhone;
    //private TextView barberAddress;
    private String barberAddress = "";
    private Button googleMapNavigate;
    private Button reviews;
    private Button makeAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_profile);

        // barberImage = (ImageView) findViewById(R.id.barber_image);
        barberName = (Button) findViewById(R.id.shop_name_button);
        // barberPhone = (TextView) findViewById(R.id.barber_phone);
        //barberAddress = (TextView) findViewById(R.id.barber_address);
        googleMapNavigate = (Button) findViewById(R.id.navigate_button);
        reviews = (Button) findViewById(R.id.review_button);
        makeAppointments = (Button) findViewById(R.id.appointment_button);


        Intent intent = getIntent();
        barberProfileId = intent.getIntExtra("id", -1);
        Log.i("barber profile id ", barberProfileId + "");

        // Toast.makeText(this, barberProfileId + "", Toast.LENGTH_SHORT);

//      Log.i("activity", idValue + "");

        if (networkIsAvailable()) {
            GetBarberProfile getProfile = new GetBarberProfile();
            getProfile.execute();

        }

//        makeAppointments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(BarberProfile.this, "Appointment made!!", Toast.LENGTH_LONG);
//
//            }
//        });

        makeAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2012, 0, 19, 7, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2012, 0, 19, 8, 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Haircut appointment")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Haircut appointment with" + barberName.getText().toString())
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, barberName.getText().toString() + "'s Barber Shop")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, barberName.getText().toString() + "@gmail.com");
                startActivity(intent);
            }
        });


        reviews.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           Intent reviewsIntent = new Intent(BarberProfile.this, View_reviews.class);
                                           reviewsIntent.putExtra("profile_id", barberProfileId);
                                           startActivity(reviewsIntent);

                                       }
                                   });


        googleMapNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] addressArr = barberAddress.split(" ");
                String address = "";
                for (int i = 0; i < addressArr.length; i++)
                    address += addressArr[i] + "+";

                //https://www.google.com/maps/place/400+Magnolia+St,+Orangeburg,+SC+29115/@33.4990214,-80.854211,17z/data=!3m1!4b1!4m2!3m1!1s0x88f8d4fdebd176e3:0x49f8c1a22dce3683
                Intent mapActivity = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + address));
                startActivity(mapActivity);
            }

        });

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
        // barberPhone.setText(barberDes.getString("phone"));

        String complete_address = barberDes.getString("address") + " " + barberDes.getString("city") + " "
                + barberDes.getString("state") + " " + barberDes.getString("zip");

        //barberAddress.setText(complete_address);
        barberAddress = complete_address;

        //Log.i("imageUrl" , " " + imageUrl );

//        if (networkIsAvailable())
//        {
//            DownloadImageTask getImage = new DownloadImageTask(barberImage);
//            getImage.execute("https://fathomless-temple-1065.herokuapp.com" + barberDes.getString("avatar"));
//        }


    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//
//
//    }

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
