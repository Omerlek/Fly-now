package com.example.flynow.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.flynow.Model.Flight;
import com.example.flynow.Model.Seat;
import com.example.flynow.R;
import com.example.flynow.adapter.SeatAdapter;
import com.example.flynow.databinding.ActivitySeatListBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

public class SeatListActivity extends BaseActivity {

    private ActivitySeatListBinding binding;
    private Flight flight;
    private  Double price = 0.0 ;
    private int num = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        GetIntentExtra();     //לשאוב נתונים שהועברו לפעילות דרך Intent (את אובייקט Flight)
        initSeatList();      //ליצור את רשימת המושבים, להוסיף אותם ל-RecyclerView, ולהגדיר מתאם (Adapter) שמנהל את התצוגה והאינטראקציות
        setVariable();       //לטפל בלחיצה על כפתור חזור ולהחזיר את המשתמש למסך הקודם
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num > 0){
                    flight.setPassenger(binding.nameSeatSelectedTxt.getText().toString());
                    flight.setPrice(price);
                    Intent intent = new Intent(SeatListActivity.this, TicketDetailActivity.class);
                    intent.putExtra("flight",flight);
                    startActivity(intent);

                }else{
                    Toast.makeText(SeatListActivity.this, "Please select your seats", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSeatList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,7);   //  יוצר GridLayoutManager שמחלק את התצוגה של ה-RecyclerView לעמודות ושורות בנוסף 7 זה מספר העמודות בפריסה
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {            //מגדיר את SpanSizeLookup, שמכתיב כמה עמודות כל פריט ברשימה יתפוס
            @Override
            public int getSpanSize(int position) {
                return (position % 7 == 3) ? 1 :1 ;      //אם המיקום הוא בעמודה הרביעית בשורה
            }
        });

        binding.seatRecyclerview.setLayoutManager(gridLayoutManager);

        List<Seat> seatList = new ArrayList<>();
        int row = 0;
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() / 7) + 1;  //אם המיקום הוא בעמודה הרביעית בשורה לדלג להבא


        Map<Integer,String> seatAlphabetMap = new HashMap<>();  //מיפוי האינדקסים לאותיות של מושבים
        seatAlphabetMap.put(0,"A");
        seatAlphabetMap.put(1,"B");
        seatAlphabetMap.put(2,"C");
        seatAlphabetMap.put(4,"D");
        seatAlphabetMap.put(5,"E");
        seatAlphabetMap.put(6,"F");

        for(int i=0; i < numberSeat; i++){   //יצירת מושבים
            if(i%7 == 0){  //אם שורה חדשה
                row++;
            }
            if(i%7 == 3){  //אם זה מעבר אז להתעלם
                seatList.add(new Seat(Seat.SeatStatus.EMPTY,String.valueOf(row)));
            }else{
                String seatName = seatAlphabetMap.get(i % 7) + row;
                Seat.SeatStatus seatStatus = flight.getReservedSeats().contains(seatName) ? Seat.SeatStatus.UNAVAILABLE : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }

        int totalPassengers = getIntent().getIntExtra("numPassenger", 0);

        SeatAdapter seatAdapter = new SeatAdapter(seatList, this, totalPassengers, new SeatAdapter.SelectedSeat() {  //יוצר מתאם (SeatAdapter) שמטפל ברשימת המושבים
            @Override
            public void Return(String selectedName, int num) {
                int selectedSeatsCount = 0;
                for (Seat seat : seatList) {
                    if (seat.getStatus() == Seat.SeatStatus.SELECTED) {
                        selectedSeatsCount++;
                    }
                }
            binding.numberSelectedTxt.setText(num + " Seat Selected");  //מספר המושבים שנבחרו
            binding.nameSeatSelectedTxt.setText(selectedName);          //שמות המושבים שנבחרו
            DecimalFormat df = new DecimalFormat("#.##");
            price = (Double.valueOf(df.format(num * flight.getPrice())));
            SeatListActivity.this.num = num;
            binding.priceTxt.setText("$" + price);                  //המחיר הכולל של המושבים הנבחרים
                binding.confirmBtn.setEnabled(selectedSeatsCount == totalPassengers);

            }
        });

        binding.seatRecyclerview.setAdapter(seatAdapter);            //מחבר את ה-RecyclerView למתאם seatAdapter, כך שהמושבים יוצגו ב-RecyclerView
        binding.seatRecyclerview.setNestedScrollingEnabled(false);  //מבטל גלילה פנימית
    }

    private void GetIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}