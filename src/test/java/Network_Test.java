import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.SourceType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Network_Test {

    public static void main(String[] args) throws InterruptedException, IOException, CsvValidationException {

        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        //sayfaya gidilir
        driver.get("https://www.network.com.tr/");
        driver.manage().window().maximize();
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        //dogru sayfada olup olmadıgımızın kontrolu yapılır
        String url = driver.getCurrentUrl();
        Assert.assertEquals("https://www.network.com.tr/",url);

        //arama yerine ceket yazıp aratılır.
        driver.findElement(By.id("search")).sendKeys("ceket");
        Thread.sleep(2000);
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ENTER).perform();

        //scroll ile asagı kaydırılıp daha fazla göster butonuna tıklanır.
        JavascriptExecutor js=(JavascriptExecutor)driver;
        Thread.sleep(2000);
        js.executeScript("window.scroll(0,15500)");
        driver.findElement(By.xpath("//button[contains(text(),'Daha fazla göster')]")).click();
        Thread.sleep(3000);

        //ikinci sayfaya geçip geçmediğimizin kontrolü yapılır
        String url2 = driver.getCurrentUrl();
        Assert.assertEquals("https://www.network.com.tr/search?searchKey=ceket&page=2",url2);

        //indirimli ürünlere geçilir
        WebElement editor = driver.findElement(By.cssSelector("span[class='tool__text']"));
        editor.click();
        WebElement indirimli_urunler = driver.findElement(By.cssSelector("span[class='sortingList__text']"));
        indirimli_urunler.click();

        //indirimli ürünün üzerine mouse hover ettirilir
        WebElement indirimli_urun = driver.findElement(By.id("product-123599"));
        Actions action2 = new Actions(driver);
        action2.moveToElement(indirimli_urun).perform();

        //beden seçilir ve ürün sepete eklenir ardından sepete gidilir.
        WebElement beden= driver.findElement(By.cssSelector("label[extcode=\"1077384001\"]"));
        beden.click();
        Thread.sleep(4000);
        WebElement shopping_bag = driver.findElement(By.cssSelector("svg[class='header__icon -shoppingBag']"));
        shopping_bag.click();
        WebElement go_to_cart = driver.findElement(By.cssSelector("a[class='button -primary header__basket--checkout header__basketModal -checkout']"));
        go_to_cart.click();

        //sepete gelip gelmediğimizin kontrolü yapılır
        String url3 = driver.getCurrentUrl();
        Assert.assertEquals("https://www.network.com.tr/cart",url3);
        driver.navigate().refresh();
        Thread.sleep(1000);

        //devam et butonuna tıklanır.
        WebElement devam=driver.findElement(By.xpath("//button[@class='continueButton n-button large block text-center -primary' and contains(text(),' DEVAM ET ')]"));
        Actions action3=new Actions(driver);
        action3.moveToElement(devam).click().perform();
        driver.navigate().refresh();

        //csv dosyamızdan kullanıcı bilgileri çekilerek mail ve şifre alanları doldurulur.
        CSVReader reader = new CSVReader(new FileReader("drivers/odev.csv"));

        String csvCell[];

        while ((csvCell=reader.readNext()) != null)
        {
               String email=csvCell[0];
               String password=csvCell[1];

                driver.findElement(By.id("n-input-email")).sendKeys(email);
                driver.findElement(By.id("n-input-password")).sendKeys(password);

        }

        //giris yap butonu kontrol edilir
        Thread.sleep(1000);
        WebElement giris_yap= driver.findElement(By.xpath("//button[contains(text(),'GİRİŞ YAP')]"));
        giris_yap.click();

        //Network logosuna tıklanıp ana sayfaya gidilir.
        WebElement logo=driver.findElement(By.cssSelector("svg[class='headerCheckout__logo__img']"));
        logo.click();

        driver.navigate().refresh();

        //çanta logosuna tıklanıp sağ taraftan sepet açılır.
        WebElement canta_logo = driver.findElement(By.cssSelector("svg[class='header__icon -shoppingBag']"));
        canta_logo.click();
        Thread.sleep(2000);

        //sepetten çöp kutusuna tıklanar.
        WebElement remove = driver.findElement(By.xpath("//div[@class='header__basketProductBtn header__basketModal -remove']"));
        remove.click();

        //Çıkart butonu onaylanarak ürün tamamen sepetten çıkarılır.
        WebElement remove_item = driver.findElement(By.xpath("//button[@class='btn -black o-removeCartModal__button' and contains(text(),'Çıkart')]"));
        remove_item.click();

        //tekrar çanta logosuna tıklanır sepetin boş olduğu gözükür.
        canta_logo.click();

        //sepetin boş olduğu kontrol ettirilir eğer sepetimiz boş ise kod ekranımızda "Sepet Henüz Boş" yazısı çıkar.
        String sepet_kontrol = driver.findElement(By.xpath("//span[contains(text(),\"Sepetiniz Henüz Boş\")]")).getText();
        System.out.println(sepet_kontrol.toString());

        Thread.sleep(3000);
        driver.quit();


        }

    }


