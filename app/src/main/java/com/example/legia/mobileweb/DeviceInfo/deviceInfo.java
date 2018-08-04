package com.example.legia.mobileweb.DeviceInfo;

import com.jaredrummler.android.device.DeviceName;

public class deviceInfo {
    static DeviceName.DeviceInfo info;

    public static String getDeviceName(){
        return info.getName();
    }

    public static String getBrand(){
        return info.manufacturer;
    }

    public static String getModel(){
        return info.model;
    }
}
