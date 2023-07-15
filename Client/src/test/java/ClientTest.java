import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class ClientTest {

    private Client c;

    @BeforeEach
    public void init() throws IOException {
        System.out.println("Тесты запущенны");
        c = new Client();
    }

    @BeforeAll
    public static void started() {
        System.out.println("Старт теста");
    }

    @AfterEach
    public void finished() {
        System.out.println("Окончание теста");
    }

    @AfterAll
    public static void finishedAll() {
        System.out.println("Тесты завершенны");
    }

    @Test
    public void testNameRegistration() {

        //String input = "Artem";
        String expected = "Artem";

        String result = c.nameRegistration();

        //MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }
}