package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Objects;


/**
 * This is the Login Scene Controller. It handles the connection to the server.
 * @author AlessandroG
 * @version 1.0
 */
public class LoginSceneController implements SceneController {

    private Gui gui;
    private String ipAddress = "localhost"; //default value
    private int port = 2351; //default value

    @FXML
    private TextField serverAddressField;

    @FXML
    private TextField serverPortField;


    @FXML
    void onConnectButtonPressed(ActionEvent event) {
        //TODO add syntax controls
        ipAddress = serverAddressField.getText();
        if(!Objects.equals(serverPortField.getText(), ""))
            port = Integer.parseInt(serverPortField.getText());

        if ((serverAddressField.getText().trim().isEmpty() || serverAddressField.getText().trim().isBlank() &&
                serverPortField.getText().trim().isEmpty()) || serverPortField.getText().trim().isBlank()) {
            ipAddress = "localhost";
            port = 2351;
        }
        gui.adapter = new Adapter(gui, ipAddress, port);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void setMessage(Message message) { }

    @Override
    public void createScene() {

    }
}
