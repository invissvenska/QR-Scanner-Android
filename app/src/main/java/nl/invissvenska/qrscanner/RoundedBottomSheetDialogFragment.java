package nl.invissvenska.qrscanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    LinearLayout first;
    LinearLayout second;

    private ClickOnFirst clickie;

    public RoundedBottomSheetDialogFragment(ClickOnFirst clickie) {
        this.clickie = clickie;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment_bottom_sheet, container);

        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickie.onClickOnFirst();
            }
        });

        return view;
    }

    public interface ClickOnFirst {
        void onClickOnFirst();
    }
}