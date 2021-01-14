package com.src.Module.Home.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.chabbal.sliding.SlidingSplashView;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.nex3z.notificationbadge.NotificationBadge;
import com.src.Model.Product;
import com.src.Module.Home.IOnClickProduct;
import com.src.Module.Home.adapter.NewProductAdapter;
import com.src.Module.Home.presenter.IBanner;
import com.src.Module.Home.presenter.IProduct;
import com.src.Module.Home.presenter.PresenterBanner;
import com.src.Module.Home.presenter.PresenterProduct;
import com.src.NavigationActivity;
import com.src.Utils.DialogLoading;
import com.src.Utils.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment2 extends Fragment implements IProduct.IViewProduct, IBanner.IViewBanner, IOnClickProduct {
    private RecyclerView recyclerNewProduct, recyclerAllProduct;
    private SlidingSplashView splashExplore;
    private List<Product> products, productsNew;
    private GoogleProgressBar progressBarExplore;
    private static NotificationBadge badge;
    private TextInputEditText edtSearch;
    private NestedScrollView nestedScrollMenu;
    private PresenterProduct presenterProduct;
    private androidx.appcompat.widget.Toolbar toolbarExplore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        init(view);
        onScrollListener();
        return view;
    }

    @SuppressLint("WrongConstant")
    private void onScrollListener() {
        nestedScrollMenu.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                toolbarExplore.setVisibility(View.GONE);

            }
            if (scrollY > oldScrollY) {
                toolbarExplore.setVisibility(View.VISIBLE);
                toolbarExplore.setTitle("H E I N");

                //hide
                NavigationActivity.navigation.setVisibility(View.GONE);

                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        NavigationActivity.navigation.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(200);
                animate.setFillAfter(true);
                NavigationActivity.navigation.startAnimation(animate);


            } else if (scrollY < oldScrollY) {
                toolbarExplore.setVisibility(View.VISIBLE);

                toolbarExplore.setTitle("H E I N");

                //show
                NavigationActivity.navigation.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        NavigationActivity.navigation.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(200);
                animate.setFillAfter(true);
                NavigationActivity.navigation.startAnimation(animate);

            }
        });
    }


    private void init(View view) {
        nestedScrollMenu = view.findViewById(R.id.nestedScrollMenu);
        toolbarExplore = view.findViewById(R.id.toolbarExplore);
        recyclerNewProduct = view.findViewById(R.id.recyclerNewProduct);
        splashExplore = view.findViewById(R.id.splashExplore);
        progressBarExplore = view.findViewById(R.id.progressExplore);
        recyclerAllProduct = view.findViewById(R.id.recyclerAllProduct);
        edtSearch = view.findViewById(R.id.edtSearch);
        FrameLayout framBadge = view.findViewById(R.id.layout_Badge);
        Button btnViewAllProduct = view.findViewById(R.id.btnViewAllProduct);
        PresenterBanner presenterBanner = new PresenterBanner(this);
        products = new ArrayList<>();
        productsNew = new ArrayList<>();
        btnViewAllProduct.setOnClickListener(v -> viewAllProduct());

        if (products != null) {
            products.clear();
        }
        presenterProduct = new PresenterProduct(this);

        DialogLoading.LoadingGoogle(true, progressBarExplore);

// 3lấy danh sách sản phẩm
        presenterProduct.getListProductMore(new Random().nextInt(5));
        presenterProduct.getNewListProduct();
        presenterBanner.getBanner();
        handlerSearch();

    }
//1tìm kiếm sản phầm lấy theo tên trong data
    private void handlerSearch() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                DialogLoading.LoadingGoogle(true, progressBarExplore);

                presenterProduct.handlerSearch(v.getText().toString());
            }
            return true;

        });
    }

    private void viewAllProduct() {
        startActivity(new Intent(getActivity(), AllProductActivity.class));
    }

    private void loadData() {
        NewProductAdapter newProductAdapter = new NewProductAdapter(getActivity(), R.layout.custom_layout_new_product, productsNew, this, "News");
        NewProductAdapter allProductAdapter = new NewProductAdapter(getActivity(), R.layout.custom_layout_new_product, products, this, "All");
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin5);
        recyclerNewProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerNewProduct.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
        recyclerNewProduct.setNestedScrollingEnabled(false);
        recyclerNewProduct.setAdapter(newProductAdapter);
        newProductAdapter.notifyDataSetChanged();
        recyclerAllProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerAllProduct.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
        recyclerAllProduct.setAdapter(allProductAdapter);
        allProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        products.addAll(productList);
        loadData();

    }

    @Override
    public void onGetListProductFailed(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onGetListNewProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        productsNew.addAll(productList);
        loadData();
    }

    @Override
    public void onGetListNewProductFailed(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();

    }
//tìm kiếm sản phẩm
    @Override
    public void onSearchProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        if (productList != null && productList.size() > 0) {
            edtSearch.setText("");
            Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.setContentView(R.layout.custom_dialog_search_list);
            RecyclerView recyclerSearch = dialog.findViewById(R.id.recyclerSearch);
            TextView txtCloseDialogSearch = dialog.findViewById(R.id.txtCloseDialogSearch);
            NewProductAdapter searchAdapter = new NewProductAdapter(getActivity(), R.layout.custom_layout_new_product, productList, this, "Search");
            txtCloseDialogSearch.setOnClickListener(v -> dialog.dismiss());
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin5);
            recyclerSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerSearch.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
            recyclerSearch.setNestedScrollingEnabled(false);
            recyclerSearch.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
            dialog.show();
        } else {
            Toasty.info(getActivity(), "Sản phẩm không tồn tại", Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSearchProductFailed(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        Toasty.warning(getActivity(), msg, Toasty.LENGTH_SHORT).show();
    }

    @Override
    public void OnClickProductDetails(Product product) {

        Intent intent = new Intent(getActivity(), ProductDetails2Activity.class);
        intent.putExtra("product", product);
        startActivity(intent);


    }

    @Override
    public void OnClickBadge() {
        try {
            badge.setNumber(NavigationActivity.numberBadge);
        } catch (Exception e) {
            Log.d("FsBs", "OnClickBadge: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FsBs", "onDestroy: Trang chủ");
    }


    @Override
    public void onSuccessGetBanner(String[] listBanner) {
        splashExplore.setImageResources(listBanner);
        splashExplore.setAutoPage();
    }

    @Override
    public void onFailGetBanner(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();

    }
}
