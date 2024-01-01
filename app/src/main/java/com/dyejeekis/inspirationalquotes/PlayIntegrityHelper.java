package com.dyejeekis.inspirationalquotes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.StandardIntegrityManager;

public class PlayIntegrityHelper {

    public static final String TAG = PlayIntegrityHelper.class.getName();

    private final Context context;

    private StandardIntegrityManager.StandardIntegrityTokenProvider integrityTokenProvider;

    public PlayIntegrityHelper(Context context) {
        this.context = context;
    }

    public void prepareTokenProvider(long cloudProjectNumber) {
        // Create an instance of a manager.
        StandardIntegrityManager standardIntegrityManager =
                IntegrityManagerFactory.createStandard(context);

        // Prepare integrity token. Can be called once in a while to keep internal
        // state fresh.
        standardIntegrityManager.prepareIntegrityToken(
                        StandardIntegrityManager.PrepareIntegrityTokenRequest.builder()
                                .setCloudProjectNumber(cloudProjectNumber)
                                .build())
                .addOnSuccessListener(tokenProvider -> {
                    integrityTokenProvider = tokenProvider;
                })
                .addOnFailureListener(this::handleError);
    }

    public void requestIntegrityVerdict(String requestHash) {
        if (integrityTokenProvider == null) {
            if (context instanceof Activity) {
                String message = "Failed to initialize integrity token provider";
                Util.displaySnackbar((Activity) context, message, Snackbar.LENGTH_SHORT);
            }
            return;
        }
        // Request integrity token by providing a user action request hash. Can be called
        // several times for different user actions.
        Task<StandardIntegrityManager.StandardIntegrityToken> integrityTokenResponse =
                integrityTokenProvider.request(
                        StandardIntegrityManager.StandardIntegrityTokenRequest.builder()
                                .setRequestHash(requestHash)
                                .build());
        integrityTokenResponse
                .addOnSuccessListener(response -> sendToServer(response.token()))
                .addOnFailureListener(this::handleError);
    }

    private void sendToServer(String token) {
        Log.d(TAG, "Integrity token: " + token);
    }

    private void handleError(Exception e) {
        if (context instanceof Activity) {
            String message = e.getMessage();
            Log.e(TAG, message);
            Util.displaySnackbar((Activity) context, message, Snackbar.LENGTH_LONG);
        }
    }
}
