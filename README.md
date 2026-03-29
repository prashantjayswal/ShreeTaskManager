# Shree Task Manager

This is a simple Android application (Kotlin) that combines a daily task manager with an expense tracker.

## Features

- Manage daily tasks (add, complete, view by calendar date)
- Log expenses and investments, view by date
- Persistent storage using Room database
- Calendar view integration for date-wise navigation
- Task reminders with notifications via WorkManager (including configurable types)
- Tasks can now be rescheduled with a note explaining delays via long‑press on the list
- Gmail login and Drive permission flow for backups; data would be stored under a `ShreeTaskManager` folder in Drive (stubbed)
- UI enhancements: professional color palette, fade‑in animations, and free Material icons

## Getting Started

1. **Requirements**
   - Android Studio with Kotlin support
   - Android SDK (API level 34 or higher)
   - Google Play services (for Drive/Sign-In)
   - Gradle (will be downloaded by Android Studio)

2. **Opening the project**
   - Launch Android Studio and choose "Open an existing project".
   - Select the `d:/App/ShreeTaskManager` directory.
   - Allow the IDE to sync and download required dependencies.

3. **Building & Running**
   - Build the project using `Build > Make Project`.
   - Run on an emulator or physical device using the Run button.

4. **Using the app**
   - Use calendar view to switch dates and review tasks/expenses.
   - Tap **Add Task/Expense** to insert items; tasks include a reminder type spinner with many common categories (Generic, Birthday, Anniversary, Bill, Meeting, Call, Grocery, Study, Work, Personal). These can be further extended as needed.
   - If a task isn’t finished you can long‑press it in the list to reschedule it to a later date and optionally add a note explaining the delay; the list will display the due date/notes.
   - Sign in with your Google account using the buttons at top, then press **Backup** or **Restore** to sync data. The actual Drive upload/download is stubbed; integrate the Drive API for full functionality.

4. **Extending the app**
   - Add full Drive backup implementation under a `ShreeTaskManager` folder.
   - Enhance task editing UI, show notes and due dates in list items.
   - Improve UI animations or add onboarding.
   - Add expense listing views, charts, and filter by type.

## Project Structure

```
ShreeTaskManager/
├── app/
│   ├── src/main/java/com/example/shreetaskmanager
│   │   ├── MainActivity.kt
│   │   ├── TaskManager.kt
│   │   └── ExpenseManager.kt
│   ├── src/main/res/
│   │   └── layout/activity_main.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```
