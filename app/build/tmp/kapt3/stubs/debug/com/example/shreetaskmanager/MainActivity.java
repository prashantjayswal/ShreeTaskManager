package com.example.shreetaskmanager;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0014J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\"H\u0002J\b\u0010#\u001a\u00020\u001aH\u0002J\b\u0010$\u001a\u00020\u001aH\u0002J\"\u0010%\u001a\u00020\u001a2\u0006\u0010&\u001a\u00020\u00182\u0006\u0010\'\u001a\u00020\u00182\b\u0010(\u001a\u0004\u0018\u00010)H\u0014J\b\u0010*\u001a\u00020\u001aH\u0002J\b\u0010+\u001a\u00020\u001aH\u0002J\b\u0010,\u001a\u00020\u001aH\u0002J\b\u0010-\u001a\u00020\u001aH\u0002J\b\u0010.\u001a\u00020\u001aH\u0002J\u0010\u0010/\u001a\u0002002\u0006\u00101\u001a\u000202H\u0016J\u0010\u00103\u001a\u0002002\u0006\u00104\u001a\u000205H\u0016J\b\u00106\u001a\u00020\u001aH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/example/shreetaskmanager/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "<init>", "()V", "taskManager", "Lcom/example/shreetaskmanager/TaskManager;", "gson", "Lcom/google/gson/Gson;", "expenseManager", "Lcom/example/shreetaskmanager/ExpenseManager;", "adapter", "Lcom/example/shreetaskmanager/TaskAdapter;", "calendarView", "Landroid/widget/CalendarView;", "recyclerTasks", "Landroidx/recyclerview/widget/RecyclerView;", "buttonAdd", "Landroid/widget/Button;", "buttonSignIn", "buttonBackup", "buttonRestore", "googleSignInClient", "Lcom/google/android/gms/auth/api/signin/GoogleSignInClient;", "RC_SIGN_IN", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "loadTasksFor", "dayMillis", "", "showRescheduleDialog", "task", "Lcom/example/shreetaskmanager/TaskManager$Task;", "setupGoogleSignIn", "signIn", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "performBackup", "performRestore", "showAddDialog", "showAddTaskDialog", "showAddExpenseDialog", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "createNotificationChannel", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.shreetaskmanager.TaskManager taskManager;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    private com.example.shreetaskmanager.ExpenseManager expenseManager;
    private com.example.shreetaskmanager.TaskAdapter adapter;
    private android.widget.CalendarView calendarView;
    private androidx.recyclerview.widget.RecyclerView recyclerTasks;
    private android.widget.Button buttonAdd;
    private android.widget.Button buttonSignIn;
    private android.widget.Button buttonBackup;
    private android.widget.Button buttonRestore;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient googleSignInClient;
    private final int RC_SIGN_IN = 9001;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadTasksFor(long dayMillis) {
    }
    
    private final void showRescheduleDialog(com.example.shreetaskmanager.TaskManager.Task task) {
    }
    
    private final void setupGoogleSignIn() {
    }
    
    private final void signIn() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void performBackup() {
    }
    
    private final void performRestore() {
    }
    
    private final void showAddDialog() {
    }
    
    private final void showAddTaskDialog() {
    }
    
    private final void showAddExpenseDialog() {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    private final void createNotificationChannel() {
    }
}