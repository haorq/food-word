package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.check.ContentCheckService;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dao.CateringOrdersAppraiseMapper;
import com.meiyuan.catering.order.dto.AppraiseReplyDto;
import com.meiyuan.catering.order.dto.query.admin.*;
import com.meiyuan.catering.order.dto.query.merchant.*;
import com.meiyuan.catering.order.dto.query.wx.AppraiseBrowseDTO;
import com.meiyuan.catering.order.dto.query.wx.AppraiseQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseParamDTO;
import com.meiyuan.catering.order.entity.CateringOrdersAppraiseEntity;
import com.meiyuan.catering.order.service.CateringOrdersAppraiseService;
import com.meiyuan.catering.order.service.CateringOrdersService;
import com.meiyuan.catering.order.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 订单评价表(CateringOrdersAppraise)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersAppraiseService")
public class CateringOrdersAppraiseServiceImpl extends ServiceImpl<CateringOrdersAppraiseMapper, CateringOrdersAppraiseEntity> implements CateringOrdersAppraiseService {
    @Resource
    private CateringOrdersAppraiseMapper cateringOrdersAppraiseMapper;
    @Resource
    private CateringOrdersService cateringOrdersService;
    @Autowired
    private ContentCheckService contentCheckService;

    /**
     * 功能描述: 后台——订单评价列表
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    @Override
    public IPage<OrdersAppraiseAdminDTO> appraiseListQuery(OrdersAppraiseParamAdminDTO param) {
        if (param.getStoreName() != null) {
            param.setStoreName(CharUtil.disposeChar(param.getStoreName()));
        }
        Page<OrdersAppraiseAdminDTO> page = new Page<>(param.getPageNo(), param.getPageSize());
        return this.cateringOrdersAppraiseMapper.appraiseListQuery(page, param);
    }

    /**
     * 功能描述: 后台——订单评价详情
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    @Override
    public OrdersAppraiseDetailAdminDTO appraiseDetailQuery(OrdersAppraiseDetailParamAdminDTO param) {
        OrdersAppraiseDetailAdminDTO ordersAppraiseDetailAdminDTO = new OrdersAppraiseDetailAdminDTO();
        // 查询当前门店的总体评价信息
        OrdersAppraiseAdminDTO ordersAppraiseAdminDTO = this.cateringOrdersAppraiseMapper.storeOrdersAppraise(param.getShopId());
        ordersAppraiseDetailAdminDTO.setOrdersAppraise(ordersAppraiseAdminDTO);
        // 查询当前门店的评价列表信息
        Page<OrdersAppraiseDetailListAdminDTO> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OrdersAppraiseDetailListAdminDTO> ordersAppraiseListPage = this.cateringOrdersAppraiseMapper.ordersAppraiseListQuery(page, param);
        PageData<OrdersAppraiseDetailListAdminDTO> pageData = new PageData<>(ordersAppraiseListPage.getRecords(), ordersAppraiseListPage.getTotal());
        ordersAppraiseDetailAdminDTO.setOrdersAppraiseList(pageData);
        return ordersAppraiseDetailAdminDTO;
    }

    /**
     * 功能描述: 商户——评价评分信息
     *
     * @param shopId 商户ID
     * @return com.meiyuan.catering.order.dto.query.merchant.OrdersAppraiseMerchantDTO
     * @author xie-xi-jie
     * @date 2020/5/19 14:03
     * @since v 1.1.0
     */
    @Override
    public OrdersAppraiseMerchantDTO appraisBaseQueryMerchant(Long shopId) {
        OrdersAppraiseMerchantDTO ordersAppraiseMerchantDTO = new OrdersAppraiseMerchantDTO();
        MerchantCountVO merchantCount = this.cateringOrdersService.getMerchantCount(shopId);
        if (merchantCount != null) {
            ordersAppraiseMerchantDTO.setTotalScore(BigDecimal.valueOf(merchantCount.getShopGrade() == null ? 0 : merchantCount.getShopGrade()));
            ordersAppraiseMerchantDTO.setTasteScore(BigDecimal.valueOf(merchantCount.getTasteGrade() == null ? 0 : merchantCount.getTasteGrade()));
            ordersAppraiseMerchantDTO.setPackScore(BigDecimal.valueOf(merchantCount.getPackGrade() == null ? 0 : merchantCount.getPackGrade()));
            ordersAppraiseMerchantDTO.setServiceScore(BigDecimal.valueOf(merchantCount.getServiceGrade() == null ? 0 : merchantCount.getServiceGrade()));
        }
        return ordersAppraiseMerchantDTO;
    }

    /**
     * 功能描述: 商户——订单评价列表
     *
     * @param param 请求参数
     * @return: 订单评价列表信息
     */
    @Override
    public IPage<OrdersAppraiseListMerchantDTO> appraiseListQueryMerchant(OrdersAppraiseParamMerchantDTO param) {
        Page<OrdersAppraiseListMerchantDTO> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<OrdersAppraiseListMerchantDTO> appraisePage = this.cateringOrdersAppraiseMapper.appraiseListQueryMerchant(page, param);
        List<OrdersAppraiseListMerchantDTO> records = appraisePage.getRecords();
        //判断是否存在图片集合
        if (BaseUtil.judgeList(records)) {
            records.forEach(record -> {
                if (BaseUtil.judgeString(record.getAppraisePicture())) {
                    String[] split = record.getAppraisePicture().split(",");
                    List<String> list = Arrays.asList(split);
                    record.setAppraisePictures(list);
                }
            });
        }
        return appraisePage;
    }

    @Override
    public String appraiseSet(AppraiseSetParamDTO param) {
        CateringOrdersAppraiseEntity cateringOrdersAppraiseEntity = this.cateringOrdersAppraiseMapper.selectById(param.getAppraiseId());
        if (cateringOrdersAppraiseEntity == null) {
            throw new CustomException("评论不存在");
        }
        cateringOrdersAppraiseEntity.setCanShow(param.getShow());
        cateringOrdersAppraiseEntity.setUpdateTime(LocalDateTime.now());
        int upRes = this.cateringOrdersAppraiseMapper.updateById(cateringOrdersAppraiseEntity);
        return BaseUtil.insertUpdateDelSetString(upRes, "操作成功", "操作失败");
    }

    @Override
    public AppraiseReplyDto appraiseReply(AppraiseReplyParamDTO param) {
        CateringOrdersAppraiseEntity cateringOrdersAppraiseEntity = this.cateringOrdersAppraiseMapper.selectById(param.getAppraiseId());
        if (cateringOrdersAppraiseEntity == null) {
            throw new CustomException("评论不存在");
        }

        if (!StringUtils.isEmpty(cateringOrdersAppraiseEntity.getReply())) {
            throw new CustomException("评论已回复，不可重复回复");
        }

        String reply = param.getReply();
        if (StringUtils.isEmpty(reply)) {
            throw new CustomException("评论回复内容不能为空");
        }
        Result result = this.contentCheckService.checkText(reply);
        if (result.failure()) {
            throw new CustomException("评论内容不能包含敏感词汇");
        }
        cateringOrdersAppraiseEntity.setReply(reply);
        LocalDateTime replyTime = LocalDateTime.now();
        cateringOrdersAppraiseEntity.setReplyTime(replyTime);
        int res = this.cateringOrdersAppraiseMapper.updateById(cateringOrdersAppraiseEntity);
        AppraiseReplyDto dto = new AppraiseReplyDto();
        dto.setAppraiseId(param.getAppraiseId());
        dto.setReply(reply);
        dto.setReplyTime(replyTime);
        return dto;
    }

    /**
     * @Description 微信——【我的评价列表】
     * @Date 2020/3/12 0012 15:38
     */
    @Override
    public IPage<MyAppraiseDTO> myAppraiseList(MyAppraiseParamDTO param) {
        return this.cateringOrdersAppraiseMapper.myAppraiseList(param.getPage(), param);
    }

    /**
     * @Description 微信——【我的评价详情】
     * @Date 2020/3/12 0012 15:38
     */
    @Override
    public MyAppraiseDTO myAppraiseDetail(Long appraiseId) {
        return this.cateringOrdersAppraiseMapper.myAppraiseDetail(appraiseId);
    }

    @Override
    public AppraiseStatisticsInfoVO statisticsInfo(Long shopId) {
        AppraiseGradeVO gradeVO = cateringOrdersAppraiseMapper.gradeInfo(shopId);
        AppraiseNumberVO numberVO = cateringOrdersAppraiseMapper.numberInfo(shopId);
        AppraiseStatisticsInfoVO statisticsInfoVO = new AppraiseStatisticsInfoVO();
        if (gradeVO != null && numberVO != null) {
            BeanUtils.copyProperties(gradeVO, statisticsInfoVO);
            BeanUtils.copyProperties(numberVO, statisticsInfoVO);
        }
        return statisticsInfoVO;
    }

    @Override
    public IPage<AppraiseListVO> pageList(AppraiseQueryDTO queryDTO) {
        IPage<AppraiseListVO> page = cateringOrdersAppraiseMapper.pageList(queryDTO.getPage(), queryDTO);
        return page;
    }

    @Override
    public String browse(AppraiseBrowseDTO param) {
        CateringOrdersAppraiseEntity appraiseEntity = this.getById(param);
        if (appraiseEntity == null) {
            throw new CustomException("评论不存在");
        }
        if (!appraiseEntity.getUserId().equals(param.getUserId())) {
            Integer browse;
            if (appraiseEntity.getBrowse() == null) {
                browse = 0;
            } else {
                browse = appraiseEntity.getBrowse();
            }
            browse += 1;
            appraiseEntity.setBrowse(browse);
            this.updateById(appraiseEntity);
        }
        return BaseUtil.insertUpdateDelBatchSetString(true, "记录成功", "记录失败");
    }

    @Override
    public PageData<OrderAppraiseListVO> queryPageAppraise(MyAppraiseParamDTO dto) {
        return new PageData<>(this.baseMapper.queryPageAppraise(dto.getPage(),dto));
    }

    @Override
    public CateringOrdersAppraiseEntity queryDetailById(Long id) {
        return this.baseMapper.selectById(id);
    }
}
