package com.example.bepnhataapp.features.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.AddressItem;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private final List<AddressItem> items;
    private int selectedPos = 0;
    public AddressAdapter(List<AddressItem> items){
        this.items = items;
        // mark first selected by default
    }
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AddressViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,int position){
        AddressItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPhone.setText(item.getPhone());
        holder.tvAddress.setText(item.getAddress());
        holder.radio.setChecked(position==selectedPos);
        holder.tvDefault.setVisibility(item.isDefault()?View.VISIBLE:View.GONE);

        holder.itemView.setOnClickListener(v->{
            if(selectedPos!=position){
                selectedPos = position;
                notifyDataSetChanged();
            }
        });
    }
    @Override public int getItemCount(){return items==null?0:items.size();}
    static class AddressViewHolder extends RecyclerView.ViewHolder{
        android.widget.RadioButton radio; TextView tvName,tvPhone,tvAddress,tvDefault;
        AddressViewHolder(@NonNull View v){super(v);radio=v.findViewById(R.id.radioSelect);tvName=v.findViewById(R.id.tvName);tvPhone=v.findViewById(R.id.tvPhone);tvAddress=v.findViewById(R.id.tvAddress);tvDefault=v.findViewById(R.id.tvDefaultLabel);} }
} 