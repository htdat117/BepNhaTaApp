package com.example.bepnhataapp.common.model;

public class CartItem implements java.io.Serializable {
    private long productId;
    private String title;
    private int price;       // giá hiện tại cho gói 2 người
    private int oldPrice;    // giá gốc cho gói 2 người
    private int quantity;
    private boolean selected;
    private String serving;
    private String thumb;

    public CartItem(long productId,String title,int price,int oldPrice,int quantity,String thumb){
        this.productId=productId;
        this.title=title;
        this.price=price;
        this.oldPrice=oldPrice;
        this.quantity=quantity;
        this.thumb=thumb;
        this.selected=false;
        this.serving="2 người";
    }

    public long getProductId(){return productId;}
    public String getTitle(){return title;}
    public int getPrice(){return price;}
    public int getOldPrice(){return oldPrice;}
    public int getQuantity(){return quantity;}
    public void setQuantity(int q){this.quantity=q;}
    public boolean isSelected(){return selected;}
    public void setSelected(boolean s){this.selected=s;}

    public int getTotal(){
        return price*quantity*getServingFactor();
    }
    public int getSavePerUnit(){return oldPrice-price;}
    public int getTotalSave(){return getSavePerUnit()*quantity*getServingFactor();}

    public String getServing(){return serving;}
    public void setServing(String s){this.serving=s;}

    private int getServingFactor(){
        return serving!=null && serving.startsWith("4") ? 2 : 1;
    }

    public String getThumb(){return thumb;}
    public void setThumb(String t){this.thumb=t;}

    public void setPrice(int p){this.price=p;}
    public void setOldPrice(int p){this.oldPrice=p;}
} 
