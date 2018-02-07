/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma.takin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Epulapp
 */
public class Agent extends Observable implements Runnable{

    private Grid grid;
    private Point position;
    private Point objective;
    private Point previousPosition;
    private String ident;
    private ConcurrentLinkedQueue<Message> messages;
    
    public Agent(Grid grid, String ident){
        this.grid = grid;
        this.ident = ident;
        this.previousPosition = null;
        this.messages = new ConcurrentLinkedQueue();
        Random r = new Random();
        
        
        do{
            position = new Point(r.nextInt(grid.getXMax()),r.nextInt(grid.getYMax()));
        }while(!grid.isFree(position));
        
        do{
            objective = new Point(r.nextInt(grid.getXMax()),r.nextInt(grid.getYMax()));
        }while(!grid.isFreeObjective(objective));
    }
    
    
    @Override
    public void run() {
        Boolean stop = false;
        while(!stop){
            Point decision = this.position;
            Boolean move = false;
            
            //create an arraylist with the possible next positions
            ArrayList<Point> positionsSuivantes = new ArrayList<>();
            if(position.y < grid.getYMax()-1)
                positionsSuivantes.add(new Point(position.x, position.y+1));
            if(position.y > 0)
                positionsSuivantes.add(new Point(position.x, position.y-1));
            if(position.x < grid.getXMax()-1)
                positionsSuivantes.add(new Point(position.x+1, position.y));
            if(position.x > 0)
                positionsSuivantes.add(new Point(position.x-1, position.y));
            //Shuffle arraylist to avoid infinite loop;
            Collections.shuffle(positionsSuivantes);
            
            //traitement des messages
            if(this.messages.size() > 0){
                Message message = messages.poll();
                if(message.getType() == MessageType.Move){
                    if(message.getPosition().equals(this.getPosition())) {
                        for(Point ps : positionsSuivantes){
                            if(grid.isAgent(ps) == null) {
                                decision = ps;
                                move = true;
                                previousPosition = null;
                                break;
                            }
                        }
                        if(this.objective.equals(this.position) && !move){
                            Agent agent = grid.isAgent(positionsSuivantes.get(0));
                            if(agent != null)
                                askMove(agent,positionsSuivantes.get(0));
                        }
                    }
                }
            }

            //choix de la direction
            //calcul des distances
            if(!this.objective.equals(this.position) && !move) {
                Collections.sort(positionsSuivantes, (Point o1, Point o2) -> {
                    double distanceO1 = o1.distance(objective);
                    double distanceO2 = o2.distance(objective);
                    return (int)(distanceO1-distanceO2);
                });

                //verification si la place est disponible et decision
                decision = this.position;
                for (Point p: positionsSuivantes){
                    if(previousPosition == null || !p.equals(previousPosition)){
                        Agent agent = grid.isAgent(p);
                        if(agent == null) {
                            decision = p;
                        } else {
                            askMove(agent,p);
                        }
                        break;
                    }
                }
            }
            
            //interaction
            this.previousPosition = this.position;
            this.position = decision;
            setChanged();
            notifyObservers();
            
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                stop = true;
            }
        }
        
        
        
    }
    
    public Point getPosition(){
        return position;
    }

    public Point getObjective() {
        return this.objective;
    }
    
    public String getIdent(){
        return this.ident;
    }
    
    public void addMessage(Message message) {
        this.messages.add(message);
    }
    
    public void askMove(Agent agent, Point position) {
        agent.addMessage(new Message(this,agent,MessageType.Move,position));
    }
    
}
