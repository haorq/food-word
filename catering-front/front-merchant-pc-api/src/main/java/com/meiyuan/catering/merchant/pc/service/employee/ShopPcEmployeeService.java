package com.meiyuan.catering.merchant.pc.service.employee;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.meiyuan.catering.admin.dto.role.RoleRelationDTO;
import com.meiyuan.catering.admin.fegin.RoleClient;
import com.meiyuan.catering.admin.fegin.RoleRelationClient;
import com.meiyuan.catering.core.enums.base.TokenEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.PageUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.core.util.token.TokenUtil;
import com.meiyuan.catering.merchant.dto.auth.MerchantAccountDTO;
import com.meiyuan.catering.merchant.dto.merchant.AppJwtTokenDTO;
import com.meiyuan.catering.merchant.dto.merchant.MerchantTokenDTO;
import com.meiyuan.catering.merchant.dto.shop.ShopEmployeeDTO;
import com.meiyuan.catering.merchant.dto.shop.config.EmployeeQueryPageDTO;
import com.meiyuan.catering.merchant.entity.CateringShopEmployeeEntity;
import com.meiyuan.catering.merchant.feign.ShopClient;
import com.meiyuan.catering.merchant.pc.api.employee.ShopPcEmployeeController;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fql
 */
@Service
public class ShopPcEmployeeService {

    private static Logger logger = LoggerFactory.getLogger(ShopPcEmployeeService.class);

    @Resource
    private ShopClient shopClient;
    @Resource
    private RoleRelationClient roleRelationClient;
    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private RoleClient roleClient;


    public Result<PageData<ShopEmployeeDTO>> queryPageShopEmployee(EmployeeQueryPageDTO dto){
        PageData<ShopEmployeeDTO> pageShopEmployee = shopClient.queryPageShopEmployee(dto);
        List<ShopEmployeeDTO> shopEmployee = pageShopEmployee.getList();
        List<RoleRelationDTO> roleRelation = roleRelationClient.selectRoleRelationByShopId(dto.getShopId());
        Map<Long, List<RoleRelationDTO>> roleGroup = roleRelation.stream().collect(Collectors.groupingBy(RoleRelationDTO::getEmployeeId));
        shopEmployee.forEach(item ->{
            if(roleGroup.containsKey(item.getId())){
                List<String> roleNameList = roleGroup.get(item.getId()).stream().map(RoleRelationDTO::getRoleName).collect(Collectors.toList());
                List<String> roleIdList = roleGroup.get(item.getId()).stream().map(RoleRelationDTO::getRoleId).map(Object::toString).collect(Collectors.toList());
                String roleStr = String.join(",",roleNameList);
                item.setRoleId(String.join(",",roleIdList));
                item.setRoleName(roleStr);
            }
        });
        if(Objects.nonNull(dto.getRoleId())){
            shopEmployee = shopEmployee.stream().filter(item -> item.getRoleId().contains(dto.getRoleId().toString())).collect(Collectors.toList());
        }
        PageData<ShopEmployeeDTO> pageData = new PageData<>();
        pageData.setTotal(pageShopEmployee.getTotal());
        pageData.setList(shopEmployee);
        pageData.setLastPage(PageUtil.lastPages(pageData.getTotal(), dto.getPageSize().intValue(), dto.getPageNo().intValue()));
        return Result.succ(pageData);
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveUpdateEmployeeInfo(ShopEmployeeDTO shopEmployeeDTO) {
        //更新员工信息
        Long employeeId = shopClient.saveUpdateEmployeeInfo(shopEmployeeDTO);
        if(StringUtils.isNotEmpty(shopEmployeeDTO.getRoleId())){
            List<String> roleList = Arrays.asList(shopEmployeeDTO.getRoleId().split(","));
            List<Long> roleIds = roleList.stream().map(Long::valueOf).collect(Collectors.toList());
            Integer roleSize = roleClient.selectByIds(shopEmployeeDTO.getShopId(), roleIds);
            if(roleSize.compareTo(roleIds.size()) != 0){
                throw new CustomException("角色发生变更，请重新选择");
            }
        }
        //更新角色信息
        roleRelationClient.updateEmployeeRole(employeeId,shopEmployeeDTO.getRoleId(),false);
        return "操作成功";
    }

    @Transactional(rollbackFor = Exception.class)
    public String delEmployeeById(Long id) {
        String merchantPcToken = merchantUtils.getMerchantPcToken(id.toString());
        MerchantTokenDTO merchantAppToken = merchantUtils.getMerchantByToken(id.toString());
        if(StringUtils.isEmpty(merchantPcToken) && Objects.isNull(merchantAppToken)){
            //删除角色
            roleRelationClient.updateEmployeeRole(id, null, true);
            return shopClient.delEmployeeById(id);
        }

        MerchantAccountDTO accountDto = null;
        try {
            if(StringUtils.isNotEmpty(merchantPcToken)){
                accountDto = TokenUtil.getFromToken(merchantPcToken, TokenEnum.MERCHANT, MerchantAccountDTO.class);

            }
            logger.info("token data is :",accountDto);
        } catch (Exception e) {
            logger.error("员工删除失败：", e);
        }
        if (Objects.nonNull(accountDto) && accountDto.getLogBackType().compareTo(1)==0) {
            throw new CustomException("员工已登录，无法删除");
        }
        if (Objects.nonNull(merchantAppToken) && merchantAppToken.getLogBackType().compareTo(1)==0) {
            throw new CustomException("员工已登录，无法删除");
        }
        return "员工删除失败";

    }

    public String delAllEmployee(Long shopId) {
        List<CateringShopEmployeeEntity> allEmployee = shopClient.selectShopEmployee(shopId);
        //删除角色关系
        if(CollectionUtils.isNotEmpty(allEmployee)){
            List<Long> ids = allEmployee.stream().map(CateringShopEmployeeEntity::getId).collect(Collectors.toList());
            roleRelationClient.delAllEmployeeRole(ids);
        }
        //删除员工
        return shopClient.delAllEmployee(shopId);
    }
}
