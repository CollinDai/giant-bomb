## Description

This app provide allow you to search game by name.

## Features

Fast search: show maximum 3 game title while you type. Clicking the game title will take you to the game detail

Search: type and tap search button will search the games by the query. Maximum 25 result for best performance.

Search history: show a list of recent search history when the search box is active.

## Run the app

This app is built using:

* JDK 17
* kotlin 1.9.22
* target Android 34
* min Android 28

You'll also need to add the API key for Giant Bomb in your local.properties. Otherwise the built app will not be able to fetch the data because it is built using the default dummy api key.

```properties
GIANT_BOMB_API_KEY=<api_key>
```

To build the app in the command line, run

```shell
./gradlew app:installDebug
```

To run this project in Android Studio, make sure you have the Android Studio Jellyfish 2023.3.1 installed. This project is built with AGB 8.4.0-alpha11. See more compatibility instruction here: https://developer.android.com/studio/releases#android_gradle_plugin_and_android_studio_compatibility
