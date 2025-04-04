package com.progetto_swe.domain_model;

public class PhysicalCopies {
    private int numberOfPhysicalCopies;
    private boolean borrowable;

    public PhysicalCopies(int numberOfPhysicalCopies, boolean borrowable) {
        this.numberOfPhysicalCopies = numberOfPhysicalCopies;
        this.borrowable = borrowable;
    }

    public int getNumberOfPhysicalCopies() {return this.numberOfPhysicalCopies;}
    public boolean isBorrowable() { return this.borrowable; }
}
