package com.src.Module.Home.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chabbal.sliding.SlidingSplashView;
import com.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import com.src.NavigationActivity;
import com.src.Model.OrderProvisional;
import com.src.Model.Product;
import com.src.Model.Review;
import com.src.Module.Home.adapter.ReviewAdapter;
import com.src.Module.Home.presenter.IProductDetails;
import com.src.Module.Home.presenter.PresenterProductDetails;
import com.src.Network.EndPoint;
import com.src.Utils.DialogLoading;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsActivity extends AppCompatActivity implements IProductDetails.IViewProductTDetails {

    private SlidingSplashView introProductDetail;
    private LinearLayout bodyProductDetail, layoutWriteComment;
    private TextView txtRateProductDetails,txtCommentProductDetails,txtDescription,txtNameProductDetails,txtNoComment;
    private TextInputEditText edtComment;
    private ImageView imgFavouriteProductDetails;
    private RatingBar rateBar;
    private boolean checkedFavourite = false;
    private Product product;
    private SharedPreferences sharedPreferences;
    private PresenterProductDetails presenterProductDetails;
    private RecyclerView recyclerReviewAdapter;
    private GoogleProgressBar progress_review;
    ExploreFragment exploreFragmentCallback = new ExploreFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_product_details);
        init();
//        getSizeDevice();
        getDataProduct();
        handlerFavourite(product.getProductId());
        presenterProductDetails.getComment(product.getProductId());
    }


    private void getDataProduct() {
        Intent intent = getIntent();
        if (intent != null) {
            product = intent.getParcelableExtra("product");
            assert product != null;
            txtNameProductDetails.setText(product.getName());
            txtDescription.setText(product.getDescription());
            presenterProductDetails.getImages(product.getProductId());

        }
    }

    private boolean handlerFavourite(String productId) {
        sharedPreferences = getSharedPreferences("favourite", MODE_PRIVATE);
        checkedFavourite = sharedPreferences.getBoolean(productId, false);
        if (checkedFavourite) {
            imgFavouriteProductDetails.setImageResource(R.drawable.ic_heart_checked);
        } else {
            imgFavouriteProductDetails.setImageResource(R.drawable.ic_heart);
        }
        return checkedFavourite;
    }


    @SuppressLint("CutPasteId")
    private void init() {
        introProductDetail = findViewById(R.id.introProductDetail);
        bodyProductDetail = findViewById(R.id.bodyProductDetail);
        TextView txtWriteComment = findViewById(R.id.txtWriteComment);
        layoutWriteComment = findViewById(R.id.layoutWriteComment);
        txtDescription = findViewById(R.id.txtDescription);
        txtNameProductDetails = findViewById(R.id.txtNameProductDetails);
        ImageView imgBackProductDetails = findViewById(R.id.imgBackProductDetails);
        imgFavouriteProductDetails = findViewById(R.id.imgFavouriteProductDetails);
        Button btnOrderNowDetails = findViewById(R.id.btnOrderNowDetails);
        edtComment = findViewById(R.id.edtComment);
        Button btnSubmitComment = findViewById(R.id.btnSubmitComment);
        rateBar = findViewById(R.id.rateBar);
        recyclerReviewAdapter = findViewById(R.id.recyclerReview);
        progress_review = findViewById(R.id.progress_review);
        txtNoComment = findViewById(R.id.txtNoComment);
        DialogLoading.LoadingGoogle(true, progress_review);
        presenterProductDetails = new PresenterProductDetails(this);
        txtWriteComment.setOnClickListener(v -> layoutWriteComment.setVisibility(View.VISIBLE));
        imgBackProductDetails.setOnClickListener(v -> finish());
        btnOrderNowDetails.setOnClickListener(v -> orderProduct());
        imgFavouriteProductDetails.setOnClickListener(v -> {
            checkedFavourite = handlerFavourite(product.getProductId());
            Editor editor = sharedPreferences.edit();
            if (!checkedFavourite) {
                editor.putBoolean(product.getProductId(), true);
                editor.apply();
                imgFavouriteProductDetails.setImageResource(R.drawable.ic_heart_checked);
            } else {
                editor.putBoolean(product.getProductId(), false);
                editor.apply();
                imgFavouriteProductDetails.setImageResource(R.drawable.ic_heart);
            }
        });

        btnSubmitComment.setOnClickListener(v -> handlerComment());

    }

    private void orderProduct() {
        boolean exits = false;
        if (NavigationActivity.orderDetails.size() > 0) {
            for (OrderProvisional i : NavigationActivity.orderDetails) {
                if (!(product.getProductId().equalsIgnoreCase(i.getProduct().getProductId()))) {
                    exits = false;
                    continue;
                } else {
                    exits = true;
                    break;
                }
            }
            if (exits) {

                Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã có trong giỏ hàng", Toasty.LENGTH_SHORT).show();

            } else {

                OrderProvisional order = new OrderProvisional(product, 1);
                NavigationActivity.orderDetails.add(order);
                NavigationActivity.numberBadge = NavigationActivity.numberBadge + 1;
                Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã được thêm vào giỏ hàng", Toasty.LENGTH_SHORT).show();
                exploreFragmentCallback.OnClickBadge();

            }
        } else {
            OrderProvisional order = new OrderProvisional(product, 1);
            NavigationActivity.orderDetails.add(order);
            NavigationActivity.numberBadge = NavigationActivity.numberBadge + 1;
            Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã được thêm vào giỏ hàng", Toasty.LENGTH_LONG).show();
            exploreFragmentCallback.OnClickBadge();
        }


    }
// xử lý đánh giá khách hàng
    @SuppressLint("CheckResult")
    private void handlerComment() {
        String comment = Objects.requireNonNull(edtComment.getText()).toString().trim();
        rateBar.setStepSize((float) 1.0);
        int rate = (int) rateBar.getRating();
        if (comment.isEmpty()) {
            edtComment.setError("");
        }
        if (rate == 0) {
            Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Rate?", Toasty.LENGTH_LONG);
        }
        DialogLoading.LoadingGoogle(true, progress_review);
        presenterProductDetails.addCommentToServer(comment, product.getProductId(), rate);
    }
    @Override
    public void onSuccessAddComment(String msg) {
        txtNoComment.setVisibility(View.GONE); //Thuộc tính View.GONE sẽ giúp bạn ẩn 1 view đi hoàn toàn, nó sẽ biến mất và các view khác sẽ chiếm lấy vị trí của nó.
        edtComment.setText("");
        rateBar.setRating(0);
        DialogLoading.LoadingGoogle(false, progress_review);
        Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Bình luận thành công", Toasty.LENGTH_LONG).show();
        layoutWriteComment.setVisibility(View.GONE);
        presenterProductDetails.getComment(product.getProductId());
    }
    @Override
    public void onFailedAddComment(String msg) {
        DialogLoading.LoadingGoogle(false, progress_review);
        Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Có lỗi xảy ra, vui lòng thử lại!", Toasty.LENGTH_LONG).show();
    }
    @Override
    public void onSuccessGetComment(List<Review> reviews) {
        DialogLoading.LoadingGoogle(false, progress_review);
        if (!reviews.isEmpty()) {
            txtNoComment.setVisibility(View.GONE);
            showReview(reviews);
        }else {
            txtNoComment.setVisibility(View.VISIBLE);
            //Thuốc tính View.INVISIBLE giúp bạn chỉ ẩn View đó đi và không cho nó hiện lên nhưng nó vẫn giữ vị trí trong toàn bộ Giao diện đó,
            // Không View nào có thể chiếm lấy vị trí mà nó đang đứng.
        }
    }
    @Override
    public void onFailedGetComment(String msg) {
        Toasty.error(getApplicationContext(), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessGetImages(String[] images) {
        if (images != null) {
            introProductDetail.setImageResources(images);
            introProductDetail.setAutoPage();
        }else {
            introProductDetail.setImageResources(new String[]{EndPoint.BASE_URL_PUBLIC + product.getImage()});
        }
    }

    @Override
    public void onFailGetImages(String msg) {

    }

    private void showReview(List<Review> reviews) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(ProductDetailsActivity.this, reviews);
        recyclerReviewAdapter.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, true));
        recyclerReviewAdapter.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        // khi được gọi, nó sẽ xem những mục nào được hiển thị trên màn hình tại thời điểm cuộc gọi của nó
        // (chính xác hơn là chỉ mục hàng nào) và gọi getView() với các vị trí đó.
    }


}
