package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty(value = "icon")
public class StringBoundsValidator extends ValidatorBase {
    private int minLength;
    private int maxLength;

    public StringBoundsValidator(String message) {
        super(message);
    }


    public StringBoundsValidator() {
    }

    public StringBoundsValidator(int minLength, int maxLength) {
        super(String.format("Must be in range %s to %s", minLength, maxLength));
        this.minLength = minLength;
        this.maxLength = maxLength;
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

        boolean valid = org.apache.commons.lang3.Range.between(minLength, maxLength).contains(text.length());
        hasErrors.set(!valid);
    }

    public void setBounds(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}
