package com.example.bepnhataapp.features.voucher;
public class VoucherItem{
    public String code,desc,expire;
    public boolean enabled;
    public int point; // điểm cần để đổi voucher
    public VoucherItem(String c,String d,String e,boolean en, int point){code=c;desc=d;expire=e;enabled=en;this.point=point;}
    // Constructor cũ để không lỗi khi khởi tạo cũ
    public VoucherItem(String c,String d,String e,boolean en){code=c;desc=d;expire=e;enabled=en;this.point=0;}
} 
