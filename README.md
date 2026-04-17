# Shree Task Manager

**Shree Task Manager** is an intelligent, high-performance Android productivity application built with Kotlin. It seamlessly integrates advanced **Task Management** with **Automated Financial Tracking**, providing users with a comprehensive hub for both professional and personal organization.

---

## 🚀 Key Features

### 📅 Advanced Task Management
- **Calendar Integration**: Precise date-wise task organization with an intuitive horizontal date picker.
- **Smart Reminders**: 
    - Real-time notifications triggered at task start.
    - **Pre-emptive Alerts**: Get notified 15 minutes before an event starts.
    - **Optional Alarms**: Configurable high-priority alarm sounds for critical tasks (via Settings).
- **Task Lifecycle**: Add, Edit, Reschedule, and Complete tasks with status tracking (Pending, In-Pipeline, Completed, Rescheduled).
- **Visual Analytics**: Dynamic bar graphs showing your task completion rate and productivity trends.

### 💰 Next-Level Financial Intelligence
- **Automated SMS Tracking**: Intelligently reads bank transaction SMS (HDFC, SBI, ICICI, etc.) to log expenses and investments automatically.
- **AI Categorization**: Automatically sorts spending into categories like *Food, Shopping, Bills, Travel, Health, and Entertainment* using merchant keyword detection.
- **Point-in-Time Bank Balances**: Maintains a historical record of your bank balances, allowing you to see your exact holdings on any past date.
- **Historical Inbox Sync**: One-tap "Sync All Past SMS" feature to populate your entire financial history from your existing inbox.
- **Gmail Integration**: Link your Google account to sync bank alerts directly from your email inbox.

### 🔒 Privacy & Security
- **Optional App Lock**: Secure your sensitive data with Biometric Authentication (Fingerprint / Face Unlock).
- **Local & Cloud Backup**:
    - **Cloud**: Secure backups to your personal Google Drive account.
    - **Local**: Export and Restore data using `.json` files saved directly to your device storage.
- **Clean Slate**: One-tap "Clean All Data" option with safety confirmation for full privacy management.

---

## 🛠 Tech Stack
- **Language**: Kotlin
- **Database**: Room Persistence Library (Version 9 with Unique Constraints)
- **Background Tasks**: WorkManager for reliable reminders and notifications.
- **Security**: Biometric API for hardware-level security.
- **UI/UX**: Modern Material Design 3 with custom vector assets and adaptive branding.
- **Networking**: Google Sign-In & Drive REST API integration.

---

## 📖 Usage Details

### 1. Initial Setup
Upon first launch, the app will request the following permissions:
- **SMS**: To automatically track bank transactions.
- **Notifications**: To deliver task reminders.
- **Biometrics**: If you choose to enable the App Lock in Settings.

### 2. Financial Tracking
- Go to the **Home** screen and tap **"Sync All Past SMS"** to instantly load your financial history.
- Use the **Analysis** tab to view your spending broken down by category, bank source, and date range.
- Link your **Email** from the Home dashboard to enable dual-source financial syncing.

### 3. Task Management
- Tap the **"+"** button on the Home or Calendar screen to add a new task.
- Set a **Start Time** to enable automatic reminders.
- Long-press any task to **Reschedule** it if your plans change.

### 4. Data Management
- Navigate to **Home** to perform backups.
- Use **"Cloud Backup"** for cross-device sync.
- Use **"Local Backup"** for offline data safety.

---

## 📦 Getting Started for Developers

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/ShreeTaskManager.git
   ```
2. **Open in Android Studio**: Choose *File > Open* and select the project folder.
3. **API Keys**: Ensure you replace `YOUR_WEB_CLIENT_ID` in `strings.xml` with your actual Google Cloud Console Client ID to enable Drive functionality.
4. **Build & Run**: Use a physical device with a SIM card to test the SMS tracking features.

---

## 📝 License
This project is open-source and available under the [MIT License](LICENSE).
