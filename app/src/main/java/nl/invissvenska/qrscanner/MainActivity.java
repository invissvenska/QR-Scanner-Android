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

    private void setScannerProperties() {
        List<BarcodeFormat> lists = new ArrayList<>();
        lists.add(BarcodeFormat.QR_CODE);
        scanner.setFormats(lists);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                String[] myStrings = {Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this, myStrings, 1);
                return;
            }
        }
        scanner.startCamera();
        scanner.setResultHandler(this);
    }

    @Override
    public void handleResult(Result result) {
        if (result != null) {

            RoundedBottomSheetDialogFragment mySheetDialog = new RoundedBottomSheetDialogFragment(result.getText(), new RoundedBottomSheetDialogFragment.OpenBrowser() {
                @Override
                public void onClickOpenBrowser() {
                    Toast.makeText(getApplicationContext(), "open browser", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Result copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            FragmentManager fm = getSupportFragmentManager();
            mySheetDialog.show(fm, "modalSheetDialog");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Result", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    private void shareIntent(String text) {
//        String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, "Choose your app"));
    }

    /**
     * Resume the camera after 2 seconds when qr code successfully scanned through bar code reader.
     */
//    private void resumeCamera() {
//        Toast.LENGTH_LONG
//        Handler handler =  new Handler();
//        handler.postDelayed({ scanner.resumeCameraPreview(this) }, 2000);
//    }
}
