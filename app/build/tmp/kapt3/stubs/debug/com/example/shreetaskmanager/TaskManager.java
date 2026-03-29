package com.example.shreetaskmanager;

/**
 * Simple manager for keeping track of daily tasks.
 * This would later be expanded with persistence (Room, file storage, etc.)
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u001c\u001dB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J@\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\tH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0013J(\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u00072\b\u0010\u0016\u001a\u0004\u0018\u00010\tH\u0086@\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001b\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/example/shreetaskmanager/TaskManager;", "", "dao", "Lcom/example/shreetaskmanager/database/TaskDao;", "<init>", "(Lcom/example/shreetaskmanager/database/TaskDao;)V", "addTask", "", "title", "", "dateMillis", "type", "Lcom/example/shreetaskmanager/TaskManager$ReminderType;", "dueMillis", "notes", "(Ljava/lang/String;JLcom/example/shreetaskmanager/TaskManager$ReminderType;Ljava/lang/Long;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "completeTask", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveTask", "newDate", "note", "(JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "tasksForDay", "", "Lcom/example/shreetaskmanager/TaskManager$Task;", "dayMillis", "ReminderType", "Task", "app_debug"})
public final class TaskManager {
    @org.jetbrains.annotations.NotNull()
    private final com.example.shreetaskmanager.database.TaskDao dao = null;
    
    public TaskManager(@org.jetbrains.annotations.NotNull()
    com.example.shreetaskmanager.database.TaskDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addTask(@org.jetbrains.annotations.NotNull()
    java.lang.String title, long dateMillis, @org.jetbrains.annotations.NotNull()
    com.example.shreetaskmanager.TaskManager.ReminderType type, @org.jetbrains.annotations.Nullable()
    java.lang.Long dueMillis, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object completeTask(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object moveTask(long id, long newDate, @org.jetbrains.annotations.Nullable()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object tasksForDay(long dayMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.shreetaskmanager.TaskManager.Task>> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\r\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r\u00a8\u0006\u000e"}, d2 = {"Lcom/example/shreetaskmanager/TaskManager$ReminderType;", "", "<init>", "(Ljava/lang/String;I)V", "GENERIC", "BIRTHDAY", "ANNIVERSARY", "BILL", "MEETING", "CALL", "GROCERY", "STUDY", "WORK", "PERSONAL", "app_debug"})
    public static enum ReminderType {
        /*public static final*/ GENERIC /* = new GENERIC() */,
        /*public static final*/ BIRTHDAY /* = new BIRTHDAY() */,
        /*public static final*/ ANNIVERSARY /* = new ANNIVERSARY() */,
        /*public static final*/ BILL /* = new BILL() */,
        /*public static final*/ MEETING /* = new MEETING() */,
        /*public static final*/ CALL /* = new CALL() */,
        /*public static final*/ GROCERY /* = new GROCERY() */,
        /*public static final*/ STUDY /* = new STUDY() */,
        /*public static final*/ WORK /* = new WORK() */,
        /*public static final*/ PERSONAL /* = new PERSONAL() */;
        
        ReminderType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.example.shreetaskmanager.TaskManager.ReminderType> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\nH\u00c6\u0003J\u0010\u0010!\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0019J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003JX\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010$J\u0013\u0010%\u001a\u00020\u00072\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0015\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u001a\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012\u00a8\u0006*"}, d2 = {"Lcom/example/shreetaskmanager/TaskManager$Task;", "", "id", "", "title", "", "completed", "", "dateMillis", "type", "Lcom/example/shreetaskmanager/TaskManager$ReminderType;", "dueMillis", "notes", "<init>", "(JLjava/lang/String;ZJLcom/example/shreetaskmanager/TaskManager$ReminderType;Ljava/lang/Long;Ljava/lang/String;)V", "getId", "()J", "getTitle", "()Ljava/lang/String;", "getCompleted", "()Z", "getDateMillis", "getType", "()Lcom/example/shreetaskmanager/TaskManager$ReminderType;", "getDueMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getNotes", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(JLjava/lang/String;ZJLcom/example/shreetaskmanager/TaskManager$ReminderType;Ljava/lang/Long;Ljava/lang/String;)Lcom/example/shreetaskmanager/TaskManager$Task;", "equals", "other", "hashCode", "", "toString", "app_debug"})
    public static final class Task {
        private final long id = 0L;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String title = null;
        private final boolean completed = false;
        private final long dateMillis = 0L;
        @org.jetbrains.annotations.NotNull()
        private final com.example.shreetaskmanager.TaskManager.ReminderType type = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long dueMillis = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String notes = null;
        
        public Task(long id, @org.jetbrains.annotations.NotNull()
        java.lang.String title, boolean completed, long dateMillis, @org.jetbrains.annotations.NotNull()
        com.example.shreetaskmanager.TaskManager.ReminderType type, @org.jetbrains.annotations.Nullable()
        java.lang.Long dueMillis, @org.jetbrains.annotations.Nullable()
        java.lang.String notes) {
            super();
        }
        
        public final long getId() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTitle() {
            return null;
        }
        
        public final boolean getCompleted() {
            return false;
        }
        
        public final long getDateMillis() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.shreetaskmanager.TaskManager.ReminderType getType() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getDueMillis() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getNotes() {
            return null;
        }
        
        public final long component1() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        public final boolean component3() {
            return false;
        }
        
        public final long component4() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.shreetaskmanager.TaskManager.ReminderType component5() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component6() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component7() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.shreetaskmanager.TaskManager.Task copy(long id, @org.jetbrains.annotations.NotNull()
        java.lang.String title, boolean completed, long dateMillis, @org.jetbrains.annotations.NotNull()
        com.example.shreetaskmanager.TaskManager.ReminderType type, @org.jetbrains.annotations.Nullable()
        java.lang.Long dueMillis, @org.jetbrains.annotations.Nullable()
        java.lang.String notes) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}