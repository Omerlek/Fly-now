package com.example.flynow.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.flynow.Model.Flight;
import com.example.flynow.R;
import com.example.flynow.databinding.ActivityTicketDetailBinding;

public class TicketDetailActivity extends BaseActivity {

    private ActivityTicketDetailBinding binding;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        GetIntentExtra();     //לשאוב נתונים שהועברו לפעילות דרך Intent (את אובייקט Flight)
        setVariable();       //לטפל בלחיצה על כפתור חזור ולהחזיר את המשתמש למסך הקודם

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.fromTxt.setText(flight.getFromShort());
        binding.fromSmallTxt.setText(flight.getFrom());
        binding.toTxt.setText(flight.getTo());
        binding.toShortTxt.setText(flight.getToShort());
        binding.toSmallTxt.setText(flight.getTo());
        binding.dateTxt.setText(flight.getDate());
        binding.timeTxt.setText(flight.getTime());
        binding.arrivalTxt.setText(flight.getArriveTime());
        binding.classTxt.setText(flight.getClassSeat());
        binding.priceTxt.setText("$" + flight.getPrice());
        binding.airlinesTxt.setText(flight.getAirlineName());
        binding.seatsTxt.setText(flight.getPassenger());

        Glide.with(TicketDetailActivity.this).load(flight.getAirlineLogo()).into(binding.logo);

        binding.dowmloadTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TicketDetailActivity.this, "Download completed!\nThank you for choosing Fly Now!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void GetIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}