import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Q7DesignChallenge {

    static void test(int size) {

        List<Integer> arrayList = new ArrayList<>();
        Deque<Integer> arrayDeque = new ArrayDeque<>();

        // Original solution: ArrayList
        long start = System.nanoTime();

        for (int i = 0; i < size; i++) {
            arrayList.add(0, i);
        }

        long arrayListInsertEnd = System.nanoTime();

        for (int i = 0; i < size; i++) {
            arrayList.remove(0);
        }

        long arrayListEnd = System.nanoTime();

        // Redesigned solution: ArrayDeque
        for (int i = 0; i < size; i++) {
            arrayDeque.addFirst(i);
        }

        long arrayDequeInsertEnd = System.nanoTime();

        for (int i = 0; i < size; i++) {
            arrayDeque.removeFirst();
        }

        long arrayDequeEnd = System.nanoTime();

        System.out.println("\nData Size: " + size);

        System.out.println("ArrayList Front Insertion: "
                + (arrayListInsertEnd - start) / 1_000_000.0
                + " ms");

        System.out.println("ArrayList Front Removal: "
                + (arrayListEnd - arrayListInsertEnd) / 1_000_000.0
                + " ms");

        System.out.println("ArrayDeque Front Insertion: "
                + (arrayDequeInsertEnd - arrayListInsertEnd) / 1_000_000.0
                + " ms");

        System.out.println("ArrayDeque Front Removal: "
                + (arrayDequeEnd - arrayDequeInsertEnd) / 1_000_000.0
                + " ms");
    }

    public static void main(String[] args) {

        test(1000);
        test(10000);
        test(50000);
    }
}
