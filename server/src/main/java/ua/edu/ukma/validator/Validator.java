package ua.edu.ukma.validator;

import ua.edu.ukma.validator.annotations.NotBlank;
import ua.edu.ukma.validator.annotations.NotNull;
import ua.edu.ukma.validator.annotations.Positive;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public List<Violation> validate(Object object) {
        List<Violation> violations = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                try {
                    if (annotation.annotationType().equals(NotNull.class)) {
                        processNotNull(field, object, violations);
                    } else if (annotation.annotationType().equals(NotBlank.class)) {
                        processNotBlank(field, object, violations);
                    } else if (annotation.annotationType().equals(Positive.class)) {
                        processPositive(field, object, violations);
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Field " + field.getName() + " is not accessible");
                    throw new RuntimeException(e);
                }
            }
        }
        return violations;
    }

    private void processNotNull(Field field, Object object, List<Violation> violations) throws IllegalAccessException {
        if (field.get(object) == null) {
            violations.add(new Violation(field.getName(), "Field value shouldn't be null"));
        }
    }

    private void processPositive(Field field, Object object, List<Violation> violations) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (!fieldType.equals(Integer.class) && !fieldType.equals(Double.class)) {
            violations.add(new Violation(field.getName(), "Field type should be Integer.class or Double.class"));
            return;
        }

        if (fieldType.equals(Integer.class)) {
            Integer value = (Integer) field.get(object);
            if (value == null || value < 0) {
                violations.add(new Violation(field.getName(), "Field value shouldn't be null or negative"));
            }
        } else {
            Double value = (Double) field.get(object);
            if (value == null || value < 0) {
                violations.add(new Violation(field.getName(), "Field value shouldn't be null or negative"));
            }
        }
    }

    private void processNotBlank(Field field, Object object, List<Violation> violations) throws IllegalAccessException {
        if (!field.getType().equals(String.class)) {
            violations.add(new Violation(field.getName(), "Field type should be String.class"));
            return;
        }
        String string = (String) field.get(object);
        if (string == null || string.isBlank()) {
            violations.add(new Violation(field.getName(), "Field should be not null and not blank"));
        }
    }
}