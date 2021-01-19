package com.src.Module.Order.view;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import com.src.NavigationActivity;
import com.src.Model.Gift;
import com.src.Model.Order;
import com.src.Model.OrderDetails;
import com.src.Model.OrderProvisional;
import com.src.Module.Home.view.ExploreFragment;
import com.src.Module.Order.AdapterCartProvisional;
import com.src.Module.Order.IOnClickCart;
import com.src.Module.Order.IOrder;
import com.src.Module.Order.presenter.PresenterOrder;
import com.src.Utils.DialogLoading;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderActivity extends AppCompatActivity implements IOnClickCart, IOrder.IViewOrder {

    private RecyclerView recyclerProductCart;
    private TextView txtTotalPrice, txtTotalAmount;
    private LinearLayout layoutCartEmpty, layoutCart;
    private AdapterCartProvisional adapterCartProvisional;
    private Button btnCheckoutCart;
    private PresenterOrder presenterOrder;
    private double totalPrice = 0;
    private GoogleProgressBar progressOrder;
    ExploreFragment exploreFragmentCallback = new ExploreFragment();
//    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order);
        initView();

    }


    private void initView() {
        recyclerProductCart = findViewById(R.id.recyclerProductCart);
        txtTotalAmount = findViewById(R.id.txtTotalAmountCart);
        txtTotalPrice = findViewById(R.id.txtTotalPriceCart);
        layoutCartEmpty = findViewById(R.id.layoutCartEmpty);
        layoutCart = findViewById(R.id.layoutCart);
        btnCheckoutCart = findViewById(R.id.btnCheckoutCart);
        presenterOrder = new PresenterOrder(this);
        progressOrder = findViewById(R.id.progressOrder);
        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());


        checkVisibleCart();
    }
    // xử lý nhận dữ liệu từ giỏ hàng
    private void handlerGetDataCart() {
        adapterCartProvisional = new AdapterCartProvisional(MyOrderActivity.this, NavigationActivity.orderDetails, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerProductCart.setLayoutManager(layoutManager);
        recyclerProductCart.setAdapter(adapterCartProvisional);
        handlerTotalPrice();
        adapterCartProvisional.notifyDataSetChanged();
        btnCheckoutCart.setOnClickListener(v -> checkOut());

    }
// thanh toán
    private void checkOut() {
        DialogLoading.LoadingGoogle(true,progressOrder);
        String orderId = String.valueOf(new Random().nextInt(900000));
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (OrderProvisional i : NavigationActivity.orderDetails) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setIdOrder(orderId);
            orderDetails.setIdOrderDetails(String.valueOf(new Date().getTime()));
            orderDetails.setAmount(i.getAmount());
            orderDetails.setPrice(Integer.parseInt(i.getProduct().getPrice()) * i.getAmount());
            orderDetails.setProductId(i.getProduct().getProductId());
            orderDetailsList.add(orderDetails);
        }
        Order order = new Order(orderId, NavigationActivity.numberBadge, totalPrice, "Đang chờ xác nhận");
        presenterOrder.addCartToServer(order, orderDetailsList);
    }
    // tăng số lượng sản phẩm trong giỏ hàng
    @Override
    public void increase(OrderProvisional orderProvisional, TextView numberCart) {
        orderProvisional.setAmount(orderProvisional.getAmount() + 1);
        numberCart.setText(String.valueOf(orderProvisional.getAmount()));
        handlerTotalPrice();
    }

    // giảm và xóa sản phẩm trong giỏ hàng
    @SuppressLint("SetTextI18n")
    @Override
    public void reduce(OrderProvisional orderProvisional, TextView numberCart) {

        if (orderProvisional.getAmount() == 1) {
            new AlertDialog.Builder(MyOrderActivity.this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có muốn xoá sản phẩm " + orderProvisional.getProduct().getName())
                    .setPositiveButton("Có", (dialog, which) -> {
                        NavigationActivity.orderDetails.remove(orderProvisional); //giảm sô ở giỏ hàng tạm thời
                        NavigationActivity.numberBadge = NavigationActivity.numberBadge - 1;
                        exploreFragmentCallback.OnClickBadge();
                        adapterCartProvisional.notifyDataSetChanged();
                        handlerTotalPrice();
                        checkVisibleCart();
                    }).setNegativeButton("Không", (dialog, which) -> dialog.dismiss()).show();
        } else {
            orderProvisional.setAmount(orderProvisional.getAmount() - 1);
            numberCart.setText(orderProvisional.getAmount() + "");
            handlerTotalPrice();

        }


    }
    // xử lý tính tổng số tiền đơn hàng
    private void handlerTotalPrice() {
        totalPrice = 0;
        for (OrderProvisional i : NavigationActivity.orderDetails) {
            totalPrice += i.getAmount() * Double.parseDouble(i.getProduct().getPrice());
        }
        txtTotalPrice.setText(totalPrice + " VND");
    }

    private void checkVisibleCart() {
        if (NavigationActivity.orderDetails.size() <= 0) {
            layoutCartEmpty.setVisibility(View.VISIBLE);
            layoutCart.setVisibility(View.GONE);
        } else {
            layoutCart.setVisibility(View.VISIBLE);
            layoutCartEmpty.setVisibility(View.GONE);
            handlerGetDataCart();


        }
    }

    @Override
    public void onSuccess(String msg) {
//        DialogLoading.LoadingGoogle(false,progressOrder);
        //cart
        final AlertDialog.Builder   builder = new AlertDialog.Builder(MyOrderActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View viewDialogPickUp = layoutInflater.inflate(R.layout.custom_dialog_success, null);
        builder.setView(viewDialogPickUp);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


//        NavigationActivity.orderDetails.clear();
//        NavigationActivity.numberBadge = 0;
        exploreFragmentCallback.OnClickBadge();
        new Handler().postDelayed(() -> finish(), 500);
    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (alertDialog != null && alertDialog.isShowing()) {
//            alertDialog.dismiss();
//        }
//    }

    @Override
    public void onFailed(String msg) {

    }

}
