package claflin.livechair.com.livechair;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LiveChair extends ActionBarActivity {

    Button findBarbers;
    Button myAppointments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_barbers);

        findBarbers = (Button) findViewById(R.id.barber_button);
        myAppointments = (Button) findViewById(R.id.appointment_button);


        findBarbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barberIntent = new Intent(LiveChair.this, BarberListActivity.class);
                startActivity(barberIntent);
            }
        });

        myAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentIntent = new Intent(LiveChair.this, Sign_in.class);
                startActivity(appointmentIntent);
            }
        });


    }







}
