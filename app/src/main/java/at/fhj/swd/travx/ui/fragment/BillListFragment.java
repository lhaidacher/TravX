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
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.ui.BillActivity;
import at.fhj.swd.travx.ui.adapter.BillListAdapter;

public class BillListFragment extends Fragment implements BillListAdapter.BillItemClickListener {
    private RecyclerView rvBills;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_list_fragment, container, false);

        rvBills = view.findViewById(R.id.rvBills);
        rvBills.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void update(List<Bill> bills) {
        BillListAdapter adapter = new BillListAdapter(bills, this);
        rvBills.setAdapter(adapter);
    }

    @Override
    public void onBillItemClicked(Bill bill) {
        Intent intent = new Intent(getContext(), BillActivity.class);
        intent.putExtra("journey_name", bill.getJourneyTitle());
        intent.putExtra("bill_id", bill.getId());
        startActivity(intent);
    }
}
