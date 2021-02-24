package com.clusterexample.huawei.utils;

import com.huawei.clustering.ClusterItem;
import com.huawei.hms.maps.model.LatLng;

public class MyHuaweiItem implements ClusterItem {

    private final LatLng location;
    public final String name;
    public final int profilePhoto;

    public MyHuaweiItem(LatLng location, String name, int pictureResource) {
        this.location = location;
        this.name = name;
        profilePhoto = pictureResource;
    }

    @Override
    public double getLatitude() {
        return location.latitude;
    }

    @Override
    public double getLongitude() {
        return location.longitude;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
