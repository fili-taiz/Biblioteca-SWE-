package com.progetto_swe.domain_model;
import java.util.ArrayList;

public class ListOfHirers {
    ArrayList<Hirer> hirers;

    public ListOfHirers(ArrayList<Hirer> hirers){
        this.hirers = hirers;
    }

    public Hirer getHirer(String usercode){
        for(Hirer h : this.hirers){
            if(h.getUserCode().equals(usercode)){
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

    public ArrayList<Hirer> getHirers() { return this.hirers; }
}
