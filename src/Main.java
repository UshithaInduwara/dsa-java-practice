import datastructures.DynamicArray;
import datastructures.DoublyLinkedList;
import datastructures.Queue;
import datastructures.SinglyLinkedList;
import datastructures.Stack;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * ============================================================
 *  Main.java — Interactive DSA Terminal Runner
 * ============================================================
 * A menu-driven CLI for exploring and testing each data structure
 * interactively. All operations display immediate visual feedback.
 *
 * Navigation:
 *   - Top-level menu: pick a data structure
 *   - Sub-menu: perform operations on the chosen structure
 *   - Enter 0 or "Back" to return to the previous menu
 *   - Enter -1 at the top menu to quit
 *
 * Usage (from project root):
 *   javac -d out src/datastructures/*.java src/Main.java
 *   java  -cp out Main
 */
public class Main {

    // ── Shared Scanner ────────────────────────────────────────────────────
    private static final Scanner sc = new Scanner(System.in);

    // ── ANSI colour codes for richer terminal output ──────────────────────
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BLUE   = "\u001B[34m";

    // ── Entry Point ───────────────────────────────────────────────────────

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("  Your choice: ");
            switch (choice) {
                case 1  -> menuDynamicArray();
                case 2  -> menuSinglyLinkedList();
                case 3  -> menuDoublyLinkedList();
                case 4  -> menuStack();
                case 5  -> menuQueue();
                case 0  -> { printInfo("Goodbye! Keep practising DSA!"); running = false; }
                default -> printError("Invalid choice. Please select 1-5 or 0 to quit.");
            }
        }
        sc.close();
    }

    // =========================================================
    //  TOP-LEVEL MENUS
    // =========================================================

    /** Main menu listing all available data structures. */
    private static void printMainMenu() {
        System.out.println();
        System.out.println(BOLD + CYAN + "╔══════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN + "║     DSA INTERACTIVE TERMINAL RUNNER     ║" + RESET);
        System.out.println(BOLD + CYAN + "╠══════════════════════════════════════════╣" + RESET);
        System.out.println(BOLD + CYAN + "║  Select a Data Structure:               ║" + RESET);
        System.out.println(BOLD + CYAN + "║                                          ║" + RESET);
        System.out.println(BOLD + CYAN + "║  1. Dynamic Array (ArrayList)            ║" + RESET);
        System.out.println(BOLD + CYAN + "║  2. Singly Linked List                   ║" + RESET);
        System.out.println(BOLD + CYAN + "║  3. Doubly Linked List                   ║" + RESET);
        System.out.println(BOLD + CYAN + "║  4. Stack  (Array | Linked)              ║" + RESET);
        System.out.println(BOLD + CYAN + "║  5. Queue  (Circular | Linked)           ║" + RESET);
        System.out.println(BOLD + CYAN + "║  0. Exit                                 ║" + RESET);
        System.out.println(BOLD + CYAN + "╚══════════════════════════════════════════╝" + RESET);
    }

    // =========================================================
    //  1. DYNAMIC ARRAY MENU
    // =========================================================

    private static void menuDynamicArray() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        boolean active = true;
        while (active) {
            printSubHeader("Dynamic Array");
            System.out.println("  1. Add element (append)");
            System.out.println("  2. Get element by index");
            System.out.println("  3. Set element at index");
            System.out.println("  4. Remove element by index");
            System.out.println("  5. Size & isEmpty");
            System.out.println("  6. Print / Display state");
            System.out.println("  0. Back to main menu");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter integer to add: ");
                    arr.add(val);
                    printSuccess("Added " + val + " to the array.");
                    arr.print();
                    printComplexity("add(T)", "O(1) amortized", "O(1)");
                }
                case 2 -> {
                    int idx = readInt("  Enter index (0-based): ");
                    try {
                        int val = arr.get(idx);
                        printSuccess("arr[" + idx + "] = " + val);
                    } catch (IndexOutOfBoundsException e) { printError(e.getMessage()); }
                    printComplexity("get(index)", "O(1)", "O(1)");
                }
                case 3 -> {
                    int idx = readInt("  Enter index to update: ");
                    int val = readInt("  Enter new value: ");
                    try {
                        arr.set(idx, val);
                        printSuccess("Set arr[" + idx + "] = " + val);
                        arr.print();
                    } catch (IndexOutOfBoundsException e) { printError(e.getMessage()); }
                    printComplexity("set(index, T)", "O(1)", "O(1)");
                }
                case 4 -> {
                    int idx = readInt("  Enter index to remove: ");
                    try {
                        int removed = arr.remove(idx);
                        printSuccess("Removed value " + removed + " from index " + idx);
                        arr.print();
                    } catch (IndexOutOfBoundsException e) { printError(e.getMessage()); }
                    printComplexity("remove(index)", "O(n)", "O(1)");
                }
                case 5 -> {
                    printInfo("Size    : " + arr.size());
                    printInfo("IsEmpty : " + arr.isEmpty());
                    printInfo("Capacity: " + arr.capacity());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 6 -> {
                    printInfo("Current state of the Dynamic Array:");
                    arr.print();
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-6.");
            }
        }
    }

    // =========================================================
    //  2. SINGLY LINKED LIST MENU
    // =========================================================

    private static void menuSinglyLinkedList() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        boolean active = true;
        while (active) {
            printSubHeader("Singly Linked List");
            System.out.println("  1. Insert at HEAD");
            System.out.println("  2. Insert at TAIL");
            System.out.println("  3. Insert at INDEX");
            System.out.println("  4. Delete HEAD");
            System.out.println("  5. Delete TAIL");
            System.out.println("  6. Delete by VALUE");
            System.out.println("  7. Search for value");
            System.out.println("  8. Print (Forward)");
            System.out.println("  9. Size & isEmpty");
            System.out.println("  0. Back to main menu");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to insert at head: ");
                    list.insertHead(val);
                    printSuccess("Inserted " + val + " at HEAD.");
                    list.printForward();
                    printComplexity("insertHead(T)", "O(1)", "O(1)");
                }
                case 2 -> {
                    int val = readInt("  Enter value to insert at tail: ");
                    list.insertTail(val);
                    printSuccess("Inserted " + val + " at TAIL.");
                    list.printForward();
                    printComplexity("insertTail(T)", "O(1)", "O(1)");
                }
                case 3 -> {
                    int idx = readInt("  Enter 0-based index: ");
                    int val = readInt("  Enter value to insert: ");
                    try {
                        list.insertAt(idx, val);
                        printSuccess("Inserted " + val + " at index " + idx + ".");
                        list.printForward();
                    } catch (IndexOutOfBoundsException e) { printError(e.getMessage()); }
                    printComplexity("insertAt(index, T)", "O(n)", "O(1)");
                }
                case 4 -> {
                    try {
                        int removed = list.deleteHead();
                        printSuccess("Deleted HEAD value: " + removed);
                        list.printForward();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("deleteHead()", "O(1)", "O(1)");
                }
                case 5 -> {
                    try {
                        int removed = list.deleteTail();
                        printSuccess("Deleted TAIL value: " + removed);
                        list.printForward();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("deleteTail()", "O(n)", "O(1)");
                }
                case 6 -> {
                    int val = readInt("  Enter value to delete: ");
                    boolean found = list.deleteValue(val);
                    if (found) {
                        printSuccess("Deleted first occurrence of " + val + ".");
                        list.printForward();
                    } else {
                        printError("Value " + val + " not found in the list.");
                    }
                    printComplexity("deleteValue(T)", "O(n)", "O(1)");
                }
                case 7 -> {
                    int val = readInt("  Enter value to search for: ");
                    int idx = list.search(val);
                    if (idx != -1) {
                        printSuccess("Found " + val + " at index " + idx + ".");
                    } else {
                        printError("Value " + val + " not found.");
                    }
                    printComplexity("search(T)", "O(n)", "O(1)");
                }
                case 8 -> {
                    printInfo("Current state of Singly Linked List:");
                    list.printForward();
                }
                case 9 -> {
                    printInfo("Size    : " + list.size());
                    printInfo("IsEmpty : " + list.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-9.");
            }
        }
    }

    // =========================================================
    //  3. DOUBLY LINKED LIST MENU
    // =========================================================

    private static void menuDoublyLinkedList() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        boolean active = true;
        while (active) {
            printSubHeader("Doubly Linked List");
            System.out.println("  1.  Insert at HEAD");
            System.out.println("  2.  Insert at TAIL");
            System.out.println("  3.  Insert at INDEX");
            System.out.println("  4.  Delete HEAD");
            System.out.println("  5.  Delete TAIL");
            System.out.println("  6.  Delete by VALUE");
            System.out.println("  7.  Search for value");
            System.out.println("  8.  Print FORWARD  (head -> tail)");
            System.out.println("  9.  Print BACKWARD (tail -> head)");
            System.out.println("  10. Size & isEmpty");
            System.out.println("  0.  Back to main menu");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to insert at head: ");
                    list.insertHead(val);
                    printSuccess("Inserted " + val + " at HEAD.");
                    list.printForward();
                    printComplexity("insertHead(T)", "O(1)", "O(1)");
                }
                case 2 -> {
                    int val = readInt("  Enter value to insert at tail: ");
                    list.insertTail(val);
                    printSuccess("Inserted " + val + " at TAIL.");
                    list.printForward();
                    printComplexity("insertTail(T)", "O(1)", "O(1)");
                }
                case 3 -> {
                    int idx = readInt("  Enter 0-based index: ");
                    int val = readInt("  Enter value to insert: ");
                    try {
                        list.insertAt(idx, val);
                        printSuccess("Inserted " + val + " at index " + idx + ".");
                        list.printForward();
                    } catch (IndexOutOfBoundsException e) { printError(e.getMessage()); }
                    printComplexity("insertAt(index, T)", "O(n)", "O(1)");
                }
                case 4 -> {
                    try {
                        int removed = list.deleteHead();
                        printSuccess("Deleted HEAD value: " + removed);
                        list.printForward();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("deleteHead()", "O(1)", "O(1)");
                }
                case 5 -> {
                    try {
                        int removed = list.deleteTail();
                        printSuccess("Deleted TAIL value: " + removed);
                        list.printForward();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("deleteTail()", "O(1)", "O(1)  ← Advantage of DLL over SLL!");
                }
                case 6 -> {
                    int val = readInt("  Enter value to delete: ");
                    boolean found = list.deleteValue(val);
                    if (found) {
                        printSuccess("Deleted first occurrence of " + val + ".");
                        list.printForward();
                    } else {
                        printError("Value " + val + " not found.");
                    }
                    printComplexity("deleteValue(T)", "O(n)", "O(1)");
                }
                case 7 -> {
                    int val = readInt("  Enter value to search for: ");
                    int idx = list.search(val);
                    if (idx != -1) {
                        printSuccess("Found " + val + " at index " + idx + ".");
                    } else {
                        printError("Value " + val + " not found.");
                    }
                    printComplexity("search(T)", "O(n)", "O(1)");
                }
                case 8 -> {
                    printInfo("Forward traversal (HEAD -> TAIL):");
                    list.printForward();
                    printComplexity("printForward()", "O(n)", "O(1)");
                }
                case 9 -> {
                    printInfo("Backward traversal (TAIL -> HEAD):");
                    list.printBackward();
                    printComplexity("printBackward()", "O(n)", "O(1)");
                }
                case 10 -> {
                    printInfo("Size    : " + list.size());
                    printInfo("IsEmpty : " + list.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-10.");
            }
        }
    }

    // =========================================================
    //  4. STACK MENU (with sub-variant selection)
    // =========================================================

    private static void menuStack() {
        printSubHeader("Stack — Choose Implementation");
        System.out.println("  1. Array-based Stack");
        System.out.println("  2. Linked-List-based Stack");
        System.out.println("  0. Back");
        int variant = readInt("  Your choice: ");
        System.out.println();

        switch (variant) {
            case 1  -> menuArrayStack();
            case 2  -> menuLinkedStack();
            case 0  -> { /* back */ }
            default -> printError("Invalid option.");
        }
    }

    private static void menuArrayStack() {
        Stack.ArrayStack<Integer> stack = new Stack.ArrayStack<>();
        boolean active = true;
        while (active) {
            printSubHeader("Array Stack");
            System.out.println("  1. Push");
            System.out.println("  2. Pop");
            System.out.println("  3. Peek (top)");
            System.out.println("  4. Size & isEmpty");
            System.out.println("  5. Print / Display state");
            System.out.println("  0. Back");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to push: ");
                    stack.push(val);
                    printSuccess("Pushed " + val + " onto the stack.");
                    stack.print();
                    printComplexity("push(T)", "O(1) amortized", "O(1)");
                }
                case 2 -> {
                    try {
                        int popped = stack.pop();
                        printSuccess("Popped: " + popped);
                        stack.print();
                    } catch (java.util.EmptyStackException e) { printError("Stack is empty!"); }
                    printComplexity("pop()", "O(1)", "O(1)");
                }
                case 3 -> {
                    try {
                        printSuccess("Top of stack: " + stack.peek());
                    } catch (java.util.EmptyStackException e) { printError("Stack is empty!"); }
                    printComplexity("peek()", "O(1)", "O(1)");
                }
                case 4 -> {
                    printInfo("Size    : " + stack.size());
                    printInfo("IsEmpty : " + stack.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 5 -> {
                    printInfo("Current state of Array Stack:");
                    stack.print();
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-5.");
            }
        }
    }

    private static void menuLinkedStack() {
        Stack.LinkedStack<Integer> stack = new Stack.LinkedStack<>();
        boolean active = true;
        while (active) {
            printSubHeader("Linked Stack");
            System.out.println("  1. Push");
            System.out.println("  2. Pop");
            System.out.println("  3. Peek (top)");
            System.out.println("  4. Size & isEmpty");
            System.out.println("  5. Print / Display state");
            System.out.println("  0. Back");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to push: ");
                    stack.push(val);
                    printSuccess("Pushed " + val + " onto the stack.");
                    stack.print();
                    printComplexity("push(T)", "O(1)", "O(1)");
                }
                case 2 -> {
                    try {
                        int popped = stack.pop();
                        printSuccess("Popped: " + popped);
                        stack.print();
                    } catch (java.util.EmptyStackException e) { printError("Stack is empty!"); }
                    printComplexity("pop()", "O(1)", "O(1)");
                }
                case 3 -> {
                    try {
                        printSuccess("Top of stack: " + stack.peek());
                    } catch (java.util.EmptyStackException e) { printError("Stack is empty!"); }
                    printComplexity("peek()", "O(1)", "O(1)");
                }
                case 4 -> {
                    printInfo("Size    : " + stack.size());
                    printInfo("IsEmpty : " + stack.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 5 -> {
                    printInfo("Current state of Linked Stack:");
                    stack.print();
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-5.");
            }
        }
    }

    // =========================================================
    //  5. QUEUE MENU (with sub-variant selection)
    // =========================================================

    private static void menuQueue() {
        printSubHeader("Queue — Choose Implementation");
        System.out.println("  1. Circular Array Queue");
        System.out.println("  2. Linked-List Queue");
        System.out.println("  0. Back");
        int variant = readInt("  Your choice: ");
        System.out.println();

        switch (variant) {
            case 1  -> menuCircularQueue();
            case 2  -> menuLinkedQueue();
            case 0  -> { /* back */ }
            default -> printError("Invalid option.");
        }
    }

    private static void menuCircularQueue() {
        Queue.CircularArrayQueue<Integer> queue = new Queue.CircularArrayQueue<>();
        boolean active = true;
        while (active) {
            printSubHeader("Circular Array Queue");
            System.out.println("  1. Enqueue (add to rear)");
            System.out.println("  2. Dequeue (remove from front)");
            System.out.println("  3. Peek (front)");
            System.out.println("  4. Size & isEmpty");
            System.out.println("  5. Print / Display state");
            System.out.println("  0. Back");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to enqueue: ");
                    queue.enqueue(val);
                    printSuccess("Enqueued " + val + " at REAR.");
                    queue.print();
                    printComplexity("enqueue(T)", "O(1) amortized", "O(1)");
                }
                case 2 -> {
                    try {
                        int dequeued = queue.dequeue();
                        printSuccess("Dequeued from FRONT: " + dequeued);
                        queue.print();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("dequeue()", "O(1)", "O(1)");
                }
                case 3 -> {
                    try {
                        printSuccess("Front element: " + queue.peek());
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("peek()", "O(1)", "O(1)");
                }
                case 4 -> {
                    printInfo("Size    : " + queue.size());
                    printInfo("IsEmpty : " + queue.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 5 -> {
                    printInfo("Current state of Circular Array Queue:");
                    queue.print();
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-5.");
            }
        }
    }

    private static void menuLinkedQueue() {
        Queue.LinkedQueue<Integer> queue = new Queue.LinkedQueue<>();
        boolean active = true;
        while (active) {
            printSubHeader("Linked Queue");
            System.out.println("  1. Enqueue (add to rear)");
            System.out.println("  2. Dequeue (remove from front)");
            System.out.println("  3. Peek (front)");
            System.out.println("  4. Size & isEmpty");
            System.out.println("  5. Print / Display state");
            System.out.println("  0. Back");
            int choice = readInt("  Your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> {
                    int val = readInt("  Enter value to enqueue: ");
                    queue.enqueue(val);
                    printSuccess("Enqueued " + val + " at REAR.");
                    queue.print();
                    printComplexity("enqueue(T)", "O(1)", "O(1)");
                }
                case 2 -> {
                    try {
                        int dequeued = queue.dequeue();
                        printSuccess("Dequeued from FRONT: " + dequeued);
                        queue.print();
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("dequeue()", "O(1)", "O(1)");
                }
                case 3 -> {
                    try {
                        printSuccess("Front element: " + queue.peek());
                    } catch (java.util.NoSuchElementException e) { printError(e.getMessage()); }
                    printComplexity("peek()", "O(1)", "O(1)");
                }
                case 4 -> {
                    printInfo("Size    : " + queue.size());
                    printInfo("IsEmpty : " + queue.isEmpty());
                    printComplexity("size / isEmpty", "O(1)", "O(1)");
                }
                case 5 -> {
                    printInfo("Current state of Linked Queue:");
                    queue.print();
                }
                case 0 -> active = false;
                default -> printError("Invalid option. Enter 0-5.");
            }
        }
    }

    // =========================================================
    //  UI HELPERS
    // =========================================================

    /** Prints the ASCII art welcome banner. */
    private static void printBanner() {
        System.out.println();
        System.out.println(BOLD + PURPLE +
            "  ██████╗ ███████╗ █████╗     ██████╗ ██╗      █████╗ ██╗   ██╗ ██████╗ ██████╗  ██████╗ ██╗   ██╗███╗   ██╗██████╗ " + RESET);
        System.out.println(BOLD + PURPLE +
            "  ██╔══██╗██╔════╝██╔══██╗    ██╔══██╗██║     ██╔══██╗╚██╗ ██╔╝██╔════╝ ██╔══██╗██╔═══██╗██║   ██║████╗  ██║██╔══██╗" + RESET);
        System.out.println(BOLD + PURPLE +
            "  ██║  ██║███████╗███████║    ██████╔╝██║     ███████║ ╚████╔╝ ██║  ███╗██████╔╝██║   ██║██║   ██║██╔██╗ ██║██║  ██║" + RESET);
        System.out.println(BOLD + PURPLE +
            "  ██║  ██║╚════██║██╔══██║    ██╔═══╝ ██║     ██╔══██║  ╚██╔╝  ██║   ██║██╔══██╗██║   ██║██║   ██║██║╚██╗██║██║  ██║" + RESET);
        System.out.println(BOLD + PURPLE +
            "  ██████╔╝███████║██║  ██║    ██║     ███████╗██║  ██║   ██║   ╚██████╔╝██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝" + RESET);
        System.out.println(BOLD + PURPLE +
            "  ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝ " + RESET);
        System.out.println();
        System.out.println(BOLD + YELLOW + "  ╔══════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + YELLOW + "  ║   Fundamental Data Structures — Interactive Terminal Runner ║" + RESET);
        System.out.println(BOLD + YELLOW + "  ║   Pure Core Java | Generic Types | O(n) Complexity Labels   ║" + RESET);
        System.out.println(BOLD + YELLOW + "  ╚══════════════════════════════════════════════════════════════╝" + RESET);
    }

    /** Prints a highlighted sub-header for a data structure sub-menu. */
    private static void printSubHeader(String name) {
        System.out.println();
        System.out.println(BOLD + BLUE + "  ┌─────────────────────────────────────────┐" + RESET);
        System.out.printf (BOLD + BLUE + "  │  %-40s│%n" + RESET, name);
        System.out.println(BOLD + BLUE + "  └─────────────────────────────────────────┘" + RESET);
    }

    /** Prints a green success message. */
    private static void printSuccess(String msg) {
        System.out.println(GREEN + "  ✔ " + msg + RESET);
    }

    /** Prints a yellow informational message. */
    private static void printInfo(String msg) {
        System.out.println(YELLOW + "  ℹ " + msg + RESET);
    }

    /** Prints a red error message. */
    private static void printError(String msg) {
        System.out.println(RED + "  ✘ ERROR: " + msg + RESET);
    }

    /**
     * Prints the time and space complexity for an operation.
     *
     * @param operation  the name of the operation
     * @param time       time complexity string
     * @param note       space complexity string or additional note
     */
    private static void printComplexity(String operation, String time, String note) {
        System.out.printf(CYAN + "  ⏱  Complexity of %-22s │ Time: %-16s │ Space: %s%n" + RESET,
            operation, time, note);
    }

    /**
     * Reads a validated integer from stdin.
     * Re-prompts on invalid (non-integer) input.
     *
     * @param prompt the prompt string
     * @return the integer entered by the user
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = sc.nextInt();
                sc.nextLine(); // consume trailing newline
                return val;
            } catch (InputMismatchException e) {
                sc.nextLine(); // discard bad token
                printError("Please enter a valid integer.");
            }
        }
    }
}
