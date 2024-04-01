package com.example.qrcheckin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Syncs a recycler view displaying a list of attendee profiles with firestore database
 */
public class AttendeeAdapter extends FirestoreRecyclerAdapter<Attendee, AttendeeAdapter.AttendeeViewHolder> {
    private EventAdapter.OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AttendeeAdapter(@NonNull FirestoreRecyclerOptions<Attendee> options) {
        super(options);
    }

    /**
     * Displays the Attendee in the appropriate view in the view holder (invoked by the layout manager)
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                      item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     * @param model     The model object containing the data that should be used to populate the view.
     */
    @Override
    protected void onBindViewHolder(@NonNull AttendeeAdapter.AttendeeViewHolder holder, int position, @NonNull Attendee model) {
        holder.tvName.setText(model.getProfile().getName());
        if (model.getProfile().getProfilePicture() != null) {
            ImageStorageManager storage = new ImageStorageManager(model.getProfile().getProfilePicture(), "/ProfilePictures");
            storage.displayImage(holder.ivProfilePic);
        }
    }

    /**
     * Creates a view holder for a new Attendee item
     * @param parent    The ViewGroup into which the new View will be added after it is bound to
     *                      an adapter position.
     * @param viewType  The view type of the new View.
     * @return          ViewHolder with the created view
     */
    @NonNull
    @Override
    public AttendeeAdapter.AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendee,
                parent, false);
        return new AttendeeAdapter.AttendeeViewHolder(view);
    }

    // https://stackoverflow.com/questions/36712704/why-is-my-item-image-in-custom-recyclerview-changing-while-scrolling, Fathima km, 2017
    // Override getItemId and getItemViewType methods to prevent flickering of images while scrolling through the recycler view
    /**
     * Returns the position as the stable item ID
     * @param position Adapter position to query
     * @return position Int of the item's positon
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the position as the view type of the item
     * @param position position to query
     * @return position Int of the item's position
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Holds the view where attendee data is sent
     */
    class AttendeeViewHolder extends RecyclerView.ViewHolder {
        //TextView tvCheckIns;
        TextView tvName;
        ImageView ivProfilePic;
        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewAttendeeName);
            ivProfilePic = itemView.findViewById(R.id.imageViewAttendeeIcon);

            // Set onclick listener to open event page when this event's view is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();

                    // In the event that you click an item that is in it's removal animation (no
                    // valid position)
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    /**
     * Sends a document snapshot to the Event Page activity
     */
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    /**
     * Used in the Event Page activity to set a listener for an adapter
     * @param listener The listener to set for an adapter
     */
    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}