package datastructures;

/**
 * ============================================================
 *  Stack<T> — Two Implementations in One Class
 * ============================================================
 * LIFO (Last-In, First-Out) data structure provided in two variants:
 *
 *  1. ArrayStack<T>  — backed by a resizable Object array
 *  2. LinkedStack<T> — backed by a singly-linked chain of nodes
 *
 * Both are public static inner classes so they can be used
 * independently:
 *   Stack.ArrayStack<Integer>  arrStack  = new Stack.ArrayStack<>();
 *   Stack.LinkedStack<Integer> linkStack = new Stack.LinkedStack<>();
 *
 * ============================================================
 *  ARRAY STACK
 * ============================================================
 * Complexity:
 * ┌───────────┬────────────┬────────┐
 * │ Operation │ Time       │ Space  │
 * ├───────────┼────────────┼────────┤
 * │ push(T)   │ O(1) amort │ O(1)   │
 * │ pop()     │ O(1)       │ O(1)   │
 * │ peek()    │ O(1)       │ O(1)   │
 * │ isEmpty() │ O(1)       │ O(1)   │
 * │ size()    │ O(1)       │ O(1)   │
 * └───────────┴────────────┴────────┘
 *
 * ============================================================
 *  LINKED STACK
 * ============================================================
 * Complexity:
 * ┌───────────┬────────┬────────┐
 * │ Operation │ Time   │ Space  │
 * ├───────────┼────────┼────────┤
 * │ push(T)   │ O(1)   │ O(1)   │
 * │ pop()     │ O(1)   │ O(1)   │
 * │ peek()    │ O(1)   │ O(1)   │
 * │ isEmpty() │ O(1)   │ O(1)   │
 * │ size()    │ O(1)   │ O(1)   │
 * └───────────┴────────┴────────┘
 */
public class Stack {

    // =========================================================
    //  ARRAY-BACKED STACK
    // =========================================================

    /**
     * A generic stack implemented with a resizable backing array.
     * The "top" of the stack is at index {@code top} (the last pushed element).
     */
    public static class ArrayStack<T> {

        private static final int DEFAULT_CAPACITY = 8;

        /** Backing array; grows by 2x when full, shrinks by 0.5x when < 25% used. */
        private Object[] data;

        /** Index of the top element (-1 means empty). */
        private int top;

        /** Current capacity of the backing array. */
        private int capacity;

        // ── Constructor ───────────────────────────────────────────────────

        /** Creates an empty ArrayStack with default initial capacity (8). */
        public ArrayStack() {
            capacity = DEFAULT_CAPACITY;
            data     = new Object[capacity];
            top      = -1;
        }

        // ── Core Operations ───────────────────────────────────────────────

        /**
         * Pushes an element onto the top of the stack.
         * Doubles backing array capacity if full.
         *
         * Time  : O(1) amortized
         * Space : O(1)
         *
         * @param element the value to push
         */
        public void push(T element) {
            if (top + 1 == capacity) {
                resize(capacity * 2);
            }
            data[++top] = element;
        }

        /**
         * Removes and returns the top element.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the top element
         * @throws java.util.EmptyStackException if the stack is empty
         */
        @SuppressWarnings("unchecked")
        public T pop() {
            checkNotEmpty();
            T element = (T) data[top];
            data[top--] = null; // GC help
            if (top >= 0 && top + 1 <= capacity / 4) {
                resize(capacity / 2);
            }
            return element;
        }

        /**
         * Returns (without removing) the top element.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the top element
         * @throws java.util.EmptyStackException if the stack is empty
         */
        @SuppressWarnings("unchecked")
        public T peek() {
            checkNotEmpty();
            return (T) data[top];
        }

        /**
         * Returns the number of elements in the stack.
         *
         * Time  : O(1)
         */
        public int size() { return top + 1; }

        /**
         * Returns {@code true} if the stack contains no elements.
         *
         * Time  : O(1)
         */
        public boolean isEmpty() { return top == -1; }

        /**
         * Prints the stack from TOP to BOTTOM.
         *
         * Time  : O(n)
         */
        @SuppressWarnings("unchecked")
        public void print() {
            if (isEmpty()) {
                System.out.println("  [ EMPTY STACK ]");
                return;
            }
            System.out.println("  ┌──────────┐  <- TOP");
            for (int i = top; i >= 0; i--) {
                System.out.printf("  │  %-7s │%n", data[i]);
            }
            System.out.println("  └──────────┘  <- BOTTOM");
            System.out.printf("  size=%d  capacity=%d%n", size(), capacity);
        }

        // ── Helpers ───────────────────────────────────────────────────────

        private void resize(int newCapacity) {
            Object[] newData = new Object[newCapacity];
            System.arraycopy(data, 0, newData, 0, top + 1);
            data     = newData;
            capacity = newCapacity;
        }

        private void checkNotEmpty() {
            if (isEmpty()) throw new java.util.EmptyStackException();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("TOP -> [");
            for (int i = top; i >= 0; i--) {
                sb.append(data[i]);
                if (i > 0) sb.append(", ");
            }
            return sb.append("] <- BOTTOM").toString();
        }
    }

    // =========================================================
    //  LINKED-LIST-BACKED STACK
    // =========================================================

    /**
     * A generic stack implemented with a singly-linked chain of nodes.
     * The top of the stack is always the head of the chain.
     * No resizing needed — nodes are allocated on demand.
     */
    public static class LinkedStack<T> {

        // ── Inner Node class ──────────────────────────────────────────────
        private static class Node<T> {
            T       data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // ── Fields ────────────────────────────────────────────────────────

        /** The top of the stack (head of the linked chain). */
        private Node<T> top;

        /** Number of elements on the stack. */
        private int size;

        // ── Constructor ───────────────────────────────────────────────────

        /** Creates an empty LinkedStack. */
        public LinkedStack() {
            top  = null;
            size = 0;
        }

        // ── Core Operations ───────────────────────────────────────────────

        /**
         * Pushes an element onto the top of the stack.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @param element the value to push
         */
        public void push(T element) {
            Node<T> newNode = new Node<>(element);
            newNode.next    = top;
            top             = newNode;
            size++;
        }

        /**
         * Removes and returns the top element.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the top element
         * @throws java.util.EmptyStackException if the stack is empty
         */
        public T pop() {
            checkNotEmpty();
            T data = top.data;
            top    = top.next;
            size--;
            return data;
        }

        /**
         * Returns (without removing) the top element.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the top element
         * @throws java.util.EmptyStackException if the stack is empty
         */
        public T peek() {
            checkNotEmpty();
            return top.data;
        }

        /**
         * Returns the number of elements on the stack.
         *
         * Time  : O(1)
         */
        public int size() { return size; }

        /**
         * Returns {@code true} if the stack is empty.
         *
         * Time  : O(1)
         */
        public boolean isEmpty() { return top == null; }

        /**
         * Prints the stack from TOP to BOTTOM.
         *
         * Time  : O(n)
         */
        public void print() {
            if (isEmpty()) {
                System.out.println("  [ EMPTY STACK ]");
                return;
            }
            System.out.println("  ┌──────────┐  <- TOP");
            Node<T> current = top;
            while (current != null) {
                System.out.printf("  │  %-7s │%n", current.data);
                current = current.next;
            }
            System.out.println("  └──────────┘  <- BOTTOM");
            System.out.printf("  size=%d%n", size);
        }

        // ── Helpers ───────────────────────────────────────────────────────

        private void checkNotEmpty() {
            if (isEmpty()) throw new java.util.EmptyStackException();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("TOP -> ");
            Node<T> current = top;
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) sb.append(" -> ");
                current = current.next;
            }
            return sb.append(" <- BOTTOM").toString();
        }
    }
}
