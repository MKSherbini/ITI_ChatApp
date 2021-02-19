package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.validation.base.ValidatorBase;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.helpers.StageCoordinator;
import javafx.beans.DefaultProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;

import java.rmi.RemoteException;

@DefaultProperty(value = "icon")
public class ExistingPhoneNumberValidator extends ValidatorBase {
    boolean shouldExist;

    public ExistingPhoneNumberValidator(String message) {
        super(message);
    }

    public ExistingPhoneNumberValidator(String message, boolean shouldExist) {
        this(message);
        this.shouldExist = shouldExist;
    }


    public ExistingPhoneNumberValidator() {
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
            if (userServices != null)
                registered = userServices.checkUserId(text);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }

        hasErrors.set(shouldExist != registered);
    }

}
