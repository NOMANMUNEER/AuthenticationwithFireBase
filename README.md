# Firebase Authentication Android Template

This is a starter template for a native Android application built with Java that demonstrates a complete, secure user authentication flow using Google's Firebase platform. It includes user sign-up, login, and a "gatekeeper" activity that protects routes from unauthenticated users.

This project was built to serve as a solid foundation for any Android app that requires user accounts.

***

## ✨ Features

* **User Sign-Up:** New users can create an account using their email and password.
* **User Login:** Existing users can securely log in to their accounts.
* **Firestore Integration:** Upon registration, user details (like name and phone) are saved to a corresponding document in the Cloud Firestore database.
* **Protected Routes:** A "gatekeeper" `MainActivity` checks the user's login status on app startup and directs them to either the `HomeActivity` (if logged in) or the `LoginActivity` (if not).
* **Clean UI:** Simple, clean, and functional XML layouts for all screens.

***

## 📱 Screenshots

It's highly recommended to add screenshots of your app here. Take screenshots of your Login, Sign Up, and Home screens and upload them to your GitHub repository. Then, replace the links below.

| Login Screen                                       | Sign-Up Screen                                         | Home Screen                                      |
| :------------------------------------------------: | :----------------------------------------------------: | :----------------------------------------------: |
| (https://github.com/NOMANMUNEER/AuthenticationwithFireBase/blob/main/Login.png)) | ![Sign Up Screen](URL_TO_YOUR_SIGNUP_SCREENSHOT.png) | ![Home Screen](URL_TO_YOUR_HOME_SCREENSHOT.png) |

***

## 🛠️ Tech Stack & Dependencies

* **Language:** Java
* **Platform:** Native Android (Android SDK)
* **Backend:** Firebase
    * **Firebase Authentication:** For managing user accounts.
    * **Cloud Firestore:** As the NoSQL database for storing user profile data.
* **UI:** Android XML Layouts

***

## 🚀 Getting Started

To get this project up and running on your own machine, follow these steps.

### Prerequisites

* Android Studio (latest version recommended)
* A Google Account to use Firebase

### Installation & Firebase Configuration

This project **will not run** out of the box until you connect it to your own Firebase project.

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/AuthenticationwithFireBase.git](https://github.com/your-username/AuthenticationwithFireBase.git)
    ```

2.  **Open in Android Studio:**
    * Open Android Studio and choose "Open an existing project."
    * Navigate to the cloned folder and open it.

3.  **Create your Firebase Project:**
    * Go to the [Firebase Console](https://console.firebase.google.com/).
    * Click **"Add project"** and give your project a name.

4.  **Connect the App to Firebase:**
    * Inside your new Firebase project, click the Android icon to **"Add an app"**.
    * **Android package name:** You must use the package name from this project. You can find it in your `app/build.gradle.kts` file, inside the `defaultConfig` block (e.g., `com.example.gidanbeachresorts`). Enter this exact name in the Firebase setup.
    * **Download `google-services.json`:** Firebase will prompt you to download a `google-services.json` configuration file.
    * **Add the file to your project:** In Android Studio, switch to the **Project** view (from the default **Android** view) and drag the downloaded `google-services.json` file into the `app/` directory. This is a critical step.

5.  **Enable Firebase Services:**
    * In the Firebase Console, go to the **Authentication** section.
    * Click the "Sign-in method" tab and **enable** the **Email/Password** provider.
    * Next, go to the **Firestore Database** section.
    * Click **"Create database"**, start in **production mode**, and choose a location.
    * Go to the **Rules** tab in Firestore and paste in the following rules to allow users to sign up and read content:
        ```rules
        rules_version = '2';

        service cloud.firestore {
          match /databases/{database}/documents {
            // Allows a user to create and update their own profile
            match /Users/{userId} {
              allow read, write: if request.auth != null && request.auth.uid == userId;
            }
          }
        }
        ```
    * Click **Publish**.

6.  **Run the App:**
    * Clean and rebuild your project in Android Studio.
    * Run the app on an emulator or a physical device. It should now be fully connected to your Firebase backend.
