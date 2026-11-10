import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

interface Chargeable {
    double chargingFee(int kWh);
}

abstract class Vehicle {
    private final String regNo;

    Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) {
            throw new IllegalArgumentException("Invalid registration number");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    protected abstract double ratePerHour();

    public double calculateFee(long minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        if (minutes == 0) return 0;

        long hours = (minutes + 59) / 60;
        return Math.min(hours * ratePerHour(), 300.0);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vehicle
                && regNo.equals(((Vehicle) o).regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + regNo;
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 10;
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    protected double ratePerHour() {
        return 30;
    }
}

class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    public double chargingFee(int kWh) {
        if (kWh < 0) {
            throw new IllegalArgumentException("Invalid charging units");
        }
        return kWh * 12.0;
    }
}

class ParkingTicket {
    private static long nextId = 1000;

    final long id;
    final Vehicle vehicle;
    final int floor;
    final int slot;
    final LocalDateTime entry;

    ParkingTicket(Vehicle vehicle, int floor, int slot) {
        this.id = ++nextId;
        this.vehicle = vehicle;
        this.floor = floor;
        this.slot = slot;
        this.entry = LocalDateTime.now();
    }
}

class ParkingFacility {
    private final Vehicle[][] slots;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();

    ParkingFacility(int[] capacity) {
        slots = new Vehicle[capacity.length][];

        for (int i = 0; i < capacity.length; i++) {
            if (capacity[i] < 0) {
                throw new IllegalArgumentException("Invalid floor capacity");
            }
            slots[i] = new Vehicle[capacity[i]];
        }
    }

    public ParkingTicket enter(Vehicle vehicle) {
        if (activeTickets.containsKey(vehicle.getRegNo())) {
            throw new IllegalArgumentException("Duplicate registration");
        }

        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = vehicle;
                    ParkingTicket ticket = new ParkingTicket(vehicle, f, s);
                    activeTickets.put(vehicle.getRegNo(), ticket);
                    return ticket;
                }
            }
        }

        throw new IllegalStateException("Parking facility is full");
    }

    public double exit(String regNo, long minutes,
                       boolean validMonthlyPass, int chargingKWh) {
        ParkingTicket ticket = activeTickets.get(regNo);

        if (ticket == null) {
            throw new IllegalArgumentException("No active ticket found");
        }

        double fee = validMonthlyPass
                ? 0
                : ticket.vehicle.calculateFee(minutes);

        if (ticket.vehicle instanceof Chargeable chargeable) {
            fee += chargeable.chargingFee(chargingKWh);
        }

        slots[ticket.floor][ticket.slot] = null;
        activeTickets.remove(regNo);
        return fee;
    }
}

public class CampusParkingDemo {
    public static void main(String[] args) {
        ParkingFacility parking = new ParkingFacility(new int[]{2, 1});

        ParkingTicket t1 = parking.enter(new TwoWheeler("TN01A1111"));
        ParkingTicket t2 = parking.enter(new Car("TN02B2222"));
        ParkingTicket t3 = parking.enter(new ElectricCar("TN03C3333"));

        System.out.println("Ticket: " + t1.id
                + " slot " + t1.floor + "," + t1.slot);

        System.out.println("Two-wheeler fee: Rs "
                + parking.exit("TN01A1111", 60, false, 0));

        System.out.println("Car monthly-pass fee: Rs "
                + parking.exit("TN02B2222", 120, true, 0));

        System.out.println("EV fee incl. charging: Rs "
                + parking.exit("TN03C3333", 61, false, 2));
    }
}
