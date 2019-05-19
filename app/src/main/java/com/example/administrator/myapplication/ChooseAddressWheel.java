package com.example.administrator.myapplication;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.myapplication.adapter.AreaWheelAdapter;
import com.example.administrator.myapplication.adapter.CityWheelAdapter;
import com.example.administrator.myapplication.adapter.ProvinceWheelAdapter;
import com.example.administrator.myapplication.listener.OnAddressChangeListener;
import com.example.administrator.myapplication.model.AddressModel;
import com.example.administrator.myapplication.utils.Utils;
import com.example.administrator.myapplication.wheelview.MyOnWheelChangedListener;
import com.example.administrator.myapplication.wheelview.MyWheelView;

import java.util.List;
public class ChooseAddressWheel implements MyOnWheelChangedListener {

    MyWheelView provinceWheel;
    MyWheelView cityWheel;
    MyWheelView districtWheel;
    private TextView cancel_button,confirm_button;

    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;

    private List<AddressModel.DataBeanXX> province = null;

    private OnAddressChangeListener onAddressChangeListener = null;

    public ChooseAddressWheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        provinceWheel = context.findViewById(R.id.province_wheel);
        cityWheel = context.findViewById(R.id.city_wheel);
        districtWheel = context.findViewById(R.id.district_wheel);
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();
    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_city_layout, null);
        cancel_button = parentView.findViewById(R.id.cancel_button);
        confirm_button = parentView.findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        provinceWheel.setVisibleItems(5);
        cityWheel.setVisibleItems(5);
        districtWheel.setVisibleItems(5);

        provinceWheel.setShadowFontSize(5, 30, 5);
        cityWheel.setShadowFontSize(5, 30, 5);
        districtWheel.setShadowFontSize(5, 30, 5);
//        0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9
        provinceWheel.setCyclic(false);
        cityWheel.setCyclic(false);
        districtWheel.setCyclic(false);

        provinceWheel.addChangingListener(this);
        cityWheel.addChangingListener(this);
        districtWheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (Utils.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }

    private void bindData() {
        provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
        updateCitiy();
        updateDistrict();
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        } else if (wheel == cityWheel) {
            updateDistrict();//城市改变后地区联动
        } else if (wheel == districtWheel) {
        }
    }

    private void updateCitiy() {
        int index = provinceWheel.getCurrentItem();
        if (province.get(index).getChildren().getData() != null) {
            List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = province.get(index).getChildren().getData();
            if (citys != null && citys.size() > 0) {
                cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
                cityWheel.setCurrentItem(0);
                updateDistrict();
            }
        }
    }

    private void updateDistrict() {
        int provinceIndex = provinceWheel.getCurrentItem();
//        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(provinceIndex).City;
        if (province.get(provinceIndex).getChildren().getData() != null) {
            List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = province.get(provinceIndex).getChildren().getData();
            int cityIndex = cityWheel.getCurrentItem();
            if (citys.get(cityIndex).getChildren().getData() != null) {
                List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> districts = citys.get(cityIndex).getChildren().getData();
                if (districts != null && districts.size() > 0) {
                    districtWheel.setViewAdapter(new AreaWheelAdapter(context, districts));
                    districtWheel.setCurrentItem(0);
                }
            }
        }
    }

    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void setProvince(List<AddressModel.DataBeanXX> province) {
        this.province = province;
        bindData();
    }

    public void defaultValue(String provinceStr, String city, String arae) {
        if (TextUtils.isEmpty(provinceStr)) return;
        for (int i = 0; i < province.size(); i++) {
            AddressModel.DataBeanXX provinces = province.get(i);
            if (province.get(i) != null) {
                if (provinces != null && provinces.getTitle().equalsIgnoreCase(provinceStr)) {
                    provinceWheel.setCurrentItem(i);
                    if (TextUtils.isEmpty(city)) return;
                    if (provinces.getChildren().getData() != null) {
                        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = provinces.getChildren().getData();
                        for (int j = 0; j < citys.size(); j++) {
                            if (citys.get(j) != null) {
                                AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX cityEntity = citys.get(j);
                                if (cityEntity != null && cityEntity.getTitle().equalsIgnoreCase(city)) {
                                    cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
                                    cityWheel.setCurrentItem(j);
                                    if (TextUtils.isEmpty(arae)) return;
                                    if (cityEntity.getChildren().getData() != null) {
                                        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> areas = cityEntity.getChildren().getData();
                                        for (int k = 0; k < areas.size(); k++) {
                                            if (areas.get(k) != null) {
                                                AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean areaEntity = areas.get(k);
                                                if (areaEntity != null && areaEntity.getTitle().equalsIgnoreCase(arae)) {
                                                    districtWheel.setViewAdapter(new AreaWheelAdapter(context, areas));
                                                    districtWheel.setCurrentItem(k);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void confirm() {
        if (onAddressChangeListener != null) {
            int provinceIndex = provinceWheel.getCurrentItem();
            int cityIndex = cityWheel.getCurrentItem();
            int areaIndex = districtWheel.getCurrentItem();

            String provinceName = null, cityName = null, areaName = null;

            List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = null;
            if (province != null && province.size() > provinceIndex) {
                AddressModel.DataBeanXX provinceEntity = province.get(provinceIndex);
                citys = provinceEntity.getChildren().getData();
                provinceName = provinceEntity.getTitle() + "." + provinceEntity.getId();
            }
            List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> districts = null;
            if (citys != null && citys.size() > cityIndex) {
                AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX cityEntity = citys.get(cityIndex);
                districts = cityEntity.getChildren().getData();
                cityName = cityEntity.getTitle() + "." + cityEntity.getId();
            }

            if (districts != null && districts.size() > areaIndex) {
                AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean areaEntity = districts.get(areaIndex);
                areaName = areaEntity.getTitle() + "." + areaEntity.getId();
            }

            onAddressChangeListener.onAddressChange(provinceName, cityName, areaName);
        }
        cancel();
    }

    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }
}