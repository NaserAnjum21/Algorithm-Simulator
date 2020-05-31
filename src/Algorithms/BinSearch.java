/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import algosimmain.AlgoSimMain;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BinSearch {

    AlgoSimMain main;
    int[] array;

    public BinSearch(AlgoSimMain main) {
        this.main = main;
    }

    public void search(int[] arr, int elem) throws InterruptedException {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int start = 0;
                int end = arr.length - 1;
                int mid = 0;
                boolean found=false;
                while (start <= end) {
                    try {
                        mid = (start + end) / 2;                    
                        main.description.clear();
                        main.description.setText("Start-"+start+" Mid-"+mid+" End-"+end);                       
                        color(start,mid,end);
                        Thread.sleep(4000);
                        
                        if (elem < arr[mid]) {                    
                            fade(mid,end);
                            end = mid - 1;
                        } else if (elem > arr[mid]) {
                            fade(start,mid);
                            start = mid + 1;
                        } else {
                            main.description.clear();
                            main.description.setText("Found "+elem+" at index "+mid);
                            main.rect.get(mid).setFill(Color.RED);
                            found=true;
                            break;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BinSearch.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                if(!found)
                {
                    main.description.clear();
                    main.description.setText(elem+ " not found!");
                }
            }
        });
        t.start();
    }


    public void color(int a,int b,int c) {
            main.rect.get(a).setFill(Color.YELLOW);
            main.rect.get(c).setFill(Color.DARKGREEN);
            main.rect.get(b).setFill(Color.STEELBLUE);

    }
    
    public void fade(int a,int b)
    {
        for(int i=a;i<=b;i++)
        { 
            FillTransition ft=new FillTransition(Duration.millis(100), main.rect.get(i), (Color) main.rect.get(i).getFill(), Color.GREY);
            ft.play();
            FadeTransition fdt=new FadeTransition(Duration.millis(1000), main.rect.get(i));
            fdt.setFromValue(1.0);
            fdt.setToValue(0.3);
            fdt.play();
        }
    }

}
