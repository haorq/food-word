package com.meiyuan.catering.admin.fegin;


import com.meiyuan.catering.admin.service.CateringSubjectRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectRoleRelationClient {

    @Autowired
    private CateringSubjectRoleRelationService cateringSubjectRoleRelationService;

    public void verifyHasUserOfRole(Long roleId) {
        cateringSubjectRoleRelationService.verifyHasUserOfRole(roleId);
    }


}
