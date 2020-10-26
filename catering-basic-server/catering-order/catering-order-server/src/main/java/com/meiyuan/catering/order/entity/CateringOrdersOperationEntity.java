package com.meiyuan.catering.order.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * operation_(CateringOrdersOperation)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_operation")
public class CateringOrdersOperationEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 336385327724000434L;

    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
    /** 操作阶段（1：订单已提交；2：订单已完成；3：订单已取消，4：订单已退款，5：订单已关闭） */
    private Integer operationPhase;
    /** 操作时间 */
    private LocalDateTime operationTime;
    /** 操作人类型（1：客户；2-商家；3-系统） */
    private Integer operationType;
    /** 操作人ID */
    private Long operationId;
    /** 操作人姓名 */
    private String operationName;
    /** 操作人电话 */
    private String operationPhone;
    /** 操作说明 */
    private String operationExplain;

}
