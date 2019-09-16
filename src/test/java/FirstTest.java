import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(TestSuiteProfilerExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class FirstTest {

    private static List<String> threadNames = Collections.synchronizedList(new ArrayList<>());

    @AfterAll
    static void afterAll() {

        Assumptions.assumeTrue(FirstTest::isParallelExecutionEnable);

        long count = threadNames.stream()
                .distinct()
                .count();

        assertThat(count).isEqualTo(2);
    }

    private static boolean isParallelExecutionEnable() {

        String ENABLED_PARALLEL_EXECUTION = "junit.jupiter.execution.parallel.enabled";
        String PARALLELISM_FACTOR = "junit.jupiter.execution.parallel.config.fixed.parallelism";

        try {
            Properties junitConfig = new Properties();
            junitConfig.load(new ClassPathResource("junit-platform.properties").getInputStream());

            boolean enabled =
                    Boolean.valueOf(junitConfig.getProperty(ENABLED_PARALLEL_EXECUTION, "false"));

            if (!enabled) return false;

            int parallelismFactor =
                    Integer.valueOf(junitConfig.getProperty(PARALLELISM_FACTOR, "1"));

            return parallelismFactor > 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    void sabra() throws InterruptedException {
        Thread.sleep(10000);
        String name = Thread.currentThread().getName();
        threadNames.add(name);
        System.out.println("SABRA! " + name);
    }

    @Test
    void cadabra() throws InterruptedException {
        Thread.sleep(10000);
        String name = Thread.currentThread().getName();
        threadNames.add(name);
        System.out.println("CADABRA! " + name);
    }

    @TestFactory
    Stream<DynamicTest> dynamicTests() {
        return Stream.generate(Math::random)
                .limit(2)
                .mapToInt(v -> (int) (v * 1000))
                .mapToObj(i -> dynamicTest("testing sum opperation for value " + i,
                        () -> assertThat("Test")));
    }
}