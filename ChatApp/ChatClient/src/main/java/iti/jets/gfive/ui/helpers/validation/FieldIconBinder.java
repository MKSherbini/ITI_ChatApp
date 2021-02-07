package iti.jets.gfive.ui.helpers.validation;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import org.kordamp.ikonli.javafx.FontIcon;

// todo fix the validation event to listen when submit button is clicked
public class FieldIconBinder {
    private static final FieldIconBinder instance = new FieldIconBinder();

    private FieldIconBinder() {
    }

    public static FieldIconBinder getInstance() {
        return instance;
    }

    public void bind(JFXTextField text, FontIcon icon) {
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

    private void runValidationLater(JFXTextField text, FontIcon icon) {
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

    public void bind(JFXPasswordField text, FontIcon icon) {
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

    private void runValidationLater(JFXPasswordField text, FontIcon icon) {
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
