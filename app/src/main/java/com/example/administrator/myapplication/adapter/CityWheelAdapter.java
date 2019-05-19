package com.example.administrator.myapplication.adapter;

import android.content.Context;

import com.example.administrator.myapplication.base.BaseWheelAdapter;
import com.example.administrator.myapplication.model.AddressDtailsEntity.ProvinceEntity.CityEntity;
import com.example.administrator.myapplication.model.AddressModel;

import java.util.List;

public class CityWheelAdapter extends BaseWheelAdapter<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> {
    public CityWheelAdapter(Context context, List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> list) {
        super(context, list);
    }

    @Override
    protected CharSequence getItemText(int index) {
        AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX data = getItemData(index);
        if (data != null) {
            return data.getTitle();
        }
        return null;
    }
}
