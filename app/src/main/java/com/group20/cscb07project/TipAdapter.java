package com.group20.cscb07project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.TipViewHolder> {
    private List<TipAdapter.Tip> tips;

    public TipAdapter(List<TipAdapter.Tip> tips) {
        this.tips = tips;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tip_card, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        TipAdapter.Tip tip = tips.get(position);
        holder.tipText.setText(tip.getContent());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public void updateTips(List<TipAdapter.Tip> newTips) {
        this.tips = newTips;
        notifyDataSetChanged();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        final TextView tipText;

        TipViewHolder(View itemView) {
            super(itemView);
            tipText = itemView.findViewById(R.id.tip_text);
        }
    }

    public static class Tip {
        private final String content;
        private final String questionId;

        public Tip(String content, String questionId) {
            this.content = content;
            this.questionId = questionId;
        }

        public String getContent() {
            return content;
        }

    }
} 