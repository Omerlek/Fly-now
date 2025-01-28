package com.example.flynow.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flynow.Activity.SeatListActivity;
import com.example.flynow.Model.Flight;
import com.example.flynow.databinding.ViewholderFlightBinding;

import java.util.ArrayList;


//המתאם FlightAdapter מנהל רשימת טיסות ומציג אותן ב-RecyclerView בצורה דינמית ומבצע-----
//יוצר תצוגה עבור כל פריט (onCreateViewHolder)
//ממלא את הנתונים בכל פריט (onBindViewHolder)
//מטפל בלחיצה על פריט ומבצע מעבר ל-SeatListActivity
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.Viewholder> {

    private final ArrayList<Flight> flights;
    private Context context;
    private int numPassenger;

    public FlightAdapter(ArrayList<Flight> flights, int numPassenger) {
        this.flights = flights;
        this.numPassenger = numPassenger;
    }

    @NonNull
    @Override
    public FlightAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //יצירת והחזרת מופע של viewholder
        context = parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(LayoutInflater.from(context),parent,false);//יוצר אובייקט binding שמחבר בין המידע לxml
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightAdapter.Viewholder holder, int position) {//מטרת המטודה לקשר את הנתונים לאובייקט ה-viewholder
        Flight flight = flights.get(position);
        Glide.with(context).load(flight.getAirlineLogo()).into(holder.binding.logo);

        //קישור כל שדה לtextview המתאים לו
        holder.binding.fromTxt.setText(flight.getFrom());
        holder.binding.fromShortTxt.setText(flight.getFromShort());
        holder.binding.toTxt.setText(flight.getTo());
        holder.binding.toShortTxt.setText(flight.getToShort());
        holder.binding.arrivalTxt.setText(flight.getArriveTime());
        holder.binding.classTxt.setText(flight.getClassSeat());
        holder.binding.priceTxt.setText("$" + flight.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {       //בלחיצה על טיסה כלשהי מעביר לseatListActivity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight",flight);
                intent.putExtra("numPassenger", numPassenger);
                context.startActivity(intent);  //  מעביר את אובייקט Flight לactivity החדש
            }
        });

    }

    @Override
    public int getItemCount() {  //מספר הטיסות ברשימה שהrecyclerView ידע כמה פריטים להציג
        return flights.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {  //מחלקת viewholder שמייצגת פריט בודד ברשימה
        private final ViewholderFlightBinding binding;
        public Viewholder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
