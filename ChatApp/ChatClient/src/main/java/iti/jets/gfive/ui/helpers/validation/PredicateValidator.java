package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

import java.rmi.RemoteException;
import java.util.function.Predicate;

@DefaultProperty(value = "icon")
public class PredicateValidator extends ValidatorBase {
    Predicate<String> predicate;

    public PredicateValidator(String message) {
        super(message);
    }

    public PredicateValidator(String message, Predicate<String> predicate) {
        this(message);
        this.predicate = predicate;
    }


    public PredicateValidator() {
    }

    @Override
    protected void eval() {
        if (srcControl.get() instanceof TextInputControl) {
            evalPhoneExists();
        }
    }

    private void evalPhoneExists() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        String text = (textField.getText() == null) ? "" : textField.getText(); // Treat null like empty string

        boolean valid = predicate.test(text);
        System.out.println("text = " + text);
        System.out.println("valid = " + valid);
        hasErrors.set(!valid);
    }

}
