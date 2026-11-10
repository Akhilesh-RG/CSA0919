import java.time.*;

interface ParkingPass {
    boolean isValid(LocalDate date);
    boolean isFree();
}

class OneDayPass implements ParkingPass {
    private final LocalDate validDate;

    OneDayPass(LocalDate validDate) {
        this.validDate = validDate;
    }

    @Override
    public boolean isValid(LocalDate date) {
        return validDate.equals(date);
    }

    @Override
    public boolean isFree() {
        return true;
    }
}

class MonthlyPass implements ParkingPass {
    private final YearMonth validMonth;

    MonthlyPass(YearMonth validMonth) {
        this.validMonth = validMonth;
    }

    @Override
    public boolean isValid(LocalDate date) {
        return validMonth.equals(YearMonth.from(date));
    }

    @Override
    public boolean isFree() {
        return true;
    }
}

abstract class Vehicle {
    private final String regNo;

    Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) {
            throw new IllegalArgumentException("Registration is required");
        }
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public abstract double calculateFee(long minutes);
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(long minutes) {
        if (minutes <= 0) return 0;
        long hours = (minutes + 59) / 60;
        return hours * 30.0;
    }
}

class EBike extends Vehicle {
    private static final double FLAT_FEE = 25.0;

    EBike(String regNo) {
        super(regNo);
    }

    @Override
    public double calculateFee(long minutes) {
        return minutes <= 0 ? 0 : FLAT_FEE;
    }
}

class DeliveryVan extends Vehicle {
    DeliveryVan(String regNo) {
        super(regNo);
    }

    // Double the car rate (Rs 60/hour) from 9 AM to 5 PM.
    // Outside that period, the normal car rate (Rs 30/hour) applies.
    @Override
    public double calculateFee(long minutes) {
        return minutes <= 0 ? 0 : minutes * 0.5;
    }

    public double calculateFee(LocalDateTime entry, LocalDateTime exit) {
        if (exit.isBefore(entry)) {
            throw new IllegalArgumentException("Exit cannot be before entry");
        }

        double amount = 0;
        LocalDateTime current = entry;

        while (current.isBefore(exit)) {
            LocalDate date = current.toLocalDate();
            LocalDateTime nextMidnight = date.plusDays(1).atStartOfDay();
            LocalDateTime segmentEnd = exit.isBefore(nextMidnight)
                    ? exit : nextMidnight;

            LocalDateTime peakStart = date.atTime(9, 0);
            LocalDateTime peakEnd = date.atTime(17, 0);

            LocalDateTime peakFrom = current.isAfter(peakStart)
                    ? current : peakStart;
            LocalDateTime peakTo = segmentEnd.isBefore(peakEnd)
                    ? segmentEnd : peakEnd;

            long peakMinutes = 0;
            if (peakTo.isAfter(peakFrom)) {
                peakMinutes = Duration.between(peakFrom, peakTo).toMinutes();
            }

            long totalMinutes =
                    Duration.between(current, segmentEnd).toMinutes();
            long normalMinutes = totalMinutes - peakMinutes;

            amount += (peakMinutes / 60.0) * 60.0;
            amount += (normalMinutes / 60.0) * 30.0;

            current = segmentEnd;
        }

        return amount;
    }
}

public class Task12Adaptation {
    public static void main(String[] args) {
        EBike ebike = new EBike("TN01E1111");
        DeliveryVan van = new DeliveryVan("TN02D2222");

        LocalDate today = LocalDate.now();
        OneDayPass visitorPass = new OneDayPass(today);

        System.out.println("E-bike fee: Rs " + ebike.calculateFee(90));

        LocalDateTime in = today.atTime(10, 0);
        LocalDateTime out = today.atTime(12, 0);
        System.out.println("Delivery van fee: Rs "
                + van.calculateFee(in, out));

        System.out.println("One-day pass valid today: "
                + visitorPass.isValid(today));
        System.out.println("One-day pass parking fee: Rs "
                + (visitorPass.isValid(today) && visitorPass.isFree()
                   ? 0 : 50));
    }
}
