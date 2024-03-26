import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        size = 0;
        for (T t : data) {
            if (t == null) {
                throw new IllegalArgumentException("Data is null.");
            }
            add(t);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        root = insert(root, data);
    }

    /**
     * Helper method to add to an AVL tree
     * @param node node to start at
     * @param data data to add to tree
     * @return return data inserted
     */
    private AVLNode<T> insert(AVLNode<T> node, T data) {
        if (node == null) {
            size += 1;
            return new AVLNode<>(data);
        }
        int compare = data.compareTo(node.getData());

        if (compare < 0) {
            node.setLeft(insert(node.getLeft(), data));
        } else if (compare > 0) {
            node.setRight(insert(node.getRight(), data));
        }

        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        int bf = getBF(node);

        if (bf > 1) {
            if (data.compareTo(node.getLeft().getData()) < 0) {
                return rotateRight(node);                       //rotate right
            } else {
                node.setRight(rotateRight(node.getRight()));
                return rotateLeft(node);                        //right-left rotation
            }
        } else if (bf < -1) {
            if (data.compareTo(node.getRight().getData()) > 0) {
                return rotateLeft(node);                        //rotate left
            } else {
                node.setRight(rotateRight(node.getRight()));
                return rotateLeft(node);                        //right-left rotation
            }
        }
        return node;
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("Data not found!");
        }
        root = delete(root, data);
        size -= 1;

        return data;
    }

    /**
     * Helper method to remove data from an AVL tree
     * @param node node to start at
     * @param data data you want to delete
     * @return returns deleted data
     */
    private AVLNode<T> delete(AVLNode<T> node, T data) {
        if (node == null) {
            return node;
        }
        int compare = data.compareTo(node.getData());

        if (compare < 0) {
            node.setLeft(delete(node.getLeft(), data));
        } else if (compare > 0) {
            node.setRight(delete(node.getRight(), data));
        } else {
            if (node.getLeft() == null || node.getRight() == null) {
                AVLNode<T> temp = (node.getLeft() != null) ? node.getLeft() : node.getRight();
                node = temp;
            } else {
                AVLNode<T> temp = findMax(node.getLeft());
                node.setData(temp.getData());
                node.setLeft(delete(node.getLeft(), temp.getData()));
            }
        }
        if (node == null) {
            return node;
        }

        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        int bf = getBF(node);

        if (bf > 1) {
            if (getBF(node.getLeft()) >= 0) {
                return rotateRight(node);                       //only rightrotate
            } else {
                node.setLeft(rotateLeft(node.getLeft()));      //left-right rotation
                return rotateRight(node);
            }
        } else if (bf < -1) {
            if (getBF(node.getRight()) <= 0) {
                return rotateLeft(node);                        //only leftrotate
            } else {
                node.setRight(rotateRight(node.getRight()));   //right-left rotation
                return rotateLeft(node);
            }
        }
        return node;
    }

    /**
     * Helper method to find the max node in a subtree
     * @param node the node to start at
     * @return the node with the max value
     */
    private AVLNode<T> findMax(AVLNode<T> node) {
        if (node.getRight() == null) {
            return node;
        }
        return findMax(node.getRight());
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        AVLNode<T> node = search(root, data);
        if (node != null) {
            return node.getData();
        } else {
            throw new NoSuchElementException("Data not found!");
        }
    }

    /**
     * Search for a node in an AVL tree
     * @param node node to start searching at
     * @param data data to search for
     * @return return the node searched for
     */
    private AVLNode<T> search(AVLNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        int compare = data.compareTo(node.getData());
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return search(node.getLeft(), data);
        } else {
            return search(node.getRight(), data);
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        try {
            get(data);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return root.getHeight();
    }

    /**
     * Helper method for returning tree height
     * @param node node to find height of
     * @return returns height of node
     */
    private int height(AVLNode<T> node) {
        return (node != null) ? node.getHeight() : -1;
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * The predecessor is the largest node that is smaller than the current data.
     *
     * Should be recursive.
     *
     * This method should retrieve (but not remove) the predecessor of the data
     * passed in. There are 2 cases to consider:
     * 1: The left subtree is non-empty. In this case, the predecessor is the
     * rightmost node of the left subtree.
     * 2: The left subtree is empty. In this case, the predecessor is the lowest
     * ancestor of the node containing data whose right child is also
     * an ancestor of data.
     *
     * This should NOT be used in the remove method.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *     76
     *   /    \
     * 34      90
     *  \    /
     *  40  81
     * predecessor(76) should return 40
     * predecessor(81) should return 76
     *
     * @param data the data to find the predecessor of
     * @return the predecessor of data. If there is no smaller data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T predecessor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("No predecessor found.");
        }

        AVLNode<T> predecessor = findPredecessor(root, data);
        return predecessor != null ? predecessor.getData() : null;
    }

    /**
     * Helper method to find predecessor recursively
     * @param node node to start at
     * @param data data at node
     * @return returns predecessor node
     */
    private AVLNode<T> findPredecessor(AVLNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        int compare = data.compareTo(node.getData());

        if (compare <= 0) {
            return findPredecessor(node.getLeft(), data);
        } else {
            AVLNode<T> right = findPredecessor(node.getRight(), data);
            return right != null ? right : node;
        }
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with
     * the deepest depth.
     *
     * Should be recursive.
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    public T maxDeepestNode() {
        AVLNode<T> maxDeepest = findMaxDeepestNode(root);
        return maxDeepest != null ? maxDeepest.getData() : null;
    }

    /**
     * Helper method to search for max deepest node
     * @param node node to start at
     * @return returns maximum deepest node
     */
    private AVLNode<T> findMaxDeepestNode(AVLNode<T> node) {
        if (node == null) {
            return null;
        }
        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());

        if (leftHeight > rightHeight) {
            return findMaxDeepestNode(node.getLeft());
        } else if (leftHeight < rightHeight) {
            return findMaxDeepestNode(node.getRight());
        }

        AVLNode<T> right = findMaxDeepestNode(node.getRight());
        if (right != null) {
            return right;
        }
        return node;
    }

    /**
     * Helper method to get balance factor of a node
     * @param node node to get BF of
     * @return returns balance factor
     */
    private int getBF(AVLNode<T> node) {
        return height(node.getLeft()) - height(node.getRight());
    }

    /**
     * Helper method for left rotation
     * @param node node to start the rotation at
     * @return return rotated node
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        AVLNode<T> newRoot = node.getRight();
        node.setRight(newRoot.getLeft());
        newRoot.setLeft(node);
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        newRoot.setHeight(1 + Math.max(height(newRoot.getLeft()), height(newRoot.getRight())));
        return newRoot;
    }

    /**
     * Helper method for right rotation
     * @param node node to start the rotation at
     * @return return rotated node
     */
    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> newRoot = node.getLeft();
        node.setLeft(newRoot.getRight());
        newRoot.setRight(node);
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        newRoot.setHeight(1 + Math.max(height(newRoot.getLeft()), height(newRoot.getRight())));
        return newRoot;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
