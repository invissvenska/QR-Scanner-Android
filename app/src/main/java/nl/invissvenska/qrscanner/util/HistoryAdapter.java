package nl.invissvenska.qrscanner.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.invissvenska.qrscanner.R;
import nl.invissvenska.qrscanner.database.table.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final LayoutInflater inflater;
    private List<History> histories;
    private OnItemClickListener itemClickListener;

    public HistoryAdapter(Context context, OnItemClickListener itemClickListener) {
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if (histories != null) {
            History current = histories.get(position);
            holder.date.setText(current.getDate().toString());
            holder.value.setText(current.getValue());
            if (!URLUtil.isValidUrl(current.getValue())) {
                holder.value.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_barcode, 0, 0, 0);
            } else {
                holder.value.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_url, 0, 0, 0);
            }
        }
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (histories != null) {
            return histories.size();
        } else {
            return 0;
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView date;
        private final TextView value;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            value = itemView.findViewById(R.id.barcode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(histories.get(getAdapterPosition()).getValue());
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(String value);
    }
}
