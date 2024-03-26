import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * To initialize the backing array, create a Comparable array and then cast
     * it to a T array.
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * size of the passed in ArrayList (not INITIAL_CAPACITY). Index 0 should
     * remain empty, indices 1 to n should contain the data in proper order, and
     * the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        backingArray = (T[]) new Comparable[data.size() * 2 + 1];
        size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("Element in data can't be null.");
            }
            backingArray[i + 1] = data.get(i);
        }
        // build heap
        for (int i = size / 2; i > 0; i--) {
            downHeap(i);
        }
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     * The order property of the heap must be maintained after adding. You can
     * assume that no duplicate data will be passed in.
     * 
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        if (size + 1 == backingArray.length) {
            resize();
        }
        backingArray[size + 1] = data;
        size += 1;
        upHeap(size);
    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     * The order property of the heap must be maintained after removing.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("The heap can't be empty.");
        }
        // remove root
        T removed = backingArray[1];
        // take bottom node and put at root
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        // downheap new root
        size -= 1;
        downHeap(1);
        // return removed
        return removed;
    }

    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("The heap can't be empty.");
        }
        //return root since its a minheap
        //root is at index 1 since index 0 is null
        return backingArray[1];
    }

    /**
     * Start from root and swap element if it has a bad relationship with its children.
     * @param start starting index
     */
    private void downHeap(int start) {
        int currIndex = start;
        T curr = backingArray[start];

        while (currIndex <= size / 2) {
            int leftIndex = currIndex * 2;
            int rightIndex = leftIndex + 1;
            int smallerIndex = leftIndex;

            if (rightIndex <= size && backingArray[rightIndex].compareTo(backingArray[leftIndex]) < 0) {
                smallerIndex = rightIndex;
            }

            if (curr.compareTo(backingArray[smallerIndex]) <= 0) {
                break;
            }

            backingArray[currIndex] = backingArray[smallerIndex];
            currIndex = smallerIndex;
        }
        backingArray[currIndex] = curr;

    }

    /**
     * Takes the new element and swaps with its parent until it has a good relationship.
     *
     * @param index starting index.
     */
    private void upHeap(int index) {
        T data = backingArray[index];

        // while target node < parent node
        while (index > 1 && data.compareTo(backingArray[index / 2]) < 0) {
            backingArray[index] = backingArray[index / 2];
            index = index / 2;
        }
        backingArray[index] = data;
    }

    /**
     * Resizes the arraylist with length * 2.
     */
    private void resize() {
        int newCap = backingArray.length * 2;
        T[] newArr = (T[]) new Object[newCap];

        for (int i = 1; i <= size; i++) {
            newArr[i] = backingArray[i];
        }

        backingArray = newArr;
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
        backingArray[0] = null;
    }

    /**
     * Returns the backing array of the heap.
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
     * Returns the size of the heap.
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
