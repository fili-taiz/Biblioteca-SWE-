package com.progetto_swe.domain_model;

public class PhysicalCopies {
    private int numberOfPhysicalCopies;
    private boolean borrowable;

    public PhysicalCopies(int numberOfPhysicalCopies, boolean borrowable) {
        this.numberOfPhysicalCopies = numberOfPhysicalCopies;
        this.borrowable = borrowable;
    }

    public int getNumberOfPhysicalCopies() {return this.numberOfPhysicalCopies;}
    public void setNumberOfPhysicalCopies(int numberOfPhysicalCopies) {this.numberOfPhysicalCopies = numberOfPhysicalCopies;}
    public boolean isBorrowable() {return this.borrowable;}
    public void setBorrowable(boolean borrowable) {this.borrowable = borrowable;}
}
