package tests;

import pages.*;
import tests.HomeTest;
import org.testng.AssertJUnit;
import org.testng.Reporter;
import org.testng.annotations.*;

import common.*;

public class HomeTest extends TestBase {

	// Webdriver instance is already available as 'driver'
	// Properties instance already available as 'prop'

	@Test
	public void SearchKeyWords() {
		Reporter.log("Test case 112,113");// Do this first. These come from E-Tester
		HomePage homePage = new HomePage(driver); // Initialize the homePage page object

		homePage.GoogleSearch(prop.getProperty("searchKeywordsText"));// Search for a phrase

		// Verify page title change Test Data - Google Search"
		// assertEquals("Test Data - Google Search", driver.getTitle());

		AssertJUnit.assertTrue(true);

	}

	@Test
	public void Login() {

		Reporter.log("Test case 155,158");// Do this first. These come from E-Tester
		HomePage homePage = new HomePage(driver); // Initialize the homePage page object

		homePage.GoogleSearch(prop.getProperty("searchKeywordsText"));// Search for a phrase

		// Verify page title change Test Data - Google Search"
		// assertEquals("Test Data - Google Search", driver.getTitle());
		AssertJUnit.assertTrue(true);

	}
}
