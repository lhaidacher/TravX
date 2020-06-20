package at.fhj.swd.travx.domain;

import java.util.List;

public class JourneyStats {
    private Long sum;
    private Double avg;
    private Bill highest;
    private Bill lowest;
    private Bill last;

    public JourneyStats(List<Bill> bills) {
        sum = 0L;
        generateStats(bills);
    }

    private void generateStats(List<Bill> bills) {
        for (Bill bill : bills) {
            add(bill);
            checkMax(bill);
            checkMin(bill);
            checkLast(bill);
            setAverage(bills.size());
        }
    }

    private void add(Bill bill) {
        sum += bill.getValue();
    }

    private void checkMax(Bill bill) {
        if (highest == null || highest.getValue().compareTo(bill.getValue()) < 0)
            this.highest = bill;
    }

    private void checkMin(Bill bill) {
        if (lowest == null || lowest.getValue().compareTo(bill.getValue()) > 0)
            this.lowest = bill;
    }

    private void checkLast(Bill bill) {
        if (last == null || last.getCreatedAt().compareTo(bill.getCreatedAt()) < 0) {
            this.last = bill;
        }
    }

    private void setAverage(int n) {
        avg = Double.valueOf(sum) / n;
    }

    public boolean available() {
        return highest != null && lowest != null && last != null;
    }

    public Long getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public Bill getHighest() {
        return highest;
    }

    public Bill getLowest() {
        return lowest;
    }

    public Bill getLast() {
        return last;
    }
}
