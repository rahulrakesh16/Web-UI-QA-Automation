package common;

import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

@Listeners({ WebDriverSuiteListener.class, WebDriverEventListener.class })
public class TestBase {

	final Logger logger = LoggerFactory.getLogger(TestBase.class);
	public WebDriver driver;
	public PageSeleniumHelper helper;
	public Properties prop;

	public WebDriver getDriver() {
		return driver;
	}

	public TestBase() {
	}

	@BeforeClass
	public void beforeClass() {
		driver = LocalDriverManager.getDriver();// Gets a threadsafe instance of Webdriver
		// Load the properties file
		prop = new Properties();

		try {
			prop.load(TestBase.class.getResourceAsStream("/TestData.properties"));

		} catch (IOException e) {
			e.printStackTrace();
			logger.info("Unable to load TestData.Properties file");
			System.exit(0);
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.info("Unable to load TestData.Properties file. File null.");
			System.exit(0);

		}

	}

	public void main() {
	};

}
