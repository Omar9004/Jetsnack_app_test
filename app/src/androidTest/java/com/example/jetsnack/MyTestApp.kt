package com.example.jetsnack
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import com.example.jetsnack.model.CollectionType
import com.example.jetsnack.model.SnackRepo
import com.example.jetsnack.ui.MainActivity
import org.junit.Rule
import org.junit.Test


class MyTestApp {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    //Scrolling part
    @Test
    fun scrolls(){
        composeTestRule.onNodeWithText("HOME").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android's picks").assertIsDisplayed()

        // Scrolling vertically
        // Get the content of the column from LazyColumn exists within SnackCollectionList() in the Feed.kt
        val sectionColumns = composeTestRule.onNodeWithTag("SectionColumns", useUnmergedTree = true)
        val targetedSection = "Newly Added"
        sectionColumns.performScrollToNode(hasText(targetedSection))
        composeTestRule.onNodeWithText(targetedSection).assertIsDisplayed()

        // Scrolling horizontally
        // 1. Get the SnackCollection (or list) for "Newly Added" section to find its ID and determine its type.
        val newlyAddedColl = SnackRepo.getSnacks().firstOrNull(){it.name == targetedSection}
        if(newlyAddedColl == null){
            throw AssertionError("SnackCollection 'Newly Added' not found in Snackrepo collection")
        }
        // Get the snacks list that has previously assigned to "Newly Added" section
        val newlyAddedCollSnacks = newlyAddedColl.snacks

        // 2. We have to determine the correct LazyRow tag based on the CollectionType either normal, or highlighted
        val hLazyRowTag: String //horizontal LazyRow Tag
        if(newlyAddedColl.type == CollectionType.Highlight){
            hLazyRowTag = "HHSnackList"
        }else{
            hLazyRowTag = "SHSnackList"
        }
        // Bring the list of snacks from the chosen LazyRow exits either on HighlightedSnacks() or Snack() from Snacks.kt
        val newlyAddedLazyRow = composeTestRule.onNodeWithTag(hLazyRowTag, useUnmergedTree = true)
        newlyAddedLazyRow.assertExists("Horizontal LazyRow for 'Newly Added' section not found with tag:$hLazyRowTag")

        //3. Scrolling to through "Newly Added" horizontally back and forth
        // Scroll to the last Snack item/index
        val lastSnackIndex = newlyAddedCollSnacks.size-1
        val lastSnackName = newlyAddedCollSnacks.last().name
        newlyAddedLazyRow.performScrollToIndex(lastSnackIndex)
        composeTestRule.onNodeWithText(lastSnackName).assertIsDisplayed()

        //Scroll to back to the first Snack item/index
        val firstSnackName = newlyAddedCollSnacks.first().name
        newlyAddedLazyRow.performScrollToIndex(0)
        composeTestRule.onNodeWithText(firstSnackName).assertIsDisplayed()
    }


    // Search part
    @Test
    fun searchP(){
        // Click on Search tab from the below bar then clicking on search bar
        composeTestRule.onNodeWithText("SEARCH").performClick().assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Jetsnack").performClick()
        composeTestRule.waitForIdle() //Wait until the Search screen becomes visible

        // Tag the Composable part responsible for text on Search Bar using testTag inside the BasicTextField component within SearchBar()
        val searchBar = composeTestRule.onNodeWithTag("searchBar")
        val searchedText = "Mango"
        searchBar.performTextInput(searchedText) // Type mango within the search bar
        composeTestRule.waitForIdle()//Wait until the Search result

        //Click on the found product from the search result
        composeTestRule.onNode(hasText(searchedText) and hasText("A tag line")).performClick()
        composeTestRule.waitForIdle()
        //Random check on the Snack/item (Mango) screen to make sure that description has showed up correctly.
        composeTestRule.onNodeWithText( "Ingredients" ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Lorem ipsum", substring = true).assertIsDisplayed()

    }
}


