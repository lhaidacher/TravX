package at.fhj.swd.travx.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.fhj.swd.travx.R;
import at.fhj.swd.travx.domain.Bill;
import at.fhj.swd.travx.util.CurrencyUtils;
import at.fhj.swd.travx.util.DateUtils;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.BillViewHolder> {
    private List<Bill> bills;
    private BillListAdapter.BillItemClickListener itemClickListener;

    public BillListAdapter(List<Bill> bills, BillListAdapter.BillItemClickListener listener) {
        this.bills = bills;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public BillListAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_list_item, parent, false);
        return new BillListAdapter.BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillListAdapter.BillViewHolder holder, int position) {
        holder.bindItem(bills.get(position));
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        private TextView tvValue;
        private TextView tvCreated;
        private View rootView;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tvBillValue);
            tvCreated = itemView.findViewById(R.id.tvBillCreated);
            rootView = itemView.findViewById(R.id.cvBillListItem);
        }

        public void bindItem(Bill bill) {
            tvValue.setText(CurrencyUtils.format(bill.getValue()));
            tvCreated.setText(DateUtils.formatExtended(bill.getCreatedAt()));
            rootView.setOnClickListener(view -> itemClickListener.onBillItemClicked(bill));
        }
    }

    public interface BillItemClickListener {
        void onBillItemClicked(Bill bill);
    }
}
