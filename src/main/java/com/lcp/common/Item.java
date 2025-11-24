package com.lcp.common;

import lombok.Data;

@Data
public class Item{
    private String name;
    private String subtotal;
    private String tax;
    private String total;

    public Item(String name, String subtotal, String tax, String total) {
        this.name = name;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }
}
