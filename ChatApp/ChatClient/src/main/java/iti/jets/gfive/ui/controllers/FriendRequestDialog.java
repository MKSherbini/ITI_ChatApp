package iti.jets.gfive.ui.controllers;

import iti.jets.gfive.ui.helpers.SceneData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendRequestDialog implements Initializable {
    @FXML
    ListView listView ;
    @FXML
    private Button btnOk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*listView.setCellFactory((Callback<ListView<User>, ListCell<User>>) param -> {
            return new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (user == null || empty) {
                        setText(null);
                    } else {
                        // Here we can build the layout we want for each ListCell. Let's use a HBox as our root.
                        HBox root = new HBox(10);
                        root.setAlignment(Pos.CENTER_LEFT);
                        root.setPadding(new Insets(5, 10, 5, 10));

                        // Within the root, we'll show the username on the left and our two buttons to the right
                        root.getChildren().add(new Label(user.getUsername()));

                        // I'll add another Region here to expand, pushing the buttons to the right
                        Region region = new Region();
                        HBox.setHgrow(region, Priority.ALWAYS);
                        root.getChildren().add(region);

                        // Now for our buttons
                        Button btnAddFriend = new Button("Add Friend");
                        btnAddFriend.setOnAction(event -> {
                            // Code to add friend
                            System.out.println("Added " + user.getUsername() + " as a friend!");
                        });
                        Button btnRemove = new Button("Remove");
                        btnRemove.setOnAction(event -> {
                            // Code to remove friend
                            System.out.println("Broke up with " + user.getUsername() + "!");
                        });
                        root.getChildren().addAll(btnAddFriend, btnRemove);

                        // Finally, set our cell to display the root HBox
                        setText(null);
                        setGraphic(root);
                    }

                }
            };

        });
*/
        //after the above we need to add items to the list view

    }
    @FXML
    public void applyOkPressed(ActionEvent actionEvent) {
//        Platform.exit();
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}
