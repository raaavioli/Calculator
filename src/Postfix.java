import java.util.regex.Pattern;

/**
 * The Postfix class implements an evaluator for integer postfix expressions.
 * <p>
 * Postfix notation is a simple way to define and write arithmetic expressions
 * without the need for parentheses or priority rules. For example, the postfix
 * expression "1 2 - 3 4 + *" corresponds to the ordinary infix expression
 * "(1 - 2) * (3 + 4)". The expressions may contain decimal 32-bit integer
 * operands and the four operators +, -, *, and /. Operators and operands must
 * be separated by whitespace.
 *
 * @author Oliver Eriksson
 * @version 2018-01-23
 */
public class Postfix {
    public static class ExpressionException extends Exception {
        public ExpressionException(String message) {
            super(message);
        }
    }


    /**
     * Evaluates the given postfix expression.
     *
     * @param expr Arithmetic expression in postfix notation
     * @return The value of the evaluated expression
     * @throws ExpressionException if the expression is wrong
     */
    public static double evaluate(String expr) throws ExpressionException {
        LinkedList<Double> stack = new LinkedList<>();
        String[] values = Pattern.compile("\\s+|\\t+").split(expr.trim());

        for (int i = 0; i < values.length; i++) {
            String value = values[i];

            if (isDouble(value)) {
                double operand = Double.parseDouble(value);
                stack.push(operand);
            } else if (isOperator(value)) {
                if (stack.size() < 2) {
                    throw new ExpressionException("Operator " + value + " encountered when less than two operands in stack");
                }

                double second = stack.pop();
                double first = stack.pop();

                switch (value) {
                    case "+":
                        double sum = first + second;
                        stack.push(sum);
                        break;
                    case "-":
                        double difference = first - second;
                        stack.push(difference);
                        break;
                    case "*":
                        double product = first * second;
                        stack.push(product);
                        break;
                    case "/":
                        if (second != 0) {
                            double quota = first / second;
                            stack.push(quota);
                        } else {
                            throw new ExpressionException("Divide by zero");
                        }
                        break;
                    case "^":
                        double result = Math.pow(first, second);
                        stack.push(result);
                        break;
                }

            } else {
                throw new ExpressionException("Value is neither valid operator nor operand");
            }
        }

        if (stack.size() > 1) {
            throw new ExpressionException("Too few operators");
        }

        return stack.pop();
    }

    /**
     * Returns true if s is an operator.
     * <p>
     * A word of caution on using the String.matches method: it returns true
     * if and only if the whole given string matches the regex. Therefore
     * using the regex "[0-9]" is equivalent to "^[0-9]$".
     * <p>
     * An operator is one of '+', '-', '*', '/', '^'.
     */
    public static boolean isOperator(String s) {
        return s.matches("[\\s]*[-+*/^][\\s]*");
    }

    /**
     * Returns true if s is an integer.
     * <p>
     * A word of caution on using the String.matches method: it returns true
     * if and only if the whole given string matches the regex. Therefore
     * using the regex "[0-9]" is equivalent to "^[0-9]$".
     * <p>
     * We accept two types of integers:
     * <p>
     * - the first type consists of an optional '-'
     * followed by a non-zero digit
     * followed by zero or more digits,
     * <p>
     * - the second type consists of an optional '-'
     * followed by a single '0'.
     */
    public static boolean isDouble(String s) {
        return s.matches("[-~]?([0]|[1-9][0-9]*)[.]?[0-9]*");
    }
}
