import java.io.Serializable;

public class Payment implements Serializable {
    private static final long serialVersionUID = 7420193723650627658L;
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    String s;
    String details;

    public Payment(String s){
        this.s = s;
    }
    public Payment(String s, String d){
        this.s=s;
        this.details = d;
    }
}
