package com.example.shreetaskmanager;

/**
 * Stub class for Google Drive backup integration.
 * Real implementation would use Google Sign-In and Drive REST API to upload
 * a file containing JSON of tasks/expenses or use the Drive Android API.
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ*\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00050\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/example/shreetaskmanager/DriveBackupManager;", "", "<init>", "()V", "backupData", "", "context", "Landroid/content/Context;", "data", "", "account", "Lcom/google/android/gms/auth/api/signin/GoogleSignInAccount;", "restoreData", "onComplete", "Lkotlin/Function1;", "app_debug"})
public final class DriveBackupManager {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.shreetaskmanager.DriveBackupManager INSTANCE = null;
    
    private DriveBackupManager() {
        super();
    }
    
    /**
     * account obtained through GoogleSignIn. Data is a JSON string.
     * Implementation should use Drive REST API or DriveResourceClient to create
     * or open a folder named "ShreeTaskManager" under the app's space and
     * write the data as a file (e.g. tasks.json).
     * This is a stub showing where to plug real Drive code.
     */
    public final void backupData(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String data, @org.jetbrains.annotations.NotNull()
    com.google.android.gms.auth.api.signin.GoogleSignInAccount account) {
    }
    
    public final void restoreData(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.google.android.gms.auth.api.signin.GoogleSignInAccount account, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onComplete) {
    }
}