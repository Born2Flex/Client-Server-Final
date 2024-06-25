package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.exceptions.BadRequestException;
import ua.edu.ukma.services.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapperTest {
    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        jsonMapper = new JsonMapper();
    }

    @Test
    void testParseObject() {
        String json = "{\"name\":\"John\",\"age\":30}";
        Person person = jsonMapper.parseObject(json, Person.class);
        assertNotNull(person);
        assertEquals("John", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    void testParseObjectInvalidJson() {
        String json = "{\"name\":\"John\",\"age\":30";
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> jsonMapper.parseObject(json, Person.class));
        assertEquals("Bad Request body", exception.getMessage());
    }

    @Test
    void testToJsonValidObject() {
        Person person = new Person("John", 30);
        String json = jsonMapper.toJson(person);

        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
    }

    public static class Person {
        private String name;
        private int age;

        public Person() {}

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}