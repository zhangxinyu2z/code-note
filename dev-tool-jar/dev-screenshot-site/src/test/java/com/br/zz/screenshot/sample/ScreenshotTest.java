package com.br.zz.screenshot.sample;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xinyu.zhang
 * @since 2022/11/14 16:41
 */
public class ScreenshotTest {

    @Test
    public void tt() {
        // TODO Auto-generated method stub
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "D:/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        //设置浏览器参数
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.setHeadless(true);

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

        //        driver.manage().window().maximize();
        driver.manage().window().maximize();

        driver.get("https://www.dianhun.cn/");              //take screenshot of the entire page
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
        //        Screenshot screenshot = new AShot().takeScreenshot(driver);
        try {
            ImageIO.write(screenshot.getImage(), "jpg", new File("D:/test.jpg"));//截图存放路径
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        driver.quit();

    }

    @Test
    public void t2() {
        //这里设置下载的驱动路径，Windows对应chromedriver.exe Linux对应chromedriver，具体路径看你把驱动放在哪
        System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //截屏支持
        options.setCapability("takesScreenshot", true);
        //css搜索支持
        options.setCapability("cssSelectorsEnabled", true);
        //设置浏览器参数
        options.addArguments("--headless"); // 浏览器不弹窗
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(options);
        //设置超时，避免有些内容加载过慢导致截不到图
        driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MINUTES);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
        driver.manage().timeouts().setScriptTimeout(1, TimeUnit.MINUTES);
        try {
            //设置需要访问的地址
            driver.get("https://www.dianhun.cn/");
            //获取高度和宽度一定要在设置URL之后，不然会导致获取不到页面真实的宽高，而是弹窗的高和宽
            Long width = (Long)driver.executeScript("return document.documentElement.scrollWidth");
            Long height = (Long)driver.executeScript("return document.documentElement.scrollWidth");
            System.out.println("高度：" + height + "宽度：" + width);
            //这里需要模拟滑动，有些是滑动的时候才加在的
            long temp_height = 0;
            while (true) {
                //每次滚动500个像素，因为懒加载所以每次等待2S 具体时间可以根据具体业务场景去设置
                Thread.sleep(2000);
                driver.executeScript("window.scrollBy(0,500)");
                temp_height += 500;
                if (temp_height >= height) {
                    break;
                }
            }
            //设置窗口宽高，设置后才能截全
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            //设置截图文件保存的路
            // 径
            String screenshotPath = "D:\\images\\imgGG.png";
            File srcFile = driver.getScreenshotAs(OutputType.FILE);
            System.out.println(srcFile.getName());
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        } catch (Exception e) {
            throw new RuntimeException("截图失败", e);
        } finally {
            driver.quit();
        }
    }

    @Test
    @Deprecated
    public void t3() {
        String url = "https://www.al-ghurair.com/en/";
        String IMGSAVEPATH = "d:\\images\\";

        /*** 滚动截图--有重叠bug* * @param url 访问url地址* @param IMGSAVEPATH 存储路径* @return */
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "d:\\chromedriver.exe");
        driver = new ChromeDriver();
        String files = "";
        try {
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

            driver.manage().window().maximize();
            driver.get(url);
            // 等待页面加载完成
            new WebDriverWait(driver, 30).until(drivers -> ((JavascriptExecutor)drivers).executeScript("return document.readyState").equals("complete"));
            JavascriptExecutor jexec = (JavascriptExecutor)driver;//获取指定元素总宽度

            String string = jexec.executeScript("return document.body.scrollWidth").toString();
            int width = Integer.valueOf(string);
            System.out.println(width);//获取指定元素可见高度为滚动距离
            String innerHeights = jexec.executeScript("return document.getElementsByClassName(\"outside\")[0].clientHeight").toString();
            int innerHeight = 300;//自定义每次滚动距离
            // 浏览器指定元素的总高度
            String string1 = jexec.executeScript("return document.getElementsByClassName(\"outside\")[0].scrollHeight").toString();
            int height = Integer.valueOf(string1);
            System.out.println(height);
            // 设置浏览窗口大小
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(600, height));
            // 滚动次数
            int frequency = height % innerHeight == 0 ? height / innerHeight : (height / innerHeight);
            System.out.println(frequency);
            for (int i = 0; i < frequency; i++) {
                int length = i * innerHeight;

                if (i + 1 == frequency) {
                    length = (i * innerHeight) + (height % innerHeight);
                }
                System.out.println(length);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {// TODO Auto-generated catch blocke.printStackTrace();
                    //
                }
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(0," + length + ")");
                ((JavascriptExecutor)driver).executeScript("document.getElementsByClassName(\"outside\")[0].scrollTo(0," + length + ")");

                //截图
                Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver);
                BufferedImage image = screenshot.getImage();
                ImageIO.write(image, "PNG", new File(IMGSAVEPATH + "test" + i + ".png"));
                if (i == 0)
                    files = files + IMGSAVEPATH + "test" + i + ".png";
                else
                    files = files + "," + IMGSAVEPATH + "test" + i + ".png";
                //设置浏览窗口大小
                driver.manage().window().setSize(new Dimension(1440, height));
                // 重新拉到页面顶端
                ((JavascriptExecutor)driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
            }
        } catch (IOException e) {

        }
    }

    @Test
    public void tx() {
        try {
            URL url = new URL("http://www.baidu.com");
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String data = br.readLine();
            while (data != null) {
                System.out.println(data);
                data = br.readLine();
            }
            br.close();
            isr.close();
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void tt1() {
        try {
            // @param {String} $token - String containing your API Key
            // @param {String} $url - Encoded URI string container the URI you're targeting
            // @param {Integer} $width - Integer indicating the width of your target render
            // @param {Integer} $height - Integer indicating the height of your target render
            // @param {String} $output - String specifying the output format, "image" or "json"
            String token = "KGGMYV8-4SFMHSG-MYSZJJ9-16A2HWS";
            String url = URLEncoder.encode("https://www.baidu.com");
            int width = 1920;
            int height = 1080;
            String output = "image";

            // Construct the query params and URL
            String query = "https://shot.screenshotapi.net/screenshot";
            query += String.format("?token=%s&url=%s&width=%d&height=%d&output=%s", token, url, width, height, output);
            URL apiUrl = new URL(query);

            // Call the API and save the screenshot
            InputStream inputStream = apiUrl.openStream();
            OutputStream outputStream = new FileOutputStream("./screenshot.png");
            Path outPath = Paths.get("./screenshot.png");
            Files.copy(inputStream, outPath);
            //            inputStream.transferTo(outputStream);

            inputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void t33() throws IOException {
        String token = "KGGMYV8-4SFMHSG-MYSZJJ9-16A2HWS";
        String url = URLEncoder.encode("https://www.baidu.com");
        int width = 1920;
        int height = 1080;
        String output = "image";
        String query = "https://shot.screenshotapi.net/screenshot";
        query += String.format("?token=%s&url=%s&width=%d&height=%d", token, url, width, height);
        URL apiUrl = new URL(query);

        HttpURLConnection con = (HttpURLConnection)apiUrl.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(100000);

        con.setRequestProperty("Content-Type", "application/json");
        //            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        con.setRequestProperty("connection", "keep-alive");
        con.setRequestProperty("User-Agent", "Mozilla/4.76");
        con.setUseCaches(false);//设置不要缓存
        con.setInstanceFollowRedirects(true);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.connect();
        InputStream inStream = con.getInputStream();
        Path outPath = Paths.get("./screenshot.png");
        Files.copy(inStream, outPath);
    }
}
