package nl.invissvenska.qrscanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.content)
    TextView tvContent;
    @BindView(R.id.openBrowser)
    LinearLayout llOpenBrowser;
    @BindView(R.id.shareResult)
    LinearLayout llShareResult;
    @BindView(R.id.copyResult)
    LinearLayout llCopyResult;

    private String content;
    private OpenBrowser openBrowser;
    private ShareResult shareResult;
    private CopyResult copyResult;

    public RoundedBottomSheetDialogFragment(String content, OpenBrowser openBrowser, ShareResult shareResult, CopyResult copyResult) {
        this.content = content;
        this.openBrowser = openBrowser;
        this.shareResult = shareResult;
        this.copyResult = copyResult;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment_bottom_sheet, container);
        ButterKnife.bind(this, view);

        tvContent.setText(content);
        if(!URLUtil.isValidUrl(content)) {
            llOpenBrowser.setVisibility(View.GONE);
        }

        llOpenBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser.onClickOpenBrowser();
            }
        });

        llShareResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareResult.onClickShareResult(content);
            }
        });

        llCopyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyResult.onClickShareResult(content);
            }
        });

        return view;
    }

    public interface OpenBrowser {
        void onClickOpenBrowser();
    }

    public interface ShareResult {
        void onClickShareResult(String result);
    }

    public interface CopyResult {
        void onClickShareResult(String result);
    }
}