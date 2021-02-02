package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty(value = "icon")
public class MatchingStringValidator extends ValidatorBase {
    private StringProperty string1;
    private StringProperty string2;

    public MatchingStringValidator(String message) {
        super(message);
    }


    public MatchingStringValidator() {
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

        hasErrors.set(!string1.get().equals(string2.get()));
    }

    public void setStrings(StringProperty string1, StringProperty string2) {
        this.string1 = (string1);
        this.string2 = (string2);
    }

}
