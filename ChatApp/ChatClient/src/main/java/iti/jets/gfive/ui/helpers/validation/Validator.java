package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;

import java.util.Map;

// todo create a builder to add multiple validators then register with event
// add additional validators
// add validate on text change after first validation
// if only we could use IFXValidatableControl
public class Validator {
    private static final Validator validator = new Validator();
    private static final String passwordRgx = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*";
    private static final String emailRgx = "^[A-Za-z0-9._-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,6}$";
    private static final String phoneRgx = "^(\\+2)?01\\d{9}$";

    private Validator() {

    }

    public static Validator getInstance() {
        return validator;
    }

    public void buildEmailValidation(JFXTextField email) {
        addRegexValidation(email, emailRgx, "Must be in format name@domain.com");
        setValidateOnEvent(email);
    }

    public void buildPhoneValidation(JFXTextField phone) {
        addRegexValidation(phone, phoneRgx, "Must be in format 01XXXXXXXXX");
        setValidateOnEvent(phone);
    }

    //    "Must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    public void buildPasswordValidation(JFXPasswordField password) {
        addBoundsValidation(password, 4, 8);
        addRegexValidation(password, "(?=.*[a-z]).*", "Must contain at least one lower letter");
        addRegexValidation(password, "(?=.*[A-Z]).*", "Must contain at least one uppercase letter");
        addRegexValidation(password, "(?=.*\\d).*", "Must contain at least one number");
        addRegexValidation(password, "(?=.*[@$!%*?&]).*", "Must contain at least one of @$!%*?&");
        addRegexValidation(password, passwordRgx, "Invalid character used");
        setValidateOnEvent(password);
    }

    public void buildRepeatPasswordValidation(JFXPasswordField repeatPassword, JFXPasswordField originalPassword) {
        addMatchValidation(repeatPassword, originalPassword, "Passwords don't match");
        setValidateOnEvent(repeatPassword);
    }

    public void buildRequiredPasswordValidation(JFXPasswordField passwordField) {
        addRequiredFieldValidation(passwordField);
        setValidateOnEvent(passwordField);
    }

    public void buildNameValidation(JFXTextField textField) {
        addBoundsValidation(textField, 2, 10);
        setValidateOnEvent(textField);
    }

    public void buildDateValidation(JFXDatePicker date) {
        addRequiredFieldValidation(date);
        setValidateOnEvent(date);
    }


    private void addBoundsValidation(JFXTextField textField, int minTextLength, int maxTextLength) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textField.getValidators().add(validator);
    }

    private void addMatchValidation(JFXPasswordField field, JFXPasswordField matches, String errorMsg) {
        MatchingStringValidator validator = new MatchingStringValidator(errorMsg);
        validator.setStrings(field.textProperty(), matches.textProperty());
        field.getValidators().add(validator);
    }

    private void addBoundsValidation(JFXPasswordField textField, int minTextLength, int maxTextLength) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textField.getValidators().add(validator);
    }

    private void addRequiredFieldValidation(JFXDatePicker textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Required date field");
        textField.getValidators().add(validator);
    }

    private void addRequiredFieldValidation(JFXPasswordField textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Password field is required");
        textField.getValidators().add(validator);
    }

    private void addRequiredFieldValidation(JFXTextField textField) {
        RequiredFieldValidator validator = new RequiredFieldValidator("Required field");
        textField.getValidators().add(validator);
    }

    private void addRegexValidation(JFXTextField textField, String regex, String errorMsg) {
        RegexValidator regexValidator = new RegexValidator(errorMsg);
        regexValidator.setRegexPattern(regex);
        textField.getValidators().add(regexValidator);
    }

    private void addRegexValidation(JFXPasswordField textField, String regex, String errorMsg) {
        RegexValidator regexValidator = new RegexValidator(errorMsg);
        regexValidator.setRegexPattern(regex);
        textField.getValidators().add(regexValidator);
    }

    private void setValidateOnEvent(JFXDatePicker textField) {
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void setValidateOnEvent(JFXTextField textField) {
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void setValidateOnEvent(JFXPasswordField textField) {
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }
}
