package com.twainarc.tourmate.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.twainarc.tourmate.R;
import com.twainarc.tourmate.model.Expenses;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expenses> {

    private List<Expenses> expensesList;
    private int mResource;
    private Context mContext;

    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull List<Expenses> objects) {

        super(context, resource, objects);

        mContext = context;
        mResource = resource;
        expensesList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView details = convertView.findViewById(R.id.expenseDetails);
        TextView time = convertView.findViewById(R.id.expenseTime);
        TextView amount = convertView.findViewById(R.id.expenseAmount);

        details.setText(expensesList.get(position).getExpenseDetails());
        time.setText(expensesList.get(position).getTime());
        amount.setText(expensesList.get(position).getExpenseAmount());

        return convertView;
    }
}
