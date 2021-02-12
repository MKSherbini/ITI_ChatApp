package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;

import java.rmi.RemoteException;

@DefaultProperty(value = "icon")
public class DuplicatePhoneNumberValidator extends ValidatorBase {

    public DuplicatePhoneNumberValidator(String message) {
        super(message);
    }


    public DuplicatePhoneNumberValidator() {
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

        boolean registered = false;
        try {
            UserDBCrudInter userServices = UserDBCrudService.getUserService();
            registered = userServices.checkUserId(text);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        hasErrors.set(registered);
    }

}
