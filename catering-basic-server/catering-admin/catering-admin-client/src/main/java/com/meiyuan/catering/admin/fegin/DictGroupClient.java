package com.meiyuan.catering.admin.fegin;

import com.meiyuan.catering.admin.service.CateringDictGroupService;
import com.meiyuan.catering.admin.vo.admin.admin.DicAllVo;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.vo.base.DicDetailsAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lhm
 * @date 2020/5/19 9:59
 **/
@Service
public class DictGroupClient {
    @Autowired
    private CateringDictGroupService cateringDictGroupService;


    /**
     * @Author lhm
     * @Description 查询所有字典
     * @Date 2020/5/19
     * @Param []
     * @return {@link Result< List< DicAllVo>>}
     * @Version v1.1.0
     */
    public Result<List<DicAllVo>> findDicGroup() {
        return Result.succ(cateringDictGroupService.findDicGroup());
    }


   /**
    * @Author lhm
    * @Description 通过codes查询字典详情
    * @Date 2020/5/19
    * @Param [codes]
    * @return {@link Result< List< DicDetailsAllVo>>}
    * @Version v1.1.0
    */
    public Result<List<DicDetailsAllVo>> detail(List<String> codes) {
        return Result.succ(cateringDictGroupService.detail(codes));
    }
}
