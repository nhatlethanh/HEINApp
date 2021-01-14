package com.src.Module.Home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import com.src.Model.Product;
import com.src.Module.Home.IOnClickProduct;
import com.src.Module.Home.adapter.NewProductAdapter;
import com.src.Module.Home.presenter.IProduct;
import com.src.Module.Home.presenter.PresenterProduct;
import com.src.Utils.DialogLoading;
import com.src.Utils.ItemOffsetDecoration;

public class AllProductActivity extends AppCompatActivity implements IProduct.IViewProduct, IOnClickProduct {

    private RecyclerView recyclerViewAllProduct;
    private int page = 1;
    private PresenterProduct presenterProduct;
    private List<Product> products;
    private NewProductAdapter allProductAdapter;
    int findLastItem, totalItemCount;
    private GridLayoutManager layoutManager;
    private GoogleProgressBar progress_productAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        initView();

        listenerScrollRecyclerAllProduct();
        DialogLoading.LoadingGoogle(true, progress_productAll);
        presenterProduct.getListProductMore(page);

    }

    private void initView() {
        recyclerViewAllProduct = findViewById(R.id.recyclerViewAllProductPaging);
        progress_productAll = findViewById(R.id.progress_productAll);
        products = new ArrayList<>();
        presenterProduct = new PresenterProduct(this);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewAllProduct.setLayoutManager(layoutManager);

        allProductAdapter = new NewProductAdapter(getApplicationContext(), R.layout.custom_layout_new_product, products, this, "All");
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin10);
        recyclerViewAllProduct.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
        recyclerViewAllProduct.setAdapter(allProductAdapter);

    }

    private void listenerScrollRecyclerAllProduct() {
        recyclerViewAllProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                findLastItem = layoutManager.findLastCompletelyVisibleItemPosition();

                if (findLastItem == (totalItemCount -1)) {
                    DialogLoading.LoadingGoogle(true, progress_productAll);
                    presenterProduct.getListProductMore(page);
                }

            }
        });
    }

    @Override
    public void onGetListProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progress_productAll);

        if (productList.size() == 0) {
            Toasty.info(AllProductActivity.this, "Đã hết sản phẩm", Toasty.LENGTH_LONG).show();
            return;
        }
        products.addAll(productList);
        allProductAdapter.notifyDataSetChanged();

        page++;

    }

    @Override
    public void onGetListProductFailed(String msg) {
        DialogLoading.LoadingGoogle(false, progress_productAll);


    }

    @Override
    public void onGetListNewProductSuccess(List<Product> productList) {
        //null
    }

    @Override
    public void onGetListNewProductFailed(String msg) {
        //null
    }

    @Override
    public void onSearchProductSuccess(List<Product> productList) {
        //null
    }

    @Override
    public void onSearchProductFailed(String msg) {
        //null
    }

    @Override
    public void OnClickProductDetails(Product product) {

        Intent intent = new Intent(AllProductActivity.this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);

    }

    @Override
    public void OnClickBadge() {

    }
}
