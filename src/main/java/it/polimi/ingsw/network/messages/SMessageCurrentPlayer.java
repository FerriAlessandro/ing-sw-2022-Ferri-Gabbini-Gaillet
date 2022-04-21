package it.polimi.ingsw.network.messages;

<<<<<<< HEAD
public class SMessageCurrentPlayer extends Message{

    private String nickName;

    public SMessageCurrentPlayer(String nickName){
        this.type = MessageType.S_PLAYER;
        this.nickName = nickName;
    }

    public String getNickName(){
        return this.nickName;
=======
public class SMessageCurrentPlayer extends SMessage{
    public String nickname;
    public SMessageCurrentPlayer(String nickname){
        super(MessageType.S_PLAYER);
        this.nickname = nickname;
>>>>>>> main
    }
}
