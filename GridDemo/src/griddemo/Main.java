package griddemo;

import org.accurate.grid.*;

public class Main {

    public static void main(String[] args) {
        Job[] jobs = new Job[10];

        //first spawn a few jobs;
        for(int i=0; i<10; i++) {
            Calculator c = new Calculator();
            c.setOperation(Calculator.Operation.ADDITION);
            c.setOperand(i, i);

            Job j=new Job(c);
            jobs[i]=j;
            j.start();
        }

        //now wait while they complete
        for(int i=0; i<10; i++) {
            jobs[i].join();
        }

        //now print the result
        System.out.println("\n\n");
        for(int i=0; i<10; i++) {
            Calculator c;
            c = (Calculator)jobs[i].getTarget();
            System.out.println(c.getResult());
        }
    }

}
