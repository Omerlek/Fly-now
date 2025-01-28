package com.example.flynow.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flynow.Model.Location;
import com.example.flynow.R;
import com.example.flynow.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.text.ParseException;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;//חוסך את findViewById וניגש ישירות על ידי binding
    private int adultPassenger = 1, childPassenger = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLocations(); //פונקציה המציגה את היעדים של הto ואת הfrom
        initPassengers(); //פונקציה המציגה את כמות הנוסעים
        initClassSeat(); //פונקציית בחירת סוג המחלקה שטסים
        initDatePickup(); // פונקציית בחירת תאריך טיסה
        setVariable();//ההזנה לכפתור searchBtn שמחפש אחרי הזנת מידע
    }

    private void setVariable() {
        binding.SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM,yyyy", Locale.ENGLISH);

                    String originalDate = binding.departureDateTxt.getText().toString();
                    String formattedDate = outputFormat.format(inputFormat.parse(originalDate));

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("from", ((Location)binding.FromSp.getSelectedItem()).getName());
                    intent.putExtra("to", ((Location)binding.toSp.getSelectedItem()).getName());
                    intent.putExtra("date", formattedDate);
                    intent.putExtra("numPassenger", adultPassenger + childPassenger);
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();//אובקייט של תאריך נוכחי
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.departureDateTxt.setText(currentDate);//שם בתיבת הטקסט

        Calendar calendarTommorow = Calendar.getInstance();
        calendarTommorow.add(Calendar.DAY_OF_YEAR,1);//יוצר ומוסיף יום שיהיה תאריך של מחר
        String tommorowDate = dateFormat.format(calendarTommorow.getTime());
        binding.returnDateTxt.setText(tommorowDate);

        binding.departureDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(binding.departureDateTxt);
            }
        });

        binding.returnDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(binding.returnDateTxt);
            }
        });

    }

    private void initClassSeat() {

        binding.progressBarClass.setVisibility(View.VISIBLE); //מראה טעינה שעולים הנתונים
        ArrayList<String> list = new ArrayList<>();
        list.add("Business Class");
        list.add("First Class");
        list.add("Economy Class");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item,list);//מעביר את התנונים לספינר
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.classSp.setAdapter(adapter);
        binding.progressBarClass.setVisibility(View.GONE);//מסתיר את הטעינה ברגע שנתונים עלו
    }

    private void initPassengers() {

        binding.plusAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adultPassenger++;
                binding.adultTxt.setText(adultPassenger + " Adult");
            }
        });
        
        binding.minusAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adultPassenger > 1){
                    adultPassenger--;
                    binding.adultTxt.setText(adultPassenger + " Adult");
                }
            }
        });

        binding.plusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childPassenger++;
                binding.childTxt.setText(childPassenger + " Child");
            }
        });

        binding.minusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(childPassenger > 0){
                    childPassenger--;
                    binding.childTxt.setText(childPassenger + " Child");
                }
            }
        });

    }

    private void initLocations() {
        binding.progressBarFrom.setVisibility(View.VISIBLE); //מראה טעינה שעולים הנתונים
        binding.progressBarTo.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference("Locations");//ניגש לפיירביס ומעתיק את כל המיקומים למערך חדש
        ArrayList<Location> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Location.class));
                        ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);//מעביר את התנונים לספינר
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.FromSp.setAdapter(adapter);
                        binding.toSp.setAdapter(adapter);

                        binding.FromSp.setSelection(1);
                        binding.progressBarFrom.setVisibility(View.GONE);//מסתיר את הטעינה ברגע שנתונים עלו
                        binding.progressBarTo.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDatePickerDialog(TextView textView){
       int year = calendar.get(calendar.YEAR) ;
       int month = calendar.get(calendar.MONTH) ;
       int day = calendar.get(calendar.DAY_OF_MONTH) ;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,(view,selectedYear,selectedMonth,selectedDay)->{

            calendar.set(selectedYear,selectedMonth,selectedDay);
            String formattedDate = dateFormat.format(calendar.getTime());

            textView.setText(formattedDate);
        },year,month,day);
        datePickerDialog.show();

    }
}