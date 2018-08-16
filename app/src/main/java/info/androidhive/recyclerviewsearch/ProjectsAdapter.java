package info.androidhive.recyclerviewsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pawar on 7/7/18.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Project> contactList;
    private List<Project> contactListFiltered;
    private ProjectsAdapter.ProjectsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc, dateofs, status;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            desc = view.findViewById(R.id.desc);
            thumbnail = view.findViewById(R.id.thumbnail1);
            dateofs = view.findViewById(R.id.dateofs);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ProjectsAdapter(Context context, List<Project> contactList, ProjectsAdapter.ProjectsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public ProjectsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_row_item, parent, false);

        return new ProjectsAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ProjectsAdapter.MyViewHolder holder, final int position) {
        final Project contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.desc.setText(contact.getPhone());
        holder.dateofs.setText(contact.getDate());
        if (contact.getImage().equals("employee")){
            Glide.with(context)
                    .load(R.drawable.resume)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);

        }

        if (contact.getImage().equals("client")){
            Glide.with(context)
                    .load(R.drawable.blue_button_background)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);

        }

        if (contact.getImage().equals("project")){
            Glide.with(context)
                    .load(R.drawable.manege)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);

        }


    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Project> filteredList = new ArrayList<>();
                    for (Project row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Project>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProjectsAdapterListener {
        void onContactSelected(Project project);
    }
}
