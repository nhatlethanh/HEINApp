package com.src.Module.Home.model;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.ErrorResponse;
import com.src.Model.Images;
import com.src.Model.Product;
import com.src.Model.Review;
import com.src.Module.Home.presenter.PresenterProduct;
import com.src.Module.Home.presenter.PresenterProductDetails;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

public class ModelProduct {
    IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);

    public void listProduct(PresenterProduct presenterProduct) {
        Call<BaseResponse<List<Product>>> callProduct = apiService.getListProduct();
        callProduct.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    presenterProduct.onResult(true, response.body().getData(), "");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterProduct.onResult(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {
                presenterProduct.onResult(false, null, t.getMessage());
            }
        });

    }
    public void listNewProduct(PresenterProduct presenterProduct) {
        Log.d("FsBs", "listProduct: ");
        Call<BaseResponse<List<Product>>> callProduct = apiService.getNewListProduct();
        callProduct.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    presenterProduct.onResultNewProduct(true, response.body().getData(), "");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterProduct.onResultNewProduct(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {
                presenterProduct.onResultNewProduct(false, null, t.getMessage());
            }
        });

    }
    public void listProductMore(int page,PresenterProduct presenterProduct) {
        Log.d("FsBs", "getListProductMore: " + page);

        Call<BaseResponse<List<Product>>> callProduct = apiService.getListProductPaging(page);
        callProduct.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    presenterProduct.onResult(true, response.body().getData(), "");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterProduct.onResult(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {
                presenterProduct.onResult(false, null, t.getMessage());
            }
        });

    }

    public void addComment(String comment, String productId, int rate, PresenterProductDetails presenterProductDetails) {
        Call<BaseResponse<Review.ReviewDetails>> callReview = apiService.addComment(comment, rate, productId);
        callReview.enqueue(new Callback<BaseResponse<Review.ReviewDetails>>() {
            @Override
            public void onResponse(Call<BaseResponse<Review.ReviewDetails>> call, Response<BaseResponse<Review.ReviewDetails>> response) {
                if(response.isSuccessful()){
                    presenterProductDetails.resultAddComment(true,"Thành công");
                }else {
                    presenterProductDetails.resultAddComment(false,"failed");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Review.ReviewDetails>> call, Throwable t) {
                presenterProductDetails.resultAddComment(false,t.getMessage());
            }
        });
    }


    public void getComment(String productId,PresenterProductDetails presenterProductDetails){
        Call<BaseResponse<List<Review>>> callReview = apiService.getListReview(productId);
        callReview.enqueue(new Callback<BaseResponse<List<Review>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Review>>> call, Response<BaseResponse<List<Review>>> response) {
                if(response.isSuccessful()){

                    presenterProductDetails.resultGetComment(true,response.body().getData());
                }else {
                    presenterProductDetails.resultGetComment(false,  null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Review>>> call, Throwable t) {
                presenterProductDetails.resultGetComment(false,  null);

            }
        });
    }


    public void getImages(String productId, PresenterProductDetails presenterProductDetails){
        Call<BaseResponse<List<Images>>> callImageProduct = apiService.getImagesProduct(productId);
        callImageProduct.enqueue(new Callback<BaseResponse<List<Images>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Images>>> call, Response<BaseResponse<List<Images>>> response) {
                if(response.isSuccessful()){
                    presenterProductDetails.resultGetImages(true,response.body().getData());
                }else {
                    presenterProductDetails.resultGetImages(false,null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Images>>> call, Throwable t) {
                presenterProductDetails.resultGetImages(false,null);
            }
        });
    }

    public void handlerSearch(String query,PresenterProduct presenterProduct){
        Call<BaseResponse<List<Product>>> callSearch = apiService.getSearchProduct(query);
        callSearch.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                Log.d("FsBs", "onResponse: search" + response.code());
                if(response.isSuccessful()){
                    presenterProduct.resultSearch(true,response.body().getData());
                }else{
                    presenterProduct.resultSearch(false,null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {
                presenterProduct.resultSearch(false,null);
            }
        });
    }
}

