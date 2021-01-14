package com.src.Module.Category.presenter;

import java.util.List;

import com.src.Model.Cate;
import com.src.Model.Product;
import com.src.Module.Category.ICate;
import com.src.Module.Category.model.ModelCate;

public class PresenterCate implements ICate.IPresenterCate {
    ICate.IViewCate iViewCate;
    ICate.IViewProductForCate iViewProductForCate;
    ModelCate modelCate;

    public PresenterCate(ICate.IViewCate iViewCate, ICate.IViewProductForCate iViewProductForCate) {
        this.iViewCate = iViewCate;
        this.iViewProductForCate = iViewProductForCate;
        modelCate = new ModelCate();
    }

    @Override
    public void getListCate() {
        modelCate.getListCate(this);
    }

    @Override
    public void resultGetListCate(boolean success, List<Cate> cateList, String msg) {
        if (success) {
            iViewCate.onGetListSuccess(cateList);
        } else {
            iViewCate.onGetListFailed(msg);
        }
    }

    @Override
    public void getProductForCate(String cateId) {
        modelCate.listProductForCate(cateId, this);

    }

    @Override
    public void resultGetProductForCate(boolean success, List<Product> productList, String msg) {
        if (success) {
            if (productList.size() != 0) {
                iViewProductForCate.onGetListProductSuccess(productList);

            } else {
                iViewProductForCate.onGetListProductFailed("Không có dữ liệu");

            }
        } else {
            iViewProductForCate.onGetListProductFailed(msg);

        }
    }


}
