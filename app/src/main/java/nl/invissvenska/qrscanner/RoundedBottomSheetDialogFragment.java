package nl.invissvenska.qrscanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.content)
    TextView tvContent;
    @BindView(R.id.first)
    LinearLayout first;
    @BindView(R.id.second)
    LinearLayout second;

    private ClickOnFirst clickie;
    private String content;

    public RoundedBottomSheetDialogFragment(String content, ClickOnFirst clickie) {
        this.content = content;
        this.clickie = clickie;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment_bottom_sheet, container);
        ButterKnife.bind(this, view);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickie.onClickOnFirst();
            }
        });
        tvContent.setText(content);

        return view;
    }

    public interface ClickOnFirst {
        void onClickOnFirst();
    }
}