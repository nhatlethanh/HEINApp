package com.src.Module.Home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Random;


import com.src.Model.Product;
import com.src.Module.Home.IOnClickProduct;
import com.src.Network.EndPoint;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolderNewProduct> {
    private Context context;
    private int resource;
    private List<Product> productList;
    private IOnClickProduct iOnClickProduct;



    public NewProductAdapter(Context context, int resource, List<Product> productList, IOnClickProduct iOnClickProduct, String titleMar){
        this.context = context;
        this.resource = resource;
        this.productList = productList;
        this.iOnClickProduct = iOnClickProduct;


    }

    @NonNull
    @Override
    public NewProductAdapter.ViewHolderNewProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolderNewProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ViewHolderNewProduct holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getName());
        holder.txtPriceProduct.setText(product.getPrice());
        int randomQuick = new Random().nextInt(7);
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                .load(EndPoint.BASE_URL_PUBLIC + product.getImage())
                .error(R.drawable.ic_error_outline_white_24dp).into(holder.imgProduct);

        holder.layout_item_new_Product.setOnClickListener(v -> {
            iOnClickProduct.OnClickProductDetails(product);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolderNewProduct extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtRate, txtComment,txtTitleMar,txtPriceProduct;
        RoundedImageView imgProduct;
        CardView layout_item_new_Product;

        ViewHolderNewProduct(@NonNull View itemView) {
            super(itemView);
            layout_item_new_Product = itemView.findViewById(R.id.layout_item_new_Product);
            txtNameProduct = itemView.findViewById(R.id.txtNameProductNew);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProductNew);
            imgProduct = itemView.findViewById(R.id.imgProductNew);
            txtNameProduct.setMaxLines(1);
        }
    }

}
