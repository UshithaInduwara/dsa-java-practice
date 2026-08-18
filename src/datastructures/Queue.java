package datastructures;

/**
 * ============================================================
 *  Queue<T> — Two Implementations in One Class
 * ============================================================
 * FIFO (First-In, First-Out) data structure provided in two variants:
 *
 *  1. CircularArrayQueue<T>  — backed by a fixed-capacity circular array
 *                              (auto-resizes when full)
 *  2. LinkedQueue<T>         — backed by a singly-linked chain of nodes
 *
 * ============================================================
 *  CIRCULAR ARRAY QUEUE
 * ============================================================
 * Uses modular arithmetic to wrap the front/rear indices around
 * the backing array, avoiding O(n) shifts on dequeue.
 *
 * Complexity:
 * ┌───────────┬────────────┬────────┐
 * │ Operation │ Time       │ Space  │
 * ├───────────┼────────────┼────────┤
 * │ enqueue   │ O(1) amort │ O(1)   │
 * │ dequeue   │ O(1)       │ O(1)   │
 * │ peek      │ O(1)       │ O(1)   │
 * │ isEmpty   │ O(1)       │ O(1)   │
 * │ size      │ O(1)       │ O(1)   │
 * └───────────┴────────────┴────────┘
 *
 * ============================================================
 *  LINKED QUEUE
 * ============================================================
 * Complexity:
 * ┌───────────┬────────┬────────┐
 * │ Operation │ Time   │ Space  │
 * ├───────────┼────────┼────────┤
 * │ enqueue   │ O(1)   │ O(1)   │
 * │ dequeue   │ O(1)   │ O(1)   │
 * │ peek      │ O(1)   │ O(1)   │
 * │ isEmpty   │ O(1)   │ O(1)   │
 * │ size      │ O(1)   │ O(1)   │
 * └───────────┴────────┴────────┘
 */
public class Queue {

    // =========================================================
    //  CIRCULAR ARRAY QUEUE
    // =========================================================

    /**
     * A generic FIFO queue backed by a circular (ring-buffer) array.
     *
     * <pre>
     * Indices wrap around using modulo:
     *   rear  = (rear  + 1) % capacity   on enqueue
     *   front = (front + 1) % capacity   on dequeue
     * </pre>
     *
     * Auto-resizes (doubles) when the array is full.
     */
    public static class CircularArrayQueue<T> {

        private static final int DEFAULT_CAPACITY = 8;

        /** Backing array. */
        private Object[] data;

        /** Index of the front (oldest) element. */
        private int front;

        /** Index of the next empty slot at the rear. */
        private int rear;

        /** Number of elements currently stored. */
        private int size;

        /** Current allocated capacity. */
        private int capacity;

        // ── Constructor ───────────────────────────────────────────────────

        /** Creates an empty circular queue with default capacity (8). */
        public CircularArrayQueue() {
            capacity = DEFAULT_CAPACITY;
            data     = new Object[capacity];
            front    = 0;
            rear     = 0;
            size     = 0;
        }

        // ── Core Operations ───────────────────────────────────────────────

        /**
         * Adds an element to the REAR of the queue.
         * Doubles backing array if already full.
         *
         * Time  : O(1) amortized
         * Space : O(1)
         *
         * @param element the value to enqueue
         */
        public void enqueue(T element) {
            if (size == capacity) {
                resize(capacity * 2);
            }
            data[rear] = element;
            rear       = (rear + 1) % capacity;
            size++;
        }

        /**
         * Removes and returns the element at the FRONT of the queue.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the front element
         * @throws java.util.NoSuchElementException if the queue is empty
         */
        @SuppressWarnings("unchecked")
        public T dequeue() {
            checkNotEmpty();
            T element  = (T) data[front];
            data[front] = null; // GC help
            front      = (front + 1) % capacity;
            size--;
            return element;
        }

        /**
         * Returns (without removing) the element at the FRONT.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the front element
         * @throws java.util.NoSuchElementException if the queue is empty
         */
        @SuppressWarnings("unchecked")
        public T peek() {
            checkNotEmpty();
            return (T) data[front];
        }

        /** Returns the number of elements in the queue. Time: O(1). */
        public int size() { return size; }

        /** Returns {@code true} if the queue is empty. Time: O(1). */
        public boolean isEmpty() { return size == 0; }

        /**
         * Prints the queue from FRONT to REAR.
         *
         * Time  : O(n)
         */
        public void print() {
            if (isEmpty()) {
                System.out.println("  FRONT -> [ EMPTY ] <- REAR");
                return;
            }
            StringBuilder sb = new StringBuilder("  FRONT -> [ ");
            for (int i = 0; i < size; i++) {
                sb.append(data[(front + i) % capacity]);
                if (i < size - 1) sb.append(" | ");
            }
            sb.append(" ] <- REAR");
            System.out.println(sb);
            System.out.printf("  size=%d  capacity=%d%n", size, capacity);
        }

        // ── Helpers ───────────────────────────────────────────────────────

        /**
         * Resizes the backing array to {@code newCapacity}.
         * Elements are re-laid out starting at index 0 to simplify arithmetic.
         * Time: O(n).
         */
        private void resize(int newCapacity) {
            Object[] newData = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = data[(front + i) % capacity];
            }
            data     = newData;
            front    = 0;
            rear     = size;
            capacity = newCapacity;
        }

        private void checkNotEmpty() {
            if (isEmpty()) {
                throw new java.util.NoSuchElementException("Queue is empty.");
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("FRONT -> [");
            for (int i = 0; i < size; i++) {
                sb.append(data[(front + i) % capacity]);
                if (i < size - 1) sb.append(", ");
            }
            return sb.append("] <- REAR").toString();
        }
    }

    // =========================================================
    //  LINKED QUEUE
    // =========================================================

    /**
     * A generic FIFO queue backed by a singly-linked chain of nodes.
     * Enqueue appends to the tail (O(1) with tail pointer).
     * Dequeue removes from the head (O(1)).
     */
    public static class LinkedQueue<T> {

        // ── Inner Node class ──────────────────────────────────────────────
        private static class Node<T> {
            T       data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // ── Fields ────────────────────────────────────────────────────────

        /** The FRONT of the queue (oldest element). */
        private Node<T> front;

        /** The REAR of the queue (newest element). */
        private Node<T> rear;

        /** Number of elements in the queue. */
        private int size;

        // ── Constructor ───────────────────────────────────────────────────

        /** Creates an empty LinkedQueue. */
        public LinkedQueue() {
            front = null;
            rear  = null;
            size  = 0;
        }

        // ── Core Operations ───────────────────────────────────────────────

        /**
         * Adds an element to the REAR of the queue.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @param element the value to enqueue
         */
        public void enqueue(T element) {
            Node<T> newNode = new Node<>(element);
            if (isEmpty()) {
                front = rear = newNode;
            } else {
                rear.next = newNode;
                rear      = newNode;
            }
            size++;
        }

        /**
         * Removes and returns the element at the FRONT of the queue.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the front element
         * @throws java.util.NoSuchElementException if the queue is empty
         */
        public T dequeue() {
            checkNotEmpty();
            T data  = front.data;
            front   = front.next;
            if (front == null) rear = null; // queue became empty
            size--;
            return data;
        }

        /**
         * Returns (without removing) the element at the FRONT.
         *
         * Time  : O(1)
         * Space : O(1)
         *
         * @return the front element
         * @throws java.util.NoSuchElementException if the queue is empty
         */
        public T peek() {
            checkNotEmpty();
            return front.data;
        }

        /** Returns the number of elements in the queue. Time: O(1). */
        public int size() { return size; }

        /** Returns {@code true} if the queue is empty. Time: O(1). */
        public boolean isEmpty() { return size == 0; }

        /**
         * Prints the queue from FRONT to REAR using arrow notation.
         *
         * Time  : O(n)
         */
        public void print() {
            if (isEmpty()) {
                System.out.println("  FRONT -> [ EMPTY ] <- REAR");
                return;
            }
            StringBuilder sb = new StringBuilder("  FRONT -> ");
            Node<T> current = front;
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) sb.append(" -> ");
                current = current.next;
            }
            sb.append(" <- REAR");
            System.out.println(sb);
            System.out.printf("  size=%d%n", size);
        }

        // ── Helpers ───────────────────────────────────────────────────────

        private void checkNotEmpty() {
            if (isEmpty()) {
                throw new java.util.NoSuchElementException("Queue is empty.");
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("FRONT -> [");
            Node<T> current = front;
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) sb.append(", ");
                current = current.next;
            }
            return sb.append("] <- REAR").toString();
        }
    }
}
