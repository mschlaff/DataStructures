/**
 * Your implementation of a non-circular DoublyLinkedList with a tail pointer.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class DoublyLinkedList<T> {

    // Do not add new instance variables or modify existing ones.
    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    // Do not add a constructor.

    /**
     * Adds the element to the specified index. Don't forget to consider whether
     * traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new element
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new java.lang.IndexOutOfBoundsException("Index selected is out of bounds.");
        }
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else {
            DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
            DoublyLinkedListNode<T> node;
            if (index <= size / 2) {            // decide if better to traverse from head or tail
                node = head;
                for (int i = 0; i < index; i++) {
                    node = node.getNext();
                }
            } else {
                node = tail;
                for (int i = size - 1; i > index; i--) {
                    node = node.getPrevious();
                }
            }
            if (node.getNext() != null) {           //check if node.next is null
                newNode.setNext(node.getNext());
                node.getNext().setPrevious(newNode);
            }
            node.setNext(newNode);
            newNode.setPrevious(node);
            size += 1;
        }
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        newNode.setNext(head);
        if (size == 0) {
            tail = newNode;
        } else {
            head.setPrevious(newNode);
        }
        head = newNode;
        size += 1;
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        DoublyLinkedListNode<T> newNode = new DoublyLinkedListNode<>(data);
        newNode.setPrevious(tail);
        if (size == 0) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
        size += 1;
    }

    /**
     * Removes and returns the element at the specified index. Don't forget to
     * consider whether traversing the list from the head or tail is more
     * efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new java.lang.IndexOutOfBoundsException("Index selected is out of bounds.");
        }
        DoublyLinkedListNode<T> delete = null;
        if (index == 0) {
            delete = (DoublyLinkedListNode<T>) removeFromFront();
        } else if (index == size - 1) {
            delete = (DoublyLinkedListNode<T>) removeFromBack();
        } else {
            DoublyLinkedListNode<T> node;
            if (index <= size / 2) {            // decide if better to traverse from head or tail
                node = head;
                for (int i = 0; i < index; i++) {
                    node = node.getNext();
                }
            } else {
                node = tail;
                for (int i = size - 1; i > index; i--) {
                    node = node.getPrevious();
                }
            }
            delete = node;
            node.getPrevious().setNext(node.getNext());
            node.getNext().setPrevious(node.getPrevious());
            size -= 1;
        }
        return delete.getData();
    }

    /**
     * Removes and returns the first element of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("The list is empty.");
        }
        DoublyLinkedListNode<T> delete = head;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
        size -= 1;
        return delete.getData();
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
            throw new java.util.NoSuchElementException("The list is empty.");
        }
        DoublyLinkedListNode<T> delete = tail;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
        size -= 1;
        return delete.getData();
    }

    /**
     * Returns the element at the specified index. Don't forget to consider
     * whether traversing the list from the head or tail is more efficient!
     *
     * Must be O(1) for indices 0 and size - 1 and O(n) for all other cases.
     *
     * @param index the index of the element to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new java.lang.IndexOutOfBoundsException("Index selected is out of bounds.");
        }
        DoublyLinkedListNode<T> get = null;
        if (index == 0) {
            get = head;
        } else if (index == size - 1) {
            get = tail;
        } else {
            if (index <= size / 2) {            // decide if better to traverse from head or tail
                get = head;
                for (int i = 0; i < index; i++) {
                    get = get.getNext();
                }
            } else {
                get = tail;
                for (int i = size - 1; i > index; i--) {
                    get = get.getPrevious();
                }
            }
        }
        return get.getData();
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(1) if data is in the tail and O(n) for all other cases.
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data can't be null.");
        }
        DoublyLinkedListNode<T> node = tail;
        while (node != null) {
             if (node.getData().equals(data)) {
                 if (node == head) {
                     return removeFromFront();
                 } else if (node == tail) {
                     return removeFromBack();
                 } else {
                     node.getPrevious().setNext(node.getNext());
                     node.getNext().setPrevious(node.getPrevious());
                     size -= 1;
                     return node.getData();
                 }
             }
             node = node.getPrevious();
        }
        throw new java.util.NoSuchElementException("Data was not in the list.");
    }

    /**
     * Returns an array representation of the linked list. If the list is
     * size 0, return an empty array.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length size holding all of the objects in the
     * list in the same order
     */
    public Object[] toArray() {
        Object[] arr = new Object[size];
        DoublyLinkedListNode<T> node = head;
        if (size == 0) {
            return arr;
        } else {
            for (int i = 0; i < size; i++) {       //add linked list items to array
                arr[i] = node.getData();
                node = node.getNext();
            }
        }
        return arr;
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public DoublyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the list
     */
    public DoublyLinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
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
        // DO NOT MODIFY!
        return size;
    }
}
