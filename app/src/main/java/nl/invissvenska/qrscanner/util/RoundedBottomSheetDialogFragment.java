package nl.invissvenska.qrscanner.util;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import nl.invissvenska.qrscanner.R;

public class RoundedBottomSheetDialogFragment extends BottomSheetDialogFragment {

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

        TextView tvContent = view.findViewById(R.id.content);
        TextView tvOpenBrowser = view.findViewById(R.id.openBrowser);
        TextView tvShareResult = view.findViewById(R.id.shareResult);
        TextView tvCopyResult = view.findViewById(R.id.copyResult);

        tvContent.setText(content);
        if (!URLUtil.isValidUrl(content)) {
            tvOpenBrowser.setVisibility(View.GONE);
        }

        tvOpenBrowser.setOnClickListener((View v) ->
                openBrowser.onClickOpenBrowser(content)
        );

        tvShareResult.setOnClickListener((View v) ->
                shareResult.onClickShareResult(content)
        );

        tvCopyResult.setOnClickListener((View v) ->
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