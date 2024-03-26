import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.PriorityQueue;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Array or comparator is null.");
        }
        for (int i = 1; i < arr.length; i++) {
            T curr = arr[i];
            int j = i - 1;

            while (j >= 0 && comparator.compare(curr, arr[j]) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = curr;
        }
    }

    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Array or comparator is null.");
        }
        boolean swapped;
        int start = 0;
        int end = arr.length - 1;

        do {
            swapped = false;
            int newEnd = 0;
            int newStart = end;

            for (int i = start; i < end; i++) {
                if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                    swap(arr, i, i + 1);
                    swapped = true;
                    newEnd = i;
                }
            }
            end = newEnd;

            for (int i = end; i > start; i--) {
                if (comparator.compare(arr[i - 1], arr[i]) > 0) {
                    swap(arr, i - 1, i);
                    swapped = true;
                    newStart = i;
                }
            }
            start = newStart;
        } while (swapped);
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("Array or comparator is null.");
        }

        if (arr.length > 1) {
            int middle = arr.length / 2;
            T[] left = (T[]) new Object[middle];
            T[] right = (T[]) new Object[arr.length - middle];

            for (int i = 0; i < middle; i++) {
                left[i] = arr[i];
            }
            for (int i = middle; i < arr.length; i++) {
                right[i - middle] = arr[i];
            }
            mergeSort(left, comparator);
            mergeSort(right, comparator);
            merge(arr, comparator, left, right);
        }
    }

    /**
     * Merge merges two arrays
     *
     * @param arr array to be sorted
     * @param comparator comparator used
     * @param left left array
     * @param right right Array
     * @param <T> data type to sort
     */
    private static <T> void merge(T[] arr, Comparator<T> comparator,
                                  T[] left, T[] right) {
        int leftLen = left.length;
        int rightLen = right.length;
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < leftLen && j < rightLen) {
            if (comparator.compare(left[i], right[j]) <= 0) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        while (i < leftLen) {
            arr[k++] = left[i++];
        }
        while (j < rightLen) {
            arr[k++] = right[j++];
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @throws java.lang.IllegalArgumentException if the array or comparator or
     *                                            rand is null
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null || comparator == null || rand == null) {
            throw new IllegalArgumentException("Array or comparator or rand is null.");
        }
        quickSort(arr, 0, arr.length - 1, rand, comparator);
    }

    /**
     * Helper method for quick sort
     *
     * @param arr array to be sorted
     * @param first start index
     * @param last end index
     * @param rand random object
     * @param comparator comparator used
     * @param <T> data type
     */
    private static <T> void quickSort(T[]arr, int first, int last, Random rand,
                                      Comparator<T> comparator) {
        if (last > first) {
            int pivot = partition(arr, first, last,
                    rand.nextInt(last - first + 1) + first, comparator);
            quickSort(arr, first, pivot - 1, rand, comparator);
            quickSort(arr, pivot + 1, last, rand, comparator);
        }
    }

    /**
     * Partitions array for quick sort
     *
     * @param arr array to be sorted
     * @param first start index
     * @param last end index
     * @param index pivot index
     * @param comparator comparator used
     * @param <T> data type
     * @return new pivot index
     */
    private static <T> int partition(T[] arr, int first, int last,
                                     int index, Comparator<T> comparator) {
        T pivot = arr[index];
        swap(arr, index, last);
        int id = first;
        for (int i = first; i < last; i++) {
            if (comparator.compare(arr[i], pivot) <= 0) {
                swap(arr, id, i);
                id++;
            }
        }
        swap(arr, last, id);
        return id;
    }

    /**
     * Swap two elements in an array
     *
     * @param arr the array that contains items to swap
     * @param a first item
     * @param b second item
     * @param <T>  data type to sort
     */
    private static <T> void swap(T[] arr, int a, int b) {
        T temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need. The number of iterations
     * can be determined using the number with the largest magnitude.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array is null.");
        }
        LinkedList<Integer>[] buckets = new LinkedList[19];
        for (int i = 0; i < 19; i++) {
            buckets[i] = new LinkedList<>();
        }
        int modulo = 10;
        int div = 1;
        boolean continuing = true;

        while (continuing) {
            continuing = false;
            for (int num : arr) {
                int bucket = num / div;
                int bucketIndex = bucket % modulo + 9;

                if (bucket / 10 != 0) {
                    continuing = true;
                }
                if (buckets[bucketIndex] == null) {
                    buckets[bucketIndex] = new LinkedList<>();
                }
                buckets[bucketIndex].add(num);
            }

            int index = 0;
            for (LinkedList<Integer> bucket : buckets) {
                if (bucket != null) {
                    for (int num : bucket) {
                        arr[index++] = num;
                    }
                    bucket.clear();
                }
            }
            div *= 10;
        }
    }

    /**
     * Implement heap sort.
     *
     * It should be:
     * out-of-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Use java.util.PriorityQueue as the heap. Note that in this
     * PriorityQueue implementation, elements are removed from smallest
     * element to largest element.
     *
     * Initialize the PriorityQueue using its build heap constructor (look at
     * the different constructors of java.util.PriorityQueue).
     *
     * Return an int array with a capacity equal to the size of the list. The
     * returned array should have the elements in the list in sorted order.
     *
     * @param data the data to sort
     * @return the array with length equal to the size of the input list that
     * holds the elements from the list is sorted order
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public static int[] heapSort(List<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(data);
        int[] sortedArr = new int[data.size()];
        int index = 0;
        while (!minHeap.isEmpty()) {
            sortedArr[index] = minHeap.poll();
            index += 1;
        }
        return sortedArr;
    }
}
