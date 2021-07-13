package TestPackage;

import java.io.IOException;

import org.testng.annotations.Test;

public class TestNgClass extends PocTest {

	@Test(priority = 0)

	public void Login() throws IOException {
		PocTest.LoginToBugzilla();
	}

	@Test(priority = 1)
	public void CreatNewBug() throws IOException {
		PocTest.CreatingBug();
	}

	@Test(priority = 2)
	public void ScreenShot() throws IOException {
		PocTest.TakingScreenShot();

	}

	@Test(priority = 3)
	public void Email() throws IOException {
		PocTest.SendingEmail();

	}
}
