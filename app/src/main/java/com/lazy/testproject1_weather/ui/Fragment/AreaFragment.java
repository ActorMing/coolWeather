package com.lazy.testproject1_weather.ui.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lazy.testproject1_weather.R;
import com.lazy.testproject1_weather.base.BaseApplication;
import com.lazy.testproject1_weather.di.component.DaggerAreaFragmentComponent;
import com.lazy.testproject1_weather.di.module.AreaFragmentModule;
import com.lazy.testproject1_weather.entity.datasupport.City;
import com.lazy.testproject1_weather.entity.datasupport.County;
import com.lazy.testproject1_weather.entity.datasupport.Province;
import com.lazy.testproject1_weather.mvp.contract.AreaContract;
import com.lazy.testproject1_weather.ui.activity.WeatherActivity;
import com.lazy.testproject1_weather.util.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.lazy.testproject1_weather.util.ConstantUtils.LEVEL_CITY;
import static com.lazy.testproject1_weather.util.ConstantUtils.LEVEL_COUNTY;
import static com.lazy.testproject1_weather.util.ConstantUtils.LEVEL_PROVINCE;

/**
 * 地区碎片
 * <p>
 * Created by liming on 2017/6/15.
 */

public class AreaFragment extends Fragment implements AreaContract.View {

    private Context mContext;

    private ProgressDialog progressDialog;

    private TextView tvTitle;

    private Button btnBack;

    private ListView listView;

    private ArrayAdapter<String> stringArrayAdapter;

    private List<String> stringList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 选中的级别
     */
    private int currentLevel;

    @Inject
    AreaContract.Presenter areaPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        listView = (ListView) view.findViewById(R.id.lv_area);
        stringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(stringArrayAdapter);

        mContext = getActivity();

        initInject();

        return view;
    }

    private void initInject() {
        DaggerAreaFragmentComponent.builder()
                .appComponent(BaseApplication.get(AreaFragment.this.getActivity()).getAppComponent())
                .areaFragmentModule(new AreaFragmentModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的级别进行数据查询
                if (currentLevel == LEVEL_PROVINCE) {
                    queryCities(provinceList.get(position));
                } else if (currentLevel == LEVEL_CITY) {
                    queryCounties(cityList.get(position));
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 后退,是从县级退到市级,从市级退到省级
                if (currentLevel == ConstantUtils.LEVEL_COUNTY) {
                    queryCities(selectedProvince);
                } else if (currentLevel == ConstantUtils.LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

        // 默认去加载全国的省份信息
        queryProvinces();
    }

    /**
     * 查询全国的省份信息
     */
    @Override
    public void queryProvinces() {
        tvTitle.setText(getString(R.string.china));
        btnBack.setVisibility(View.GONE);
        provinceList = areaPresenter.getProvinceList();
        if (provinceList.size() > 0) {
            stringList.clear();
            for (Province province : provinceList) {
                stringList.add(province.getProvinceName());
            }
            stringArrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = ConstantUtils.LEVEL_PROVINCE;
        } else {
            // 查询全国的所有省份
            areaPresenter.queryFromServer(ConstantUtils.LEVEL_PROVINCE, null, null);
        }
    }

    /**
     * 根据省份信息查询城市信息
     */
    @Override
    public void queryCities(Province province) {
        selectedProvince = province;
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setText(selectedProvince.getProvinceName());
        cityList = areaPresenter.getCityList(province);
        if (cityList.size() > 0) {
            stringList.clear();
            for (City city : cityList) {
                stringList.add(city.getCityName());
            }
            stringArrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = ConstantUtils.LEVEL_CITY;
        } else { // 没有数据需要去服务器获取
            // 因为查询城市需要知道省份需要知道省份编号
            int provinceCode = selectedProvince.getProvinceCode();
            areaPresenter.queryFromServer(ConstantUtils.LEVEL_CITY, selectedProvince, null, provinceCode);
        }

    }

    /**
     * 根据城市信息查询县级信息
     */
    @Override
    public void queryCounties(City city) {
        selectedCity = city;
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setText(selectedCity.getCityName());
        countyList = areaPresenter.getCountyList(selectedCity);
        if (countyList.size() > 0) {
            stringList.clear();
            for (County county : countyList) {
                stringList.add(county.getCountyName());
            }
            stringArrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = ConstantUtils.LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            // 因为查询县信息需要知道省份和城市,所以传递两个参数
            areaPresenter.queryFromServer(ConstantUtils.LEVEL_COUNTY, selectedProvince, selectedCity, provinceCode, cityCode);
        }
    }


    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
