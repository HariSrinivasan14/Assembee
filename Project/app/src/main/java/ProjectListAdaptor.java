import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assembee.DashboardFragment;
import com.example.assembee.ProjectDetail;
import com.example.assembee.R;

import java.util.ArrayList;

public class ProjectListAdaptor extends RecyclerView.Adapter<ProjectListAdaptor.ViewHolder>{
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> owners = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> projectIds = new ArrayList<>();
    private Context context;

    public ProjectListAdaptor(ArrayList<String> titles, ArrayList<String> owners, ArrayList<String> descriptions, ArrayList<String> projectIds, Context context) {
        this.titles = titles;
        this.owners = owners;
        this.descriptions = descriptions;
        this.projectIds = projectIds;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.owner.setText(owners.get(position));
        holder.description.setText(descriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView owner;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.project_title);
            owner = itemView.findViewById(R.id.project_owner);
            description = itemView.findViewById(R.id.project_description);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("clicked card", ""+getAdapterPosition());
                    // send intent to notify the new project detail activity
                    Intent intent = new Intent(context, ProjectDetail.class);
//                    Log.d("adaptor length", ""+getItemCount());
                    intent.putExtra("projectId", projectIds.get(getAdapterPosition()));

                    context.startActivity(intent);
                }
            });

        }
    }
}
