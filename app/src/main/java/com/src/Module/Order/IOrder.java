package com.src.Module.Order;

import java.util.List;

import com.src.Model.Gift;
import com.src.Model.Order;
import com.src.Model.OrderDetails;

public interface IOrder {
    interface IPresenterOrder{
        void addCartToServer(Order order, List<OrderDetails> orderDetails);
        void resultAddCart(boolean result, String msg);

    }
    interface IViewOrder{
        void onSuccess(String msg);
        void onFailed(String msg);

    }
}
