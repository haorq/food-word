package com.meiyuan.catering.core.check;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.meiyuan.catering.core.enums.base.CheckTypeEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.util.Result;
import org.apache.commons.lang.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author MeiTao
 * @Description 文本/图片 类容审核service
 * @Date 2020/3/27 0027 23:24
 */
@Service
public class ContentCheckService {
    private static final Logger logger = LoggerFactory.getLogger(ContentCheckService.class);

    @Resource
    private AipContentCensor aipContentCensor;

    /**
     * 图片审查 -- 图片地址
     *
     * @param url 图片地址
     * @return
     */
    public Result checkImageByUrl(String url) {
        JSONObject jsonResult = aipContentCensor.imageCensorUserDefined(url, EImgType.URL, null);
        return getCheckResult(jsonResult, CheckTypeEnum.PHOTO.getStatus());
    }


    /**
     * 图片审查--图片
     *
     * @param file
     * @return
     */
    public Result checkImageByFile(MultipartFile file) {
        // 参数为本地图片文件二进制数组
        if (file == null || file.getSize() == 0) {
            throw new CustomException("请传入正确图片");
        }
        byte[] bytes = new byte[1000];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonResult = aipContentCensor.imageCensorUserDefined(bytes, null);
        //@TODO 百度图片校验不可用，暂时关闭
        //getCheckResult(jsonResult);
        return Result.succ();
    }

    /**
     * 文本内容审核
     *
     * @param text 待审核文本内容
     * @return
     */
    public Result<String> checkText(String text) {
        //敏感词审核
        JSONObject jsonObject = aipContentCensor.textCensorUserDefined(text);
        return getCheckResult(jsonObject, CheckTypeEnum.TEXT.getStatus());
    }

    /**
     * 图片/文字 审核结果 判断
     *
     * @param jsonObject
     * @param type       类型 ：true：文字，false：图片
     * @return
     */
    private Result<String> getCheckResult(JSONObject jsonObject, Integer type) {
        try {
            logger.debug("图片/文字 审核结果:{}", jsonObject);
            //查询结果
            Integer conclusionType = jsonObject.getInt(ContentCheckConstant.CONCLUSION_TYPE);

            //判断审核是否成功
            if (!ObjectUtils.equals(conclusionType, CheckResultEnum.COMPLIANCE.getStatus())) {
                if (ObjectUtils.equals(type, CheckTypeEnum.TEXT.getStatus())) {
                    throw new CustomException("内容不能包含敏感词汇");
                }
                if (ObjectUtils.equals(type, CheckTypeEnum.PHOTO.getStatus())) {
                    throw new CustomException("图片包含敏感内容");
                }
            }

            return Result.succ(jsonObject.getString(ContentCheckConstant.CONCLUSION));
        } catch (JSONException e) {
            logger.debug("JSONException:", e);
            return Result.succ();
        }
    }
}
