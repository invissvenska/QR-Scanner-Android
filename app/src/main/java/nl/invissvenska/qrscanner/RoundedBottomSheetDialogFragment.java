package nl.invissvenska.qrscanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.content)
    TextView tvContent;
    @BindView(R.id.openBrowser)
    TextView llOpenBrowser;
    @BindView(R.id.shareResult)
    TextView llShareResult;
    @BindView(R.id.copyResult)
    TextView llCopyResult;

    private String content;
    private OpenBrowser openBrowser;
    private ShareResult shareResult;
    private CopyResult copyResult;
    private OnCancel onCancel;

    public RoundedBottomSheetDialogFragment(String content, OpenBrowser openBrowser, ShareResult shareResult, CopyResult copyResult, OnCancel onCancel) {
        this.content = content;
        this.openBrowser = openBrowser;
        this.shareResult = shareResult;
        this.copyResult = copyResult;
        this.onCancel = onCancel;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container);
        ButterKnife.bind(this, view);

        tvContent.setText(content);
        if (!URLUtil.isValidUrl(content)) {
            llOpenBrowser.setVisibility(View.GONE);
        }

        llOpenBrowser.setOnClickListener((View v) ->
                openBrowser.onClickOpenBrowser(content)
        );

        llShareResult.setOnClickListener((View v) ->
                shareResult.onClickShareResult(content)
        );

        llCopyResult.setOnClickListener((View v) ->
                copyResult.onClickShareResult(content)
        );

        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        onCancel.onCancelDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        openBrowser = null;
        shareResult = null;
        copyResult = null;
    }

    public interface OpenBrowser {
        void onClickOpenBrowser(String result);
    }

    public interface ShareResult {
        void onClickShareResult(String result);
    }

    public interface CopyResult {
        void onClickShareResult(String result);
    }

    public interface OnCancel {
        void onCancelDialog();
    }
}