package com.meiyuan.catering.es.util;

import com.meiyuan.catering.core.page.BasePageDTO;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.GpsCoordinateUtils;
import com.meiyuan.catering.core.util.LatitudeUtils;
import com.meiyuan.catering.es.dto.geo.EsGeoSearchDTO;
import com.meiyuan.catering.es.dto.geo.GeoLimitQueryDTO;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO;
import com.meiyuan.catering.es.dto.sku.SkuPriceDTO;
import com.meiyuan.catering.es.entity.EsGoodsEntity;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxf
 * @date 2020/3/24 13:49
 * @description 简单描述
 **/
public class EsUtil {

    private EsUtil() {
    }

    /**
     * 设置嵌套查询的参数
     *
     * @param queryBuilder           查询语句
     * @param nestedField            嵌套字段
     * @param nestedFieldNameAndType 嵌套字段类型 比如 skuList.skuCode.keyword
     * @param value                  查询的值
     * @author: wxf
     * @date: 2020/3/24 13:50
     **/
    public static void setNestedQuery(BoolQueryBuilder queryBuilder, String nestedField, String nestedFieldNameAndType, String value) {
        //nestedQuery 嵌套查询
        //ScoreMode.Total
        QueryBuilder nestedQuery = QueryBuilders.nestedQuery(nestedField,
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(nestedFieldNameAndType, value)),
                ScoreMode.None);
        queryBuilder.must(QueryBuilders.boolQuery().should(nestedQuery));
    }

    public static Long[] idListToArray(List<Long> mGoodsIdList) {
        Long[] idArray = new Long[mGoodsIdList.size()];
        mGoodsIdList.toArray(idArray);
        return idArray;
    }

    /**
     * 商户菜单商品公共查询queryBuilder
     *
     * @param dataBindType 数据绑定类型
     * @author: wxf
     * @date: 2020/3/28 13:39
     * @return: {@link BoolQueryBuilder}
     **/
    public static BoolQueryBuilder merchantMenuGoodsPublicQueryBuilder(Integer dataBindType) {
        // 查询语句
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 数据类型
        queryBuilder.must(QueryBuilders.termQuery("dataBindType", dataBindType));
        return queryBuilder;
    }

    /**
     * 设置标签展示的个数
     *
     * @param goodsList 商品集合
     * @author: wxf
     * @date: 2020/3/30 10:04
     **/
    public static void setLabelShowSize(List<EsGoodsDTO> goodsList) {
        // 产品说 最多展示3个
        int labelShowSize = 3;
        goodsList.forEach(
                i -> {
                    if (BaseUtil.judgeList(i.getLabelList())) {
                        i.setLabelList(i.getLabelList().stream().limit(labelShowSize).collect(Collectors.toList()));
                    }
                }
        );
    }

    /**
     * 计算多少米
     *
     * @param dataLat 数据里面的维度
     * @param dataLon 数据里面的经度
     * @param lat     当前位置的维度
     * @param lon     当前位置的经度
     * @author: wxf
     * @date: 2020/4/22 10:11
     * @return: {@link double}
     **/
    public static double calculate(double dataLat, double dataLon, double lat, double lon) {
        return GeoDistance.ARC.calculate(dataLat, dataLon, lat, lon, DistanceUnit.METERS);
    }

    public static String distanceStr(String userLocation, String shopLocation) {
        return LatitudeUtils.countDistance(distance(userLocation, shopLocation));
    }

    public static double distance(String userLocation, String shopLocation) {
        userLocation = GpsCoordinateUtils.calGCJ02toBD09(userLocation);
        String[] shop = shopLocation.split(BaseUtil.COMMA);
        String[] user = userLocation.split(BaseUtil.COMMA);
        return calculate(
                new Double(shop[1]),
                new Double(shop[0]),
                new Double(user[1]),
                new Double(user[0]));
    }

    /**
     * 判断当前页是不是最后一页
     *
     * @param total        总页数
     * @param size         每页条数
     * @param currentPages 当前页
     * @return
     */
    public static boolean lastPages(int total, int size, int currentPages) {
        if (size == 0) {
            return true;
        }
        int pages = total / size;
        if (total % size != 0) {
            pages++;
        }
        return currentPages == pages;
    }

    /**
     * 转换m/km
     *
     * @param paramLat 查询参数的纬度
     * @param paramLng 查询参数的经度
     * @author: wxf
     * @date: 2020/5/13 17:29
     * @return: {@link String}
     **/
    public static String convertLocation(double paramLat, double paramLng, String[] location) {
        double dataLat = new Double(location[0]);
        double dataLng = new Double(location[1]);
        double calculate = EsUtil.calculate(
                dataLat,
                dataLng,
                paramLat,
                paramLng);
        return calculate < 1000 ? (int) calculate + "m" : Math.rint(calculate / 100) / 10 + "km";
    }

    /**
     * 经纬度之间距离计算
     *
     * @param fromLat
     * @param fromLng
     * @param toLat
     * @param toLng
     * @return 单位：米
     */
    public static double convertLocation(double fromLat, double fromLng, double toLat, double toLng) {
        double calculate = EsUtil.calculate(
                fromLat,
                fromLng,
                toLat,
                toLng);
        return calculate;
    }

    /**
     * 过滤被删除的sku
     *
     * @param skuList sku集合
     * @author: wxf
     * @date: 2020/5/27 10:49
     * @version 1.0.1
     **/
    public static List<EsGoodsSkuDTO> filterSkuList(List<EsGoodsSkuDTO> skuList) {
        return skuList.stream().filter(i -> !i.getDel()).collect(Collectors.toList());
    }

    public static String[] stringIdListToArray(List<String> mGoodsIdList) {
        String[] idArray = new String[mGoodsIdList.size()];
        mGoodsIdList.toArray(idArray);
        return idArray;
    }

    public static void entityFilterSkuList(List<EsGoodsEntity> esGoodsEntityList) {
        esGoodsEntityList.forEach(
                i -> {
                    if (BaseUtil.judgeList(i.getSkuList())) {
                        i.setSkuList(filterSkuList(i.getSkuList()));
                    }
                }
        );
    }

    /**
     * 设置 geo查询 相关参数
     *
     * @param lat          纬度
     * @param lng          经度
     * @param pageNo       页码
     * @param pageSize     条数
     * @param geo          geo类
     * @param clazz        返回的类class
     * @param queryBuilder 查询参数
     * @param distanceUnit 单位 米还是千米
     * @param indexs       索引
     * @author: wxf
     * @date: 2020/5/28 14:48
     * @version 1.0.1
     **/
    public static <T> void setGeo(Double lat, Double lng,
                                  Long pageNo, Long pageSize,
                                  EsGeoSearchDTO<T> geo, Class clazz,
                                  BoolQueryBuilder queryBuilder, DistanceUnit distanceUnit, String[] indexs) {
        String location = "location";
        // 搜索半径  默认500公里
        double distance = 500;
        geo.setClazz(clazz);
        geo.setLat(lat);
        geo.setLon(lng);
        geo.setQueryBuilder(queryBuilder);
        geo.setLocation(location);
        geo.setDistance(distance);
        geo.setDistanceUnit(distanceUnit);
        geo.setIndex(indexs);
        geo.setPageNo(pageNo);
        geo.setPageSize(pageSize);
        geo.setSortOrder(SortOrder.ASC);
    }

    public static <T> void setGeo(EsGeoSearchDTO<T> searchDTO, BoolQueryBuilder queryBuilder, String location, BasePageDTO dto,
                                  Class clazz, DistanceUnit distanceUnit, String[] indexs) {
        String s = GpsCoordinateUtils.calGCJ02toBD09(location);
        String[] split = s.split(BaseUtil.COMMA);
        setGeo(new Double(split[1]), new Double(split[0]),
                dto.getPageNo(), dto.getPageSize(),
                searchDTO, clazz,
                queryBuilder, distanceUnit,
                indexs);

    }

    /**
     * 获取经纬度排序需要的参数DTO
     *
     * @param queryBuilder es查询条件
     * @param lat          维度
     * @param lng          经度
     * @param pageNo       页码
     * @param pageSize     条数
     * @author: wxf
     * @date: 2020/6/2 16:33
     * @return: {@link GeoLimitQueryDTO}
     * @version 1.0.1
     **/
    public static GeoLimitQueryDTO getGeoLimitQueryDTO(BoolQueryBuilder queryBuilder, Double lat, Double lng, Long pageNo, Long pageSize) {
        GeoLimitQueryDTO geoLimitQueryDto = new GeoLimitQueryDTO();
        geoLimitQueryDto.setQueryBuilder(queryBuilder);
        geoLimitQueryDto.setLat(lat);
        geoLimitQueryDto.setLng(lng);
        geoLimitQueryDto.setPageNo(pageNo);
        geoLimitQueryDto.setPageSize(pageSize);
        return geoLimitQueryDto;
    }

    public static SkuPriceDTO skuListSorted(EsGoodsDTO entity) {

        if (entity.getGoodsSpecType().equals(2)) {
            return skuListSorted(entity.getSkuList());
        } else {
            return skuListSorted2(entity.getSkuList());
        }
    }

    public static SkuPriceDTO skuListSorted(EsGoodsEntity entity) {

        if (entity.getGoodsSpecType().equals(2)) {
            return skuListSorted(entity.getSkuList());
        } else {
            return skuListSorted2(entity.getSkuList());
        }
    }

    public static SkuPriceDTO skuListSorted2(List<EsGoodsSkuDTO> skuList) {
        EsGoodsSkuDTO skuDTO = skuList.get(0);

        SkuPriceDTO priceDTO = new SkuPriceDTO();

        BigDecimal marketPrice = skuDTO.getMarketPrice();
        priceDTO.setMarketPrice(marketPrice);

        BigDecimal salesPrice1 = skuDTO.getSalesPrice();

        BigDecimal salesPrice = !BaseUtil.isNullOrNegativeOne(salesPrice1)
                ? salesPrice1
                : marketPrice;

        priceDTO.setSalesPrice(salesPrice);

        BigDecimal enterprisePrice = skuDTO.getEnterprisePrice();

        BigDecimal enterprise = !BaseUtil.isNullOrNegativeOne(enterprisePrice)
                ? enterprisePrice
                : marketPrice;

        priceDTO.setEnterprisePrice(enterprise);

        return priceDTO;
    }

    public static SkuPriceDTO skuListSorted(List<EsGoodsSkuDTO> skuList) {

        int size = skuList.size();
        long sCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getSalesPrice())).count();
        long eCount = skuList.stream().filter(e -> !BaseUtil.isNullOrNegativeOne(e.getEnterprisePrice())).count();

        BigDecimal marketPrice = skuList.stream().min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).get().getMarketPrice();

        EsGoodsSkuDTO salesSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getSalesPrice())).min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).orElse(null);

        EsGoodsSkuDTO enterpriseSku = skuList.stream().filter(sku -> !BaseUtil.isNegativeOne(sku.getEnterprisePrice())).min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice)).orElse(null);

        SkuPriceDTO priceDTO = new SkuPriceDTO();
        priceDTO.setMarketPrice(marketPrice);


        BigDecimal salesPrice;
        if (sCount == size) {
            salesPrice = salesSku != null
                    ? salesSku.getSalesPrice()
                    : marketPrice;
        } else {
            EsGoodsSkuDTO skuDTO = skuList.stream()
                    .filter(e -> BaseUtil.isNegativeOne(e.getSalesPrice()))
                    .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
            if (skuDTO != null) {
                salesPrice = salesSku != null && BaseUtil.priceIsLt(salesSku.getSalesPrice(), skuDTO.getMarketPrice())
                        ? salesSku.getSalesPrice()
                        : skuDTO.getMarketPrice();
            } else {
                salesPrice = salesSku != null ? salesSku.getSalesPrice() : marketPrice;
            }
//            salesPrice = salesSku != null && BaseUtil.priceIsLt(salesSku.getSalesPrice(), marketPrice)
//                    ? salesSku.getSalesPrice()
//                    : marketPrice;
        }
        priceDTO.setSalesPrice(salesPrice);


        BigDecimal enterprise;
        if (eCount == size) {
            enterprise = enterpriseSku != null
                    ? enterpriseSku.getEnterprisePrice()
                    : marketPrice;
        } else {
            EsGoodsSkuDTO skuDTO = skuList.stream()
                    .filter(e -> BaseUtil.isNegativeOne(e.getEnterprisePrice()))
                    .min(Comparator.comparing(EsGoodsSkuDTO::getMarketPrice)).orElse(null);
            if (skuDTO != null) {
                enterprise = enterpriseSku != null && BaseUtil.priceIsLt(enterpriseSku.getEnterprisePrice(), skuDTO.getMarketPrice())
                        ? enterpriseSku.getEnterprisePrice()
                        : skuDTO.getMarketPrice();
            } else {
                enterprise = enterpriseSku != null ? enterpriseSku.getEnterprisePrice() : marketPrice;
            }

        }
        priceDTO.setEnterprisePrice(enterprise);


        return priceDTO;
    }

    /**
     * 方法描述 : 商品sku集合处理
     * 1、商品中若销售价不存在则将原价赋值给销售价
     * 2、商品中若企业价不存在则将销售价赋值给企业价，若销售价也不存在则将原价赋值给企业价
     * 3、将赋值后的价格按照userType排序[价格从低到高]
     *
     * @param skuList
     * @param userType 是否是企业用户，true：是
     * @Author: MeiTao
     * @Date: 2020/7/31 0031 13:54
     * @return: java.util.List<com.meiyuan.catering.es.dto.sku.EsGoodsSkuDTO>
     * @Since version-1.2.0
     */
    public static EsGoodsSkuDTO handleSkuList(List<EsGoodsSkuDTO> skuList, Boolean userType) {
        //企业取值调整 (销售价/企业价)
        skuList.forEach(sku -> {
            if (userType) {
                if (BaseUtil.isNegativeOne(sku.getEnterprisePrice())) {
                    sku.setEnterprisePrice(sku.getMarketPrice());
                }
            } else {
                if (BaseUtil.isNegativeOne(sku.getSalesPrice())) {
                    sku.setSalesPrice(sku.getMarketPrice());
                }
            }
        });
        EsGoodsSkuDTO esGoodsSkuDTO;
        if (userType) {
            esGoodsSkuDTO = skuList.stream().min(Comparator.comparing(EsGoodsSkuDTO::getEnterprisePrice)).get();
        } else {
            esGoodsSkuDTO = skuList.stream().min(Comparator.comparing(EsGoodsSkuDTO::getSalesPrice)).get();
        }
        return esGoodsSkuDTO;
    }

    /**
     * 根据距离计算配送时间，3KM内60分钟，超过3KM，每超过1KM加15分钟，超出部分，不足1KM按1KM算
     *
     * @param distance 单位：米
     * @return
     */
    public static double calMinuteByDistance(double distance) {
        double minute = 60;
        if (distance > 3000) {
            double distanceDiff = distance - 3000;
            minute = minute + new Double((distanceDiff / 1500) * 15) + new Double((distanceDiff % 1500) > 0 ? 15 : 0);
        }
        return minute;
    }
}
