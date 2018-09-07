/**
 * Created by 352videoquiz on 2018/2/24.
 */

import java.io.*;
import java.util.Scanner;


public class test {
    private static BoardState loadFile(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int[] score = new int[2];
            int[] house = new int[12];
            score[0] = sc.nextInt();
            score[1] = sc.nextInt();
            for (int i = 0; i < 6; i++) {
                house[11-i] = sc.nextInt();
            }
            for (int i = 0; i < 6; i++) {
                house[i] = sc.nextInt();
            }
            sc.close();
            return new BoardState(house, score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public  static void main (String [] args) {

        int maxDepth  = Integer.parseInt(args[0]);
        studentAI ai = new studentAI();
        ai.setMaxDepth(maxDepth);
        BoardState state = loadFile(args[1]);
        System.out.println(state.toString());
        System.out.println("Max Depth is : " + maxDepth);

       if (args[1].equals("test1.txt") || args[1].equals("test2.txt") ||args[1].equals("test3.txt") ) {
           System.out.println("AB search result is :" + ai.alphabetaSearch(state,maxDepth));
       }
       else if (args[1].equals("test4.txt") )  {
           System.out.println("Max Value test1 Search Result is :" + ai.maxValue(state, maxDepth, 0, -1000, 1000));

       } else if (args[1].equals("test5.txt")) {
           System.out.println("Max Value test2 Search Result is :" + ai.maxValue(state, maxDepth, 10, -1000, 1000));

       } else if (args[1].equals("test6.txt")  ) {
           System.out.println("Max Value test3 Search Result is :" + ai.maxValue(state, maxDepth, 1, -1000, 1000));

       } else if (args[1].equals("test7.txt")) {
           System.out.println("Max Value test 4 Search Result is :" + ai.maxValue(state, maxDepth, 2, -1000, 1000));

       } else if (args[1].equals("test8.txt") )  {
           System.out.println("min Value test1 Search Result is :" + ai. minValue(state, maxDepth, 0, -1000, 1000) );


       } else if (args[1].equals("test9.txt")) {
           System.out.println("min Value test2 Search Result is :" + ai. minValue(state, maxDepth, 10, -1000, 1000) );


       } else if (args[1].equals("test10.txt")) {
           System.out.println("min Value test3 Search Result is :" + ai. minValue(state, maxDepth, 1, -1000, 1000) );


       } else if (args[1].equals("test11.txt")) {
           System.out.println("min Value test 4 Search Result is :" + ai. minValue(state, maxDepth, 2, -1000, 1000) );


       }








    }
}
