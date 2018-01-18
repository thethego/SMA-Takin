/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma.takin;

import java.awt.Point;

/**
 *
 * @author Epulapp
 */
public class Message {
    private Agent sender;
    private Agent receiver;
    private MessageType type;
    private Point position;

    public Message(Agent sender, Agent receiver, MessageType type, Point position) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.position = position;
    }
    
    public Agent getSender() {
        return sender;
    }

    public void setSender(Agent sender) {
        this.sender = sender;
    }

    public Agent getReceiver() {
        return receiver;
    }

    public void setReceiver(Agent receiver) {
        this.receiver = receiver;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
   
    
}
