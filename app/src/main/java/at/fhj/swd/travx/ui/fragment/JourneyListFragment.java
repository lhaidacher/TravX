package at.fhj.swd.travx.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.domain.Journey;
import at.fhj.swd.travx.ui.JourneyActivity;
import at.fhj.swd.travx.ui.adapter.JourneyListAdapter;

public class JourneyListFragment extends Fragment implements JourneyListAdapter.JourneyItemClickListener {
    private RecyclerView rvJourney;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.journey_list_fragment, container, false);

        rvJourney = view.findViewById(R.id.rvJourney);
        rvJourney.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void update(List<Journey> journeys) {
        JourneyListAdapter adapter = new JourneyListAdapter(journeys, this);
        rvJourney.setAdapter(adapter);
    }

    @Override
    public void onJourneyItemClicked(Journey journey) {
        Log.i("JOURNEY_ITEM_CLICKED", journey.getTitle());
        Intent intent = new Intent(getContext(), JourneyActivity.class);
        intent.putExtra("journey_name", journey.getTitle());
        startActivity(intent);
    }
}
