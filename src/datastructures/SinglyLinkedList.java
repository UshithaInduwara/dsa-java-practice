package datastructures;

/**
 * ============================================================
 *  SinglyLinkedList<T> — Generic Singly-Linked List
 * ============================================================
 * Each element (Node) holds a data value and a single pointer
 * to the next node.  The list tracks both head and tail for
 * O(1) insertions at either end.
 *
 * Complexity Overview:
 * ┌──────────────────────┬────────┬────────┐
 * │ Operation            │ Time   │ Space  │
 * ├──────────────────────┼────────┼────────┤
 * │ insertHead(T)        │ O(1)   │ O(1)   │
 * │ insertTail(T)        │ O(1)   │ O(1)   │
 * │ insertAt(index, T)   │ O(n)   │ O(1)   │
 * │ deleteHead()         │ O(1)   │ O(1)   │
 * │ deleteTail()         │ O(n)   │ O(1)   │
 * │ deleteValue(T)       │ O(n)   │ O(1)   │
 * │ search(T)            │ O(n)   │ O(1)   │
 * │ printForward()       │ O(n)   │ O(1)   │
 * │ size / isEmpty       │ O(1)   │ O(1)   │
 * └──────────────────────┴────────┴────────┘
 */
public class SinglyLinkedList<T> {

    // ── Inner Node class ──────────────────────────────────────────────────

    /**
     * A single element of the linked list.
     */
    private static class Node<T> {
        T    data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────

    /** Pointer to the first node; null when the list is empty. */
    private Node<T> head;

    /** Pointer to the last node; null when the list is empty. */
    private Node<T> tail;

    /** Cached count of live nodes. */
    private int size;

    // ── Constructor ───────────────────────────────────────────────────────

    /** Creates an empty singly-linked list. */
    public SinglyLinkedList() {
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
            head = newNode;
        }
        size++;
    }

    /**
     * Inserts a new node at the TAIL (back) of the list.
     *
     * Time  : O(1)   — tail pointer kept up-to-date
     * Space : O(1)
     *
     * @param data the value to store
     */
    public void insertTail(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Inserts a new node at a specific 0-based index.
     * Index 0 is equivalent to insertHead; index == size is insertTail.
     *
     * Time  : O(n)   — traversal to position
     * Space : O(1)
     *
     * @param index 0-based position (0 .. size, inclusive)
     * @param data  the value to store
     * @throws IndexOutOfBoundsException if index is out of [0, size]
     */
    public void insertAt(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + size);
        }
        if (index == 0)    { insertHead(data); return; }
        if (index == size) { insertTail(data); return; }

        Node<T> prev = getNode(index - 1);
        Node<T> newNode = new Node<>(data);
        newNode.next = prev.next;
        prev.next    = newNode;
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
        head   = head.next;
        if (head == null) tail = null; // list became empty
        size--;
        return data;
    }

    /**
     * Removes and returns the TAIL node's data.
     * Requires a full traversal to find the new tail.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * @return the removed data value
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T deleteTail() {
        checkNotEmpty();
        T data = tail.data;
        if (head == tail) {         // single element
            head = tail = null;
        } else {
            Node<T> prev = head;
            while (prev.next != tail) {
                prev = prev.next;
            }
            prev.next = null;
            tail = prev;
        }
        size--;
        return data;
    }

    /**
     * Removes the FIRST occurrence of the specified value.
     * Uses .equals() for comparison.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * @param data the value to remove
     * @return {@code true} if the value was found and removed, {@code false} otherwise
     */
    public boolean deleteValue(T data) {
        if (isEmpty()) return false;

        // Edge case: head is the target
        if (equals(head.data, data)) {
            deleteHead();
            return true;
        }

        Node<T> prev = head;
        while (prev.next != null) {
            if (equals(prev.next.data, data)) {
                if (prev.next == tail) tail = prev; // update tail
                prev.next = prev.next.next;
                size--;
                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    // ── Search ────────────────────────────────────────────────────────────

    /**
     * Searches for a value and returns its 0-based index, or -1 if not found.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * @param data the value to search for
     * @return index of first occurrence, or -1
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

    /** Returns the number of nodes in the list. Time: O(1). */
    public int size() { return size; }

    /** Returns {@code true} if the list has no nodes. Time: O(1). */
    public boolean isEmpty() { return size == 0; }

    // ── Display ───────────────────────────────────────────────────────────

    /**
     * Prints the list from head to tail using arrow notation.
     *
     * Time  : O(n)
     * Space : O(1)
     *
     * Example output:  HEAD -> 10 -> 20 -> 30 -> NULL
     */
    public void printForward() {
        if (isEmpty()) {
            System.out.println("  HEAD -> NULL  (empty list)");
            return;
        }
        StringBuilder sb = new StringBuilder("  HEAD");
        Node<T> current = head;
        while (current != null) {
            sb.append(" -> ").append(current.data);
            current = current.next;
        }
        sb.append(" -> NULL");
        System.out.println(sb);
        System.out.printf("  size=%d%n", size);
    }

    // ── Internal Helpers ──────────────────────────────────────────────────

    /**
     * Returns the node at the given 0-based index (no bounds checked).
     * Time: O(n).
     */
    private Node<T> getNode(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
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
        StringBuilder sb = new StringBuilder("HEAD");
        Node<T> current = head;
        while (current != null) {
            sb.append(" -> ").append(current.data);
            current = current.next;
        }
        return sb.append(" -> NULL").toString();
    }
}
