package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty(value = "icon")
public class MatchingStringValidator extends ValidatorBase {
    private StringProperty string2;
    boolean shouldMatch = true;

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
        hasErrors.set(shouldMatch != text.equals(string2.get()));
    }

    public void setStringMatch(StringProperty string2) {
        this.string2 = (string2);
    }

    public void setStringMatch(StringProperty string2, boolean shouldMatch) {
        this.string2 = (string2);
        this.shouldMatch = shouldMatch;
    }

}
