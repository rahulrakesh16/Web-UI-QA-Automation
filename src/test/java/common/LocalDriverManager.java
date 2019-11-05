package common;

import org.openqa.selenium.WebDriver;

/**
 * LocalDriverManager creates a threadsafe local copy of the WebDriver in use. 
 * Objects making use of WebDriver get it from LocalDriverManager
 * and return it (set) after use (unless terminating the driver instance entirely)
 * @param
 * @return
 */
public class LocalDriverManager {

	private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {
		return webDriver.get();
	}

	static void setWebDriver(WebDriver driver) {
		webDriver.set(driver);
	}
}