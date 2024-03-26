import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Your implementation of a BST.
 *
 * @author Madison Schlaff
 * @version 1.0
 *
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        this();
        if (data == null) {
            throw new IllegalArgumentException("Data or an element in data is null.");
        } else {
            for (T element : data) {
                add(element);
            }
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        root = insert(root, data);
    }

    /**
     * Helper method to insert data into a tree.
     *
     * @param node node to be added at.
     * @param data data to be added.
     * @return node that you added.
     */
    private BSTNode<T> insert(BSTNode<T> node, T data) {
        if (node == null) {
            size += 1;
            return new BSTNode<T>(data);
        }

        int compare = data.compareTo(node.getData()) ;
        if (compare < 0) {
            node.setLeft(insert(node.getLeft(), data));
        } else if (compare > 0){
            node.setRight(insert(node.getRight(), data));
        }
        return node;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        if (!contains(data)) {
            throw new NoSuchElementException("Data not found.");
        }
        root = delete(root, data);
        size -= 1;
        return data;
    }

    /**
     * Helper method to delete a node from a tree.
     *
     * @param node given node.
     * @param data data you want to delete.
     * @return returns the deleted data.
     */
    private BSTNode<T> delete(BSTNode<T> node, T data) {
        if (node == null) {
            return node;
        }
        int compare = data.compareTo(node.getData());
        if (compare < 0) {
            node.setLeft(delete(node.getLeft(), data));
        } else if (compare > 0) {
            node.setRight(delete(node.getRight(), data));
        } else {
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }
            node.setData(findMin(node.getRight()).getData());
            node.setRight(delete(node.getRight(), node.getData()));
        }
        return node;
    }

    /**
     * Helper method to find minimum node
     *
     * @param node node to find min from.
     * @return returns minimum child node.
     */
    private BSTNode<T> findMin(BSTNode<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        BSTNode<T> result = (BSTNode<T>) find(root, data);
        if (result == null) {
            throw new NoSuchElementException("The data you're looking for doesn't exist.");
        }
        return result.getData();
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        return find(root, data) != null;
    }

    /**
     * Finds a data in the BST.
     *
     * @param data data you want to find.
     * @param node BST node.
     * @return data that is found.
     */
    private BSTNode<T> find(BSTNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        int compare = data.compareTo(node.getData());
        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return find(node.getLeft(), data);
        } else {
            return find(node.getRight(), data);
        }
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    /**
     * Helper method for doing preorder traversal.
     *
     * @param node node to start at
     * @param result result array to add nodes to
     */
    private void preorder(BSTNode<T> node, List<T> result) {
        if (node == null) {
            return;
        }
        result.add(node.getData());
        preorder(node.getLeft(), result);
        preorder(node.getRight(), result);
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    /**
     * Helper method for inorder traversal.
     *
     * @param node node to start at
     * @param result result list to add nodes to
     */
    private void inorder(BSTNode<T> node, List<T> result) {
        if (node == null) {
            return;
        }
        inorder(node.getLeft(), result);
        result.add(node.getData());
        inorder(node.getRight(), result);
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    /**
     * Helper method for postorder traversal.
     *
     * @param node node to start at
     * @param result result list to add nodes to
     */
    private void postorder(BSTNode<T> node, List<T> result) {
        if (node == null) {
            return;
        }
        postorder(node.getLeft(), result);
        postorder(node.getRight(), result);
        result.add(node.getData());
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<BSTNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            BSTNode<T> current = queue.poll();
            result.add(current.getData());
            if (current.getLeft() != null) {
                queue.offer(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.offer(current.getRight());
            }
        }
        return result;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return height(root);
    }

    /**
     * Helper method to find the height of the tree.
     *
     * @param node node to start from
     * @return returns height.
     */
    private int height(BSTNode<T> node) {
        if (node == null) {
            return -1;
        }
        int lH = height(node.getLeft());
        int rH = height(node.getRight());
        return Math.max(lH, rH) + 1;
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Finds the path between two elements in the tree, specifically the path
     * from data1 to data2, inclusive of both.
     *
     * This must be done recursively.
     * 
     * A good way to start is by finding the deepest common ancestor (DCA) of both data
     * and add it to the list. You will most likely have to split off and
     * traverse the tree for each piece of data adding to the list in such a
     * way that it will return the path in the correct order without requiring any
     * list manipulation later. One way to accomplish this (after adding the DCA
     * to the list) is to then traverse to data1 while adding its ancestors
     * to the front of the list. Finally, traverse to data2 while adding its
     * ancestors to the back of the list. 
     *
     * Please note that there is no relationship between the data parameters 
     * in that they may not belong to the same branch. 
     * 
     * You may only use 1 list instance to complete this method. Think about
     * what type of list to use considering the Big-O efficiency of the list
     * operations.
     *
     * This method only needs to traverse to the deepest common ancestor once.
     * From that node, go to each data in one traversal each. Failure to do
     * so will result in a penalty.
     * 
     * If both data1 and data2 are equal and in the tree, the list should be
     * of size 1 and contain the element from the tree equal to data1 and data2.
     *
     * Ex:
     * Given the following BST composed of Integers
     *              50
     *          /        \
     *        25         75
     *      /    \
     *     12    37
     *    /  \    \
     *   11   15   40
     *  /
     * 10
     * findPathBetween(10, 40) should return the list [10, 11, 12, 25, 37, 40]
     * findPathBetween(50, 37) should return the list [50, 25, 37]
     * findPathBetween(75, 75) should return the list [75]
     *
     * Must be O(log n) for a balanced tree and O(n) for worst case.
     *
     * @param data1 the data to start the path from
     * @param data2 the data to end the path on
     * @return the unique path between the two elements
     * @throws java.lang.IllegalArgumentException if either data1 or data2 is
     *                                            null
     * @throws java.util.NoSuchElementException   if data1 or data2 is not in
     *                                            the tree
     */
    public List<T> findPathBetween(T data1, T data2) {
        if (data1 == null || data2 == null) {
            throw new IllegalArgumentException("Data can't be null.");
        }
        if (!contains(data1) || !contains(data2)) {
            throw new NoSuchElementException("The data you're looking for doesn't exist.");
        }
        BSTNode<T> ancestor = findLCA(root, data1, data2);
        if (ancestor == null) {
            throw new NoSuchElementException("Common ancestor not found.");
        }
        List<T> path = new ArrayList<>();
        findAncestor(ancestor, data1, path);
        path.add(ancestor.getData());

        List<T> reversePath = new ArrayList<>();
        findAncestor(ancestor, data2, reversePath);
        for(int i = reversePath.size() - 2; i >= 0; i--) {
            path.add(reversePath.get(i));
        }
        return path;
    }

    /**
     * Helper method to find lowest common ancestor between two datas.
     *
     * @param node starting node
     * @param data1 first data
     * @param data2 second data
     * @return returns the lowest common ancestor
     */
    private BSTNode<T> findLCA(BSTNode<T> node, T data1, T data2) {
        if (node == null) {
            return null;
        }

        int compare1 = data1.compareTo(node.getData());
        int compare2 = data2.compareTo(node.getData());

        if (compare1 < 0 && compare2 < 0) {
            return findLCA(node.getLeft(), data1, data2);
        } else if (compare1 > 0 && compare2 > 0) {
            return findLCA(node.getRight(), data1, data2);
        } else {
            return node;
        }
    }

    /**
     * Helper method to find the path to the ancestors.
     *
     * @param node starting node
     * @param data data 1
     * @param path given list to add to
     * @return returns if the data has an ancestor
     */
    private boolean findAncestor(BSTNode<T> node, T data, List<T> path) {
        if (node == null) {
            return false;
        }

        if (node.getData().equals(data) || findAncestor(node.getLeft(), data, path) || findAncestor(node.getRight(), data, path)) {
            path.add(node.getData());
            return true;
        }

        return false;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
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
