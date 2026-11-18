import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Q2ListBenchmark {

    static void test(int size) {

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        // Add at end
        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            arrayList.add(i);
        }

        long arrayEnd = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            linkedList.add(i);
        }

        long linkedEnd = System.nanoTime();

        System.out.println("\nSize: " + size);
        System.out.println("ArrayList add: "
                + (arrayEnd - start) / 1_000_000.0 + " ms");

        System.out.println("LinkedList add: "
                + (linkedEnd - arrayEnd) / 1_000_000.0 + " ms");

        // Insert at front
        start = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            arrayList.add(0, i);
        }

        arrayEnd = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            linkedList.add(0, i);
        }

        linkedEnd = System.nanoTime();

        System.out.println("ArrayList front insert: "
                + (arrayEnd - start) / 1_000_000.0 + " ms");

        System.out.println("LinkedList front insert: "
                + (linkedEnd - arrayEnd) / 1_000_000.0 + " ms");

        // Access middle element
        start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            arrayList.get(size / 2);
        }

        arrayEnd = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            linkedList.get(size / 2);
        }

        linkedEnd = System.nanoTime();

        System.out.println("ArrayList access: "
                + (arrayEnd - start) / 1_000_000.0 + " ms");

        System.out.println("LinkedList access: "
                + (linkedEnd - arrayEnd) / 1_000_000.0 + " ms");

        // Iteration
        start = System.nanoTime();

        for (Integer value : arrayList) {
            int temp = value;
        }

        arrayEnd = System.nanoTime();

        for (Integer value : linkedList) {
            int temp = value;
        }

        linkedEnd = System.nanoTime();

        System.out.println("ArrayList iteration: "
                + (arrayEnd - start) / 1_000_000.0 + " ms");

        System.out.println("LinkedList iteration: "
                + (linkedEnd - arrayEnd) / 1_000_000.0 + " ms");
    }

    public static void main(String[] args) {

        test(1000);
        test(10000);
        test(100000);
    }
}
