/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import algosimmain.AlgoSimMain;
import java.util.Stack;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author ASUS
 */
public class DFS {

    private Stack<Integer> stack;
    AlgoSimMain main;
    Paint[] color={Color.YELLOW,Color.VIOLET,Color.CHARTREUSE,Color.DIMGRAY,Color.CHOCOLATE};
    public static int idx=0;
    

    public DFS(AlgoSimMain main) {
        stack = new Stack<Integer>();
        this.main = main;
    }

    public void dfs(int adjacency_matrix[][], int source) throws InterruptedException {
        int number_of_nodes = adjacency_matrix[source].length - 1;

        boolean visited[] = new boolean[number_of_nodes + 1];
        int[] level = new int[number_of_nodes + 1];
        
        sideshow();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int element = source;
                int i = source;
                level[source]=0;
                System.out.print(element + "\t");
                visited[source] = true;
                stack.push(source);
                animate(main.ball[source], main.ball[source],level[source]);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DFS.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (!stack.isEmpty()) {
                    element = stack.peek();
                    i = element;
                    while (i <= number_of_nodes) {
                        if (adjacency_matrix[element][i] == 1 && !visited[i]) {
                            stack.push(i);
                            level[i] = level[element] + 1;
                            animate(main.ball[i], main.ball[element],level[i]);
                            visited[i] = true;
                            element = i;
                            i = 1;
                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DFS.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            continue;
                        }
                        i++;
                    }
                    stack.pop();
                }
            }
        });
        t.start();

    }

    void animate(Circle circ, Circle circ2,int n) {
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
    
    void sideshow()
    {
        Rectangle[] rect=new Rectangle[5];
        for(int i=0;i<5;i++)
        {
            rect[i]=new Rectangle(30, 30, color[i]);
            rect[i].setX(540);
            rect[i].setY(40*i+90);
            Text txt=new Text(480, 40*i+105, "Level "+ i);
            main.grp2.getChildren().addAll(rect[i],txt);
        }
    }

}
