import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Unit 1 on 26-Oct-17.
 */
class MarionetteDriver {

    WebDriver getFirefoxDriver() {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        setGeckoDriver();
        final WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    private void setGeckoDriver() {
        String pathToDriver = null;

        final String osName = System.getProperty("os.name").toLowerCase();

        if (osName.toLowerCase().contains("windows")) {
            pathToDriver = System.getProperty("user.dir") + "/conf/geckodriver.exe";
        }
        final File file = new File(pathToDriver);
        System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
    }
}