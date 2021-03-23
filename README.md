# firebase_project

## Functionalties Implemented
1. Basic registration/login functionalities without password;
2. Users can send stickers to other registered users in the USERS tab by clicking one of the four predefined stickers;
3. Users can check the history of chats by scrolling up and down in the chat; (Currently the sticker size is fairly large which makes the scrolling a bit hard, pending fix)
4. Notifications would be sent for every message. It uses a "Tokens" Firebase real-time database to record the mapping between the userId to the client token that the account was registered, and it would send the notification with the token associated with the other user.
