# Jetsnack_app_test

I started by reviewing the example AppTest class provided in the repository to understand the fundamentals of how Jetpack Compose testing works.

Then, I created my own test class, MyAppTest, to implement the requirements of the assignment.
For better organization and simplicity, I divided the UI test functionality into two main test functions:

1. Scrolles() responsible for handling scroll gestures, both vertical and horizontal.

2. Searching() responsible for testing the search functionality in the app.

To implement these tests effectively, I thoroughly explored the app's source code to understand its structure and how its components are connected.
This deep understanding significantly helped me during test development.

To give each test function access to specific UI components such as LazyColumn, LazyRow, etc., I relied heavily on tagging them using Modifier.testTag().

Finally, I ensured to document the code extensively to make it clear, maintainable, and easy to follow.

OBS: MytestAPP class exists in app/src/andriodTest/java/com/example/jetsnack/
