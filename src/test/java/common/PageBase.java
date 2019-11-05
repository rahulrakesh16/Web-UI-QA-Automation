package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageBase {

	public WebDriver driver;
	public PageSeleniumHelper helper;
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PageBase(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

}
