import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MakerTest {

    private static Playwright playwright;
    private static Browser browser;
    private static Properties properties;

    @BeforeAll
    static void setup() throws Exception {

        // Load application.properties
        properties = new Properties();

        try (InputStream input = MakerTest.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException(
                    "application.properties not found in src/test/resources"
                );
            }

            properties.load(input);
        }

        // Create Playwright
        playwright = Playwright.create();

        // Read properties
        boolean headless = Boolean.parseBoolean(
            properties.getProperty("headless", "false")
        );

        double slowMo = Double.parseDouble(
            properties.getProperty("slow.mo", "1000")
        );

        // Launch visible browser
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo)
        );

        System.out.println("Browser launched");
        System.out.println("Headless = " + headless);
        System.out.println("SlowMo = " + slowMo);
    }

    @Test
    void test() {

        Page page = browser.newPage();

        // Open application
        page.navigate(
            properties.getProperty("base.url")
        );

        // User
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("User")
        ).click(
            new Locator.ClickOptions()
                .setForce(true)
        );

        // Initiate Transaction
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("Intiate Transaction")
        ).click();

        // Amount
        page.getByRole(
            AriaRole.TEXTBOX,
            new Page.GetByRoleOptions()
                .setName("Amount")
        ).fill("11");

        // Reason
        page.getByRole(
            AriaRole.TEXTBOX,
            new Page.GetByRoleOptions()
                .setName("Reason")
        ).fill("Welcome bonus");

        // Select beneficiary
        page.locator(
            "#Checke__Transaction_Page__el_btn_1_li"
        ).click();

        // Initiate transaction
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("Initiate transaction")
        ).click();

        // Beneficiary
        page.locator(
            "#Checke__Beneficiary_Page__el_txt_1_1"
        ).click();

        // Back
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("Back")
        ).click();

        // History
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("History")
        ).click();

        // Maker Logout
        page.locator(
            "#Checke__History_Maker__AppzillonHeaderButtons__AppzillonLogoutBtn"
        ).click();
    }

    @AfterAll
    static void teardown() {

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}