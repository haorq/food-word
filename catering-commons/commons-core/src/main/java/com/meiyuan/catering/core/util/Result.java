package com.meiyuan.catering.core.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应操作结果
 *
 * <pre>
 *  {
 *      errno： 错误码，
 *      errmsg：错误消息，
 *      data：  响应数据
 *  }
 * </pre>
 *
 * <p>
 * 错误码：
 * <ul>
 * <li>0，成功；
 * <li>4xx，前端错误，说明前端开发者需要重新了解后端接口使用规范：
 * <ul>
 * <li>401，参数错误，即前端没有传递后端需要的参数；
 * <li>402，参数值错误，即前端传递的参数值不符合后端接收范围。
 * </ul>
 * <li>5xx，后端错误，除501外，说明后端开发者应该继续优化代码，尽量避免返回后端错误码：
 * <ul>
 * <li>501，验证失败，即后端要求用户登录；
 * <li>502，系统内部错误，即没有合适命名的后端内部错误；
 * <li>503，业务不支持，即后端虽然定义了接口，但是还没有实现功能；
 * <li>504，更新数据失效，即后端采用了乐观锁更新，而并发更新时存在数据更新失效；
 * <li>505，更新数据失败，即后端数据库更新失败（正常情况应该更新成功）。
 * </ul>
 * <li>6xx，小商城后端业务错误码， 具体见Dts-admin-api模块的AdminResponseCode。
 * <li>7xx，管理后台后端业务错误码， 具体见Dts-wx-api模块的WxResponseCode。
 * </ul>
 */
@Data
@SuppressWarnings("all")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    private int code = 0;
    /**
     * 消息内容
     */
    private String msg = "成功";
    /**
     * 响应数据
     */
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success() {
        return code == 0;
    }

    public boolean failure() {
        return code != 0;
    }


    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg) {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = msg;
        return this;
    }

    public Result<T> error() {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.msg = "失败";
        return this;
    }

    /**
     * 成功返回
     *
     * @param <T> 返回类型
     * @return Result
     */
    public static <T> Result<T> succ() {
        return new Result<T>().ok(null);
    }

    /**
     * 成功返回
     *
     * @param <T>  返回类型
     * @param data 返回结果
     * @return Result
     */
    public static <T> Result<T> succ(T data) {
        return new Result<T>().ok(data);
    }


    /**
     * 失败返回
     *
     * @param <T> 返回类型
     * @param msg 失败错误提示
     * @return Result
     */
    public static <T> Result<T> fail() {
        return new Result<T>().error();
    }

    /**
     * 失败返回
     *
     * @param <T> 返回类型
     * @param msg 失败错误提示
     * @return Result
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<T>().error(msg);
    }

    /**
     * 失败返回
     *
     * @param <T>  返回类型
     * @param code 失败错误码
     * @param msg  失败错误提示
     * @return Result
     */
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<T>().error(code, msg);
    }


    public static Result badArgument() {
        return fail(402, "参数不对");
    }

    public static Result badArgumentValue() {
        return fail(402, "参数值不对");
    }

    public static Result unlogin() {
        return fail(401, "请登录");
    }

    public static Result serious() {
        return fail("系统内部错误");
    }
    public static Result systemError() {
        return fail("服务器开小差啦");
    }

    public static Result unsupport() {
        return fail(503, "业务不支持");
    }

    public static Result updatedDateExpired() {
        return fail(504, "更新数据已经失效");
    }

    public static Result updatedDataFailed() {
        return fail(505, "更新数据失败");
    }

    public static Result unauthz() {
        return fail(506, "无操作权限");
    }

    public static Result maxUploadSize() {
        return fail("上传文件大小限制1M");
    }

    public static Result deprecated() {
        return fail("接口已弃用");
    }

    public static Result logicResult(Boolean logic) {
        return logic ? succ() : fail();
    }

    public static Result logicResult(Boolean logic, String failStr) {
        return logic ? succ() : fail(failStr);
    }

}
