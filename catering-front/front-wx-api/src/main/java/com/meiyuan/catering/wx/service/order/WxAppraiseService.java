package com.meiyuan.catering.wx.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.query.wx.AppraiseBrowseDTO;
import com.meiyuan.catering.order.dto.query.wx.AppraiseQueryDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseDTO;
import com.meiyuan.catering.order.dto.query.wx.MyAppraiseParamDTO;
import com.meiyuan.catering.order.feign.OrderAppraiseClient;
import com.meiyuan.catering.order.vo.AppraiseListVO;
import com.meiyuan.catering.order.vo.AppraiseStatisticsInfoVO;
import com.meiyuan.catering.order.vo.OrderAppraiseDetailVO;
import com.meiyuan.catering.order.vo.OrderAppraiseListVO;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author xie-xi-jie
 * @Description 微信我的评价
 * @Date 2020/3/12 0012 15:37
 */
@Service
public class WxAppraiseService {

    @Autowired
    private OrderAppraiseClient orderAppraiseClient;

    /**
     * @Description 微信——【我的评价列表】
     * @Date 2020/3/12 0012 15:38
     */
    public Result<PageData<MyAppraiseDTO>> myAppraiseList(MyAppraiseParamDTO param) {
        return this.orderAppraiseClient.myAppraiseList(param);
    }

    /**
     * @Description 微信——【我的评价详情】
     * @Date 2020/3/12 0012 15:38
     */
    public Result<MyAppraiseDTO> myAppraiseDetail(Long appraiseId) {
        return this.orderAppraiseClient.myAppraiseDetail(appraiseId);
    }

    /**
     * 微信-查询评价统计信息（包含评分和数量）
     *
     * @param shopId 门店ID
     * @return
     */
    public Result<AppraiseStatisticsInfoVO> statisticsInfo(Long shopId) {
        return this.orderAppraiseClient.statisticsInfo(shopId);
    }

    /**
     * 微信-分页查询评论列表
     *
     * @param queryDTO
     * @return
     */
    public Result<IPage<AppraiseListVO>> pageList(AppraiseQueryDTO queryDTO) {
        return this.orderAppraiseClient.pageList(queryDTO);
    }

    /**
     * @Description 微信——【评价浏览记录】
     * @Date 2020/3/12 0012 15:38
     */
    public Result browse(AppraiseBrowseDTO param) {
        return this.orderAppraiseClient.browse(param);
    }

    /**
     * describe: 查询我的评价列表
     * @author: yy
     * @date: 2020/8/5 17:57
     * @param dto
     * @return: {@link Result< PageData<  OrderAppraiseListVO >>}
     * @version 1.3.0
     **/
    public Result<PageData<OrderAppraiseListVO>> queryPageAppraise(MyAppraiseParamDTO dto) {
        return this.orderAppraiseClient.queryPageAppraise(dto);
    }

    /**
     * describe: 查询评价详情
     * @author: yy
     * @date: 2020/8/7 10:21
     * @param token
     * @param id
     * @return: {@link Result< OrderAppraiseDetailVO>}
     * @version 1.3.0
     **/
    public Result<OrderAppraiseDetailVO> queryDetailById(UserTokenDTO token, Long id){
        return this.orderAppraiseClient.queryDetailById(id);
    }
}
