package validator;

import ua.edu.ukma.validator.annotations.NotBlank;
import ua.edu.ukma.validator.annotations.NotNull;
import ua.edu.ukma.validator.annotations.Positive;

public class TestClass {
    @NotNull
    private String notNullField;
    @NotBlank
    private String notBlankField;
    @Positive
    private Integer positiveIntegerField;
    @Positive
    private Double positiveDoubleField;

    public TestClass() {
        notNullField = "test";
        notBlankField = "test";
        positiveIntegerField = 1;
        positiveDoubleField = 1.0;
    }

    public String getNotNullField() {
        return notNullField;
    }

    public void setNotNullField(String notNullField) {
        this.notNullField = notNullField;
    }

    public String getNotBlankField() {
        return notBlankField;
    }

    public void setNotBlankField(String notBlankField) {
        this.notBlankField = notBlankField;
    }

    public Integer getPositiveIntegerField() {
        return positiveIntegerField;
    }

    public void setPositiveIntegerField(Integer positiveIntegerField) {
        this.positiveIntegerField = positiveIntegerField;
    }

    public Double getPositiveDoubleField() {
        return positiveDoubleField;
    }

    public void setPositiveDoubleField(Double positiveDoubleField) {
        this.positiveDoubleField = positiveDoubleField;
    }
}
