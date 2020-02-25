package cn.ac.greysh.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SP {

	private static final String HOME_PAGE = "http://epub.cnki.net/KNS/brief/result.aspx?dbprefix=CJFD";

	private ChromeDriver driver;

	private ArrayList<String> authors = new ArrayList<String>();
	private ArrayList<String> articles = new ArrayList<String>();

	public static void main(String[] args) throws InterruptedException, IOException {
		SP ns = new SP();
		ns.pre();
		ns.init();
		ns.search();
		ns.quit();
	}

	public void pre() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/cnki.txt"));
		String line = br.readLine();
		while (line != null) {
			String[] result = line.split(",");
			authors.add(result[0]);
			articles.add(result[1]);
			line = br.readLine();
		}
		br.close();
	}

	@SuppressWarnings("deprecation")
	public void init() throws IOException, InterruptedException {
		File f = new File("lib/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", f.getAbsolutePath());
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_settings.popups", 0); // 2就是代表禁止加载的意思
		options.addArguments("disable-infobars");
		options.setExperimentalOption("prefs", prefs);
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(desiredCapabilities);
		driver.manage().window().maximize();
		driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public void search() throws InterruptedException, IOException {
		PrintWriter pw = new PrintWriter("data/cnki-rc-dc.txt");
		pw.println("序号,作者,篇名,被引次数(RC),下载次数(DC)");
		for (int i = 0; i < authors.size(); i++) {
			searchOne(i, authors.get(i), articles.get(i), pw);
		}
		pw.flush();
		pw.close();
	}

	public void searchOne(int i, String authorName, String articleName, PrintWriter pw) throws InterruptedException {
		driver.get(HOME_PAGE);
		WebElement high = driver.findElement(By.xpath("//*[@id=\"1_3\"]/a"));
		high.click();
		Thread.sleep(1000);
		WebElement artileInput = driver.findElementByName("txt_2_value1");
		artileInput.sendKeys(articleName);
		WebElement authorInput = driver.findElementByName("au_1_value1");
		authorInput.sendKeys(authorName);
		driver.findElementByXPath("//*[@id='ddSubmit']/span").click();
		driver.findElementByXPath("//*[@id='btnSearch']").click();
		Thread.sleep(2000);
		WebElement iframe = driver.findElement(By.id("iframeResult"));
		Thread.sleep(2000);
		String now_handle = driver.getWindowHandle();
		Set<String> all_handles = driver.getWindowHandles();
		for (String handle : all_handles) {
			if (handle != now_handle) {
				driver.switchTo().window(handle);
				driver.switchTo().frame(iframe);
				List<WebElement> tb = driver.findElements(By.xpath("//*[@id=\"ctl00\"]/table/tbody/tr[2]"));
				for (WebElement t : tb) {
					List<WebElement> tbod = t.findElements(By.tagName("tbody"));
					for (WebElement tr : tbod) {
						List<WebElement> td = tr.findElements(By.tagName("tr"));
						td.remove(0);
						for (WebElement tds : td) {
							List<WebElement> tdss = tds.findElements(By.tagName("td"));
							String rc = tdss.get(5).getText();
							String dc = tdss.get(6).getText();
							System.out.println("序号:" + i + " 作者：" + authorName + " 篇名：《" + articleName + "》 引用次数：" + rc
									+ " 下载次数：" + dc);
							pw.println(i + "," + authorName + "," + articleName + "," + rc + "," + dc);
						}
					}

				}
			}
		}
		Thread.sleep(1000);
	}

	public void quit() {
		driver.quit();
	}
}