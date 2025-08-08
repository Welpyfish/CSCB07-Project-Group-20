
Overview:
PATH is an Android application designed to support individuals in abusive or potentially unsafe relationships. It offers a private, secure space to create a personalized safety plan, based on a dynamic questionnaire, and prepare for different stages of leaving an abusive situation,  including while still in the relationship, during planning, or post-separation.
The app was developed with user privacy, accessibility, and usability in mind, offering support without drawing attention.

Core features:
Pin protected access via encrypted app preferences
Dynamic questionnaire based on users answers
Tips and advice based on the answers provided
Emergency exit button that closes the app and removes it from recents after redirection to amazon.com
User emergency information storage
Technologies used:
Language: Java
Login MVP
Firebase realtime database
Encrypted shared preferences
Android XML layouts for UI
JUnit &Mockito for testing

Prerequisites:
Android studio
Java 17 (mandatory for gradle)
Firebase project with authentication and Realtime Database Enabled

Installation
Clone repository: https://github.com/Welpyfish/CSCB07-Project-Group-20.git 
Open in android studio
Sync gradle
Add your google-services.json to the /app directory
Build and run on emulator or physical device
Testing:
The testing was done using JUnit and Mockito API to mock the view and the model, isolating the sign in and sign up presenters (focus of testing).

Packages:
Authorization package
app/src/main/java/com/group20/Authorization package includes the login features including pin setup, verification, etc. As well as fire base login and google redirection for signin.
EmergencyInfo package
app/src/main/java/com/group20/EmergencyInfo package includes the activities that deal with the emergency documents, contacts, etc.
question package
app/src/main/java/com/group20/question package includes the necessary logic and ui for the dynamic questionnaire and the connection to the firebase db.
group20 package
app/src/main/java/com/group20 package includes the miscellaneous activities that deal with eula, exit button, settings page, etc.
Unitest (mockito)
app/src/test/java/com/group20/cscb07project package includes the unitest for the sign in presenter and the sign up presenter class.

Known Issues / Limitations
Resources are currently limited to five Canadian cities
No offline mode
Only supports English
Google Sign in is pending for proper implementation
Settings page backend is still pending
App might be unstable under high load (emulator)

Future Implementations
	Pin update, delete, edit.
	Google sign in.
	Support services connections.
	Increased number of cities available.
