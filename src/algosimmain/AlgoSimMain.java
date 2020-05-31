/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algosimmain;

import Algorithms.BFS;
import Algorithms.BinSearch;
import Algorithms.DFS;
import Utility.Data;
import Utility.NetworkUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author ASUS
 */
public class AlgoSimMain extends Application implements Runnable {

    BFS bfsobj;
    DFS dfsobj;
    //Rectangle[] rect = new Rectangle[20];
    public List<Rectangle> rect = new ArrayList<Rectangle>();
    public List<Line> lines = new ArrayList<Line>();
    public Rectangle[] box = new Rectangle[10];
    double[][] rectpos = new double[5][4];
    double[] height = new double[20];
    double[] px;
    int[] input;
    int[][] adj_matrix = new int[10][10];
    Timeline[] timeline = new Timeline[20];
    int idx = 0;
    int len;
    int sortnum;
    HashMap<Integer, Rectangle> mp = new HashMap<>();
    boolean network = false;
    boolean rect_shown = false;

    public static ServerSocket servsock;
    public NetworkUtil nc;

    ParallelTransition pt = new ParallelTransition();
    Button bfsbtn, bsortbtn, sort, btn, showbtn, sesortbtn, dfsbtn, netbtn, traverse, showball, predef, traversedfs, reset, resetgraph, search, backsort, backgraph;
    Label lblscene1, lblscene2;
    public TextArea edgeta, description;
    public Text t1, t2, t3;
    Pane pane1, pane2;
    GridPane bsortpane;
    StackPane stpane;
    Pane root;
    Scene bsortscene, bfsscene, homescene, dfsscene;
    Stage stage;
    public Group group, grp2, grp3;
    public Circle[] ball = new Circle[20];
    public Circle[] ball2 = new Circle[20];
    public Line[] line;
    TextField numfield, arrfield, nodefield, searchfield;
    boolean stop = false;
    boolean animate = false;
    boolean sorted = false;
    String message;

    int columns = 5, rows = 10, dy = 200, dx = 50;
    int node, edge;
    double width = 50;

    double[] posx = new double[5];
    int xpos = 50;
    int ypos = 50;

    @Override
    public void start(Stage primaryStage) throws IOException {
        servsock = new ServerSocket(44444);
        Thread st = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket clientSock = servsock.accept();
                        nc = new NetworkUtil(clientSock);
                        message = (String) nc.read();
                        //nc.write(this);
                        System.out.println(message);
                    } catch (IOException ex) {
                        Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        st.start();

        Image image = new Image("File:images//SimulatorHome.png", true);
        ImageView imv = new ImageView(image);

        Image image2 = new Image("File:images//Sorting.jpg", true);
        Image image3 = new Image("File:images//Graph.png", true);
        Image image4 = new Image("File:images//Light.jpg", true);
        Image image5 = new Image("File:images//Light2.jpg", true);
        ImageView imv2 = new ImageView(image2);
        ImageView imv4 = new ImageView(image4);
        ImageView imv5 = new ImageView(image5);

        stage = primaryStage;
        bfsbtn = new Button();
        dfsbtn = new Button();
        bsortbtn = new Button();
        sesortbtn = new Button();
        createButton(bfsbtn, "", 520, 200);
        createButton(bsortbtn, "", 200, 200);
        bsortbtn.setGraphic(imv2);
        bfsbtn.setGraphic(new ImageView(image3));

        //btn.setText("Say 'Hello World'");
        //graphMatrix(adj_matrix);
        Thread t = new Thread(this);
        t.start();

        click_action(dfsbtn);  //function for setOnAction()
        click_action(bfsbtn);
        click_action(bsortbtn);

        pane1 = new Pane();
        //pane1.setVgap(100);
        pane1.getChildren().addAll(imv, bfsbtn, bsortbtn);
        pane1.setStyle("-fx-background-color: white;-fx-padding: 10px;");

        bsortpane = new GridPane();
        bsortpane.setStyle("-fx-background-color: red;-fx-padding: 10px;");

        homescene = new Scene(pane1, 1240, 540);
        bsortscene = new Scene(bsortpane, 500, 450);

        group = new Group();
        grp2 = new Group();
        grp3 = new Group();

        btn = new Button();
        netbtn = new Button();
        showbtn = new Button();
        showball = new Button();
        traverse = new Button();
        traversedfs = new Button();
        predef = new Button();
        reset = new Button();
        resetgraph = new Button();
        search = new Button();
        backgraph = new Button();
        backsort = new Button();

        createButton(btn, " BubbleSort", 60, 70);
        createButton(sesortbtn, " SelectionSort", 50, 100);
        createButton(netbtn, "Get From Client", 240, 70);
        createButton(backgraph, "Back", 5, 5);
        createButton(backsort, "Back", 5, 5);
        createButton(showbtn, "Show Rectangles", 140, 70);
        createButton(reset, "Reset", 140, 100);
        createButton(search, " Binary Search", 190, 100);
        createButton(showball, "Show Graph", 200, 75);
        createButton(traverse, "Traverse BFS", 285, 45);
        createButton(traversedfs, "Traverse DFS", 285, 75);
        createButton(predef, "Pre Defined Graph", 380, 45);
        createButton(resetgraph, "Reset", 380, 75);

        numfield = new TextField();
        numfield.relocate(100, 10);
        //numfield.setStyle("-fx-background-color: grey;");
        arrfield = new TextField();
        arrfield.relocate(100, 40);
        searchfield = new TextField();
        searchfield.relocate(340, 40);
        description = new TextArea();
        description.relocate(120, 150);
        description.setPrefSize(250, 30);

        Text text1 = new Text(50, 30, "Size");
        Text text2 = new Text(50, 60, "Elements");
        Text text3 = new Text(270, 60, "Num to Find");
        Text text4 = new Text(50, 165, "Description");
        Text text5 = new Text(20, 50, "Num of Nodes\n & edges");
        Text text6 = new Text(50, 85, "Edges");

        nodefield = new TextField();
        edgeta = new TextArea();
        edgeta.setPrefSize(100, 30);
        nodefield.relocate(100, 45);
        edgeta.relocate(100, 70);

        final Tooltip tooltip = new Tooltip();
        tooltip.setText("The Inputs should not be negative");

        arrfield.setTooltip(tooltip);

        group.getChildren().addAll(imv5, btn, numfield, arrfield, showbtn, netbtn, reset, search, searchfield, sesortbtn, backsort, description, text3, text1, text2, text4);
        grp2.getChildren().addAll(imv4, edgeta, nodefield, showball, traverse, predef, traversedfs, resetgraph, backgraph, text5, text6);

        click_action(btn);
        click_action(sesortbtn);
        click_action(showbtn);
        click_action(netbtn);
        click_action(reset);
        click_action(backsort);
        click_action(backgraph);
        click_action(search);
        click_action(traverse);
        click_action(traversedfs);
        click_action(resetgraph);
        click_action(showball);
        click_action(predef);

        bsortscene = new Scene(group, 700, 650);
        bfsscene = new Scene(grp2, 1024, 685);
        dfsscene = new Scene(grp3, 700, 650);

        stage.setTitle("Algorithm Simulator");

        stage.setScene(homescene);

        stage.show();
        //stage.setFullScreen(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        launch(args);
    }

    public void ButtonClicked(ActionEvent e) throws InterruptedException {

        if (e.getSource() == reset) {

            for (Rectangle temp : rect) {
                group.getChildren().remove(temp);
            }
            rect_shown = false;
            sorted = false;
            search_extra(false);
            description.clear();
            if (network) {
                nc.write(new Data(height, width, px, len, 3));
            }
        }

        if (e.getSource() == backsort) {
            for (Rectangle temp : rect) {
                group.getChildren().remove(temp);
            }
            rect_shown = false;
            sorted = false;
            description.clear();
            stage.setScene(homescene);
        }

        if (e.getSource() == resetgraph || e.getSource() == backgraph) {
            reset_graph();
            if (e.getSource() == backgraph) {
                stage.setScene(homescene);
            }
        }

        if (e.getSource() == netbtn) {
            network = true;
            String sarray = message;
            String[] stemp = sarray.split(" ");
            String str = String.valueOf(stemp.length);
            numfield.setText(str);
            arrfield.setText(sarray);
        }

        if (e.getSource() == showball) {
            String edges = edgeta.getText();
            String Nodes = nodefield.getText();
            System.out.println(Nodes);
            System.out.println(edges);
            String[] str = Nodes.split(" ");
            node = Integer.parseInt(str[0]);
            edge = Integer.parseInt(str[1]);
            line = new Line[edge];
            String[] str2 = edges.split("\n");
            for (int i = 0; i < str2.length; i++) {
                System.out.println(str2[i]);
                String[] temp = str2[i].split(" ");
                int v1 = Integer.parseInt(temp[0]);
                int v2 = Integer.parseInt(temp[1]);
                adj_matrix[v1][v2] = 1;
            }
            BFS testobj = new BFS(this);
            int temp = testobj.bfs_test(adj_matrix, 0);
            drawCircle(node, 1, testobj);
        }

        if (e.getSource() == predef) {
            graphMatrix(adj_matrix);
            line = new Line[6];
            drawCircle(7, 1);
            stage.setScene(bfsscene);
        }

        if (e.getSource() == traverse) {
            for (int i = 0; i < line.length; i++) {
                grp2.getChildren().remove(line[i]);
            }
            bfsobj = new BFS(this);
            bfsobj.bfs(adj_matrix, 0);
            stage.setScene(bfsscene);
        }

        if (e.getSource() == traversedfs) {
            for (int i = 0; i < line.length; i++) {
                grp2.getChildren().remove(line[i]);
            }
            dfsobj = new DFS(this);
            dfsobj.dfs(adj_matrix, 0);
            stage.setScene(bfsscene);
        }

        if (e.getSource() == bfsbtn) {
            //drawCircle(7, 1);
            stage.setScene(bfsscene);
        }
        if (e.getSource() == dfsbtn) {
            //drawCircle(7, 1);
            stage.setScene(dfsscene);
        }
        if (e.getSource() == bsortbtn) {

            sortnum = 1;
            stage.setScene(bsortscene);

        }
//        if (e.getSource() == sesortbtn) {
//            sortnum = 2;
//            stage.setScene(bsortscene);
//        }
        if (e.getSource() == search) {

            if (!sorted) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Sort the array first");
                alert.setContentText("");

                alert.showAndWait();
            } else {
                BinSearch bs = new BinSearch(this);
                search_extra(true);
                //stage.setScene(bsortscene);
                String str = searchfield.getText();
                int num = Integer.parseInt(str);
                //Thread.sleep(5000);
                bs.search(input, num);
            }

        }
        if (e.getSource() == showbtn && !rect_shown) {
            rect_shown = true;
            System.out.println("In show");

            String sarray = new String();

            if (network == false) {

                String slen = numfield.getText();
                len = Integer.parseInt(slen);
                sarray = arrfield.getText();
            } else {
                sarray = message;
                String[] stemp = sarray.split(" ");
                String str = String.valueOf(stemp.length);
                System.out.println(sarray);

            }
            String[] snum = sarray.split(" ");
            if (network) {
                len = snum.length;
            }
            input = new int[len];
            px = new double[len];

            for (int i = 0; i < len; i++) {
                input[i] = Integer.parseInt(snum[i]);
                System.out.print(input[i] + " ");
            }

            calcHeight(input);

            draw_rect();

            if (network) {
                nc.write(new Data(height, width, px, len, 0));
            }

            stage.setScene(bsortscene);
        }
        if (e.getSource() == btn && sortnum == 1) {
            bubble_sort();
            if (network) {
                nc.write(new Data(height, width, px, len, 1));
            }
        }

        if (e.getSource() == sesortbtn) {

            selection_sort();
            if (network) {
                nc.write(new Data(height, width, px, len, 2));
            }
            //bs.sort(rect, rectpos, this);
        }

    }

    @Override
    public void run() {
        System.out.println("Here");

    }

    void calcHeight(int[] input) {
        int max = input[0];
        int min = input[0];
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
            }

            if (input[i] < min) {
                min = input[i];
            }
        }
        for (int i = 0; i < input.length; i++) {
            height[i] = (double) (300.0 / max) * (double) input[i];
            System.out.print(height[i] + " ");
        }
    }

    void draw_rect() throws InterruptedException {

        for (int i = 0; i < len; i++) {
            rect.add(i, new Rectangle(width, height[i], Color.BLUEVIOLET));
            px[i] = 60 * i + 60;
            rect.get(i).setTranslateX(px[i]);
            rect.get(i).setTranslateY(270);
            group.getChildren().add(rect.get(i));
        }
    }

    void graphMatrix(int[][] adj_matrix) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                adj_matrix[i][j] = 0;
            }
        }
        adj_matrix[0][1] = 1;
        adj_matrix[0][2] = 1;
        adj_matrix[1][3] = 1;
        adj_matrix[1][4] = 1;
        adj_matrix[2][5] = 1;
        adj_matrix[2][6] = 1;

    }

    void reset_sort() {

    }

    void reset_graph() {
        if (line != null) {
            for (Line temp : line) {
                grp2.getChildren().remove(temp);
            }
        }

        if (ball != null) {
            for (Circle temp : ball) {
                grp2.getChildren().remove(temp);
            }
        }

        for (int[] x : adj_matrix) {
            for (int y : x) {
                y = 0;
            }
        }
        for (Iterator<Line> it = lines.iterator(); it.hasNext();) {
            Line temp = it.next();
            grp2.getChildren().remove(temp);
            //rect_shown = false;
            //sorted = false;
        }
    }

    void createButton(Button button, String str, double x, double y) {

        button.setText(str);
        button.relocate(x, y);
    }

    void drawCircle(int n, int a) {
        double[] ballx = new double[n];
        double[] bally = new double[n];
        double centerx = 250;
        double centery = 155;
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                ballx[i] = centerx;
                bally[i] = centery;
            } else if (i > 0 && i <= 2) {
                if (i % 2 == 1) {
                    ballx[i] = centerx - (centerx / 4.0);
                } else {
                    ballx[i] = centerx + (centerx / 4.0);
                }
                bally[i] = centery + 70;
            } else {
                ballx[i] = 100 + (i - 3) * (100);
                bally[i] = centery + 140;
            }
        }
        for (int i = 0; i < n; i++) {
            if (a == 1) {
                ball[i] = new Circle(ballx[i], bally[i], 15, Color.RED);
            } else {
                ball2[i] = new Circle(ballx[i], bally[i], 15, Color.RED);
            }
            //ball[i].setMouseTransparent(true);
            if (a == 1) {
                grp2.getChildren().add(ball[i]);
            } else {
                grp3.getChildren().add(ball2[i]);
            }
        }
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adj_matrix[i][j] == 1) {
                    line[idx] = new Line(ball[i].getCenterX(), ball[i].getCenterY(), ball[j].getCenterX(), ball[j].getCenterY());
                    line[idx].setStrokeWidth(1.5);
                    grp2.getChildren().add(line[idx]);
                    idx++;
                }
            }
        }

    }

    void search_extra(boolean bool) {

        if (bool) {
            t1 = new Text(470, 115, "Start");
            t2 = new Text(470, 165, "Mid");
            t3 = new Text(470, 215, "End");
            box[0] = new Rectangle(500, 100, 40, 40);
            box[0].setFill(Color.YELLOW);
            box[1] = new Rectangle(500, 150, 40, 40);
            box[1].setFill(Color.STEELBLUE);
            box[2] = new Rectangle(500, 200, 40, 40);
            box[2].setFill(Color.DARKGREEN);

            group.getChildren().addAll(box[0], box[1], box[2], t1, t2, t3);
        } else {
            group.getChildren().remove(box[0]);
            group.getChildren().remove(box[1]);
            group.getChildren().remove(box[2]);
            group.getChildren().remove(t1);
            group.getChildren().remove(t2);
            group.getChildren().remove(t3);
        }
    }

    void bubble_sort() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < len; i++) {
                    for (int j = 0; j < len - i; j++) {
                        System.out.println("Loop " + i + " " + j);
                        description.clear();
                        description.setText("Comparison between " + input[j] + " and " + input[j + 1]);
                        colorchange(j, j + 1);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (input[j] > input[j + 1]) {
                            System.out.println("Height-" + j + " " + rect.get(j).getHeight() + " " + rect.get(j + 1).getHeight());
                            description.appendText("\n Swapping " + input[j] + " and " + input[j + 1]);
                            int temp = input[j];
                            input[j] = input[j + 1];
                            input[j + 1] = temp;
                            animate(j, j + 1, 1000);
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Collections.swap(rect, j, j + 1);
                        }
                    }
                }
                description.clear();
                description.setText("SORTED");

            }
        });

        t.start();
        sorted = true;
        stage.setScene(bsortscene);
    }

    void selection_sort() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < len - 1; i++) {
                    for (int j = i + 1; j < len; j++) {
                        System.out.println("Loop " + i + " " + j);
                        description.clear();
                        description.setText("Comparison between " + input[i] + " and " + input[j]);
                        colorchange(i, j);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (input[i] > input[j]) {
                            System.out.println("Height-" + j + " " + rect.get(i).getHeight() + " " + rect.get(j).getHeight());
                            description.appendText("\n Swapping " + input[i] + " and " + input[j]);
                            int temp = input[i];
                            input[i] = input[j];
                            input[j] = temp;
                            animate(i, j, 1000);
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Collections.swap(rect, i, j);
                        }
                    }
                }
                description.clear();
                description.setText("SORTED");

            }
        });

        t.start();
        sorted = true;
        stage.setScene(bsortscene);
    }

    void drawCircle(int n, int a, BFS bfs) {
        double[] ballx = new double[n];
        double[] bally = new double[n];
        double centerx = 250;
        double centery = 155;
        int layer = bfs.maxlev;

        for (int i = 0; i < layer; i++) {
            int temp = bfs.level_count[i];
            double x = 500 / (double) (temp + 1);
            double y = centery + (i * 70);
            int cnt = 0;
            for (int j = 0; j < n; j++) {

                if (bfs.level[j] == i) {
                    cnt++;
                    ballx[j] = 10 + (double) (x * cnt);
                    bally[j] = y;
                    System.out.println(j + " " + cnt);
                }
            }

        }
        ballx[0] = 250;
        for (int i = 0; i < n; i++) {
            if (a == 1) {
                ball[i] = new Circle(ballx[i], bally[i], 15, Color.RED);
            } else {
                ball2[i] = new Circle(ballx[i], bally[i], 15, Color.RED);
            }
            //ball[i].setMouseTransparent(true);
            if (a == 1) {
                grp2.getChildren().add(ball[i]);
            } else {
                grp3.getChildren().add(ball2[i]);
            }
        }

        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adj_matrix[i][j] == 1) {
                    line[idx] = new Line(ball[i].getCenterX(), ball[i].getCenterY(), ball[j].getCenterX(), ball[j].getCenterY());
                    line[idx].setStrokeWidth(1.5);
                    grp2.getChildren().add(line[idx]);
                    idx++;
                }
            }
        }

    }

    void animate(int a, int b, int c) {

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                TranslateTransition translateTransition = new TranslateTransition();
                TranslateTransition translateTransition2 = new TranslateTransition();

                double x1 = rect.get(a).getTranslateX();
                double x2 = rect.get(b).getTranslateX();

                translateTransition.setNode(rect.get(a));
                translateTransition.setDuration(Duration.millis(c));
                translateTransition.setToX(x2);

                translateTransition2.setNode(rect.get(b));
                translateTransition2.setDuration(Duration.millis(c));
                translateTransition2.setToX(x1);

                pt.getChildren().addAll(translateTransition, translateTransition2);
                pt.play();

                System.out.println(a + " -> " + rect.get(a).getTranslateX() + " " + b + " -> " + rect.get(b).getTranslateX() + " ");

            }

        });

        t2.start();
    }

    void colorchange(int a, int b) {
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                FillTransition ft = new FillTransition(Duration.millis(800), rect.get(a), Color.BLUEVIOLET, Color.CORAL);
                FillTransition ft2 = new FillTransition(Duration.millis(800), rect.get(b), Color.BLUEVIOLET, Color.CORAL);
                ft.setAutoReverse(true);
                ft.setCycleCount(2);
                ft2.setAutoReverse(true);
                ft2.setCycleCount(2);
                ft.play();
                ft2.play();
//                ParallelTransition pt = new ParallelTransition(ft, ft2);
//                pt.play();
            }
        });
        t3.start();

    }

    void click_action(Button button) {
        button.setOnAction((ActionEvent e) -> {
            try {
                ButtonClicked(e);
            } catch (InterruptedException ex) {
                Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
