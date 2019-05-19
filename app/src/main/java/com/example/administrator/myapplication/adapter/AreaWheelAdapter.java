package com.example.administrator.myapplication.adapter;

import android.content.Context;

import com.example.administrator.myapplication.base.BaseWheelAdapter;
import com.example.administrator.myapplication.model.AddressDtailsEntity.ProvinceEntity;
import com.example.administrator.myapplication.model.AddressModel;

import java.util.List;

public class AreaWheelAdapter extends BaseWheelAdapter<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> {
    public AreaWheelAdapter(Context context, List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> list) {
        super(context,list);
    }

    @Override
    protected CharSequence getItemText(int index) {
        AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean data = getItemData(index);
        if (data != null) {
            return data.getTitle();
        }
        return null;
    }
}
