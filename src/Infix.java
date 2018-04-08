import java.util.regex.Pattern;

public class Infix extends Postfix {
    public static void main(String[] args) {
        System.out.println(evaluateInfix("0.5^0.5"));
    }

    /**
     * Calculates an infix mathematical expression using conversion to postfix.
     *
     * @param expr an infix mathematical expression repressented as a String.
     * @return the int-value of the mathematical expression passed as input. Integer.MAX_VALUE if an ecxeption was caught
     */
    public static double evaluateInfix(String expr) {
        try {
            return evaluate(infixToPostfix(expr));
        } catch (ExpressionException e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Takes a mathematical expression on infix form and outputs the same mathematical expression in postfix-form.
     * The method can take any of the four operators +, -, *, / or ^.
     *
     * @param infixString is string representing the mathematical expression on infix form.
     * @return the infix input converted to postfix.
     * @throws ExpressionException if expression is not on valid infix form and contains any non-defined characters.
     */
    private static String infixToPostfix(String infixString) throws ExpressionException {
        LinkedList<String> operatorStack = new LinkedList<>();
        StringBuilder postfixString = new StringBuilder();
        infixString = " " + infixString;
        infixString = infixString.replaceAll("([\\D])[-]([\\d]+[.]?[\\d]*)", " $1 ~$2 ").replaceAll("([-+*^/()])", " $1 ");
        String[] values = Pattern.compile("\\s+|\\t+").split(infixString.trim());

        for (String value : values) {
            value = value.trim();
            if (isDouble(value)) {
                value = value.replace("~", "-");
                postfixString.append(value);
                postfixString.append(" ");
            } else if (isOperator(value) || value.equals("(")) {
                if (operatorStack.isEmpty() || operatorStack.getFirst().equals("(") || value.equals("(")) {
                    operatorStack.push(value);
                } else if (precedence(operatorStack.getFirst()) < precedence(value)) {
                    operatorStack.push(value);
                } else if (!operatorStack.getFirst().equals("(") && precedence(operatorStack.getFirst()) >= precedence(value)) {
                    while (!operatorStack.isEmpty() && !operatorStack.getFirst().equals("(") && precedence(operatorStack.getFirst()) >= precedence(value)) {
                        postfixString.append(operatorStack.pop());
                        postfixString.append(" ");
                    }
                    operatorStack.push(value);
                }
            } else if (value.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.getFirst().equals("(")) {
                    postfixString.append(operatorStack.pop());
                    postfixString.append(" ");
                }
                operatorStack.pop();
            }
        }

        int size = operatorStack.size();

        for (int i = 0; i < size; i++) {
            postfixString.append(operatorStack.pop());
            postfixString.append(" ");
        }

        return postfixString.toString();
    }

    private static int precedence(String operator) throws ExpressionException {
        operator = operator.trim();
        switch (operator) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;

        }
        throw new ExpressionException("Unknown character");
    }
}
