/**
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
import java.util.*;

public class ArrayList<T> {

    /**
     * The initial capacity of the ArrayList.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 9;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayList.
     *
     * Java does not allow for regular generic array creation, so you will have
     * to cast an Object[] to a T[] to get the generic typing.
     */
    public ArrayList() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Adds the element to the specified index.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be amortized O(1) for index size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is < 0 or index > size.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        if (size == backingArray.length) {               // if array is full, resize
            resize();
        }
        if (index == 0) {                           // add to front
            addToFront(data);
        } else if (index == size) {                 // add to back
            addToBack(data);
        } else {
            for (int i = size; i >= index; i--) {   // shift data in array back to front
                backingArray[i+1] = backingArray[i];
            }
            backingArray[index] = data;                   // add data to front of list
            size += 1;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Remember that this add may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        if (size == backingArray.length) {           // if array is full, resize
            resize();
        }
        for (int i = size; i >= 0; i--) {           // shift data in array back to front
            backingArray[i+1] = backingArray[i];
        }
        backingArray[0] = data;                      // add data to front of list
        size += 1;
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be amortized O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        if (size == backingArray.length) {
            resize();
        }
        backingArray[size] = data;
        size += 1;
    }

    /**
     * Removes and returns the element at the specified index.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(1) for index size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is < 0 or index >= size.");
        }

        T removed = backingArray[index];                 // store data to be removed

        if (index == 0) {                           // remove from front
            removeFromFront();
        } else if (index == size-1) {                 // remove from back
            removeFromBack();
        } else {
            for (int i = index; i < size-1; i++) {   // shift data in array
                backingArray[i] = backingArray[i + 1];
            }
            backingArray[size-1] = null;
            size -= 1;
        }
        return removed;
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Remember that this remove may require elements to be shifted.
     *
     * Must be O(n).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Array list is empty.");
        }

        T removed = backingArray[0];                 // store data to be removed

        for (int i = 1; i < size; i++) {            // shift data in array
            backingArray[i - 1] = backingArray[i];
        }
        backingArray[size-1] = null;
        size -= 1;
        return removed;
    }

    /**
     * Removes and returns the last element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (isEmpty()) {
            throw new NoSuchElementException("Array list is empty.");
        }
        T removed = backingArray[size-1];                 // store data to be removed
        backingArray[size-1] = null;
        size -= 1;
        return removed;
    }

    /**
     * Returns the element at the specified index.
     *
     * Must be O(1).
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is < 0 or index >= size.");
        }
        return backingArray[index];
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the list.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Resizes the arraylist with length * 2.
     */
    private void resize() {
        int newCap = backingArray.length * 2;
        T[] newArr = (T[]) new Object[newCap];

        for (int i = 0; i < size; i++) {
            newArr[i] = backingArray[i];
        }

        backingArray = newArr;
    }

    /**
     * Returns the backing array of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
