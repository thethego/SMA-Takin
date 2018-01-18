/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma.takin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Epulapp
 */
class Grid {
    
    private ArrayList<Agent> agents;
    private int xMax;
    private int yMax;
    
    
    public Grid(int nbAgent, int xMax, int yMax){
        //init grid
        Grid grid = this;
        this.xMax = xMax;
        this.yMax = yMax;
        agents = new ArrayList();
        for (int i = 0 ; i < nbAgent; i++){
            Agent ag = new Agent(this, i+"");
            agents.add(ag);
        }
        
    }

    public void run(){
        ArrayList<Thread> threads = new ArrayList();
        agents.forEach((agent) -> {
            Thread thread = new Thread(agent);
            threads.add(thread);
            thread.start();
        });
        while(!this.allOK()){
            System.out.println(this.toString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        threads.forEach((thread) -> {
            thread.interrupt();
        });
    }
    
    public boolean isFree(Point position){
        if(!isInside(position)){
            return false;
        }
        return agents.stream().noneMatch((a) -> (a.getPosition().equals(position)));
    }
    
    public Agent isAgent(Point position){
        
        if(!isInside(position)){
            return null;
        }
        
        for(Agent a:agents){
            if(a.getPosition().equals(position)){
                return a;
            }
        }
                
        return null;
    }
    
    public boolean isInside(Point position) {
        return !(position.x >= xMax || position.y >= yMax || position.x < 0 || position.y<0);
    }
    
    public boolean isFreeObjective(Point position){
            if(position.x >= xMax){
            return false;
        }
        
        if(position.y >= yMax){
            return false;
        }
        
        return agents.stream().noneMatch((a) -> (a.getObjective().equals(position)));
    }
    
    public int getXMax(){
        return xMax;
    }
    public int getYMax(){
        return yMax;
    }
    
    @Override
    public String toString(){
        
        StringBuilder sb = new StringBuilder();
        
        agents.stream().map((a) -> {
            sb.append(a.getIdent()).append(":").append(a.getObjective().toString());
            return a;
        }).forEachOrdered((_item) -> {
            sb.append("\n");
        });
        
        for(int i = 0 ; i < xMax; i++){
            for (int j = 0 ; j < yMax; j++){
                Agent a = isAgent(new Point (i,j));
                if(a!=null){
                    sb.append("[").append(a.getIdent()).append("]");
                    
                }else{
                    sb.append("[ ]");
                }
            }
            sb.append('\n');
        }
        
        
        return sb.toString();
    }
    
        
    public Boolean allOK(){
        return agents.stream().noneMatch((agent) -> (!agent.getPosition().equals(agent.getObjective())));
    }
    
}
