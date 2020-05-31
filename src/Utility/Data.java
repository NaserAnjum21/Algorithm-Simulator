/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.Serializable;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ASUS
 */
public class Data implements Serializable,Cloneable{
 
    public double[] height;
    public double width;
    public double[] dx;
    public int size;
    public int serial;
    
    public Data(double[] arr,double d,double[] brr,int n,int a)
    {
        size=n;
        height=new double[size];
        dx=new double[size];
        System.arraycopy( arr, 0, height, 0, size );
        System.arraycopy( brr, 0, dx, 0, size); 
        width=d;
        serial=a;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
}
