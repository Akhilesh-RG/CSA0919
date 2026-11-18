public class Q1StringBenchmark {

    static String usingString(int fields) {
        String ticket = "";

        for (int i = 1; i <= fields; i++) {
            ticket = ticket + "Field" + i + " ";
        }

        return ticket;
    }

    static String usingStringBuilder(int fields) {
        StringBuilder ticket = new StringBuilder();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field").append(i).append(" ");
        }

        return ticket.toString();
    }

    static String usingStringBuffer(int fields) {
        StringBuffer ticket = new StringBuffer();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field").append(i).append(" ");
        }

        return ticket.toString();
    }

    static void benchmark(String name, int fields, int tickets) {
        long start = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            if (name.equals("String")) {
                usingString(fields);
            } else if (name.equals("StringBuilder")) {
                usingStringBuilder(fields);
            } else {
                usingStringBuffer(fields);
            }
        }

        long end = System.nanoTime();

        System.out.println(name + " : " +
                ((end - start) / 1_000_000.0) + " ms");
    }

    public static void main(String[] args) {

        int fields = 50;
        int tickets = 10000;

        System.out.println("Fields per ticket: " + fields);
        System.out.println("Number of tickets: " + tickets);
        System.out.println();

        benchmark("String", fields, tickets);
        benchmark("StringBuilder", fields, tickets);
        benchmark("StringBuffer", fields, tickets);

        System.out.println("\nSample Ticket:");
        System.out.println(usingStringBuilder(5));
    }
}
