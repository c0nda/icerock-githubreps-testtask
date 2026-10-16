# GitHubRepsIceRock

An Android app for browsing GitHub repositories.

## Features

- Sign in with a GitHub personal access token
- List of your repositories (forks are filtered out)
- Repository details screen with README rendering (Markdown)

## Tech stack

- Kotlin, Jetpack Compose
- Ktor Client (OkHttp) + Kotlin Serialization for the GitHub REST API
- Koin for dependency injection
- MVVM + clean architecture
- Navigation Compose

## Getting started

1. Clone the repository:

   ```bash
   git clone <repository-url>
   ```

2. Open the project in Android Studio and let Gradle sync.

3. Create a GitHub personal access token (Settings → Developer settings → Personal access tokens) with access to your repositories.

4. Run the `app` configuration on a device or emulator.

5. On the auth screen, paste your token to sign in.
