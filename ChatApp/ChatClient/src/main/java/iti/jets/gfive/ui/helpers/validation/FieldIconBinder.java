package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

// todo fix the validation event to listen when submit button is clicked
public class FieldIconBinder {
    private static final FieldIconBinder instance = new FieldIconBinder();
    private final Map<JFXTextField, FontIcon> textFieldFontIconMap;
    private final Map<JFXPasswordField, FontIcon> passFieldFontIconMap;

    private FieldIconBinder() {
        textFieldFontIconMap = new HashMap<>();
        passFieldFontIconMap = new HashMap<>();
    }

    public static FieldIconBinder getInstance() {
        return instance;
    }

    public void bind(JFXTextField text, FontIcon icon) {
        textFieldFontIconMap.put(text, icon);
        text.focusedProperty().addListener((observable, oldValue, newVal) -> {
            if (newVal != null && icon != null)
                if (newVal) {
                    icon.setIconColor(text.getFocusColor());
                } else {
                    runValidationLater(text, icon);
                }
        });

        text.activeValidatorProperty().addListener((observable, oldValue, newVal) -> {
            if (newVal != null && newVal.getHasErrors()) {
                runValidationLater(text, icon);
            }
        });
    }

    public void bind(JFXPasswordField text, FontIcon icon) {
        passFieldFontIconMap.put(text, icon);
        text.focusedProperty().addListener((observable, oldValue, newVal) -> {
            if (newVal != null && icon != null)
                if (newVal) {
                    icon.setIconColor(text.getFocusColor());
                } else {
                    runValidationLater(text, icon);
                }
        });
        text.activeValidatorProperty().addListener((observable, oldValue, newVal) -> {
            if (newVal != null && newVal.getHasErrors()) {
                runValidationLater(text, icon);
            }
        });
    }

    public void resetValidation(JFXTextField field) {
        field.resetValidation();
        if (textFieldFontIconMap.containsKey(field))
            runValidationLater(field, textFieldFontIconMap.get(field), false);
    }

    public void resetValidation(JFXPasswordField field) {
        field.resetValidation();
        if (passFieldFontIconMap.containsKey(field))
            runValidationLater(field, passFieldFontIconMap.get(field), false);
    }

    private void runValidationLater(JFXTextField text, FontIcon icon) {
        runValidationLater(text, icon, true);
    }

    private void runValidationLater(JFXTextField text, FontIcon icon, boolean validate) {
        if (validate)
            text.validate(); // same logic as in validator
        Runnable task = () -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                icon.setIconColor(text.getUnFocusColor());
            });
        };
//                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//                    executorService.scheduleWithFixedDelay(task, 0, 100, TimeUnit.MILLISECONDS);
        new Thread(task).start();
    }

    private void runValidationLater(JFXPasswordField text, FontIcon icon) {
        runValidationLater(text, icon, true);
    }

    private void runValidationLater(JFXPasswordField text, FontIcon icon, boolean validate) {
        if (validate)
            text.validate(); // same logic as in validator
        Runnable task = () -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                icon.setIconColor(text.getUnFocusColor());
            });
        };
//                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//                    executorService.scheduleWithFixedDelay(task, 0, 100, TimeUnit.MILLISECONDS);
        new Thread(task).start();
    }
    //    public void bind(JFXDatePicker text, FontIcon icon) {
//        text.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                text.validate(); // same logic as in validator
//                icon.setIconColor(text.styleProperty());
//            } else {
//                icon.setIconColor(text.getUnFocusColor());
//            }
//        });
//    }
}
