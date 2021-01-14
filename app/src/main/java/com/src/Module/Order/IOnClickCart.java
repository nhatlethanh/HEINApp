package com.src.Module.Order;

import android.widget.TextView;

import com.src.Model.OrderProvisional;

public interface IOnClickCart {
    void increase(OrderProvisional orderProvisional, TextView numberCart);
    void reduce(OrderProvisional orderProvisional, TextView numberCart);
}
