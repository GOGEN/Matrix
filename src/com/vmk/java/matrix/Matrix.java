package com.vmk.java.matrix;

import com.sun.deploy.util.ArrayUtil;
import com.sun.deploy.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gogen on 07.03.15.
 */
public class Matrix {
    int n, m;
    BigInteger[][] array;

    public Matrix(int n, int m) throws Exception{
        if(n < 0 || m < 0){
            throw new Exception("Invalid dimension");
        }
        this.n = n;
        this.m = m;
        array = new BigInteger[n][m];
        for( int i = 0 ; i < n ; i++ ){
            for( int j = 0; j < m; j++ ){
                array[i][j] = BigInteger.ZERO;
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for( int i = 0 ; i < n ; i++ ) {
            s += "|";
            for (int j = 0; j < m; j++) {
                s += " " + array[i][j].toString() + " |";
            }
            s += "\n";
        }
        return s;
    }

    public Matrix(BigInteger[][] a){
        n = a.length;
        m = a[0].length;
        array = new BigInteger[n][m];
        for( int i = 0 ; i < n ; i++ ){
            for( int j = 0; j < m; j++ ){
                array[i][j] = a[i][j];
            }
        }
    }

    public void input(){
        System.out.println("Input method start.");
        Scanner sc = new Scanner(System.in);
        for( int i = 0 ; i < n ; i++ ){
            for( int j = 0; j < m; j++ ){
                System.out.print(i + ", " + j + ": ");
                array[i][j] = new BigInteger(sc.nextLine());
            }
        }
    }

    public void output(){
        System.out.println(this.toString());
    }

    public BigInteger det() throws Exception{
        BigInteger sum = BigInteger.ZERO;
        if( n != m){
            throw new Exception("Not quadratic matrix");
        }
        if( n == m && m == 1){
            return array[0][0];
        }
        try {
            for (int i = 0; i < n; i++) {
                sum = sum.add((i % 2 == 0) ? array[0][i] : array[0][i].negate()).multiply(this.getMinor(0, i));
            }
        }catch (Exception e){
            System.err.println(e);
        }
        return sum;
    }

    public BigInteger getMinor(int row, int column) throws Exception{
        if(this.n <= 1 || this.m <= 1){
            throw new Exception("Ivalid dimension");
        }
        Matrix matrix = new Matrix(this.n - 1, this.m - 1);
        for(int i = 0; i < row; i++ ){
            for( int j = 0; j < column; j++ ){
                matrix.array[i][j] = this.array[i][j];
            }
            for( int j = column + 1; j < m; j++ ){
                matrix.array[i][j - 1] = this.array[i][j];
            }
        }
        for(int i = row + 1; i < n; i++ ){
            for( int j = 0; j < column; j++ ){
                matrix.array[i - 1][j] = this.array[i][j];
            }
            for( int j = column + 1; j < n; j++ ){
                matrix.array[i - 1][j - 1] = this.array[i][j];
            }
        }
        return matrix.det();
    }

    public Matrix sum(Matrix matrix) throws Exception{
        if(this.n != matrix.n || this.m != matrix.m){
            throw new Exception("Error");
        }
        Matrix newMatrix = new Matrix(n, m);
        for( int i = 0 ; i < n ; i++ ) {
            for (int j = 0; j < m; j++) {
                newMatrix.array[i][j] = array[i][j].add(matrix.array[i][j]);
            }
        }
        return newMatrix;
    }

    public Matrix multiply(Matrix matrix) throws Exception{
        if(this.m != matrix.n){
            throw new Exception("Error");
        }
        Matrix newMatrix = new Matrix(this.n, matrix.m);
        for( int i = 0 ; i < newMatrix.n ; i++ ) {
            for (int j = 0; j < newMatrix.m; j++) {
                BigInteger sum = BigInteger.ZERO;
                for( int r = 0; r < this.m; r ++){
                    sum = sum.add(this.array[i][r].multiply(matrix.array[r][j]));
                }
                newMatrix.array[i][j] = sum;
            }
        }
        return newMatrix;
    }

    public Matrix transposition(){
        BigInteger[][] a = new BigInteger[array.length][array[0].length];
        for( int i = 0 ; i < n ; i++ ) {
            for (int j = 0; j < m; j++) {
                a[i][j] = array[j][i];
            }
        }
        return new Matrix(a);
    }

    public List<Matrix> readFile(String path) throws IOException{
        Stream<String> lines = Files.lines(Paths.get(path));
        List<Matrix> matrixList = lines.map(s -> {
            String[] matrixDetails = s.split(" ");
            Matrix matrix = null;
            try {
                matrix = new Matrix(Integer.parseInt(matrixDetails[0]), Integer.parseInt(matrixDetails[1]));
            } catch (Exception e) {
                System.err.println(e);
            }
            int r = 2;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    matrix.array[i][j] = new BigInteger(matrixDetails[r++]);
                }
            }
            return matrix;
        }).collect(Collectors.toList());
        lines.close();
        return matrixList;
    }

    public void writeFile(String path) throws IOException{
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(n + " ");
        bw.write(m + " ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                bw.write(array[i][j].toString() + " ");
            }
        }
        bw.close();
    }

}
