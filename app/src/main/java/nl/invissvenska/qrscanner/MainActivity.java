package nl.invissvenska.qrscanner;

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
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scanner;
    private DrawerLayout drawerLayout;

    RoundedBottomSheetDialogFragment sheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanner = findViewById(R.id.scanner);
        setScannerProperties();


        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.copied_result) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Toast.makeText(MainActivity.this, "My Account", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_gallery:
                    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    return true;
            }
            return true;
        });
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
            case R.id.menu:
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
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
    public void handleResult(Result result) {
        if (result != null) {

            sheetDialog = new RoundedBottomSheetDialogFragment(
                    result.getText(),
                    this::openInBrowser,
                    this::shareIntent,
                    (String copyResult) -> {
                        copyToClipboard(copyResult);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.copied_result), Toast.LENGTH_SHORT).show();
                    },
                    this::onResume
            );
            FragmentManager fm = getSupportFragmentManager();
            sheetDialog.show(fm, "modalSheetDialog");
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
    protected void onPause() {
        super.onPause();
        scanner.stopCameraPreview();
        scanner.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanner.setResultHandler(null);
        scanner.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sheetDialog != null) {
            scanner.setResultHandler(null);
            scanner.stopCamera();
        }
    }

    private void openInBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri.normalizeScheme());
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(R.string.malformed_url), Toast.LENGTH_SHORT).show();
        }
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
