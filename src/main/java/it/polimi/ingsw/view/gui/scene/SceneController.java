package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.gui.Gui;

/**
 * This interface has a method shared by each specific SceneController needed for getting the GUI
 */
public interface SceneController {

    void setGui(Gui gui);

    void setMessage(Message message);

    void createScene();

}
