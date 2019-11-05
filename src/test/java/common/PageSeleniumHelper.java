package common;

import org.openqa.selenium.WebDriver;

public class PageSeleniumHelper {

	private WebDriver driver;

	public PageSeleniumHelper(WebDriver driver) {
		this.driver = driver;
	}

	public boolean SampleMethod() {
		return true;
	}

	// protected boolean isElementPresent(By by,WebDriver driver) {
	// try {
	// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	// return driver.findElement(by).isEnabled();
	// } catch (NoSuchElementException e) {
	// System.out.println("Element is missing "+e);
	// return false;
	// }
	// }

}
