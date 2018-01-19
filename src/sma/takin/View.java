package sma.takin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class View  implements Observer {
    
    JFrame jFrame;

    public View(Grid grid) {
        jFrame = new JFrame("Takin");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(new TestPane(grid));
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        jFrame.revalidate();
        jFrame.repaint();
    }

    public class TestPane extends JPanel{

        private int columnCount;
        private int rowCount;
        private Grid grid;
        private List<Rectangle> cells;

        public TestPane(Grid grid) {
            this.grid = grid;
            columnCount = grid.getXMax();
            rowCount = grid.getYMax();
            cells = new ArrayList<>(columnCount * rowCount);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(columnCount * 100, rowCount * 100);
        }

        @Override
        public void invalidate() {
            super.invalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();

            int cellWidth = width / columnCount;
            int cellHeight = height / rowCount;

            int xOffset = (width - (columnCount * cellWidth)) / 2;
            int yOffset = (height - (rowCount * cellHeight)) / 2;
            
            Agent agent = null;
            int fontSizeObjective = 20;
            int fontSizePos = 50;
            Font fontObjective = new Font("TimesRoman", Font.PLAIN, fontSizeObjective);
            Font fontPos = new Font("TimesRoman", Font.PLAIN, fontSizePos);
                     
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Point position = new Point(row,col);
                    int x = xOffset + (col * cellWidth);
                    int y = yOffset + (row * cellHeight);
                    Rectangle cell = new Rectangle(
                            x,
                            y,
                            cellWidth,
                            cellHeight);
                    g2d.setColor(Color.WHITE);
                    g2d.fill(cell);
                    cells.add(cell);
                    
                    g.setFont(fontObjective);
                    agent = grid.isAgentObjective(position);
                    if(agent != null) {
                        g.setColor(Color.BLUE);
                        g.drawString(agent.getIdent(), x+5, y+fontSizeObjective+5);
                    }
                    
                    g.setFont(fontPos);
                    agent = grid.isAgent(position);
                    if(agent != null) {
                        if(grid.allOK())
                            g.setColor(Color.GREEN);
                        else
                            g.setColor(Color.BLACK);
                        g.drawString(agent.getIdent(), x+(cellWidth/2)-10, y+(cellHeight/2)+20);
                    }
                }
            }

            g2d.setColor(Color.GRAY);
            cells.forEach((cell) -> {
                g2d.draw(cell);
            });

            g2d.dispose();
        }
    }
}