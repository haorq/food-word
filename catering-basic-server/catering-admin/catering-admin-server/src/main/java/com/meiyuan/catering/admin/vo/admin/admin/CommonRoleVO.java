package com.meiyuan.catering.admin.vo.admin.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel("角色列表实体")
public class CommonRoleVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("是否默认角色，是否默认2：否1：是(默认不能删除）")
    private Integer defaultFlag;

    @ApiModelProperty("权限摘要0:商户后台1：app")
    private List<Integer> summery = new ArrayList<>(2);

    @ApiModelProperty("权限id集合")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> permissionIds;

    @ApiModelProperty(hidden = true)
    private Integer app;

    @ApiModelProperty(hidden = true)
    private Integer pc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Integer defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public List<Integer> getSummery() {
        return summery;
    }

    public void setSummery(List<Integer> summery) {
        this.summery = summery;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Integer getApp() {
        return app;
    }

    public void setApp(Integer app) {
        if (app == 1) {
            this.summery.add(1);
        }
        this.app = app;
    }

    public Integer getPc() {
        return pc;
    }

    public void setPc(Integer pc) {
        if (pc == 1) {
            this.summery.add(0);
        }
        this.pc = pc;
    }
}
