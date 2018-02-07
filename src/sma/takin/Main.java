/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma.takin;

/**
 *
 * @author Epulapp
 */
public class Main {
    
    private static final int nbAgents = 90;
    private static final int NbRow = 10;
    private static final int nbCol = 10;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Grid grid = new Grid(nbAgents,NbRow,nbCol);
        View v = new View(grid);
        grid.getAgents().forEach((agent) -> {
            agent.addObserver(v);
        });
        System.out.print(grid.toString());
        grid.run();
    }
    
}
