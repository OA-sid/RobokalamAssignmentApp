App Features
1. Splash Screen

Features the Robokalam logo and app name
Automatically determines where to navigate based on login status

2. Login Screen

Email-based authentication with local storage using SharedPreferences
Form validation to ensure valid email format
Stores login status for automatic sign-in on subsequent app launches

3. Dashboard/Home Screen

Personalized welcome message with the student's name
Quote of the Day section that fetches from the ZenQuotes API
Navigation cards to access Portfolio and About sections

4. Portfolio Section

Complete CRUD functionality for portfolio entries
Stores user information: Name, College, Skills, and Project details
Persistent storage using Room Database
Clean UI with card-based design for better readability

5. Quote of the Day

Integrates with the ZenQuotes.io API
Caches quotes to minimize API calls
Includes fallback quotes in case of API failure

6. About Robokalam Section

Static page with information about Robokalam
Company description with logo

Technical Implementation
The app is built using modern Android development practices:

Kotlin as the programming language
Jetpack Compose for UI development
Room Database for local storage of portfolio entries
SharedPreferences for managing login status
Retrofit for API integration
Coroutines for asynchronous operations
Navigation Compose for screen navigation
