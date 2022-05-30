package com.example.zooapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class LocationPermissionChecker {
    private ComponentActivity activity;

    final ActivityResultLauncher<String[]> requestPermissionLauncher;

    public LocationPermissionChecker(ComponentActivity activity){
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
            perms.forEach((perm, isGranted) -> {
                Log.i("ZooApplication",String.format("Permission %s granted: %s", perm, isGranted));
            });
        });
    }


    public boolean ensurePermission() {
        var requiredPermissions  = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        var hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        if(hasNoLocationPerms){
            requestPermissionLauncher.launch(requiredPermissions);
            return true;
        }
        return false;
    }
}
