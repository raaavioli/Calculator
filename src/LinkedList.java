import java.util.EmptyStackException;
import java.util.NoSuchElementException;

/**
 * A singly linked list implementing the Stack interface.
 *
 * @author Oliver Eriksson
 * @version 2018-01-23
 */
public class LinkedList<T> implements Stack<T> {
    private ListElement<T> first;   // First element in list.
    private ListElement<T> last;    // Last element in list.
    private int size;               // Number of elements in list.

    /**
     * A list element.
     */
    private static class ListElement<T> {
        public T data;
        public ListElement<T> next;

        public ListElement(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Creates an empty list.
     */
    public LinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public void push(T data) {
        addFirst(data);
    }

    @Override
    public T pop() throws EmptyStackException {
        try {
            return removeFirst();
        } catch (NoSuchElementException e) {
            throw new EmptyStackException();
        }
    }

    @Override
    public T top() throws EmptyStackException {
        try {
            return getFirst();
        } catch (NoSuchElementException e) {
            throw new EmptyStackException();
        }
    }

    /**
     * Inserts the given element at the beginning of this list.
     *
     * @param element An element to insert into the list.
     */
    private void addFirst(T element) {
        ListElement<T> insertedElement = new ListElement<>(element);
        insertedElement.next = first;
        if (isEmpty()) {
            last = insertedElement;
        }
        first = insertedElement;

        size++;
    }

    /**
     * Inserts the given element at the end of this list.
     *
     * @param element An element to insert into the list.
     */
    private void addLast(T element) {
        ListElement<T> insertedElement = new ListElement<>(element);
        if (isEmpty()) {
            first = insertedElement;
        } else {
            last.next = insertedElement;
        }
        last = insertedElement;
        size++;
    }

    /**
     * @return The head of the list.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getFirst() {
        if (first == null) {
            throw new NoSuchElementException("List is empty.");
        }
        return first.data;
    }

    /**
     * @return The tail of the list.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getLast() {
        if (last == null) {
            throw new NoSuchElementException("List is empty.");
        }
        return last.data;
    }

    /**
     * Returns an element from a specified index.
     *
     * @param index A list index.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public T get(int index) {
        if (index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException("Index " + index + " was not valid.");
        }

        ListElement<T> currentElement = first;
        for (int i = 0; i < index; i++) {
            currentElement = currentElement.next;
        }
        return currentElement.data;
    }

    /**
     * Removes the first element from the list.
     *
     * @return The removed element.
     * @throws NoSuchElementException if the list is empty.
     */
    public T removeFirst() {
        if (size == 0 && isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
        T removedData = first.data;
        first = first.next;
        size--;
        if (size == 0) {
            last = null;
        }
        return removedData;
    }

    /**
     * Removes all of the elements from the list.
     */
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     * @return The number of elements in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Note that by definition, the list is empty if both first and last
     * are null, regardless of what value the size field holds (it should
     * be 0, otherwise something is wrong).
     *
     * @return <code>true</code> if this list contains no elements.
     */
    public boolean isEmpty() {
        return first == null && last == null;
    }

    /**
     * Creates a string representation of this list. The string
     * representation consists of a list of the elements enclosed in
     * square brackets ("[]"). Adjacent elements are separated by the
     * characters ", " (comma and space). Elements are converted to
     * strings by the method toString() inherited from Object.
     * <p>
     * Examples:
     * "[1, 4, 2, 3, 44]"
     * "[]"
     *
     * @return A string representing the list.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        ListElement<T> currentElement = first;
        stringBuilder.append("[");
        for (int i = 0; i < size; i++) {
            stringBuilder.append(currentElement.data);
            if (i != size - 1) {
                stringBuilder.append(", ");
            }
            currentElement = currentElement.next;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}