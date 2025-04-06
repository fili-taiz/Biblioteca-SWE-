package com.progetto_swe.domain_model;
import java.util.ArrayList;

public class ListOfHirers {
    ArrayList<Hirer> hirers = new ArrayList<>();

    public ListOfHirers(){
    }

    public ListOfHirers(ArrayList<Hirer> hirers){
        this.hirers = hirers;
    }

    public boolean addHirer(Hirer hirer){
       return hirers.add(hirer);
    }

    public Hirer getHirer(String userCode){
        if(!this.hirers.contains(userCode)){
            return null;
        }
        return this.hirers.get(this.hirers.indexOf(userCode));
    }

    public void removehirer(Hirer hirer){
        hirers.remove(hirer);
    }

    public ArrayList<Hirer> searchHirer(String keyWords) {
        String[] splittedKeyWord = keyWords.split(" ");
        ArrayList<Hirer> result = new ArrayList<>();
        for (Hirer h : this.hirers) {
            for (String keyWord : splittedKeyWord){
                if (h.contains(keyWord)) {
                    result.add(h);
                }
            }
        }
        return result;
    }
}
