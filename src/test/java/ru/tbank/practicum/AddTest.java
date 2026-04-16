package ru.tbank.practicum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tbank.practicum.service.SampleService;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(classes = {SampleService.class})
public class AddTest {
    @Autowired
    private SampleService sampleService;

    @Test
    public void testAddtion() {
        assertEquals("Result of addition", sampleService.add(2, 2), 4);
    }
}