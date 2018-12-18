package com.example.chinmay.sqlitecustomlistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable
{
    private Context context;

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<User> usersFiltered = new ArrayList<>();

    private RecyclerViewAdapterListener listener;

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

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    listener.onUserSelected(usersFiltered.get(getAdapterPosition()));
                    return true;
                }
            });

            /*itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onUserSelected(usersFiltered.get(getAdapterPosition()));
                }
            });*/
        }
    }

    /** Constructor for RecyclerViewAdapter Class */
    public RecyclerViewAdapter(Context context, ArrayList<User> users, RecyclerViewAdapterListener listener)
    {
        this.context = context;
        this.users = users;
        this.usersFiltered = users;
        this.listener = listener;
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
        User user = usersFiltered.get(i);

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
        return usersFiltered.size();
    }

    /** Filter is initialized and conditions are set, filtered results are set in the arraylist. */
    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String charString = constraint.toString();

                if(charString.isEmpty())
                {
                    usersFiltered = users;
                }
                else
                {
                    ArrayList<User> filteredUserList = new ArrayList<>();
                    for(User row : users)
                    {
                        if(row.getuName().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredUserList.add(row);
                        }
                    }

                    usersFiltered = filteredUserList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                usersFiltered = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /** Interface for getting the item which has been clicked */
    public interface RecyclerViewAdapterListener
    {
        void onUserSelected(User user);
    }
}