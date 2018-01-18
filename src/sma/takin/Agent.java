/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma.takin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Epulapp
 */
public class Agent implements Runnable{

    private Grid grid;
    private Point position;
    private Point objective;
    private Point previousPosition;
    private String ident;
    
    public Agent(Grid grid, String ident){
        this.grid = grid;
        this.ident = ident;
        this.previousPosition = null;
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
        while(true){
            //traitement des messages (v2)

            //verification de la resolution
            if(this.objective.equals(this.position)){
                return;
            }

            //choix de la direction
            //calcul des distances
            ArrayList<Point> positionsSuivantes = new ArrayList<>();
            positionsSuivantes.add(new Point(position.x, position.y+1));
            positionsSuivantes.add(new Point(position.x, position.y-1));
            positionsSuivantes.add(new Point(position.x+1, position.y));
            positionsSuivantes.add(new Point(position.x-1, position.y));
            
            Collections.sort(positionsSuivantes, (Point o1, Point o2) -> {
                double distanceO1 = o1.distance(objective);
                double distanceO2 = o2.distance(objective);
                
                return (int)(distanceO1-distanceO2);
            });
            
            //verification si la place est disponible et decision
            Point decision = this.position;
            for (Point p: positionsSuivantes){
                if(grid.isFree(p) && !p.equals(this.previousPosition)){
                    decision = p;
                    break;
                }
            }
            
            //interaction
            this.previousPosition = this.position;
            this.position = decision;
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
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

}
