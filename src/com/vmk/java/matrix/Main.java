package com.vmk.java.matrix;

/**
 * Created by gogen on 07.03.15.
 */
public class Main {
    public static void main(String[] args){
        try {
            Matrix m = new Matrix(2, 2);
            m.input();
            m.output();
        }catch (Exception e){
            System.err.println(e);
        }

    }
}
