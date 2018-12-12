package com.example.chinmay.sqlitecustomlistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private Context context;

    private ArrayList<User> users = new ArrayList<>();

    /** Constructor for RecyclerViewAdapter Class */
    public RecyclerViewAdapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
    }

    /** RecyclerView onCreateViewHolder, defined the list_adapter_view layout */
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_adapter_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /** unBindViewHolder, data is bound to the view */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i)
    {
        User user = users.get(i);

        if(user != null)
        {
            if(viewHolder.textId != null)
            {
                viewHolder.textId.setText(user.getId());
            }
            if (viewHolder.textUName != null)
            {
                viewHolder.textUName.setText(user.getuName());
            }
            if (viewHolder.textEmail != null)
            {
                viewHolder.textEmail.setText((user.geteMail()));
            }
            if (viewHolder.textPhone != null)
            {
                viewHolder.textPhone.setText((user.getPhone()));
            }
        }
    }

    /** Get size of the ArrayList */
    @Override
    public int getItemCount()
    {
        return users.size();
    }

    /** ViewHolder class, defined all the viewItems from the list_adapter_view layout */
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textId, textUName, textEmail, textPhone;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textId = itemView.findViewById(R.id.textId);
            textUName = itemView.findViewById(R.id.textUname);
            textEmail = itemView.findViewById(R.id.textEmail);
            textPhone = itemView.findViewById(R.id.textPhone);
        }
    }
}