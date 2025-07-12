package com.example.bepnhataapp.features.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.AddressItem;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    public interface OnEditClickListener{ void onEdit(AddressItem item); }

    private final List<AddressItem> items;
    private final OnEditClickListener editListener;
    private int selectedPos = 0;

    public AddressAdapter(List<AddressItem> items, OnEditClickListener listener, long preselectedId){
        this.items = items;
        this.editListener = listener;
        if(preselectedId!=0){
            for(int i=0;i<items.size();i++){
                if(items.get(i).getId()==preselectedId){
                    selectedPos=i;
                    break;
                }
            }
        }
        if(selectedPos==0){
            for(int i=0;i<items.size();i++){
                if(items.get(i).isDefault()){ selectedPos=i; break; }
            }
        }
    }

    public AddressAdapter(List<AddressItem> items, OnEditClickListener listener){
        this(items, listener, 0);
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

        holder.radio.setOnClickListener(v->{
            if(selectedPos!=position){
                selectedPos = position;
                notifyDataSetChanged();
            }
        });

        holder.tvEdit.setOnClickListener(v->{
            if(editListener!=null){
                editListener.onEdit(item);
            }
        });
    }
    @Override public int getItemCount(){return items==null?0:items.size();}
    static class AddressViewHolder extends RecyclerView.ViewHolder{
        android.widget.RadioButton radio; TextView tvName,tvPhone,tvAddress,tvDefault,tvEdit;
        AddressViewHolder(@NonNull View v){super(v);
            radio = v.findViewById(R.id.rb_default);
            tvName = v.findViewById(R.id.tv_name);
            tvPhone = v.findViewById(R.id.tv_phone);
            tvAddress = v.findViewById(R.id.tv_address);
            tvDefault = v.findViewById(R.id.tv_default);
            tvEdit = v.findViewById(R.id.tv_edit);
        }
    }
    public AddressItem getSelectedItem(){
        if(selectedPos<0 || selectedPos>=items.size()) return null;
        return items.get(selectedPos);
    }
} 
