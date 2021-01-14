package com.src.Module.Category.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.R;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.List;

import com.src.Model.Product;
import com.src.Module.Category.ICate;
import com.src.Module.Category.presenter.PresenterCate;
import com.src.Module.Home.IOnClickProduct;
import com.src.Module.Home.adapter.NewProductAdapter;
import com.src.Module.Home.view.ProductDetailsActivity;
import com.src.Utils.DialogLoading;
import com.src.Utils.ItemOffsetDecoration;

public class ProductForCateActivity extends AppCompatActivity implements ICate.IViewProductForCate, IOnClickProduct {

    private PresenterCate presenterCate;
    private RecyclerView recyclerViewProductCate;
    private TextView txtCateName;
    private GoogleProgressBar progress_productForCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodduct_for_cate);
        initView();
    }

    private void initView() {
        recyclerViewProductCate = findViewById(R.id.recyclerViewProductCate);
        progress_productForCate = findViewById(R.id.progress_productForCate);
        txtCateName = findViewById(R.id.txtCateName);

        DialogLoading.LoadingGoogle(true,progress_productForCate);
        presenterCate = new PresenterCate(null,this);
        Intent intent = getIntent();
        if(intent != null) {
            presenterCate.getProductForCate(intent.getStringExtra("cateId"));
            txtCateName.setText(intent.getStringExtra("cateName"));
        }
    }

    @Override
    public void onGetListProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false,progress_productForCate);

        NewProductAdapter ProductAdapter = new NewProductAdapter(ProductForCateActivity.this, R.layout.custom_layout_new_product, productList, this, "");
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin10);

        recyclerViewProductCate.setLayoutManager(new GridLayoutManager(ProductForCateActivity.this, 2));
        recyclerViewProductCate.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
        recyclerViewProductCate.setAdapter(ProductAdapter);
        ProductAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetListProductFailed(String msg) {
        DialogLoading.LoadingGoogle(false,progress_productForCate);

        new AlertDialog.Builder(ProductForCateActivity.this)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    @Override
    public void OnClickProductDetails(Product product) {
        Intent intent = new Intent(ProductForCateActivity.this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void OnClickBadge() {

    }
}
