package nl.invissvenska.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    @BindView(R.id.scanner)
    ZXingScannerView scanner;

    RoundedBottomSheetDialogFragment sheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setScannerProperties();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flash:
                if (scanner.getFlash()) {
                    scanner.setFlash(false);
                    item.setIcon(R.drawable.ic_flash_off);
                } else {
                    scanner.setFlash(true);
                    item.setIcon(R.drawable.ic_flash_on);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<BarcodeFormat> getFormats() {
        List<BarcodeFormat> barcodeFormats = new ArrayList<>();
        barcodeFormats.add(BarcodeFormat.AZTEC);
        barcodeFormats.add(BarcodeFormat.CODABAR);
        barcodeFormats.add(BarcodeFormat.CODE_39);
        barcodeFormats.add(BarcodeFormat.CODE_93);
        barcodeFormats.add(BarcodeFormat.CODE_128);
        barcodeFormats.add(BarcodeFormat.DATA_MATRIX);
        barcodeFormats.add(BarcodeFormat.EAN_8);
        barcodeFormats.add(BarcodeFormat.EAN_13);
        barcodeFormats.add(BarcodeFormat.ITF);
        barcodeFormats.add(BarcodeFormat.MAXICODE);
        barcodeFormats.add(BarcodeFormat.PDF_417);
        barcodeFormats.add(BarcodeFormat.QR_CODE);
        barcodeFormats.add(BarcodeFormat.RSS_14);
        barcodeFormats.add(BarcodeFormat.RSS_EXPANDED);
        barcodeFormats.add(BarcodeFormat.UPC_A);
        barcodeFormats.add(BarcodeFormat.UPC_E);
        barcodeFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
        return barcodeFormats;
    }

    private void setScannerProperties() {
        scanner.setFormats(getFormats());
        scanner.setLaserColor(getColor(R.color.laserColor));
        scanner.setMaskColor(getColor(R.color.maskColor));
        scanner.setBorderStrokeWidth(getResources().getInteger(R.integer.borderStroke));
        scanner.setBorderColor(getColor(R.color.borderColor));
        if (Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            scanner.setAspectTolerance(0.5f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sheetDialog != null) {
            sheetDialog.dismiss();
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            String[] myStrings = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, myStrings, 1);
            return;
        }

        scanner.startCamera();
        scanner.setResultHandler(this);
    }

    @Override
    public void handleResult(Result result) {
        if (result != null) {

            sheetDialog = new RoundedBottomSheetDialogFragment(result.getText(), new RoundedBottomSheetDialogFragment.OpenBrowser() {
                @Override
                public void onClickOpenBrowser(String result) {
                    openInBrowser(result);
                }
            }, new RoundedBottomSheetDialogFragment.ShareResult() {
                @Override
                public void onClickShareResult(String result) {
                    shareIntent(result);
                }
            }, new RoundedBottomSheetDialogFragment.CopyResult() {
                @Override
                public void onClickShareResult(String result) {
                    copyToClipboard(result);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.copied_result), Toast.LENGTH_SHORT).show();
                }
            }, new RoundedBottomSheetDialogFragment.OnCancel() {
                @Override
                public void onCancelDialog() {
                    onResume();
                }
            });
            FragmentManager fm = getSupportFragmentManager();
            sheetDialog.show(fm, "modalSheetDialog");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCameraPreview();
        scanner.stopCamera();
    }

    private void openInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void shareIntent(String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_title)));
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Result", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }
}
