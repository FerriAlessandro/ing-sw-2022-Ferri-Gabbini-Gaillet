package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


public class NewGameSceneController implements SceneController {

    private Gui gui;

    @FXML
    private Button backButton;

    @FXML
    private Button confirmButton;

    @FXML
    private MenuButton gameModeButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private ImageView newGameImageView;

    @FXML
    private TextField nicknameField;

    @FXML
    private MenuButton numOfPlayersButton;

    @FXML
    private Button confirmNicknameButton;

    private boolean askedToLoad = false;


    @FXML
    public void initialize(){
        backButton.setVisible(false);
        confirmButton.setVisible(false);
        confirmNicknameButton.setVisible(false);
        gameModeButton.setVisible(false);
        loadGameButton.setVisible(false);
        newGameButton.setVisible(true);
        newGameImageView.setVisible(true);
        nicknameField.setVisible(false);
        numOfPlayersButton.setVisible(false);


        confirmNicknameButton.setOnAction((event) -> {
            if(!nicknameField.getText().equals("")) {
                gui.adapter.sendMessage(new RMessageNickname(nicknameField.getText()));
                gui.setNickname(nicknameField.getText());
            }
        });


    }

    public void askUseSavedGame(SMessageLoadGame message) {

        String mode;
        if(message.expert)
            mode = "Expert";
        else mode = "Easy";
        askedToLoad = true;


        loadGameButton.setVisible(true);
        loadGameButton.setOnAction((event) -> {
                backButton.setVisible(true);
                confirmButton.setOnAction(null);
                numOfPlayersButton.setText(Integer.toString(message.numOfPlayers));
                gameModeButton.setText(mode);
                newGameButton.setVisible(false);
                loadGameButton.setVisible(false);
                numOfPlayersButton.setVisible(true);
                gameModeButton.setVisible(true);
                confirmButton.setVisible(true);
                confirmButton.setOnAction((event2) -> { //if the user clicked "load game" the confirm button will send "true" to the server
                    gui.adapter.sendMessage(new RMessageLoadGame(true));
                });

            });

        newGameButton.setOnAction(null);
        newGameButton.setOnAction((e)-> {
            gui.adapter.sendMessage(new RMessageLoadGame(false));
            numOfPlayersButton.setText("Players");
            gameModeButton.setText("Mode");
            numOfPlayersButton.getItems().add(new MenuItem("2"));
            numOfPlayersButton.getItems().add(new MenuItem("3"));

            for(MenuItem m : numOfPlayersButton.getItems())
                m.setOnAction((e2)-> numOfPlayersButton.setText(m.getText()));
            gameModeButton.getItems().add(new MenuItem("Easy"));
            gameModeButton.getItems().add(new MenuItem("Expert"));

            for(MenuItem m : gameModeButton.getItems())
                m.setOnAction((e3) -> gameModeButton.setText(m.getText()));

            confirmButton.setOnAction((e4) -> {
                if(!numOfPlayersButton.getText().equals("Players") && !gameModeButton.getText().equals("Mode"))
                    gui.adapter.sendMessage(new RMessageGameSettings(Integer.parseInt(numOfPlayersButton.getText()), gameModeButton.getText().equals("Expert")));
            });
            backButton.setVisible(false);
            newGameButton.setVisible(false);
            loadGameButton.setVisible(false);
            gameModeButton.setVisible(true);
            numOfPlayersButton.setVisible(true);
            confirmButton.setVisible(true);
        });

        backButton.setOnAction((event)->{
            loadGameButton.setVisible(true);
            newGameButton.setVisible(true);
            numOfPlayersButton.setVisible(false);
            gameModeButton.setVisible(false);
            confirmButton.setVisible(false);
            numOfPlayersButton.setText("Players");
            gameModeButton.setText("Mode");
            backButton.setVisible(false);
        });


    }


    public void askGameSettings(){
        if(!askedToLoad) {
            newGameButton.setOnAction(null);
            newGameButton.setOnAction((e) -> {
                confirmButton.setOnAction(null);
                numOfPlayersButton.getItems().add(new MenuItem("2"));
                numOfPlayersButton.getItems().add(new MenuItem("3"));

                for (MenuItem m : numOfPlayersButton.getItems())
                    m.setOnAction((e2) -> numOfPlayersButton.setText(m.getText()));

                gameModeButton.getItems().add(new MenuItem("Easy"));
                gameModeButton.getItems().add(new MenuItem("Expert"));

                for (MenuItem m : gameModeButton.getItems())
                    m.setOnAction((e3) -> numOfPlayersButton.setText(m.getText()));

                confirmButton.setOnAction((e4) -> {
                    if (!numOfPlayersButton.getText().equals("Players") && !gameModeButton.getText().equals("Mode"))
                        gui.adapter.sendMessage(new RMessageGameSettings(Integer.parseInt(numOfPlayersButton.getText()), gameModeButton.getText().equals("Expert")));
                });

                newGameButton.setVisible(false);
                loadGameButton.setVisible(false);
                numOfPlayersButton.setVisible(true);
                gameModeButton.setVisible(true);
                confirmButton.setVisible(true);
            });
        }

    }

    public void askNickName(){
        newGameButton.setVisible(false);
        loadGameButton.setVisible(false);
        numOfPlayersButton.setVisible(false);
        gameModeButton.setVisible(false);
        confirmButton.setVisible(false);
        nicknameField.setVisible(true);
        confirmNicknameButton.setVisible(true);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui= gui;
    }

    @Override
    public void setMessage(Message message) {

    }

    @Override
    public void createScene() {

    }
}
