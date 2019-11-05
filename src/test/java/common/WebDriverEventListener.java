package common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverEventListener implements ITestListener {

	final Logger logger = LoggerFactory.getLogger(WebDriverEventListener.class);

	@Override
	public void onTestStart(ITestResult result) {

		logger.info("Test Started: " + result.getMethod().getMethodName().toString());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		logger.info("Test Passed: " + result.getMethod().getMethodName().toString());

	}

	@Override
	public void onTestFailure(ITestResult result) {

		logger.info("Test Failed: " + result.getMethod().getMethodName().toString());

		screenShot(result);

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		logger.info("Test Skipped: " + result.getMethod().getMethodName().toString());

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		logger.info("Test Failed: " + result.getMethod().getMethodName().toString());
		// Take snapshot

	}

	@Override
	public void onStart(ITestContext context) {

		logger.info("Test Class Started: " + context.getClass().getName().toString());

	}

	@Override
	public void onFinish(ITestContext context) {

		logger.info("Test Class Finished: " + context.getClass().getName().toString());

	}

	public void screenShot(ITestResult result) {

		// get a reference to the test class in use
		Object currentClass = result.getInstance();
		// Get driver instance singleton from base class underneath the test class
		if (((TestBase) currentClass).getDriver() != null) {
			WebDriver driver = ((TestBase) currentClass).getDriver();

			if (driver.getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
				// if remoteDriver, need to augment the class with screenshot functionality
				driver = new Augmenter().augment(driver);
			} // if RemoteWebDriver

			File fileScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			try {

				// Save file to reporting directories
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
				String date = sdf.format(new Date());

				// File fileTestOutputCopy = new File("test-output/ScreenShot-" + date + "-" +
				// currentClass.getClass().getName() + "." + result.getMethod().getMethodName()
				// + ".png");
				File fileSurefireCopy = new File("surefire-reports/html/ScreenShot-" + date + "-"
						+ currentClass.getClass().getName() + "." + result.getMethod().getMethodName() + ".png");
				// FileUtils.copyFile(fileScreenShot, fileTestOutputCopy);
				FileUtils.copyFile(fileScreenShot, fileSurefireCopy);

				String reportLink = "<a href=\"ScreenShot-" + date + "-" + currentClass.getClass().getName() + "."
						+ result.getMethod().getMethodName() + ".png\">Screen Shot - "
						+ currentClass.getClass().getName() + "</a>";

				Reporter.log("<br/>" + reportLink, true);

			} catch (IOException e) {
				e.printStackTrace();
				Reporter.log("error generating screenshot for " + currentClass.getClass().getName());
			}

		} 
	}
}
