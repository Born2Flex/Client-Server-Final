package validator;

import org.junit.jupiter.api.Test;
import ua.edu.ukma.validator.Validator;
import ua.edu.ukma.validator.Violation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {
    private final Validator validator = new Validator();

    @Test
    void testNotNullViolation() {
        TestClass testClass = new TestClass();
        testClass.setNotNullField(null);
        List<Violation> violations = validator.validate(testClass);
        assertEquals(1, violations.size());
        assertEquals("notNullField", violations.getFirst().fieldName());
        assertEquals("Field value shouldn't be null", violations.getFirst().cause());
    }

    @Test
    void testNotBlankViolation() {
        TestClass testClass = new TestClass();
        testClass.setNotBlankField("   ");
        List<Violation> violations = validator.validate(testClass);
        assertEquals(1, violations.size());
        assertEquals("notBlankField", violations.getFirst().fieldName());
        assertEquals("Field should be not null and not blank", violations.getFirst().cause());
    }

    @Test
    void testPositiveIntegerViolation() {
        TestClass testClass = new TestClass();
        testClass.setPositiveIntegerField(-1);
        List<Violation> violations = validator.validate(testClass);
        assertEquals(1, violations.size());
        assertEquals("positiveIntegerField", violations.getFirst().fieldName());
        assertEquals("Field value shouldn't be null or negative", violations.getFirst().cause());
    }

    @Test
    void testPositiveDoubleViolation() {
        TestClass testClass = new TestClass();
        testClass.setPositiveDoubleField(-1.0);
        List<Violation> violations = validator.validate(testClass);
        assertEquals(1, violations.size());
        assertEquals("positiveDoubleField", violations.getFirst().fieldName());
        assertEquals("Field value shouldn't be null or negative", violations.getFirst().cause());
    }

    @Test
    void testAllValid() {
        TestClass testClass = new TestClass();
        testClass.setNotNullField("Valid");
        testClass.setNotBlankField("Valid");
        testClass.setPositiveIntegerField(1);
        testClass.setPositiveDoubleField(1.0);
        List<Violation> violations = validator.validate(testClass);
        assertTrue(violations.isEmpty());
    }
}
