package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

// todo create a builder to add multiple validators then register with event
public class Validator {
    private static final Validator validator = new Validator();
    private static final String phoneRgx = "^(\\+2)?01\\d{9}$";
    private static final String emailRgx = "^[A-Za-z0-9._-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,6}$";
    private static final int maxTextLength = 10;
    private static final int minTextLength = 4;
    private static final int maxPassLength = 10;
    private static final int minPassLength = 4;

    private Validator() {

    }

    public static Validator getValidator() {
        return validator;
    }

    void addEmailValidationEvt(JFXTextField email) {
        validateWithRegex(email, emailRgx, "Email is not valid!");
    }

    public void addPhoneValidationEvt(JFXTextField phone) {
        validateWithRegex(phone, phoneRgx, "Phone is not valid!");
    }

    public void addRequiredFieldValidationEvt(JFXTextField field) { // if only we could use IFXValidatableControl
        validateWithRequired(field);
    }

    public void addRequiredFieldValidationEvt(JFXPasswordField field) { // if only we could use IFXValidatableControl
        validateWithRequired(field);
    }

    public void addRequiredFieldValidationEvt(JFXDatePicker field) { // if only we could use IFXValidatableControl
        validateWithRequired(field);
    }

    public void validateWithBounds(JFXTextField textField) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    public void validateWithBounds(JFXPasswordField textField) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void validateWithRequired(JFXDatePicker textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Required date field");
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void validateWithRequired(JFXPasswordField textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Required password field");
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void validateWithRequired(JFXTextField textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Required field");
        textField.getValidators().add(validator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void validateWithRegex(JFXTextField textField, String regex, String errorMsg) {
        RegexValidator regexValidator = new RegexValidator(errorMsg);
        regexValidator.setRegexPattern(regex);

        textField.getValidators().add(regexValidator);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }


}
