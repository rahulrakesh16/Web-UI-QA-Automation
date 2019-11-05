package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import common.*;

public class HomePage extends PageBase {

	@FindBy(how = How.NAME, using = "q")
	private WebElement txtSearchTerm;

	@FindBy(how = How.ID, using = "gbqfq")
	private WebElement btnGoogleSearch;

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public void GoogleSearch(String strSearchTerm) {
		txtSearchTerm.sendKeys(strSearchTerm);
		btnGoogleSearch.click();
	}

	public void SignIn() {

	}

}
