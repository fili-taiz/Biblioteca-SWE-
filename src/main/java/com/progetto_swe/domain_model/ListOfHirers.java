package com.progetto_swe.domain_model;
import java.util.ArrayList;
import java.util.Objects;

public class ListOfHirers {
    private ArrayList<Hirer> hirers;

    public ListOfHirers(ArrayList<Hirer> hirers){
        this.hirers = hirers;
    }

    public Hirer getHirer(String userCode){
        for(Hirer h : this.hirers){
            if(h.getUserCode().equals(userCode)){
                return h;
            }
        }
        return null;
    }

    public ArrayList<Hirer> searchHirer(String keywords) {
        String[] splittedKeyword = keywords.split(" ");
        ArrayList<Hirer> result = new ArrayList<>();
        for (Hirer h : this.hirers) {
            for (String keyword : splittedKeyword){
                if (h.contains(keyword)) {
                    result.add(h);
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        ListOfHirers listOfHirers = (ListOfHirers) o;
        boolean equal = true;
        for(Hirer h : this.hirers){
            if(!h.equals(listOfHirers.getHirer(h.getUserCode()))){
                equal = false;
            }
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hirers);
    }



    public ArrayList<Hirer> getHirers() { return this.hirers; }
}
