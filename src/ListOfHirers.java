import java.util.ArrayList;

public class ListOfHirers {
    ArrayList<Hirer> hirers = new ArrayList<>();

    public void addhirer(Hirer hirer){
        hirers.add(hirer);
    }

    public void removehirer(Hirer hirer){
        hirers.remove(hirer);
    }

    public Hirer gethirer(Hirer hirer){
        return hirers.get(hirers.indexOf(hirer));
    }




}
