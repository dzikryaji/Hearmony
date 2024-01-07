package das.mobile.hearmony.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AuthUtil {

    public static void revokeAccess(GoogleSignInClient googleSignInClient) {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AuthUtil", "Revoked access to Google account");
                        } else {
                            Log.w("AuthUtil", "Failed to revoke access to Google account", task.getException());
                        }
                    }
                });
    }
}

