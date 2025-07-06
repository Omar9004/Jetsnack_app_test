1. What purpose does the test serve? Why are you creating it?

The primary purpose of these UI tests in Jetpack Compose is to:
    1.Verify that the user interface behaves correctly and meets functional requirements.
    2.Catch potential bugs or UI crashes early in the development process.
    3.Ensure that UI components and user flows work seamlessly together without unexpected behavior.
Overall, these tests help maintain app quality, reduce manual testing, and speed up development cycles.

2. What different test suites are appropriate and what are their responsibilities?

I've implemented several test cases that fall into specific test suites, each with distinct responsibilities:
    1.Navigation Tests: Validate that users can move between screens (e.g., Home to Search) via UI interactions.
    2.Scrolling and List Interaction Tests: Ensure that scrollable components like LazyColumn and LazyRow function correctly, and that items can be reached and displayed.
    3.Search and Filtering Tests: Confirm that the search bar accepts input and returns accurate, clickable results.

3. How can you make a structure for future tests for Jetsnack that is sustainable?

A sustainable strategy is to mirror the app's feature structure in the test suite. For example:
The screen in Home.kt should have a corresponding test class, like HomeScreenTest.kt.
Likewise, Cart.kt could be paired with CartScreenTest.kt, and so on.
Additionally:
Use Modifier.testTag() to assign stable identifiers to UI components and avoids fragile reliance on text or dynamic content.

4. How do you deal with flakiness in tests? What countermeasures can we take when creating our test structure?

During implementation, I encountered flakiness when navigating between screens (e.g., from Home to Search).
Sometimes, the test would fail due to the UI not being fully rendered yet.
To solve this, I used composeTestRule.waitForIdle() frequently to ensure the UI was ready before running assertions or interactions.


