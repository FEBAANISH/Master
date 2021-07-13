package TestPackage;

import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.sql.Timestamp;

public class PocTest {

	public static String baseUrl = "http://bugzilla.thinkpalm.info";
	String driverPath = "D:\\SeleniumTraining\\CHROME DRIVER\\chromedriver.exe";
	public static WebDriver driver = null;
	public static String First = null;
	public static String randomString = null;

	@BeforeTest

	public void BrowserDetails() {
		System.setProperty("webdriver.chrome.driver", driverPath);
		driver = new ChromeDriver();
		Reporter.log("=====OPEN BROWSER=====");

	}

	// *************Login to bugzilla***************

	public static void LoginToBugzilla() throws IOException {

		Reporter.log("TESTCASE 1 :LOGIN TO BUGZILLA");

		driver.get(baseUrl);

		driver.manage().window().maximize();

		driver.findElement(By.xpath("//*[@href='http://bugzilla.thinkpalm.info/index.cgi?GoAheadAndLogIn=1']")).click();
		driver.findElement(By.id("Bugzilla_login_top")).sendKeys("feba.s@thinkpalm.com");
		driver.findElement(By.id("Bugzilla_password_top")).sendKeys("Admin@123");
		driver.findElement(By.xpath("//*[@value='Log in']")).click();

		String title = driver.getTitle();

		assertEquals("Bugzilla Main Page", title);

		Reporter.log("RESULT:" + "Successfully logged in bugzilla" + "\r\n");

	}

	// ***************CREATING BUG IN BUGZILLA********************

	public static void CreatingBug() throws IOException {

		Reporter.log("TESTCASE 2:CREATING NEW BUG");

// NEW BUTTON
		driver.findElement(By.xpath("//*[@id=\"header\"]/ul/li[2]/a")).click();

		driver.findElement(By.xpath("//*[@id='bugzilla-body']/table/tbody/tr[53]/th/a[contains(text(),'test')]"))
				.click();
		driver.findElement(By.xpath("//*[@id=\"v45_component\"]")).click();

		Select versionDropDown = new Select(driver.findElement(By.name("version")));
		versionDropDown.selectByValue("1.0.1.0");

		Select SeverityDropDown = new Select(driver.findElement(By.name("bug_severity")));
		SeverityDropDown.selectByValue("Blocker");

		Select rep_platformDropDown = new Select(driver.findElement(By.name("rep_platform")));
		rep_platformDropDown.selectByValue("Other");

		Select op_sysDropDown = new Select(driver.findElement(By.name("op_sys")));
		op_sysDropDown.selectByValue("Other");

		Select cf_bug_typeDropDown = new Select(driver.findElement(By.name("cf_bug_type")));
		cf_bug_typeDropDown.selectByValue("UI");

		Select cf_classificationDropDown = new Select(driver.findElement(By.name("cf_classification")));
		cf_classificationDropDown.selectByValue("Validation");

		driver.findElement(By.xpath("//*[@id=\"expert_fields_controller\"]")).click();// show advanced
		driver.findElement(By.name("assigned_to")).clear();
		driver.findElement(By.name("assigned_to")).sendKeys("feba.s@thinkpalm.com");
		driver.findElement(By.id("short_desc")).sendKeys("Bug created for selenium automation POC");

		String myText = "STEPS TO REPRODUCE/n*login to bugzilla/n*Create new bug/n*Select severity/n*Add Attachment/n*Submit/n*After creating bug Successfully, take a screenshot/n*Send email with bug id";
		myText = myText.replace("/n", Keys.chord(Keys.SHIFT, Keys.ENTER));
		driver.findElement(By.id("comment")).sendKeys(myText);

		driver.findElement(By.xpath("//*[@id=\"attachment_false\"]/input")).click();
		driver.findElement(By.xpath("//*[@id=\"data\"]"))
				.sendKeys("C:\\Users\\feba.s\\Pictures\\Saved Pictures/images.jpg");

		driver.findElement(By.name("description")).sendKeys("Screen attached");
		driver.findElement(By.id("commit")).click();

		WebElement BugIdText = driver.findElement(By.xpath("//*[@id=\"changeform\"]/div[1]/a"));
		String bug = BugIdText.getText();

		String[] splited = bug.split(" ");

		First = splited[1];

		Reporter.log("RESULT:" + "" + "Successfully created bug in Bugzilla" + "\r\n" + "The bug id is:" + "" + First
				+ "\r\n");

	}

	// ************TAKING SCREENSHOT*************************

	public static void TakingScreenShot() throws IOException {

		Reporter.log("TESTCASE 3:TAKING SCREENSHOT");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Timestamp ts = new Timestamp(new Date().getTime());
		randomString = "Capture_" + ts + ".png";
		randomString = randomString.replaceAll(":", "-");

		FileUtils.copyFile(scrFile, new File("D:\\ScreenShot\\", randomString));

		Reporter.log(
				"RESULT:" + "" + "Successfully took screeshot" + "\r\n" + "File name is:" + "" + randomString + "\r\n");

	}

	// ***************SENDING EMAIL*****************************************

	public static void SendingEmail() throws IOException {

		{

			Reporter.log("TESTCASE 4:TO SEND EMAIL BY READING CREDENTIALS FROM EXCEL");

			File src = new File("D:\\Credencials.xlsx");
			FileInputStream finput = new FileInputStream(src);
			XSSFWorkbook wb = new XSSFWorkbook(finput);
			XSSFSheet sh1 = wb.getSheetAt(0);
			String Username = (sh1.getRow(1).getCell(0).getStringCellValue());
			String Password = (sh1.getRow(1).getCell(1).getStringCellValue());

			Properties props = new Properties();
			props.put("mail.smtp.host", "Outlook.office365.com");
			// props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "25");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "25");
			props.put("mail.smtp.starttls.enable", true);

			Session session = Session.getDefaultInstance(props,

					new javax.mail.Authenticator() {

						protected PasswordAuthentication getPasswordAuthentication() {

							return new PasswordAuthentication(Username, Password);

						}

					});

			try {
				Message message = new MimeMessage(session);

				message.setFrom(new InternetAddress("feba.s@thinkpalm.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sajan.j@thinkpalm.com"));
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("vinup.t@thinkpalm.com"));
				message.setSubject("TRAINING POC");
				BodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setText("Hi Sajan," + "\r\n" + "\r\n" + "Successfully created bug in bugzilla."
						+ "\r\n" + "\r\n" + "THE BUG ID IS :" + " " + First + " " + "\r\n" + "\r\n"
						+ "***The screenshot of bug is attached in this mail***" + "\r\n" + "\r\n"
						+ "THIS E-MAIL IS GENERATED BY SELENIUM." + "\r\n" + "\r\n" + "Regards," + " " + "\r\n"
						+ "Feba.S");
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				String filename = "D:\\ScreenShot\\" + randomString;
				DataSource source = new FileDataSource(filename);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart2);
				multipart.addBodyPart(messageBodyPart1);
				message.setContent(multipart);
				Transport.send(message);
				Reporter.log("RESULT:" + "" + "Email Sent Successfully" + "\r\n");

			} catch (MessagingException e) {

				throw new RuntimeException(e);

			}

		}
	}

	@AfterTest

	public void CloseBrowser() {
		Reporter.log("=====BROWSER CLOSED SUCCESSFULLY=====");
		driver.close();
	}

}
