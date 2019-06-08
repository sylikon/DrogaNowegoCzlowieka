package com.maciek.v2.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maciek.v2.DB.TouristListContract;
import com.maciek.v2.R;

import java.io.IOException;

/**
 * Created by Geezy on 15.07.2018.
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public TrackListAdapter(Context context, Cursor cursor, ListItemClickListener listener) {
        this.mContext = context;
        this.mCursor = cursor;
        mOnClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_element, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String titleOfTrack = getTrackName(position);
        String posOfTrack = getTrackPos(position);
        holder.titleOfTrack.setText(titleOfTrack);
        holder.positionOfTrack.setText(posOfTrack + ".");
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleOfTrack;
        public TextView positionOfTrack;

        public ViewHolder(View v) {
            super(v);
            titleOfTrack = v.findViewById(R.id.list_item_name);
            positionOfTrack = v.findViewById(R.id.list_item_position);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPostion = getAdapterPosition();
            try {
                mOnClickListener.onListItemClick(clickedPostion, getTrackAudio(clickedPostion));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getTrackName(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(mCursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_NAME));
        }
        return null;
    }

    private String getTrackAudio(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(mCursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_AUDIO));
        }
        return null;
    }

    private String getTrackPos(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(mCursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_POSITION));
        }
        return null;
    }

    private String getTrackUri(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(mCursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_AUDIO_URI));
        }
        return null;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String title) throws IOException;
    }

    private String getTrackTypeId(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor.getString(mCursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_TYPE_ID));
        }
        return null;
    }


}
