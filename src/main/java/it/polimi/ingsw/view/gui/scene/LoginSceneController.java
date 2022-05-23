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

    @FXML
    private TextField serverAddressField;

    @FXML
    private TextField serverPortField;


    @FXML
    void onConnectButtonPressed(ActionEvent event) {
        int port = 2351; //default value
        String ipAddress = "localhost"; //default
        //TODO add syntax controls
        //default value
        if(!Objects.equals(serverAddressField.getText(), ""))
            ipAddress = serverAddressField.getText();
        if(!Objects.equals(serverPortField.getText(), ""))
            port = Integer.parseInt(serverPortField.getText());

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
