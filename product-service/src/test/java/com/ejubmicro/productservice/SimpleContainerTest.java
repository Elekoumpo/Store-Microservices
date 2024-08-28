import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

public class SimpleContainerTest {

    @Test
    public void testContainer() {
        try (GenericContainer<?> container = new GenericContainer<>("alpine:latest").withCommand("sleep", "10")) {
            container.start();
            System.out.println(container.getLogs());
        }
    }
}