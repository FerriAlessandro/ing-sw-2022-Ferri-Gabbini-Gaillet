package it.polimi.ingsw.network.messages;

public class SMessageWin extends Message{

    String winner;

    public SMessageWin(String winnerNickname){
        this.type = MessageType.S_WIN;
        winner = winnerNickname;
    }
}
