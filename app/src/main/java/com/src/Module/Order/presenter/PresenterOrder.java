package com.src.Module.Order.presenter;

import java.util.List;

import com.src.Model.Gift;
import com.src.Model.Order;
import com.src.Model.OrderDetails;
import com.src.Module.Order.IOrder;
import com.src.Module.Order.model.ModelOrder;

public class PresenterOrder implements IOrder.IPresenterOrder {
    IOrder.IViewOrder iViewOrder;
    ModelOrder modelOrder;

    public PresenterOrder(IOrder.IViewOrder iViewOrder) {
        this.iViewOrder = iViewOrder;
        modelOrder = new ModelOrder();
    }


    @Override
    public void addCartToServer(Order order, List<OrderDetails> orderDetails) {
        modelOrder.addToCart(order,orderDetails,this);
    }

    @Override
    public void resultAddCart(boolean result, String msg) {
        if(result){
            iViewOrder.onSuccess(msg);
        }else {
            iViewOrder.onFailed(msg);
        }
    }

}
