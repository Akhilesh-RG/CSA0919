import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Q4Pharmacy {

    public static void main(String[] args) {

        HashMap<String, Integer> hashMap = new HashMap<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        LinkedHashMap<String, Integer> linkedHashMap =
                new LinkedHashMap<>();

        hashMap.put("M103", 50);
        hashMap.put("M101", 100);
        hashMap.put("M105", 75);
        hashMap.put("M102", 60);
        hashMap.put("M104", 90);

        treeMap.putAll(hashMap);

        linkedHashMap.put("M103", 50);
        linkedHashMap.put("M101", 100);
        linkedHashMap.put("M105", 75);
        linkedHashMap.put("M102", 60);
        linkedHashMap.put("M104", 90);

        System.out.println("HashMap:");
        System.out.println(hashMap);

        System.out.println("\nTreeMap - Sorted by Medicine Code:");
        System.out.println(treeMap);

        System.out.println("\nLinkedHashMap - Insertion Order:");
        System.out.println(linkedHashMap);

        // Lookup benchmark
        long start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            hashMap.get("M101");
        }

        long end = System.nanoTime();

        System.out.println("\nHashMap Lookup Time: "
                + (end - start) / 1_000_000.0 + " ms");

        start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            treeMap.get("M101");
        }

        end = System.nanoTime();

        System.out.println("TreeMap Lookup Time: "
                + (end - start) / 1_000_000.0 + " ms");

        start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            linkedHashMap.get("M101");
        }

        end = System.nanoTime();

        System.out.println("LinkedHashMap Lookup Time: "
                + (end - start) / 1_000_000.0 + " ms");
    }
}
