public class Calculator extends Infix {

    private String expression;

    public Calculator(String expression) {
        this.expression = expression;
    }

    public double evaluateExprAtX(double x) {
        String exprCopy = expression.replaceAll("[a-zA-Z]+", String.valueOf(x));
        return evaluateInfix(exprCopy);
    }

}
