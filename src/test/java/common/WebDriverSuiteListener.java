package common;

import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class WebDriverSuiteListener implements ISuiteListener {

	public WebDriver driver;

	String userName = "userName";
	String key = "key";
	String os = "WINDOWS";
	String browser = "firefox";
	String browserVersion = null;
	Boolean blnUseSauce = false;
	String baseUrl = "http://google.com/";
	String gridUrl = "http://localhost:4444/wd/hub";

	Process gridShellProcess;
	Process gridNodeProcess;

	final Logger logger = LoggerFactory.getLogger(WebDriverSuiteListener.class);

	public void onStart(ISuite suite) {

		logger.info("TestNG Suite" + suite.getName() + " Started");

		logger.info("Loading test parameters from Maven");
		setParameters(suite);// Load suite parameters

		// Choose the browser, version, and platform to test
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName(browser);
		capabilities.setCapability("version", browserVersion);
		capabilities.setCapability("platform", Platform.valueOf(os));

		// // Method and Description - static DesiredCapabilities internetExplorer()
		// DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		//
		// //Method and Description - void setCapability(java.lang.String
		// capabilityName, boolean value)
		// capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
		// true);
		//
		// //Among the facilities provided by the System class are standard input,
		// standard output, and error output streams; access to externally defined
		// "properties"; a means of loading files and libraries; and a utility method
		// for quickly copying a portion of an array.
		// System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
		//
		// //InternetExplorerDriver(Capabilities capabilities)
		// WebDriver driver = new InternetExplorerDriver(capabilities);

		// org.uncommons.reportng.title
		System.setProperty("org.uncommons.reportng.title", baseUrl);

		if (blnUseSauce) { // Create the connection to Sauce Labs to run the
			// tests
			try {

				driver = new RemoteWebDriver(
						new URL("http://" + userName + ":" + key + "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
				logger.info("Current Test URL: " + baseUrl);
				LocalDriverManager.setWebDriver(driver);// make it threadsafe

			} catch (MalformedURLException e) {
				e.printStackTrace();
				// logger.error(e.)
			}
			// capabilities.setCapability("name", method.getName()); //Commented out during
			// dev, may need to reinstate
		} else {
			// Create the connection to LQDT Selenium Grid Server
			// http://10.65.85.172:4444/grid/console
			try {

				StartWebGrid();

				this.driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
				logger.info("Selenium Grid Test");
				logger.info("Current Test URL: " + baseUrl);
				logger.info("Browser: " + capabilities.getBrowserName() + " " + capabilities.getVersion());
				LocalDriverManager.setWebDriver(driver);// make it threadsafe

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		driver.get(baseUrl);// launch the browser
		driver.manage().window().maximize();// maximize

	}

	public void onFinish(ISuite suite) {
		driver = LocalDriverManager.getDriver();// Gets a threadsafe instance
		driver.close();
		driver.quit();
		try {
			EndWebGrid();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("TestNG Suite completed");
	}

	private void setParameters(ISuite suite) {
		logger.info("SetParameters Called");

		if (System.getProperty("userName") != null) {

			userName = System.getProperty("userName");
		}

		if (System.getProperty("key") != null) {
			key = System.getProperty("key");
		}

		if (System.getProperty("os") != null) {
			os = System.getProperty("os");
			logger.info("OS PARAMETER FOUND: " + os);
		}

		if (System.getProperty("browser") != null) {
			browser = System.getProperty("browser");
			logger.info("BROWSER PARAMETER FOUND: " + browser);
		}

		if (System.getProperty("browserVersion") != null) {
			browserVersion = System.getProperty("browserVersion");
			logger.info("BROWSER VERSION PARAMETER FOUND: " + browserVersion);
		}

		if (System.getProperty("blnUseSauce") != null) {
			blnUseSauce = Boolean.valueOf(System.getProperty("blnUseSauce"));
			logger.info("USESAUCE PARAMETER FOUND: " + blnUseSauce);
		}

		if (System.getProperty("baseUrl") != null) {
			baseUrl = System.getProperty("baseUrl");
			logger.info("BASEURL PARAMETER FOUND: " + baseUrl);
		}

		if (System.getProperty("gridUrl") != null) {
			gridUrl = System.getProperty("gridUrl");
			logger.info("GRIDURL PARAMETER FOUND: " + gridUrl);
		}

	}

	// Start WebGrid
	public void StartWebGrid() throws IOException, InterruptedException {
		// set the location of the IE Webdriver if used
		// System.setProperty("webdriver.ie.driver","src/test/resources/IE32/IEDriverServer.exe");

		File file = new File("src/test/resources/IE32/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

		// Start WebGrid
		logger.info("Starting Selenium Grid Server");

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("java", "-jar", "src/test/resources/selenium-server-standalone-2.39.0.jar", "-role",
				"hub");

		gridShellProcess = processBuilder.start();
		logger.info("Selenium Grid Server Started");

		logger.info("Wait 10 Seconds for grid to initialize");
		Wait(10);
		logger.info("Wait completed");
		logger.info("Selenium Grid Server browseable at http://localhost:4444/grid/console");
		logger.info("Use http://localhost:4444/lifecycle-manager?action=shutdown to force grid shutdown");

		logger.info("Starting Selenium Grid Node");
		// Start the node and attach to grid

		processBuilder.command("java", "-jar", "src/test/resources/selenium-server-standalone-2.39.0.jar", "-role",
				"node", "-hub", "http://localhost:4444/grid/register");

		// java -jar selenium-server-standalone-2.25.0.jar -role webdriver
		// -browser "browserName=internet explorer,version=8,maxinstance=1,pl
		// atform=WINDOWS" -hubHost localhost –port 8989

		gridNodeProcess = processBuilder.start();
		logger.info("Selenium Grid Grid Node Started");
		logger.info("Wait5 Seconds for node to initialize");
		Wait(5);
		logger.info("Wait completed");
	}

	public void EndWebGrid() throws InterruptedException {
		gridNodeProcess.destroy();
		logger.info("Wait 5 Seconds for Grid Node to destroy");
		Wait(5);
		logger.info("Selenium Grid Node Destroyed");
		logger.info("Wait 5 Seconds for Grid Shell to destroy");
		Wait(5);
		gridShellProcess.destroy();
		logger.info("Selenium Grid Shell Destroyed");
	}

	public void Wait(Integer intSeconds) {
		Thread thread = new Thread();
		try {
			synchronized (thread) {
				thread.wait(intSeconds * 1000);
			}
		} catch (InterruptedException e) {
		}
	}
}