package com.example.flynow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flynow.Model.Seat;
import com.example.flynow.R;
import com.example.flynow.databinding.SeatItemBinding;

import java.util.ArrayList;
import java.util.List;


//המחלקה SeatAdapter מנהלת את הצגת המושבים, מעדכנת את הסטטוס שלהם ומחזירה נתונים על הבחירה
//עדכון תצוגה דינמי של כל מושב לפי הסטטוס
//תגובה ללחיצות לשינוי סטטוס המושבים
//ממשק להחזרת נתוני הבחירה (שמות ומספר המושבים שנבחרו)
public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewholder> {

    private final List<Seat> seatList;
    private final Context context;
    private ArrayList<String> selectedSeatName = new ArrayList<>();
    private SelectedSeat selectedSeat;
    private int maxSelectedSeats;


    public SeatAdapter(List<Seat> seatList, Context context, int maxSelectedSeats, SelectedSeat selectedSeat) {
        this.seatList = seatList;
        this.context = context;
        this.maxSelectedSeats = maxSelectedSeats;
        this.selectedSeat = selectedSeat;
    }

    @NonNull
    @Override
    public SeatAdapter.SeatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {           //ליצור את ה-Viewholder (התצוגה) עבור כל פריט חדש ב-RecyclerView
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SeatViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatAdapter.SeatViewholder holder, int position) {       //משנה את המראה של המושב בהתאם לסטטוס שלו (SeatStatus)
        Seat seat = seatList.get(position);
        holder.binding.seatImageView.setText(seat.getName());

        switch (seat.getStatus()){
            case AVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_available);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;

            case SELECTED:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.black));
                break;

            case UNAVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_unavailable);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.grey));
                break;

            case EMPTY:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_emptey);
                holder.binding.seatImageView.setTextColor(Color.parseColor("#00000000"));
                break;
        }

        holder.binding.seatImageView.setOnClickListener(new View.OnClickListener() {    //לטפל בלחיצות על המושבים ולשנות את הסטטוס שלהם
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Seat seat = seatList.get(position);

                    if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
                        if (selectedSeatName.size() < maxSelectedSeats) {
                            seat.setStatus(Seat.SeatStatus.SELECTED);
                            selectedSeatName.add(seat.getName());
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(context,
                                    "Cannot select more than " + maxSelectedSeats + " seats",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (seat.getStatus() == Seat.SeatStatus.SELECTED) {
                        seat.setStatus(Seat.SeatStatus.AVAILABLE);
                        selectedSeatName.remove(seat.getName());
                        notifyItemChanged(position);
                    }

                    String selected = selectedSeatName.toString()
                            .replace("[", "")
                            .replace("]", "")
                            .replace(" ", "");

                    selectedSeat.Return(selected, selectedSeatName.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public class SeatViewholder extends RecyclerView.ViewHolder {   //להחזיק את התצוגה של פריט אחד (מושב) בתוך ה-RecyclerView

        SeatItemBinding binding;
        public SeatViewholder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface SelectedSeat{             //ממשק (interface) שמוגדר בסוף המחלקה ומשמש להעברת המידע של המושבים הנבחרים למקומות אחרים באפליקציה
        void Return(String selectedName,int num);
    }

}
