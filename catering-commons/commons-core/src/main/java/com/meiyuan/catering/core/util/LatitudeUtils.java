package com.meiyuan.catering.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.dto.base.LocationDTO;
import com.meiyuan.catering.core.dto.base.SearchLocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.SloppyMath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述: 调用百度地图Api实现经纬度的获取
 *
 * @author WeiGang.
 * @version v1.0
 * @date 2019/11/23 10:07
 */
@Slf4j
public class LatitudeUtils {

    /**
     * 百度地图AK
     */
    private static final String KEY_1 = "HgVeUZy5grgtZYlBy3yO5kr6WxwWLLP1";
    private static final String STATUS = "status";
    private static final Double D1000 = 1000D;

    /**
     * @description 腾讯地图AK
     * @author yaozou
     * @date 2020/5/25 18:21
     * @since v1.1.0
     */
    private static final String TENCENT_KEY = "OUUBZ-WQN3G-7O4Q3-IFW5M-XDQ3H-CIF4M";

    /**
     * @param
     * @return
     * @description 逆地理编码 返回省市区名称 及其 行政编码
     * @author yaozou
     * @date 2020/3/16 14:31
     * @since v1.0.0
     */
    public static LocationDTO reverseGeocoding(double lat, double lng) {
        return baseReverseGeocodingForBaidu(lat, lng, 100, true);
    }

    /**
     * @param
     * @return
     * @description 腾讯地图经纬度
     * @author yaozou
     * @date 2020/5/25 17:23
     * @since v1.0.0
     */
    public static LocationDTO reverseGeocodingForTencent(double lat, double lng) {
        BufferedReader in = null;
        String location = lat + "," + lng;
        try {
            URL tirc = new URL("https://apis.map.qq.com/ws/geocoder/v1/?location=" + location + "&get_poi=1&key=" + TENCENT_KEY);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            log.debug("Tencent  reverse geocoding:{}", str);
            JSONObject json = JSONObject.parseObject(str);

            if (json.getInteger(STATUS).intValue() == 0) {
                JSONObject result = json.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("ad_info");
                LocationDTO locationDTO = JSON.parseObject(addressComponent.toJSONString(), LocationDTO.class);
                locationDTO.setDistrictCode(locationDTO.getAdcode());
                locationDTO.setFormattedAddress(result.getJSONObject("formatted_addresses").getString("recommend"));

                JSONArray pois = result.getJSONArray("pois");
                List<SearchLocationDTO> poiList = new ArrayList<>(pois.size());

                for (int i = 0; i < pois.size(); i++) {
                    JSONObject poi = pois.getJSONObject(i);
                    SearchLocationDTO dto = new SearchLocationDTO();
                    dto.setAddress(poi.getString("address"));
                    dto.setName(poi.getString("title"));

                    dto.setProvince(locationDTO.getProvince());
                    dto.setCity(locationDTO.getCity());
                    dto.setArea(locationDTO.getDistrict());

                    dto.setDistance(poi.getDouble("_distance"));

                    Location location1 = new Location();

                    JSONObject point = poi.getJSONObject("location");
                    if (poi != null) {
                        location1.setLng(point.getBigDecimal("lng").toPlainString());
                        location1.setLat(point.getBigDecimal("lat").toPlainString());
                    }
                    dto.setLocation(location1);
                    poiList.add(dto);
                }
                locationDTO.setPoiList(poiList);
                return locationDTO;
            }

            return new LocationDTO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new LocationDTO();
    }

    public static LocationDTO baseReverseGeocodingForBaidu(double lat, double lng, int radius, boolean isBaidu) {
        BufferedReader in = null;
        String location = lat + "," + lng;
        String coordtype = "";
        String retCoordtype = "";
        String poiTypes = "&extensions_poi=1&radius=" + radius;
        try {
            if (!isBaidu) {
                coordtype = "&coordtype=gcj02ll";
                retCoordtype = "&retCoordtype=gcj02ll";
            }
            URL tirc = new URL("http://api.map.baidu.com/reverse_geocoding/v3/?location=" + location + coordtype + retCoordtype + poiTypes + "&output=json&ak=" + KEY_1);
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            log.debug(" baidu reverse geocoding:{}", str);
            JSONObject json = JSONObject.parseObject(str);

            if (json.getInteger(STATUS).intValue() == 0) {
                JSONObject result = json.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                LocationDTO locationDTO = JSON.parseObject(addressComponent.toJSONString(), LocationDTO.class);
                locationDTO.setCountryCode(addressComponent.getString("country_code"));
                locationDTO.setCityLevel(addressComponent.getInteger("cityLevel"));
                locationDTO.setDistrictCode(locationDTO.getAdcode());
                locationDTO.setFormattedAddress(result.getString("formatted_address"));

                JSONArray pois = result.getJSONArray("pois");
                List<SearchLocationDTO> poiList = new ArrayList<>(pois.size());

                for (int i = 0; i < pois.size(); i++) {
                    JSONObject poi = pois.getJSONObject(i);
                    SearchLocationDTO dto = new SearchLocationDTO();
                    dto.setAddress(poi.getString("addr"));
                    dto.setName(poi.getString("name"));

                    dto.setProvince(locationDTO.getProvince());
                    dto.setCity(locationDTO.getCity());
                    dto.setArea(locationDTO.getDistrict());

                    Location location1 = new Location();

                    JSONObject point = poi.getJSONObject("point");
                    if (poi != null) {
                        location1.setLng(point.getBigDecimal("x").toPlainString());
                        location1.setLat(point.getBigDecimal("y").toPlainString());
                    }
                    dto.setLocation(location1);
                    poiList.add(dto);
                }
                locationDTO.setPoiList(poiList);
                return locationDTO;
            }

            return new LocationDTO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new LocationDTO();
    }


    /**
     * 功能描述: 转化为弧度(rad)
     *
     * @param d 半径
     * @return {@link double}
     * @author WeiGang.
     * @date 2019/11/25 20:12
     * @version v1.0
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 功能描述: 通过经纬度获取距离(单位：米)
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return {@link double}
     * @author WeiGang.
     * @date 2019/11/25 20:12
     * @version v1.0
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double s = SloppyMath.haversinMeters(lat1, lng1, lat2, lng2);
        return s;
    }

    public static String countDistance(double distance) {
        if (distance >= D1000) {
            double v = distance / 1000;
            return new BigDecimal(v).setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "km";
        }
        return new BigDecimal(distance).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "m";
    }

    public static List<SearchLocationDTO> searchByRadius(String location, int radius, boolean isBaidu) {
        double lat = new Double(location.split(",")[1]), lng = new Double(location.split(",")[0]);
        if (isBaidu) {
            LocationDTO locationDTO = baseReverseGeocodingForBaidu(lat, lng, radius, isBaidu);
            return locationDTO.getPoiList();
        }
        LocationDTO locationDTO = reverseGeocodingForTencent(lat, lng);
        return locationDTO.getPoiList();
    }

    /**
     * 方法描述: 通过详细地址获取经纬度<br>
     *
     * @author: gz
     * @date: 2020/7/10 13:36
     * @param addr 详细地址 如：北京市海淀区上地十街10号
     * @return: {@link Location}
     * @version 1.2.0
     **/
    public static Location getCoordinate(String addr) throws IOException {
        String address = null;
        Location location = null;
        try {
            address = URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoding/v3/?address=%s&output=json&ak=%s", address, KEY_1);
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            insr = new InputStreamReader(new URL(url).openStream(), "UTF-8");
            br = new BufferedReader(insr);
            String data = null;
            StringBuilder sb = new StringBuilder();
            while ((data = br.readLine()) != null) {
                sb.append(data.trim());
            }
            String str = sb.toString();
            log.debug(" baidu reverse address:{}", str);
            JSONObject jsonObject = JSONObject.parseObject(str);
            if (jsonObject.getInteger(STATUS) == 0) {
                JSONObject result = jsonObject.getJSONObject("result");
                location = result.getObject("location", Location.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (insr != null) {
                insr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return location;
    }

}




