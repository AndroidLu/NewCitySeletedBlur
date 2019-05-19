package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.cityblurdialog.CityOptionsPopupWindow;
import com.example.administrator.myapplication.listener.OnAddressChangeListener;
import com.example.administrator.myapplication.model.AddressModel;
import com.example.administrator.myapplication.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnAddressChangeListener {

    TextView chooseAddress;

    private ChooseAddressWheel chooseAddressWheel = null;
    CityOptionsPopupWindow cityOptionsPopupWindow = null;
    AddressModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        chooseAddress = findViewById(R.id.choose_address);
        chooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityOptionsPopupWindow.show();
            }
        });
        init();
    }

    private void init() {
        initWheel();
        initData();


    }

    private void initWheel() {
//        chooseAddressWheel = new ChooseAddressWheel(this);
//        chooseAddressWheel.setOnAddressChangeListener(this);
        cityOptionsPopupWindow = new CityOptionsPopupWindow(this);
        cityOptionsPopupWindow.setOnAddressChangeListener(this);
    }

    private void initData() {
        // 获取json文件输入
        InputStream is = null;
        try {
            is = getResources().getAssets().open("regions.json");
            // 将json文件读入为一个字符串
            byte[] bytearray = new byte[is.available()];
            is.read(bytearray);
            String address_json = new String(bytearray, "UTF-8");
            JSONObject jsonObject = new JSONObject(address_json);
            String jsondata = jsonObject.toString();
            model = JsonUtil.parseJson(jsondata, AddressModel.class);
            if (model != null) {
//                AddressDtailsEntity data = model.Result;
//                if (data == null) return;
                chooseAddress.setText(model.getData().get(0).getTitle() + " " + model.getData().get(0).getChildren().getData().get(0).getTitle() + " "
                        + model.getData().get(0).getChildren().getData().get(0).getChildren().getData().get(0).getTitle());
                if (model.getData().get(0) != null && model.getData().get(0).getChildren().getData().get(0) != null && model.getData().get(0).getChildren().getData().get(0).getChildren().getData().get(0) != null) {
                    cityOptionsPopupWindow.setProvince(model.getData());
                    cityOptionsPopupWindow.defaultValue(model.getData().get(0).getTitle(), model.getData().get(0).getChildren().getData().get(0).getTitle(), model.getData().get(0).getChildren().getData().get(0).getChildren().getData().get(0).getTitle());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAddressChange(String province, String city, String district) {
        chooseAddress.setText(province + " " + city + " " + district);
    }
}
