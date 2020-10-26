package com.meiyuan.catering.wx;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.core.util.CharUtil;
import com.meiyuan.catering.marketing.service.user.CateringUserTicketService;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.index.WxSameLocationDTO;
import com.meiyuan.catering.wx.service.marketing.WxMarketingSeckillService;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yaoozu
 * @description TODO
 * @date 2020/3/2615:53
 * @since v1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSeckillService {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_INVENTORY, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache cache;
    @Autowired
    private WxMarketingSeckillService seckillService;
    @Autowired
    CateringUserTicketService ticketService;
    @Autowired
    private WechatUtils wechatUtils;
    //调整队列数 拒绝服务
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(200, 200, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000));

    @Test
    public void ticketTest(){
        WxSameLocationDTO code = wechatUtils.getSameLocationCityCode("1594259388896");
        System.out.println(code);


      /*  String str = "{\"merchantId\":1245980667993395201,\"number\":-1,\"seckillGoodsId\":1248488442183028738,\"userId\":1247728048472592386}\n";

        JSONObject object = JSON.parseObject(str);
        Long seckillGoodsId = object.getLong("seckillGoodsId");
        System.out.println(seckillGoodsId);*/


       // ticketService.insertTicket(1248150766305153026L,1248154881563299842L,2);
        /*Result<UserTicketDetailsDTO> userTicketInfo =
                ticketService.getUserTicketInfo(1248493850255757314L);
        String json = JSON.toJSONString(userTicketInfo);
        log.debug(json);
        JSONObject object = JSON.parseObject(json);
        log.debug("=="+object.getLong("userTicketId"));*/

    }


    @Test
    public void killOrder(){
        /*AtomicInteger count =  new AtomicInteger();
        AtomicInteger count1 =  new AtomicInteger();
        Long goodsId = 1247443089954443265L;
        log.info("开始秒杀一");
        for(int i=0;i<500;i++){
            final long userId = i;
            Runnable task = () -> {
                Result result = seckillService.executeSeckill(goodsId,userId,-1);
                log.info("用户:{}{}",userId,result);
                count.incrementAndGet();
                if(result.success()){
                    count1.incrementAndGet();
                }
            };
            executor.execute(task);
        }
        try {
            executor.awaitTermination(2,TimeUnit.MINUTES);
            System.out.println(count.get());
            System.out.println("success:"+count1.get());
            //Thread.sleep(600000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /** key:用户ID value:token值 */
    @CreateCache(name = JetcacheNames.USER_USERID_TOKEN, area = JetcacheAreas.USER_AREA)
    private Cache<Long,String> tokenCache;

    /** key:token值 value:用户登录信息 */
    @CreateCache(name = JetcacheNames.USER_TOKEN, area = JetcacheAreas.USER_AREA)
    private Cache<String, UserTokenDTO> idCache;

    @Test
    public void addUserToRedis() throws IOException {
        File file = new File("token.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        for(int i=0;i<1000;i++){
            String token = CharUtil.getRandomString(32);

            UserTokenDTO tokenDTO = new UserTokenDTO();
            tokenDTO.setUserId(i+1L);
            tokenDTO.setUserIdReal(i+1L);
            tokenDTO.setUserType(2);

            /*tokenCache.put(tokenDTO.getUserId(),token,60,TimeUnit.MINUTES);
            idCache.put(token,tokenDTO,60,TimeUnit.MINUTES);

            outputStream.write(token.getBytes(Charset.forName("UTF-8")));
            outputStream.write("\r\n".getBytes());*/
        }
    }
}
