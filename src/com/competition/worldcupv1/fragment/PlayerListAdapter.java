package com.competition.worldcupv1.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.competition.worldcupv1.R;
import com.competition.worldcupv1.dto.Player;


public class PlayerListAdapter extends ArrayAdapter<Player>{
	
	Context context; 
	int layoutResourceId;    
	Player data[] = null;

	public PlayerListAdapter(Context context, int layoutResourceId, List<Player> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		//this.data = data;
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new PlayerHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtPosition = (TextView)row.findViewById(R.id.txtPosition);
            
            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder)row.getTag();
        }
        
        final Player rowData= getItem(position);
       // Player player = data[position];
        holder.txtName.setText(rowData.name);
        holder.imgIcon.setImageResource(rowData.icon);
        holder.txtPosition.setText(rowData.position);
        
        return row;
    }
    
    static class PlayerHolder
    {
        ImageView imgIcon;
        TextView txtName;
        TextView txtPosition;
    }
}
