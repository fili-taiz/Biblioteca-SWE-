package com.progetto_swe.domain_model;

import java.util.Objects;

public class PhysicalCopies {
    private int numberOfPhysicalCopies;
    private int numberOfAvailableCopies;
    private boolean borrowable;

    public PhysicalCopies(int numberOfPhysicalCopies, int numberOfAvailableCopies, boolean borrowable) {
        this.numberOfPhysicalCopies = numberOfPhysicalCopies;
        this.numberOfAvailableCopies = numberOfAvailableCopies;
        this.borrowable = borrowable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalCopies pc = (PhysicalCopies) o;
        return Objects.equals(this.numberOfPhysicalCopies, pc.numberOfPhysicalCopies) && Objects.equals(this.numberOfAvailableCopies, pc.numberOfAvailableCopies) && Objects.equals(this.borrowable, pc.borrowable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPhysicalCopies); // usa tutti i campi rilevanti
    }

    public int getNumberOfPhysicalCopies() {return this.numberOfPhysicalCopies;}
    public int getNumberOfAvailableCopies() {return this.numberOfAvailableCopies;}
    public boolean isBorrowable() { return this.borrowable; }
}
