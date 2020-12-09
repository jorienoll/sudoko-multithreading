/**
 * Name: Noll, Jorie
 * Multithreading
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class sodoku
{
    private static final int[][] sodokuSolution = new int[9][9];
    private static boolean[] valid;
    static Thread active = Thread.currentThread();

    public static class Row implements Runnable
    {
      //create variables and constructor for row object
        int row;
        int column;
        Row(int row, int column)
        {
            this.row = row;
            this.column = column;
        }

        @Override
        public void run()
        {
           //validate that row only contains each digit 1-9 once.
            boolean[] isValid = new boolean[9];

            //store the thread number when printing
            long threadCount = Thread.currentThread().getId() - 10;


            for(int j = 0; j < 9; j++)
            {
                int value = sodokuSolution[row][j];
                //prints invalid when the row is not valid and exits
                if(isValid[value - 1])
                {
                    System.out.println("Thread " + threadCount + ", Row "+ row+", Invalid");
                    return;
                }

                //saves true in validity array and continues
                else if(isValid[value - 1] == false)
                {
                    isValid[value - 1] = true;
                }
            }

            //row validity is confirmed, prints row validity
            System.out.println("Thread " + threadCount + ", Row " + row + ", Valid");
        }
    }


    public static class Column implements Runnable
    {
      //create variables and constructor for column object
        int row;
        int column;
        Column(int row, int column)
        {
            this.row = row;
            this.column = column;
        }

        @Override
        public void run()
        {
          //array that stores column validity
          boolean[] isValid = new boolean[9];
          //store the thread number for printing
          long threadCount = Thread.currentThread().getId() - 10;

          //validate that column only contains each digit 1-9 once.
            for(int i = 0; i < 9; i++)
            {
                int value = sodokuSolution[i][column];
                //prints and exits when row invalid is confirmed
                if(isValid[value - 1])
                {
                    System.out.println("Thread " + threadCount + ", Column "+ column +", Invalid");
                    return;
                }
                //saves valid to the validity array and continues looping
                else if(isValid[value - 1] == false)
                {
                    isValid[value - 1] = true;
                }
            }
            //column validity is confirmed, print statement
            System.out.println("Thread " + threadCount + ", Column " + column + ", Valid");
        }
    }

    public static class Grid implements Runnable
    {
        //create variables and constructor for grid object
        int row;
        int column;
        Grid(int row, int column)
        {
            this.row = row;
            this.column = column;
        }

        @Override
        public void run()
        {
          //create array to store validity
            boolean[] isValid = new boolean[9];
            //saves the thread number for printing
            long threadCounter = Thread.currentThread().getId() - 10;


            //prints and exits loop when grid is invalid
            for(int i = row; i < row + 3; i++){
                for(int j = column; j < column + 3; j++){
                    int value = sodokuSolution[i][j];
                    if(isValid[value - 1])
                    {
                        System.out.printf("Thread " + threadCounter + ", Subgrid R%d%d%dxC%d%d%d, Invalid \n",row,row+1,row+2,column,column+1,column+2 );
                        return;
                    }
                    else
                    {
                        isValid[value - 1] = true;
                    }
                }
            }
            //validity is confirmed because loop completed, print validity statement
            System.out.printf("Thread " + threadCounter + ", Subgrid R%d%d%dxC%d%d%d, Valid \n",row,row+1,row+2,column,column+1,column+2);
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
      //scans user-prompted file name from console
        System.out.print("Enter Sodoku Solution File Name: ");
        Scanner fileScanner = new Scanner(System.in);
        String file = fileScanner.nextLine();

        //scan each value from text file and enter it into 2D array
        Scanner solutionScanner = new Scanner(new File(file));
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++){
                sodokuSolution[i][j] = solutionScanner.nextInt();
            }
        }

        //Create an array of 27 total threads (9 columns, 9 rows, 9 grids) and an array of validity
        valid = new boolean[27];
        Thread[] threadArr = new Thread[27];
        int threadCounter = 0;
        for(int j = 0; j < 9; j++)
        {
            for(int k = 0; k < 9; k++)
            {
                if(j == 0)
                {
                    threadArr[threadCounter++] = new Thread(new Column(j, k));
                }
                if(k == 0)
                {
                    threadArr[threadCounter++] = new Thread(new Row(j, k));
                }
                if(k%3 == 0 && j%3 == 0)
                {
                    threadArr[threadCounter++] = new Thread(new Grid(j, k));
                }
            }
        }

        //Begin all 27 threads
        for(int j = 0; j < 27; j++)
        {
            threadArr[j].start();
        }

        //Join all 27 threads and wait for all to execute before finishing
        for(int j = 0; j < threadArr.length; j++)
        {
            try
            {
                threadArr[j].join();
            } catch (InterruptedException f)
            {
                f.printStackTrace();
            }
        }
    }
}
