package com.example.todoapp.projectadapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.TypeFaceUtil;
import com.example.todoapp.model.Project;

import java.util.Collections;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<Project> projects;
    private OnItemClickListener onItemClickListener;

    public ProjectAdapter(final List<Project> projects) {
        this.projects = projects;
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
       final View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.project_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Project project = projects.get(position);
        final Typeface typeface = TypeFaceUtil.getSelectedTypeFace();
        final float fontSize = TypeFaceUtil.getSelectedFontSize();

        if (null != typeface) {
            holder.projectNameTextView.setTypeface(typeface);
        } else if (0 != fontSize) {
            holder.projectNameTextView.setTextSize(fontSize);
        }
        holder.projectNameTextView.setText(project.getName());
        holder.itemView.setOnClickListener(view -> {
            if (null != onItemClickListener) {
                final int state = holder.getAbsoluteAdapterPosition();

                if (state != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(state);
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearProjects() {
        projects.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addProjects(final List<Project> newProjects) {
        projects.addAll(newProjects);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    @Override
    public void onItemMove(final int fromPosition, final int toPosition) {
        final Project fromProject = projects.get(fromPosition);
        final Project toProject = projects.get(toPosition);

        Collections.swap(projects, fromPosition, toPosition);
        fromProject.setProjectOrder((long) (toPosition + 1));
        toProject.setProjectOrder((long) fromPosition + 1);
        notifyItemMoved(fromPosition, toPosition);
        onItemClickListener.onProjectOrderUpdateListener(fromProject, toProject);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView projectNameTextView;
        final ImageButton removeButton;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.projectNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);

            removeButton.setOnClickListener(view -> {
                final int position = getAbsoluteAdapterPosition();

                if (position != RecyclerView.NO_POSITION && null != onItemClickListener) {
                    onItemClickListener.onRemoveButtonClick(position);
                    final Project projectToRemove = projects.get(position);

                    removeProject(projectToRemove);
                }
            });
        }
    }

    private void removeProject(final Project projectToRemove) {
        final int position = projects.indexOf(projectToRemove);

        if (-1 != position) {
            projects.remove(position);
            notifyItemRemoved(position);
        }
    }
}
