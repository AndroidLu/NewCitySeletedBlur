package com.example.administrator.myapplication.cityblurdialog;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.AreaWheelAdapter;
import com.example.administrator.myapplication.adapter.CityWheelAdapter;
import com.example.administrator.myapplication.adapter.ProvinceWheelAdapter;
import com.example.administrator.myapplication.listener.OnAddressChangeListener;
import com.example.administrator.myapplication.model.AddressModel;
import com.example.administrator.myapplication.wheelview.MyOnWheelChangedListener;
import com.example.administrator.myapplication.wheelview.MyWheelView;
import com.hitomi.cslibrary.CrazyShadow;
import com.hitomi.cslibrary.base.CrazyShadowDirection;

import java.util.List;

/**
 * @author lhh
 * @package com.sh.wheelviewdemo.cityblurdialog
 * @date on    2019/1/28
 * @desc
 */
public class CityOptionsPopupWindow extends BlurPopupWindow implements View.OnClickListener, MyOnWheelChangedListener {
    private View rootView; // 总的布局
    private View btnSubmit, btnCancel;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private CrazyShadow drawCrazyShadow;
    MyWheelView provinceWheel;
    MyWheelView cityWheel;
    MyWheelView districtWheel;

    private Context context;
    private List<AddressModel.DataBeanXX> province = null;

    private OnAddressChangeListener onAddressChangeListener = null;

    public CityOptionsPopupWindow(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected View createContentView(ViewGroup parent) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.pw_options, parent, false);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.BOTTOM;
        rootView.setLayoutParams(lp);
        rootView.setVisibility(INVISIBLE);
        drawCrazyShadow = new CrazyShadow.Builder()
                .setContext(getContext())
                .setCorner(dip2Px(5))
                .setDirection(CrazyShadowDirection.ALL)
                .setShadowRadius(dip2Px(22))
                .setBaseShadowColor(getContext().getResources().getColor(R.color.timepicker_line))
                .setImpl(CrazyShadow.IMPL_WRAP)
                .action(rootView.findViewById(R.id.shadow));
        // -----确定和取消按钮
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        provinceWheel = rootView.findViewById(R.id.province_wheel);
        cityWheel = rootView.findViewById(R.id.city_wheel);
        districtWheel = rootView.findViewById(R.id.district_wheel);
        initView();
        // ----转轮
        rootView.setOnClickListener(dismissListener);
        return rootView;
    }

    private void initView() {
        provinceWheel.setVisibleItems(5);
        cityWheel.setVisibleItems(5);
        districtWheel.setVisibleItems(5);

        provinceWheel.setShadowFontSize(5, 30, 5);
        cityWheel.setShadowFontSize(5, 30, 5);
        districtWheel.setShadowFontSize(5, 30, 5);
//        0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9

        provinceWheel.addChangingListener(this);
        cityWheel.addChangingListener(this);
        districtWheel.addChangingListener(this);
    }

    public int dip2Px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //数据显示
    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        } else if (wheel == cityWheel) {
            updateDistrict();//城市改变后地区联动
        } else if (wheel == districtWheel) {
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                getContentView().setVisibility(VISIBLE);
                getContentView().setBackgroundColor(getContext().getResources().getColor(R.color.background));
                int height = getContentView().getMeasuredHeight();
                ObjectAnimator.ofFloat(getContentView(), "translationY", height, 0).setDuration(getAnimationDuration()).start();
            }
        });
    }

    @Override
    protected ObjectAnimator createDismissAnimator() {
        int height = getContentView().getMeasuredHeight();
        return ObjectAnimator.ofFloat(getContentView(), "translationY", 0, height).setDuration(getAnimationDuration());
    }

    @Override
    protected ObjectAnimator createShowAnimator() {
        return null;
    }

    public static class Builder extends BlurPopupWindow.Builder<CityOptionsPopupWindow> {
        public Builder(Context context) {
            super(context);
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000);
        }

        @Override
        protected CityOptionsPopupWindow createPopupWindow() {
            return new CityOptionsPopupWindow(mContext);
        }
    }

    private OnClickListener dismissListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public void setProvince(List<AddressModel.DataBeanXX> province) {
        this.province = province;
        bindData();
    }

    private void bindData() {
        Log.e("tag", "p  =   " + province.size());
        provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
        updateCitiy();
        updateDistrict();
    }

    private void updateCitiy() {
        int index = provinceWheel.getCurrentItem();
        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = province.get(index).getChildren().getData();
        if (citys != null && citys.size() > 0) {
            cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
            cityWheel.setCurrentItem(0);
            updateDistrict();
        }
    }

    private void updateDistrict() {
        int provinceIndex = provinceWheel.getCurrentItem();
//        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(provinceIndex).City;
        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = province.get(provinceIndex).getChildren().getData();
        int cityIndex = cityWheel.getCurrentItem();
        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> districts = citys.get(cityIndex).getChildren().getData();
        if (districts != null && districts.size() > 0) {
            districtWheel.setViewAdapter(new AreaWheelAdapter(context, districts));
            districtWheel.setCurrentItem(0);
        }

    }

    public void defaultValue(String provinceStr, String city, String arae) {
        if (TextUtils.isEmpty(provinceStr)) return;
        for (int i = 0; i < province.size(); i++) {
            AddressModel.DataBeanXX provinces = province.get(i);
            if (provinces != null && provinces.getTitle().equalsIgnoreCase(provinceStr)) {
                provinceWheel.setCurrentItem(i);
                if (TextUtils.isEmpty(city)) return;
                List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX> citys = provinces.getChildren().getData();
                for (int j = 0; j < citys.size(); j++) {
                    AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX cityEntity = citys.get(j);
                    if (cityEntity != null && cityEntity.getTitle().equalsIgnoreCase(city)) {
                        cityWheel.setViewAdapter(new CityWheelAdapter(context, citys));
                        cityWheel.setCurrentItem(j);
                        if (TextUtils.isEmpty(arae)) return;
                        List<AddressModel.DataBeanXX.ChildrenBeanX.DataBeanX.ChildrenBean.DataBean> areas = cityEntity.getChildren().getData();
                        for (int k = 0; k < areas.size(); k++) {
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

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            this.dismiss();
            return;
        } else {
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
            this.dismiss();
            return;
        }
    }

    public void setOnAddressChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }
}
