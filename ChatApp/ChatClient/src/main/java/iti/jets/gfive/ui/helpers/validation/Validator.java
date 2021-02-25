package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.NotificationDBCrudService;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.ContactsListView;
import iti.jets.gfive.ui.helpers.ModelsFactory;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import iti.jets.gfive.ui.models.CurrentUserModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.rmi.RemoteException;
import java.util.function.Predicate;

// todo create a builder to add multiple validators then register with event
// add additional validators
// add validate on text change after first validation
// if only we could use IFXValidatableControl
public class Validator {
    private static final Validator validator = new Validator();
    private static final String passwordRgx = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*";
    private static final String emailRgx = "(^[A-Za-z0-9._-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,6}$)| [ \\t\\n]* ";
    private static final String phoneRgx = "^(\\+2)?01\\d{9}$";

    private Validator() {

    }

    public static Validator getInstance() {
        return validator;
    }

    public void buildEmailValidation(JFXTextField email) {
        addNonEmptyRegexValidation(email, emailRgx, "Must be empty or in format name@domain.com");
        setValidateOnEvent(email);
    }

    public void buildPhoneLoginValidation(JFXTextField phone) {
        addRegexValidation(phone, phoneRgx, "Must be in format 01XXXXXXXXX");
        addDBExistingPhoneValidation(phone, true);
        setValidateOnEvent(phone);
    }

    public void buildPhoneRegisterValidation(JFXTextField phone) {
        addRegexValidation(phone, phoneRgx, "Must be in format 01XXXXXXXXX");
        addDBExistingPhoneValidation(phone);
        setValidateOnEvent(phone);
    }

    public void buildPhoneContactValidation(JFXTextField phone) {
        addRegexValidation(phone, phoneRgx, "Must be in format 01XXXXXXXXX");

        ModelsFactory modelsFactory = ModelsFactory.getInstance();
        CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
        addMatchValidation(phone, currentUserModel.phoneNumberProperty(), false, "Can't add self"); // todo make sure currentUserModel is actually valid

        addDBExistingPhoneValidation(phone, true);

        addPredicateValidation(phone, "Contact already exists",
                s -> !ContactsListView.getInstance().contactExist(s));
        addPredicateValidation(phone, "A request was already sent",
                s -> {
                    try {
                        return !NotificationDBCrudService.getNotificationService().pendingNotification(currentUserModel.getPhoneNumber(), s);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        StageCoordinator.getInstance().reset();
                    }
                    return true;
                });
        addPredicateValidation(phone, "Check your notifications list",
                s -> {
                    try {
                        return !NotificationDBCrudService.getNotificationService()
                                .reverseNotification(currentUserModel.getPhoneNumber(),
                                        s);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        StageCoordinator.getInstance().reset();
                    }
                    return true;
                });
        setValidateOnEvent(phone);
    }

    //    "Must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    // todo show the user currently using that password
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

    public void buildLoginPasswordValidation(JFXPasswordField passwordField, JFXTextField userID) {
        addRequiredFieldValidation(passwordField);
        addPredicateValidation(passwordField, "Incorrect password", s -> {
            try {
                return null != UserDBCrudService.getUserService().selectFromDB(userID.textProperty().get(), s);
            } catch (RemoteException e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
            }
            return true;
        });
        setValidateOnEvent(passwordField);
    }

    public void buildNameValidation(JFXTextField textField) {
        addBoundsValidation(textField, 2, 20);
        setValidateOnEvent(textField);
    }

    public void buildBioValidation(JFXTextArea textArea) {
        addBoundsValidationForTextArea(textArea, 0, 200);
        setValidateOnEventForTextArea(textArea);
    }

    public void buildDateValidation(JFXDatePicker date) {
        addRequiredFieldValidation(date);
        setValidateOnEvent(date);
    }

    private void addBoundsValidationForTextArea(JFXTextArea textArea, int minTextLength, int maxTextLength) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textArea.getValidators().add(validator);
    }

    private void addBoundsValidation(JFXTextField textField, int minTextLength, int maxTextLength) {
        StringBoundsValidator validator = new StringBoundsValidator(minTextLength, maxTextLength);
        textField.getValidators().add(validator);
    }

    private void addMatchValidation(JFXPasswordField field, JFXPasswordField matches, String errorMsg) {
        MatchingStringValidator validator = new MatchingStringValidator(errorMsg);
        validator.setStringMatch(matches.textProperty());
        field.getValidators().add(validator);
    }

    private void addMatchValidation(JFXTextField field, javafx.beans.property.StringProperty matches, boolean shouldMatch, String errorMsg) {
        MatchingStringValidator validator = new MatchingStringValidator(errorMsg);
        validator.setStringMatch(matches, shouldMatch);
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

    private void addDBExistingPhoneValidation(JFXTextField textField) {
        addDBExistingPhoneValidation(textField, false);
    }

    private void addDBExistingPhoneValidation(JFXTextField textField, boolean shouldExist) {
        ExistingPhoneNumberValidator validator;
        if (shouldExist) {
            validator = new ExistingPhoneNumberValidator("Phone number doesn't exist", true);
        } else {
            validator = new ExistingPhoneNumberValidator("Phone number already exists");
        }
        textField.getValidators().add(validator);
    }

    private void addPredicateValidation(JFXTextField textField, String errorMessage, Predicate<String> predicate) {
        PredicateValidator validator = new PredicateValidator(errorMessage, predicate);

        textField.getValidators().add(validator);
    }

    private void addPredicateValidation(JFXPasswordField textField, String errorMessage, Predicate<String> predicate) {
        PredicateValidator validator = new PredicateValidator(errorMessage, predicate);

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

    private void addNonEmptyRegexValidation(JFXTextField textField, String regex, String errorMsg) {
        NonEmptyRegexValidator regexValidator = new NonEmptyRegexValidator(regex, errorMsg);
        textField.getValidators().add(regexValidator);
    }

    private void setValidateOnEvent(JFXDatePicker textField) {
        setErrorIcon(textField);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void setValidateOnEventForTextArea(JFXTextArea textArea) {
        setErrorIcon(textArea);
        textArea.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textArea.validate();
            }
        });
    }

    private void setValidateOnEvent(JFXTextField textField) {
        setErrorIcon(textField);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    private void setValidateOnEvent(JFXPasswordField textField) {
        setErrorIcon(textField);
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                textField.validate();
            }
        });
    }

    // fas-exclamation, fas-exclamation-circle, fas-exclamation-triangle, fas-info-circle
    // fas-khanda, fas-lightbulb, fas-minus-circle, fas-question-circle,
    // fas-skull, mdi2s-skull-outline, mdi2s-skull-scan-outline, mdi2s-skull, mdi2s-skull-crossbones-outline
    private void setErrorIcon(JFXTextArea textArea) {
        FontIcon errorIcon = new FontIcon("fas-skull");
        textArea.getValidators().forEach(validatorBase -> validatorBase.setIcon(errorIcon));
    }

    private void setErrorIcon(JFXTextField textField) {
        FontIcon errorIcon = new FontIcon("fas-skull");
        textField.getValidators().forEach(validatorBase -> validatorBase.setIcon(errorIcon));
    }

    private void setErrorIcon(JFXDatePicker textField) {
        FontIcon errorIcon = new FontIcon("fas-skull");
        textField.getValidators().forEach(validatorBase -> validatorBase.setIcon(errorIcon));
    }

    private void setErrorIcon(JFXPasswordField textField) {
        FontIcon errorIcon = new FontIcon("fas-skull");
        textField.getValidators().forEach(validatorBase -> validatorBase.setIcon(errorIcon));
    }
}
