package com.progetto_swe.domain_model;

import java.util.Objects;

public class PhysicalCopies {
    private int numberOfPhysicalCopies;
    private boolean borrowable;

    public PhysicalCopies(int numberOfPhysicalCopies, boolean borrowable) {
        this.numberOfPhysicalCopies = numberOfPhysicalCopies;
        this.borrowable = borrowable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalCopies pc = (PhysicalCopies) o;
        return Objects.equals(this.numberOfPhysicalCopies, pc.numberOfPhysicalCopies) && Objects.equals(this.borrowable, pc.borrowable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPhysicalCopies); // usa tutti i campi rilevanti
    }

    public int getNumberOfPhysicalCopies() {return this.numberOfPhysicalCopies;}
    public boolean isBorrowable() { return this.borrowable; }
}
