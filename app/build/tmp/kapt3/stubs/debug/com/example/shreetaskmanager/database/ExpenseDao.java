package com.example.shreetaskmanager.database;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\t\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\fH\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000f\u00c0\u0006\u0003"}, d2 = {"Lcom/example/shreetaskmanager/database/ExpenseDao;", "", "insert", "", "expense", "Lcom/example/shreetaskmanager/database/ExpenseEntity;", "(Lcom/example/shreetaskmanager/database/ExpenseEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "expensesForDay", "", "dayMillis", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "totalSpent", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "totalInvested", "app_debug"})
@androidx.room.Dao()
public abstract interface ExpenseDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.shreetaskmanager.database.ExpenseEntity expense, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM expenses WHERE dateMillis = :dayMillis")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object expensesForDay(long dayMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.shreetaskmanager.database.ExpenseEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT SUM(amount) FROM expenses WHERE isInvestment = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object totalSpent(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
    
    @androidx.room.Query(value = "SELECT SUM(amount) FROM expenses WHERE isInvestment = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object totalInvested(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion);
}