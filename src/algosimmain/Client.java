/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algosimmain;

import Utility.Data;
import Utility.NetworkUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author ASUS
 */
public class Client extends Application {

    public Stage stage;
    public Scene scene, sortscene;
    public Group group;
    public Group grp = new Group();
    public Button btn[] = new Button[4];
    public TextField arrfield = new TextField();
    public TextField numfield = new TextField();
    public Button send;
    public boolean sorted;
    public int len;
    public int i;
    public int[] input;
    public NetworkUtil nc;
    public List<Rectangle> rect = new ArrayList<Rectangle>();
    AlgoSimMain main;

    public static void main(String[] args) throws IOException {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //make_connection();
        nc = new NetworkUtil("127.0.0.1", 44444);

        Thread readerthread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Object ob = null;
                    ob = nc.read();
                    Data data = (Data) ob;
                    if (data != null && data.serial == 0) {
                        for (i = 0; i < data.size; i++) {
                            rect.add(i, new Rectangle(data.width, data.height[i], Color.BLUEVIOLET));
                            rect.get(i).setTranslateX(data.dx[i]);
                            rect.get(i).setTranslateY(270);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    grp.getChildren().add(rect.get(i));
                                }
                            });
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    else if (data.serial == 1 && !sorted ) {
                        bubble_sort();
                    } else if (data.serial == 2 && !sorted) {
                        selection_sort();
                    } else if (data.serial == 3) {
                        sorted=false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                for (Rectangle temp : rect) {
                                    try {
                                        grp.getChildren().remove(temp);
                                        Thread.sleep(50);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                rect.clear();
                            }
                        });

                    }
                }

            }
        });
        readerthread.start();

        stage = primaryStage;
        group = new Group();
        scene = new Scene(group, 700, 500, Color.GREY);
        Image image = new Image("File:images//AlgoHome.jpg");

        VBox vbox = new VBox();
        vbox.setPrefSize(400, 400);
        vbox.setLayoutX(150);
        vbox.setLayoutY(50);
        vbox.setPadding(new Insets(30));
        vbox.setSpacing(25);
        vbox.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        vbox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                null, new BorderWidths(3))));

        btn[0] = new Button("Sorting");
        btn[1] = new Button("Graph");

        btn[0].setOnAction(e -> {
            try {
                ButtonClicked(e);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Text text=new Text(60, 15, "Size");
        Text text2=new Text(40, 45, "Elements");
        numfield.relocate(100, 10);

        arrfield.relocate(100, 40);
        send = new Button("Send");
        send.relocate(100, 70);
        send.setOnAction((e1) -> {
            try {
                this.ButtonClicked(e1);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        grp.getChildren().addAll(numfield, arrfield, send,text,text2);
        sortscene = new Scene(grp, 700, 600, Color.GAINSBORO);

        vbox.getChildren().addAll(btn[0], btn[1]);
        group.getChildren().add(vbox);
        stage.setScene(scene);
        stage.show();
    }

    void bubble_sort() {
        sorted = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("In thread t");

                for (int i = 1; i < len; i++) {
                    for (int j = 0; j < len - i; j++) {
                        System.out.println("Loop " + i + " " + j);
//                        description.clear();
//                        description.setText("Comparison between " + input[j] + " and " + input[j + 1]);
                        colorchange(j, j + 1);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (rect.get(j).getHeight()  > rect.get(j + 1).getHeight()) {
                            System.out.println("Height-" + j + " " + rect.get(j).getHeight() + " " + rect.get(j + 1).getHeight());
                            //swap(input[j], input[j + 1]);
                            //description.appendText("\n Swapped " + input[j] + " and " + input[j + 1]);
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

            }
        });

        t.start();
        
        //stage.setScene(bsortscene);
    }

    void selection_sort() {
        sorted=true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("In thread t");

                for (int i = 0; i < len - 1; i++) {
                    for (int j = i + 1; j < len; j++) {
                        System.out.println("Loop " + i + " " + j);
                        colorchange(i, j);
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(AlgoSimMain.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (rect.get(i).getHeight()  > rect.get(j).getHeight()) {
                            System.out.println("Height-" + j + " " + rect.get(i).getHeight() + " " + rect.get(j).getHeight());
                            //swap(input[j], input[j + 1]);
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

            }
        });

        t.start();
        
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
//        for (int i = 0; i < input.length; i++) {
//            height[i] = (double) (300.0 / max) * (double) input[i];
//            System.out.print(height[i] + " ");
//        }
    }

    void animate(int a, int b, int c) {

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("In Run");

                TranslateTransition translateTransition = new TranslateTransition();
                TranslateTransition translateTransition2 = new TranslateTransition();

                double x1 = rect.get(a).getTranslateX();
                double x2 = rect.get(b).getTranslateX();

//                System.out.println(x1 + " " + x2);
//
                translateTransition.setNode(rect.get(a));
                translateTransition.setDuration(Duration.millis(c));
                translateTransition.setToX(x2);

                translateTransition2.setNode(rect.get(b));
                translateTransition2.setDuration(Duration.millis(c));
                translateTransition2.setToX(x1);

                ParallelTransition pt = new ParallelTransition();

                pt.getChildren().addAll(translateTransition, translateTransition2);

                pt.play();

                //pt.setOnFinished(event -> swap(rect[a],rect[b]));
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

    public void ButtonClicked(ActionEvent e) throws IOException {
        if (e.getSource() == btn[0]) {

            stage.setScene(sortscene);
        }
        if (e.getSource() == send) {
            String slen = numfield.getText();
            len = Integer.parseInt(slen);
            String sarray = arrfield.getText();
            //oos.writeChars(sarray);
            nc.write(sarray);
            //make_connection();
            //stage.setScene(new Scene(main.group, 700, 650));
            String[] snum = sarray.split(" ");

            input = new int[len];

            for (int i = 0; i < len; i++) {
                input[i] = Integer.parseInt(snum[i]);
                System.out.print(input[i] + " ");
            }

        }
    }

}
