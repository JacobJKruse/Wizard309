package com.example.mysqlconnectiontest.websocket;
import com.example.mysqlconnectiontest.Element.Element;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private float x;
    private float y;
    private int id;
    private String username;
    private String extension;
    public int type;
    private Element element;
    private int roomID;

    public Player(float startingX, float startingY, String username, int id, String extension, Element element, int type, int roomID){
        this.x = startingX;
        this.y = startingY;
        this.username = username;
        this.extension = extension;
        this.id = id;
        this.type = type;
        this.element = element;
        this.roomID = roomID;

    }



}
