import java.util.EmptyStackException;

/**
 * An abstract stack interface
 *
 * @author Oliver Eriksson
 * @version 2018-01-23
 */
public interface Stack<T> {

    /**
     * Adds the generic data T on top of the stack
     *
     * @param data
     */
    void push(T data);

    /**
     * Removes and returs the top element of the stack, which is the last element added to the stack.
     * Throws EmptyStackException if stack is empty.
     *
     * @return the top element T of the stack.
     * @throws EmptyStackException if stack is empty contains nothing but null.
     */
    T pop() throws EmptyStackException;

    /**
     * Returns the top element of the stack without removing it.
     * Throws EmptyStackException if stack is empty.
     *
     * @return the top element T of the stack.
     * @throws EmptyStackException if stack is empty contains nothing but null.
     */
    T top() throws EmptyStackException;

    /**
     * Returns the number of elements currently in the stack and 0 if empty.
     *
     * @return the number of elements in the stack.
     */
    int size();

    /**
     * Checks if there are any elements in the stack
     *
     * @return true if stack is empty, false if it contains any value.
     */
    boolean isEmpty();
}