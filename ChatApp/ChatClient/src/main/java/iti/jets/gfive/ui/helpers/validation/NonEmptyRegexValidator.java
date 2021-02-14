package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

import java.rmi.RemoteException;
import java.util.regex.Pattern;

@DefaultProperty(value = "icon")
public class NonEmptyRegexValidator extends ValidatorBase {
    private final String regexPattern;
    private final Pattern regexPatternCompiled;

    public NonEmptyRegexValidator(String regexPattern, String message) {
        super(message);

        this.regexPattern = regexPattern;
        this.regexPatternCompiled = Pattern.compile(regexPattern);
    }

    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        String text = (textField.getText() == null) ? "" : textField.getText(); // Treat null like empty string

        boolean valid = regexPatternCompiled.matcher(text).matches() || text.length() == 0;
        hasErrors.set(!valid);
    }
}
