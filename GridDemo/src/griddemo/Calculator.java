package griddemo;

public class Calculator implements Runnable, java.io.Serializable {
    public enum Operation {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION
    }

    private Operation op = Operation.ADDITION;
    private double arg1 = 0;
    private double arg2 = 0;
    private double res  = 0;

    public Operation setOperation(Operation op) {
        this.op = op;
        return op;
    }

    public int setOperand(double arg1, double arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        return 2;
    }

    public void run() {
        switch(op) {
            case ADDITION: res = arg1 + arg2; break;
            case SUBTRACTION: res = arg1 - arg2; break;
            case MULTIPLICATION: res = arg1 * arg2; break;
            case DIVISION: res = arg1 / arg2;
        }
        System.out.println("Calculated");
    }

    public double getResult() {
        return res;
    }
}
