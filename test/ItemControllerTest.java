import static org.fest.assertions.Assertions.*;

import com.thoughtworks.selenium.SeleneseTestBase;
import org.junit.*;
import play.mvc.Result;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


public class ItemControllerTest {

    @Test
    public void getItems_shouldReturnAllItems() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = route(fakeRequest(GET, "/api/item"));
                assertThat(result).isNull();
            }
        });
    }
}
