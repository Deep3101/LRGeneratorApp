package com.example.lrgeneratorapp;

public class LRHistory {

    String lrNo, date, consignor, consignee, fromCity, toCity, truck, invoice, pdfPath;

    public LRHistory(String lrNo, String date, String consignor, String consignee,
                     String fromCity, String toCity, String truck, String invoice, String pdfPath) {

        this.lrNo = lrNo;
        this.date = date;
        this.consignor = consignor;
        this.consignee = consignee;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.truck = truck;
        this.invoice = invoice;
        this.pdfPath = pdfPath;
    }
}
