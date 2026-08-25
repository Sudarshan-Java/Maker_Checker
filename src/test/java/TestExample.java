import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.util.Properties;

public class TestExample {

    private static Playwright playwright;
    private static Browser browser;
    private static Properties properties;

    @BeforeAll
    static void setup() throws Exception {

        // Load application.properties
        properties = new Properties();

        try (InputStream input = TestExample.class
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

        // Read headless value
        boolean headless = Boolean.parseBoolean(
            properties.getProperty("headless", "false")
        );

        // Launch browser
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(1000)
        );
    }

    @Test
    void test() {

        Page page = browser.newPage();

        // Get URL from application.properties
        String baseUrl = properties.getProperty("base.url");

        // Open application
        page.navigate(baseUrl);

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
        ).click();

        page.getByRole(
            AriaRole.TEXTBOX,
            new Page.GetByRoleOptions()
                .setName("Amount")
        ).fill("101");

        // Reason
        page.getByRole(
            AriaRole.TEXTBOX,
            new Page.GetByRoleOptions()
                .setName("Reason")
        ).click();

        page.getByRole(
            AriaRole.TEXTBOX,
            new Page.GetByRoleOptions()
                .setName("Reason")
        ).fill("Ajeet Marriage");

        // Initiate transaction
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("Initiate transaction")
        ).click();

        // Beneficiary
        page.locator(
            "#Checke__Beneficiary_Page__el_txt_1_4"
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

        // Checker
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("Checker")
        ).click();

        // Checker Profile
        page.locator(
            "#Checke__Checker_Profile__el_txt_3_3"
        ).click();

        // View History
        page.getByRole(
            AriaRole.BUTTON,
            new Page.GetByRoleOptions()
                .setName("View History")
        ).click();

        // Checker Logout
        page.locator(
            "#Checke__Checker_History__AppzillonHeaderButtons__AppzillonLogoutBtn"
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