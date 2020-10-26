package com.meiyuan.catering.merchant;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yaoozu
 * @description 商户菜单服务测试类
 * @date 2020/3/2214:03
 * @since v1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantApiApplication.class)
public class TestMerchantMenuService {
 /*   @Autowired
    private MerchantGoodsMenuService goodsMenuService;

    @Test
    public void list(){
        Long id = new Long("1240539867542065154");
        GoodsMenuLimitQueryDTO query = new GoodsMenuLimitQueryDTO();
        query.setPageNo(1L);
        query.setPageSize(20L);
        Result<PageData<GoodsMenuDTO>> result =  goodsMenuService.listLimit(query,id);
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void detail(){
        Long id = new Long("1241286631125950466");
        System.out.println(JSON.toJSONString(goodsMenuService.menuInfoById(id)));
    }*/
}
