package com.techmave.tourmate.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.techmave.tourmate.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDialog extends DialogFragment {

    @BindView(R.id.add_event_title)
    AppCompatEditText eventTitle;
    @BindView(R.id.add_event_description)
    AppCompatEditText eventDescription;
    @BindView(R.id.add_event_start_date)
    AppCompatTextView eventStartDate;
    @BindView(R.id.add_event_end_date)
    AppCompatTextView eventEndDate;

    @Override
    public void onResume() {

        super.onResume();

        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Point size = new Point();

        assert window != null;
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.85), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_event, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.add_event_start_date, R.id.add_event_end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_event_start_date:
                break;
            case R.id.add_event_end_date:
                break;
        }
    }
}
