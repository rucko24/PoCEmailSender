package com.example.javamail.util;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class ValidateMultipleMails implements Validator<String> {

    private static final String REG_EXP_MULTIPLE_EMAILS =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*" +
                    "(\\.[A-Za-z]{2,})$";

    private String errorMessage;

    public ValidateMultipleMails(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult apply(String text, ValueContext context) {
        final String[] test = text.split(",");
        boolean value = false;
        for (int i = 0; i < test.length; i++) {
            if (validEmail(test[i])) {
                value = true;
                break;
            }
        }
        if (value) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error(errorMessage);
        }
    }


    private boolean validEmail(String emails) {
        return emails.matches(REG_EXP_MULTIPLE_EMAILS);
    }
}

