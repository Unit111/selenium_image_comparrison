import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        final String url = "https://www.gog.com/";
        final String screenShotDir = System.getProperty("user.dir") + "/conf/";
        //Create the WebDriver instance
        WebDriver driver = getFirefoxDriver();

        //Open the website
        driver.get(url);

        //Take the first screenShot
        File expectedImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        //Open the website again
        driver.get(url);

        //Take the second screenShot
        File actualImage = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        //Copy the result file to the conf dir
        try {
            FileUtils.copyFile(expectedImage, new File(screenShotDir + "expectedImage.png"));
            FileUtils.copyFile(actualImage, new File(screenShotDir + "actualImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Compare the two images captured above.
        File fileA = new File(screenShotDir + "expectedImage.PNG");
        File fileB = new File(screenShotDir + "actualImage.png");
        System.out.println("Image match: " + compareImage(fileA, fileB) + "%");

        //Quit the driver
        driver.quit();
    }

    private static float compareImage(final File fileA, final File fileB) {
        float percentage = 0;
        try {
            //take buffer data from both image files //
            final BufferedImage bufferedImageA = ImageIO.read(fileA);
            final DataBuffer dataBufferA = bufferedImageA.getData().getDataBuffer();
            final int sizeA = dataBufferA.getSize();
            final BufferedImage bufferedImageB = ImageIO.read(fileB);
            final DataBuffer dataBufferB = bufferedImageB.getData().getDataBuffer();
            int count = 0;

            //compare data-buffer objects //
            for (int i = 0; i < sizeA; i++) {

                if (dataBufferA.getElem(i) == dataBufferB.getElem(i)) {
                    count = count + 1;
                }
            }
            percentage = (count * 100) / sizeA;

        } catch (Exception e) {
            System.out.println("Cannot compare images");
        }

        return percentage;
    }

    private static WebDriver getFirefoxDriver() {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        setGeckoDriver();
        final WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    private static void setGeckoDriver() {
        String pathToDriver = System.getProperty("user.dir") + "/conf/geckodriver.exe";
        final File file = new File(pathToDriver);
        System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
    }
}
