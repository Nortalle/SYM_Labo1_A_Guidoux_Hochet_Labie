package ch.heigvd.sym.template;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoggedActivity extends AppCompatActivity {

    private static final int ASK_PERMS = 10;

    private String givenImage;
    private TelephonyManager manager;
    private TextView email;
    private TextView imei;
    private ImageView avatar;
    private Boolean checkPermsOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        Intent intent   = getIntent();
        String message  = intent.getStringExtra("given_email");

        this.givenImage = intent.getStringExtra("given_image");
        this.manager    = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        this.email      = findViewById(R.id.email);
        this.imei       = findViewById(R.id.imei);
        this.avatar     = findViewById(R.id.avatar);

        this.imei.setText(R.string.not_granted_imei);
        this.email.setText(message);

        verifyPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        Log.d("permissionsResult", "Permissions received, enter check process");

        if(requestCode != ASK_PERMS)
            return;

        Map<String, Integer> perms = new HashMap<>();

        // Initialize the map with both permissions
        perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

        if (grantResults.length > 0) {

            Log.d("permissionsResult", "Some permissions were granted, or not...");

            Boolean ok = true;

            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);

            if(perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                loadImei();
            else
                ok = false;

            if(perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                loadImage();
            else
                ok = false;

            if(!ok) {
                Log.d("permissionsResult", "Some permissions were not granted! Verify them again and again");
                verifyPermissions();
            }
        }

        else
            verifyPermissions();
    }

    private void verifyPermissions() {

        Log.d("VerifyPermissions", "Entering permissions verification process");
        Boolean ok = true;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("VerifyPermissions", "Permission granted for READ_PHONE_STATE");
            loadImei();
        }
        else {
            Log.d("VerifyPermissions", "Permission NOT GRANTED for READ_PHONE_STATE");
            ok = false;
        }



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadImage();
            Log.d("VerifyPermissions", "Permission granted for READ_EXTERNAL_STORAGE");
        }
        else {
            Log.d("VerifyPermissions", "Permission NOT_GRANTED for READ_PHONE_STATE");
            ok = false;
        }

        //Permissions have already been checked, dont do it again but tell user that he's not cool
        if(!ok && checkPermsOnce) {

            AlertDialog.Builder notCoolDb = new AlertDialog.Builder(this);
            notCoolDb.setIcon(R.drawable.baseline_error_black_18dp);
            notCoolDb.setTitle(R.string.no_allow_permissions);
            notCoolDb.setMessage(R.string.no_allow_permissions_text);
            notCoolDb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    askPermissions();
                }
            });
            notCoolDb.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing, user aint cool
                }
            });

            notCoolDb.create().show();
        }

        else if(!ok) {

            Log.d("VerifyPermissions", "Missing some permissions, showing alert");
            AlertDialog.Builder alertbd = new AlertDialog.Builder(this);
            alertbd.setIcon(R.drawable.baseline_warning_black_18dp);
            alertbd.setTitle(R.string.missing_permissions);
            alertbd.setMessage(R.string.missing_permissions_text);
            alertbd.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("VerifyPermissions", "Dude said ok, go ask for those permissions");
                    checkPermsOnce = true;
                    askPermissions();
                }
            });

            alertbd.create().show();

        }
    }

    private void askPermissions() {

        Log.d("askPermissions", "Entering ask permissions process");

        ArrayList<String> neededPerms = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("askPermissions", "Permission READ_PHONE_STATE is required");
            neededPerms.add(Manifest.permission.READ_PHONE_STATE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("askPermissions", "Permission READ_EXTERNAL_STORAGE is required");
            neededPerms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!neededPerms.isEmpty()) {
            Log.d("askPermissions", "Some permissions are required, ask for them!");
            ActivityCompat.requestPermissions(this, neededPerms.toArray(new String[neededPerms.size()]), ASK_PERMS);
        }
    }

    @Override
    protected void onResume() {

        Log.d("onResumeLoggedActivity", "OnResume, activity is visible by the user");
        super.onResume();
    }

    @Override
    protected void onPause() {

        Log.d("onPauseLoggedActivity", "OnPause, activity might still be visible but not in foreground");
        super.onPause();
    }

    @Override
    protected void onStop() {

        Log.d("onStopLoggedActivity", "OnStop, activity not visible by the user anymore");
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Log.d("onDestroyLoggedActivity", "OnDestroy, activity is destroyed and finished");
        super.onDestroy();
    }


    private void loadImei() {

        Log.d("loadImei", "Checking imei number");

        try {
            if(Build.VERSION.SDK_INT >= 26)
                this.imei.setText(manager.getImei());
            else
                this.imei.setText(manager.getDeviceId());

        } catch (SecurityException e) {
            Log.d("loadImei", "Failed reading email because of security, should not happen, check those permissions");
            verifyPermissions();
        }
    }

    private void loadImage() {

        Log.d("loadImage", "Trying to load" + givenImage);

        this.avatar.setImageBitmap(BitmapFactory.decodeFile(
                Environment.getExternalStorageDirectory().getPath() + "/Download/" + givenImage));
    }

    @Override
    public void onBackPressed() {

        Log.d("BackPressed", "User pressed back, log him out");

        Toast.makeText(LoggedActivity.this, R.string.logout, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoggedActivity.this, MainActivity.class);
        intent.putExtra("imei", imei.getText()); //Pass data to previous activity like this
        startActivity(intent);
        finish();
    }
}
