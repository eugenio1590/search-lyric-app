# Search Lyric App
***
A basic demo of the android app to search song lyrics and store them on the smartphone for offline support.
***
## Getting Started
This project uses the Gradle build system. To build this project, use the `gradlew` build command or use "Import Project" in Android Studio.
***
## Structure
The app was build following the principles of a [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).
There are two modules that separate the domain classes from the presentation layer. This could be an exportable library for another environments.

The presentation layer use the [Android Component Architecture](https://developer.android.com/topic/libraries/architecture) for keeping the UI state.
It was necessary to use the design pattern [MVPVM](https://docs.microsoft.com/en-us/archive/msdn-magazine/2011/december/mvpvm-design-pattern-the-model-view-presenter-viewmodel-design-pattern-for-wpf) to manipulate the UI state stored in the ViewModels.

### Tests
Some unit tests are included for validating the business logic.
The framework to use is [Kotest](https://kotest.io/docs/framework/framework.html).
Android Studio have a [plugin](https://kotest.io/docs/intellij/intellij-plugin.html) to run them.