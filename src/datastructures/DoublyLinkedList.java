package datastructures;

/**
 * ============================================================
 *  DoublyLinkedList<T> — Generic Doubly-Linked List
 * ============================================================
 * Each node holds a data value plus TWO pointers: next and prev.
 * This enables O(1) insertions AND deletions at both ends, and
 * supports backward traversal.
 *
 * Complexity Overview:
 * ┌──────────────────────┬────────┬────────┐
 * │ Operation            │ Time   │ Space  │
 * ├──────────────────────┼────────┼────────┤
 * │ insertHead(T)        │ O(1)   │ O(1)   │
 * │ insertTail(T)        │ O(1)   │ O(1)   │
 * │ insertAt(index, T)   │ O(n)   │ O(1)   │
 * │ deleteHead()         │ O(1)   │ O(1)   │
 * │ deleteTail()         │ O(1)   │ O(1)   │
 * │ deleteValue(T)       │ O(n)   │ O(1)   │
 * │ search(T)            │ O(n)   │ O(1)   │
 * │ printForward()       │ O(n)   │ O(1)   │
 * │ printBackward()      │ O(n)   │ O(1)   │
 * │ size / isEmpty       │ O(1)   │ O(1)   │
 * └──────────────────────┴────────┴────────┘
 */
public class DoublyLinkedList<T> {

    // ── Inner Node class ──────────────────────────────────────────────────

    /**
     * A single node in the doubly-linked list.
     * Holds data plus prev/next pointers.
     */
    private static class Node<T> {
        T       data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────

    /** First node in the list. */
    private Node<T> head;

    /** Last node in the list. */
    private Node<T> tail;

    /** Cached count of live nodes. */
    private int size;

    // ── Constructor ───────────────────────────────────────────────────────

    /** Creates an empty doubly-linked list. */
    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // ── Insert Operations ─────────────────────────────────────────────────

    /**
     * Inserts a new node at the HEAD (front) of the list.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @param data the value to store
     */
    public void insertHead(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev    = newNode;
            head         = newNode;
        }
        size++;
    }

    /**
     * Inserts a new node at the TAIL (back) of the list.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @param data the value to store
     */
    public void insertTail(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next    = newNode;
            tail         = newNode;
        }
        size++;
    }

    /**
     * Inserts a new node at the specified 0-based index.
     * Index 0 == insertHead; index == size == insertTail.
     *
     * Time  : O(n)   — traversal; splits toward nearer end for efficiency
     * Space : O(1)
     *
     * @param index position in [0, size]
     * @param data  the value to store
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void insertAt(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + size);
        }
        if (index == 0)    { insertHead(data); return; }
        if (index == size) { insertTail(data); return; }

        // Navigate from the closer end for O(n/2) best case
        Node<T> successor = getNode(index);
        Node<T> newNode   = new Node<>(data);

        newNode.next        = successor;
        newNode.prev        = successor.prev;
        successor.prev.next = newNode;
        successor.prev      = newNode;
        size++;
    }

    // ── Delete Operations ─────────────────────────────────────────────────

    /**
     * Removes and returns the HEAD node's data.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @return the removed data value
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T deleteHead() {
        checkNotEmpty();
        T data = head.data;
        if (head == tail) {        // single node
            head = tail = null;
        } else {
            head      = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the TAIL node's data.
     * Unlike SinglyLinkedList, this is O(1) thanks to the prev pointer.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @return the removed data value
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T deleteTail() {
        checkNotEmpty();
        T data = tail.data;
        if (head == tail) {        // single node
            head = tail = null;
        } else {
            tail      = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    /**
     * Removes the FIRST occurrence of the given value from the list.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * @param data the value to search for and remove
     * @return {@code true} if the value was found and removed
     */
    public boolean deleteValue(T data) {
        Node<T> current = head;
        while (current != null) {
            if (equals(current.data, data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // ── Search ────────────────────────────────────────────────────────────

    /**
     * Finds and returns the 0-based index of the first occurrence of {@code data}.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * @param data the value to search for
     * @return index of first occurrence, or -1 if not found
     */
    public int search(T data) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (equals(current.data, data)) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    // ── Query ─────────────────────────────────────────────────────────────

    /** Returns the number of nodes. Time: O(1). */
    public int size() { return size; }

    /** Returns {@code true} if the list has no nodes. Time: O(1). */
    public boolean isEmpty() { return size == 0; }

    // ── Display ───────────────────────────────────────────────────────────

    /**
     * Prints the list from HEAD to TAIL.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Example: NULL <-> 10 <-> 20 <-> 30 <-> NULL
     */
    public void printForward() {
        if (isEmpty()) {
            System.out.println("  NULL <-> NULL  (empty list)");
            return;
        }
        StringBuilder sb = new StringBuilder("  NULL <-> ");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            sb.append(current.next != null ? " <-> " : " <-> NULL");
            current = current.next;
        }
        System.out.println(sb);
        System.out.printf("  size=%d%n", size);
    }

    /**
     * Prints the list from TAIL to HEAD (reverse traversal using prev pointers).
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Example: NULL <-> 30 <-> 20 <-> 10 <-> NULL
     */
    public void printBackward() {
        if (isEmpty()) {
            System.out.println("  NULL <-> NULL  (empty list)");
            return;
        }
        StringBuilder sb = new StringBuilder("  NULL <-> ");
        Node<T> current = tail;
        while (current != null) {
            sb.append(current.data);
            sb.append(current.prev != null ? " <-> " : " <-> NULL");
            current = current.prev;
        }
        System.out.println(sb);
        System.out.printf("  size=%d%n", size);
    }

    // ── Internal Helpers ──────────────────────────────────────────────────

    /**
     * Returns the node at 0-based index, traversing from the nearer end.
     * Time: O(n/2) average.
     */
    private Node<T> getNode(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    /**
     * Removes a node from the list by relinking its neighbors.
     * Time: O(1) given the node reference.
     */
    private void unlink(Node<T> node) {
        if (node == head && node == tail) {
            head = tail = null;
        } else if (node == head) {
            head      = head.next;
            head.prev = null;
        } else if (node == tail) {
            tail      = tail.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }

    /** Null-safe equality helper. */
    private boolean equals(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /** Throws if the list is empty. */
    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("List is empty.");
        }
    }

    // ── Object Overrides ──────────────────────────────────────────────────

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("NULL <-> ");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            sb.append(current.next != null ? " <-> " : " <-> NULL");
            current = current.next;
        }
        return sb.toString();
    }
}
