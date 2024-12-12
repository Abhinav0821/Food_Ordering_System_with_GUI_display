import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

public class Order implements Serializable {
    private static final long serialVersionUID = 7420193723650627658L;
    private static int idCounter = 0; // Static counter to generate unique IDs

    public int getId() {
        return id;
    }

    private final int id;
    private Customer c;
    private LocalDateTime time;
    private String status;
    private String priority;
    private double bill;
    private boolean refundProcessed;
    private String specialRequest;

    public String getPaymentDetail() {
        return PaymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        PaymentDetail = paymentDetail;
    }

    private String PaymentDetail;


    public String getspecialRequest(){
        return this.specialRequest;
    }
    public void setspecialRequest(String r){
        this.specialRequest = r;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    private String deliveryAddress;

    public String getAddress() {
        return deliveryAddress;
    }

    public void setAddress(String address) {
        this.deliveryAddress = address;
    }

    public boolean isRefundProcessed() {
        return refundProcessed;
    }

    public void setRefundProcessed(boolean refundProcessed) {
        this.refundProcessed = refundProcessed;
    }



    public Payment getMoP() {
        return MoP;
    }

    public void setMoP(Payment moP) {
        MoP = moP;
    }


    private Payment MoP;

    public Customer getC() {
        return c;
    }

    public void setC(Customer c) {
        this.c = c;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public HashMap<Item, Integer> getItemlist() {
        return Itemlist;
    }

    public void setItemlist(HashMap<Item, Integer> itemlist) {
        Itemlist = itemlist;
    }

    private HashMap<Item, Integer> Itemlist;
    public String displayItem(){
        String l = "";
        for(Item i : Itemlist.keySet()){
            l = l + i.getName()+" ";
        }
        System.out.println(l);
        return l;
    }

    public Order(Customer c, LocalDateTime time, String status, String da,String priority, double bill,
                 HashMap<Item, Integer> itemList, Payment moP, String sr) {
        this.id = ++idCounter; // Generate a unique ID
        this.c = c;
        this.time = time;
        this.status = status;
        this.priority = priority;
        this.bill = bill;
        this.Itemlist = itemList;
        this.MoP = moP;
        this.deliveryAddress=da;
        this.specialRequest = sr;
    }
}
