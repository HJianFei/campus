package com.loosoo100.campus100.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @author yang 定位工具
 */
public class MyLocationUtil {
    public static String loc = "";
    private LocationClient mClient;

    public static Double longitude = 0.0; // 经度
    public static Double latitude = 0.0; // 纬度
    private MyBDLocationListener myBDLocationListener;
    private Context mContext;

    public MyLocationUtil(Context context) {
        mContext = context;
        mClient = new LocationClient(context);
        myBDLocationListener = new MyBDLocationListener();
        // 注册定位监听器
        mClient.registerLocationListener(myBDLocationListener);
        start();
    }

    public void start() {
        initLocationClientOption();
        mClient.start();
    }

    /**
     * 初始化百度地图条件
     */
    private void initLocationClientOption() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        int span = 1000;
//        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
//		option.setIsNeedAddress(true);
//        mClient.setLocOption(option);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mClient.setLocOption(option);
    }

    class MyBDLocationListener implements BDLocationListener {

        /**
         * 获得定位数据时回调该方法
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
//            loc = location.getAddrStr(); // 获取具体地址
            if (location == null) {
                loc = "";
            }
            loc = "";
            if (!location.getProvince().equals("") && !location.getProvince().equals("null")) {
                loc = loc + location.getProvince();
            }
            if (!location.getCity().equals("") && !location.getCity().equals("null")) {
                loc = loc + location.getCity();
            }
            if (!location.getDistrict().equals("") && !location.getDistrict().equals("null")) {
                loc = loc + location.getDistrict();
            }
            if (!location.getStreet().equals("") && !location.getStreet().equals("null")) {
                loc = loc + location.getStreet();
            }
//            loc = location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
//            if (loc == null) {
//                loc = "";
//            } else {
//                loc = loc.substring(2); // 截取"中国"字段后的内容
//            }
        }
    }

    public void stop() {
        mClient.stop();
        mClient.unRegisterLocationListener(myBDLocationListener);
    }

}
