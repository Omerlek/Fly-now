package com.example.flynow.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flynow.Model.Flight;
import com.example.flynow.R;
import com.example.flynow.adapter.FlightAdapter;
import com.example.flynow.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {

    private ActivitySearchBinding binding;
    private String from,to,date;
    private int numPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //המתודה onCreate היא הנקודה שבה פעילות (Activity) נבנית ומוצגת למשתמש
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();   //שואב את הנתונים שהועברו ל-SearchActivity מ-Intent (איפה,לאן,תאריך)
        initList();         //מבצעת את השליפה של הטיסות מ-Firebase ומגדירה את הרשימה ב-RecyclerView
        setVariable();      //מטפלת בפעולות נוספות כמו לחיצה על כפתור back

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Flights");
        ArrayList<Flight> list = new ArrayList<>();
        Query query = myRef.orderByChild("from").equalTo(from);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Flight flight = issue.getValue(Flight.class);

//                        if (flight.getTo().equals(to)) {
//                            list.add(flight);
//                        }

                        if (flight.getTo().trim().equalsIgnoreCase(to.trim()) &&
                                flight.getDate().trim().toLowerCase().equals(date.trim().toLowerCase())) {
                            list.add(flight);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.searchView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                        binding.searchView.setAdapter(new FlightAdapter(list,numPassenger));
                    }else {
                        Toast.makeText(SearchActivity.this, "No flights found", Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBarSearch.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                binding.progressBarSearch.setVisibility(View.GONE);
            }
        });
    }

    private void getIntentExtra() {
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
        numPassenger = getIntent().getIntExtra("numPassenger", 0);
    }
}