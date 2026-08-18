package datastructures;

/**
 * ============================================================
 *  DynamicArray<T> — Custom Generic ArrayList Implementation
 * ============================================================
 * A resizable array that doubles its capacity whenever it's full
 * and halves it when the load factor drops below 25%.
 *
 * Internal Representation:
 *   - Object[] data  : backing array
 *   - int size       : number of live elements
 *   - int capacity   : current allocated size of backing array
 *
 * Default initial capacity: 8
 *
 * Complexity Overview:
 * ┌──────────────────┬────────────┬────────────┐
 * │ Operation        │ Time       │ Space      │
 * ├──────────────────┼────────────┼────────────┤
 * │ add(T)           │ O(1) amort │ O(1)       │
 * │ get(index)       │ O(1)       │ O(1)       │
 * │ set(index, T)    │ O(1)       │ O(1)       │
 * │ remove(index)    │ O(n)       │ O(1)       │
 * │ size / isEmpty   │ O(1)       │ O(1)       │
 * │ resize (internal)│ O(n)       │ O(n)       │
 * └──────────────────┴────────────┴────────────┘
 */
public class DynamicArray<T> {

    // ── Fields ────────────────────────────────────────────────────────────
    private static final int DEFAULT_CAPACITY = 8;

    /** Backing array — stores elements as raw Objects due to Java generics erasure. */
    private Object[] data;

    /** Number of elements currently stored. */
    private int size;

    /** Current allocated capacity of the backing array. */
    private int capacity;

    // ── Constructors ──────────────────────────────────────────────────────

    /**
     * Creates a DynamicArray with the default initial capacity (8).
     */
    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a DynamicArray with a specified initial capacity.
     *
     * @param initialCapacity must be > 0
     * @throws IllegalArgumentException if initialCapacity <= 0
     */
    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                "Initial capacity must be positive, got: " + initialCapacity);
        }
        this.capacity = initialCapacity;
        this.data     = new Object[capacity];
        this.size     = 0;
    }

    // ── Core Operations ───────────────────────────────────────────────────

    /**
     * Appends an element to the end of the array.
     * Triggers a resize (doubling) when the backing array is full.
     *
     * Time  : O(1) amortized  — occasional O(n) copy during resize
     * Space : O(1) per call   — O(n) across all inserts
     *
     * @param element the value to append (may be null)
     */
    public void add(T element) {
        ensureCapacity();
        data[size++] = element;
    }

    /**
     * Retrieves the element at the given index.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @param index 0-based index
     * @return the element at {@code index}
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    /**
     * Replaces the element at the given index with a new value.
     *
     * Time  : O(1)
     * Space : O(1)
     *
     * @param index   0-based index
     * @param element the new value
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void set(int index, T element) {
        checkIndex(index);
        data[index] = element;
    }

    /**
     * Removes and returns the element at the given index.
     * All subsequent elements are shifted left by one position.
     * Triggers a resize (halving) if load factor drops below 25%.
     *
     * Time  : O(n)  — worst case shift of all elements
     * Space : O(1)
     *
     * @param index 0-based index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];

        // Shift elements left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null; // allow GC to reclaim

        // Shrink if we are using less than 25% of capacity
        if (size > 0 && size <= capacity / 4) {
            resize(capacity / 2);
        }
        return removed;
    }

    // ── Query Operations ──────────────────────────────────────────────────

    /**
     * Returns the number of elements currently stored.
     *
     * Time  : O(1)
     * Space : O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Returns {@code true} if the array contains no elements.
     *
     * Time  : O(1)
     * Space : O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the current backing-array capacity (for diagnostic/demo use).
     */
    public int capacity() {
        return capacity;
    }

    // ── Display ───────────────────────────────────────────────────────────

    /**
     * Prints all elements in insertion order.
     *
     * Time  : O(n)
     * Space : O(1)
     */
    public void print() {
        if (isEmpty()) {
            System.out.println("  [ EMPTY ]");
            return;
        }
        StringBuilder sb = new StringBuilder("  [ ");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append("  |  ");
        }
        sb.append(" ]");
        System.out.println(sb);
        System.out.printf("  size=%d  capacity=%d%n", size, capacity);
    }

    // ── Internal Helpers ──────────────────────────────────────────────────

    /**
     * Ensures there is room for at least one more element.
     * Doubles capacity if the array is full.
     */
    private void ensureCapacity() {
        if (size == capacity) {
            resize(capacity * 2);
        }
    }

    /**
     * Creates a new backing array of the given size and copies existing elements.
     *
     * Time  : O(n)
     * Space : O(n)  (new array allocation)
     *
     * @param newCapacity the target array size
     */
    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data     = newData;
        capacity = newCapacity;
    }

    /**
     * Validates that {@code index} is in [0, size).
     *
     * @throws IndexOutOfBoundsException if out of range
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " is out of bounds for size " + size);
        }
    }

    // ── Object Overrides ──────────────────────────────────────────────────

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
