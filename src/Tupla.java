public class Tupla<First, Second> {
    private First first;
    private Second second;

    public Tupla(First first, Second second){
        this.first = first;
        this.second = second;
    }

    public First get_first(){ return this.first; }
    public Second get_second(){ return this.second; }

    public void set_first(First newFirst){ this.first = newFirst; }
    public void set_second(Second newSecond){ this.second = newSecond; }
}
