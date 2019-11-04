package Tests.Test1;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Test1 {
    private static WebDriver driver;
    private String message;

    @BeforeClass
    public static void setup() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.wiley.com/en-us");
        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"country-location-form\"]/div[3]/button[2]"));
        element2.click();
    }

    @Test
    public void checkItems() throws InterruptedException {
        Subheaders[] subheaders = Subheaders.values();
        List<WebElement> element3 = driver.findElements(By.className("who-we-serve-block-title"));

        ArrayList<String> list = new ArrayList<>();
        for (Subheaders s: subheaders)
            list.add(s.getTitle());
        ArrayList<String> list2 = new ArrayList<>();
        for (WebElement e: element3)
            list2.add(e.getText());
        List<String> errors = list.stream().filter(o1 -> list2.stream().noneMatch(o2 -> o2.equals(o1)))
                .collect(Collectors.toList());

        if(errors.size() != 0) {
            String message = "No headlines:\n";
            for(String s: errors)
                message += s + "\n";
            throw new NoSuchElementException(message);
        }
    }

    @Test
    public void useSearch() throws InterruptedException {
        message = "Nothing found";
        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"js-site-search-input\"]"));
        element2.sendKeys("Java");
        List<WebElement> element3 = driver.findElements(By.xpath("//*[@id=\"ui-id-2\"]/section/div/div"));
        if(element3.size() == 0)
            throw new NoSuchElementException(message);
    }

    @Test
    public void checkButtons() throws InterruptedException {
        message = "Errors in PRODUCTS page: ";
        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"js-site-search-input\"]"));
        element2.sendKeys("Java");
        Thread.sleep(1000);
        element2 = driver.findElement(By.xpath("//*[@id=\"main-header-container\"]/div/div[2]/div/form/div/span/button[1]"));
        element2.click();
        List<WebElement> element3 = driver.findElements(By.className("product-title"));

        if(element3.size() != 10)
            throw new NoSuchElementException(message + "books < 10 items");
        for (WebElement e: element3)
            if(!e.getText().contains("Java")) {
                System.out.println(e.getText());
                throw new NoSuchElementException(message + "wrong result search!");
            }

        element3 = driver.findElements(By.id("eBundlePlpTabMainTabPanel"));
        for (WebElement e: element3){
            List<WebElement> element4 = e.findElements(By.className("product-button"));
            if(element4.size()==0)
               throw new NoSuchElementException(message + "no button");
            element4 = e.findElements(By.className("productButtonGroupName"));
            for (WebElement e1: element4){
                if(e1.getText().equals("E-BOOK")||e1.getText().equals("PRINT")){
                    if(!e1.findElement(By.xpath("//*[@id=\"addToCartForm9780471721260\"]/button")).isEnabled())
                        throw new NoSuchElementException(message + "wrong button “Add to Cart”");
                }
                else if(e1.getText().equals("O-BOOK")){
                    if(!e1.findElement(By.xpath("//*[@id=\"9781118257517_O-Book\"]/div/div/div[6]/a")).isEnabled())
                        throw new NoSuchElementException(message + "wrong button “VIEW ON WILEY ONLINE LIBRARY”");
                }
            }
        }
    }

    @Test
    public void checkSubjects() throws InterruptedException {
        message = "is not displayed";
        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"main-header-navbar\"]/ul[1]/li[2]"));

        Thread.sleep(500);
        Actions actions = new Actions(driver);
        actions.moveToElement(element2).build().perform();

        Thread.sleep(500);
        WebElement element3 = driver.findElement(By.xpath("//*[@id=\"Level1NavNode2\"]/ul/li[9]/a"));
        actions.moveToElement(element3).build().perform();

        Thread.sleep(500);
        List<WebElement> elements = element3.findElements(By.xpath("//*[@id=\"Level1NavNode2\"]/ul/li[9]/div/ul/ul/li"));
        Subjects[] subjects = Subjects.values();

        ArrayList<String> list = new ArrayList<>();
        for (Subjects e: subjects)
            list.add(e.getTitle().trim());

        ArrayList<String> list2 = new ArrayList<>();
        for (WebElement e: elements)
            list2.add(e.getText().trim());

        list.removeAll(list2);

        if(list.size() != 0) {
            String message = "No subjects:\n";
            for(String s: list)
                message += s + "\n";
            throw new NoSuchElementException(message);
        }

        Thread.sleep(2000);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
