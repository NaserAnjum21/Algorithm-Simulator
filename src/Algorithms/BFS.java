/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

/**
 *
 * @author Samudro
 */
import algosimmain.AlgoSimMain;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BFS {

    private Queue<Integer> queue;
    //int[] level;
    AlgoSimMain main;
    Paint[] color = {Color.YELLOW, Color.VIOLET, Color.CHARTREUSE, Color.DIMGRAY, Color.CHOCOLATE};
    public int[] level_count=new int[10];
    public static int idx=0;
    public int maxlev;
    public int[] level;

    public BFS(AlgoSimMain main) {
        queue = new LinkedList<Integer>();
        this.main = main;
    }

    public void bfs(int adjacency_matrix[][], int source) {
        int number_of_nodes = adjacency_matrix[source].length - 1;
        boolean[] visited = new boolean[number_of_nodes + 1];
        level = new int[number_of_nodes + 1];
        sideshow();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i, element;
                visited[source] = true;
                level[source] = 0;
                queue.add(source);
                animate(main.ball[source], main.ball[source], level[source]);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (!queue.isEmpty()) {
                    element = queue.remove();
                    for (i = 0; i < adjacency_matrix[element].length; i++) {
                        int adj_node = adjacency_matrix[element][i];
                        if (adj_node != 0 && !visited[i]) {
                            queue.add(i);
                            visited[i] = true;
                            level[i] = level[element] + 1;
                            animate(main.ball[i], main.ball[element], level[i]);
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        });
        t.start();

    }

    public int bfs_test(int adjacency_matrix[][], int source) {

        int number_of_nodes = adjacency_matrix[source].length - 1;
        int maxlevel = 0;

        boolean[] visited = new boolean[number_of_nodes + 1];
        level = new int[number_of_nodes + 1];

        int i, element;
        visited[source] = true;
        level[source] = 0;
        queue.add(source);

        while (!queue.isEmpty()) {
            element = queue.remove();
            for (i = 0; i < adjacency_matrix[element].length; i++) {
                int adj_node = adjacency_matrix[element][i];
                if (adj_node != 0 && !visited[i]) {
                    queue.add(i);
                    visited[i] = true;
                    level[i] = level[element] + 1;
                    if (level[i] > maxlevel) {
                        maxlevel = level[i];
                    }
                }

            }
        }
        
        for(i=0;i<=maxlevel;i++)
        {
            int cnt=0;
            for(int k=0;k<number_of_nodes;k++)
            {
                if(level[k]==i)
                {
                    cnt++;
                }
            }
            level_count[i]=cnt;
        }
        maxlev=maxlevel+1;
        return maxlevel+1;
    }

    void animate(Circle circ, Circle circ2, int n) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FillTransition ft = new FillTransition(Duration.millis(2000), circ, Color.RED, (Color) color[n]);
                ScaleTransition st = new ScaleTransition(Duration.millis(1000), circ);
                st.setByX(1.1f);
                st.setByY(1.1f);
                st.setAutoReverse(true);
                st.setCycleCount(2);

                ft.play();
                st.play();

                Line line = new Line(circ.getCenterX(), circ.getCenterY(), circ2.getCenterX(), circ2.getCenterY());
                line.setStrokeWidth(2);
                line.getStrokeDashArray().addAll(5d, 5d, 5d, 5d, 5d);
                line.setStrokeDashOffset(10);
                line.setStrokeLineCap(StrokeLineCap.SQUARE);
                main.lines.add(line);

                ft.setOnFinished(event -> main.grp2.getChildren().add(main.lines.get(idx++)));
                
            }
        });
        t.start();
    }

    void sideshow() {
        Rectangle[] rect = new Rectangle[5];
        for (int i = 0; i < 5; i++) {
            rect[i] = new Rectangle(30, 30, color[i]);
            rect[i].setX(540);
            rect[i].setY(40 * i + 90);
            Text txt = new Text(480, 40 * i + 105, "Level " + i);
            main.grp2.getChildren().addAll(rect[i], txt);
        }
    }
}
