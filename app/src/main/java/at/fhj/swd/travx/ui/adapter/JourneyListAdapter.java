package at.fhj.swd.travx.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.domain.Journey;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.JourneyViewHolder> {
    private List<Journey> journeys;
    private JourneyItemClickListener itemClickListener;

    public JourneyListAdapter(List<Journey> journeys, JourneyItemClickListener listener) {
        this.journeys = journeys;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_list_item, parent, false);
        return new JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        holder.bindItem(journeys.get(position));
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    class JourneyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private View rootView;

        public JourneyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJourneyTitle);
            rootView = itemView.findViewById(R.id.cvJourney);

        }

        public void bindItem(Journey journey) {
            tvTitle.setText(journey.getTitle());
            rootView.setOnClickListener(view -> itemClickListener.onJourneyItemClicked(journey));
        }
    }

    public interface JourneyItemClickListener {
        void onJourneyItemClicked(Journey journey);
    }
}
