-- sql请加分号(;)

-- 平台广告
ALTER TABLE `catering_cloud`.`catering_advertising` ADD COLUMN `sort` int(3) not NULL DEFAULT 1 COMMENT '广告排序号' AFTER `is_del`;

-- 数据字典
update `catering_dict_group` set `name` = '用户来源' where id = 53 and `code` = 'pull_new_user_type';

-- 数据字典子项
update `catering_dict_group_item` set `name` = '发券宝',is_del = 0 WHERE id = 233 and group_id = 43;

update `catering_dict_group_item` set `name` = '自然流量' where id = 247 and group_id = 53 and `code` = 0;
update `catering_dict_group_item` set `name` = '地推' where id = 248 and group_id = 53 and `code` = 1;
update `catering_dict_group_item` set `name` = '被邀请' where id = 249 and group_id = 53 and `code` = 2;

INSERT INTO `catering_dict_group_item` VALUES (250, 45, '3', '手动领取', NULL, '2020-09-28 10:10:33', '2020-09-28 10:10:33', 0);
INSERT INTO  `catering_dict_group_item` (id,group_id,`code`,`name`) VALUES( 131, 9, 0, '0元支付');
INSERT INTO  `catering_dict_group_item` (id,group_id,`code`,`name`) VALUES( 132, 9, 3, '微信支付(通联)');

-- 平台活动
ALTER TABLE `catering_cloud`.`catering_marketing_activity`
MODIFY COLUMN `activity_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '活动类型 1:新用户注册 2:推荐有奖 3:首单奖励 4:评价赠礼 5:平台补贴 6:发券宝' AFTER `target`,
MODIFY COLUMN `release_conditions` tinyint(1) NULL DEFAULT 2 COMMENT '发放条件 1:立即 2:任务完成后自动发送 3:手动领取' AFTER `activity_type`;

-- 微信个人用户信息
ALTER TABLE `catering_cloud`.`catering_user`
MODIFY COLUMN `pull_new_user` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户来源：0:自然流量 1:地推 2:被邀请' AFTER `user_company_id`;

-- 处理微信用户来源
update catering_user set pull_new_user = 1 where ground_pusher_id is not null;
update catering_user set pull_new_user = 2 where id in (select DISTINCT referral_id from catering_recommend_record);

-- 门店员工
-- ----------------------------
-- Table structure for catering_shop_employee
-- ----------------------------
DROP TABLE IF EXISTS `catering_shop_employee`;
CREATE TABLE `catering_shop_employee`  (
                                         `id` bigint(64) NOT NULL COMMENT '主键id',
                                         `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `account_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `shop_id` bigint(64) DEFAULT NULL COMMENT '门店id',
                                         `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `sex` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                         `status` tinyint(1) DEFAULT NULL COMMENT '账号状态: 0:禁用；1:启用',
                                         `is_del` tinyint(1) DEFAULT 0 COMMENT '删除标志：0：正常，1：删除',
                                         `create_time` datetime(0) DEFAULT NULL,
                                         `update_time` datetime(0) DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店员工表' ROW_FORMAT = Dynamic;


-- 购物车
ALTER TABLE `catering_cloud`.`catering_cart`
ADD COLUMN `pack_price` decimal(10, 2) NULL DEFAULT '-1.00' COMMENT '包装费' AFTER `enterprise_price`;

-- 营销商品
ALTER TABLE `catering_cloud`.`catering_marketing_goods`
ADD COLUMN `goods_spec_type` tinyint(1) DEFAULT NULL COMMENT '规格类型 1-统一规格 2-多规格' AFTER `goods_synopsis`;
ALTER TABLE `catering_cloud`.`catering_marketing_goods`
ADD COLUMN `pack_price` decimal(10, 2) DEFAULT NULL COMMENT '打包费' AFTER `goods_spec_type`;
 -- 处理营销商品规格类型值
 UPDATE catering_marketing_goods AS cmg,
 ( SELECT goods_id, goods_spec_type FROM catering_merchant_goods_extend WHERE id IN ( SELECT max( id ) FROM catering_merchant_goods_extend GROUP BY goods_id ) ) AS cmge
 SET cmg.goods_spec_type = cmge.goods_spec_type
 WHERE
	cmg.goods_id = cmge.goods_id;

-- lh 达达配送
drop table if exists tb_orders_delivery_status_history;
CREATE TABLE `catering_orders_delivery_status_history` (
    `id` bigint(64) NOT NULL,
    `order_id` bigint(64) DEFAULT NULL COMMENT '业务系统订单ID',
    `client_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回达达运单号，默认为空',
    `order_status` int(11) DEFAULT NULL COMMENT '状态',
    `cancel_reason` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单取消原因,其他状态下默认值为空字符串',
    `cancel_from` int(11) DEFAULT NULL COMMENT '订单取消原因来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值)',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间,时间戳',
    `dm_id` int(11) DEFAULT NULL COMMENT '达达配送员id，接单以后会传',
    `dm_name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配送员姓名，接单以后会传',
    `dm_mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配送员手机号，接单以后会传',
    `signature` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '对client_id, order_id, update_time的值进行字符串升序排列，再连接字符串，取md5值',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='订单第三方配送状态记录表（目前是达达）';

-- lh 业务订单编号与第三方配送单编号关联表
drop table if exists catering_orders_delivery_no;
CREATE TABLE `catering_orders_delivery_no` (
    `id` bigint(64) unsigned NOT NULL,
    `order_id` bigint(64) DEFAULT NULL COMMENT '业务系统订单编号',
    `delivery_no` varchar(64) DEFAULT NULL COMMENT '第三方配送系统订单编号',
    `delivery_type` varchar(16) DEFAULT NULL COMMENT '第三方配送服务商',
    `delivery_type_remark` varchar(64) DEFAULT NULL COMMENT '第三方配送服务商描述',
    `create_time` datetime DEFAULT NULL,
    `distance` decimal(10,2) DEFAULT NULL COMMENT '距离（单位：米）',
    `fee` decimal(10,2) DEFAULT NULL COMMENT '实际运费(单位：元)，运费减去优惠券费用',
    `delivery_fee` decimal(10,2) DEFAULT NULL COMMENT '运费（单位：元）',
    `coupon_fee` decimal(10,2) DEFAULT NULL COMMENT '优惠券费用(单位：元)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单与第三方配送单号关联表';

-- lh
ALTER TABLE `catering_cloud`.`catering_orders_delivery_no`
    ADD COLUMN `deduct_fee` decimal(10,2) DEFAULT NULL COMMENT '商户取消订单，达达扣除违约金';

-- lh
drop table if exists catering_orders_delivery_cancel_record;
CREATE TABLE `catering_orders_delivery_cancel_record` (
    `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
    `order_id` bigint(64) DEFAULT NULL COMMENT '业务订单ID',
    `dada_order_id` bigint(64) DEFAULT NULL COMMENT '达达订单号，达达方非必传',
    `cancel_reason` varchar(256) DEFAULT NULL COMMENT '骑士取消原因',
    `deal_ret` tinyint(1) DEFAULT '0' COMMENT '商家处理结果。1:同意。2:不同意',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骑手主动取消订单，推送消息记录表';

-- 订单新增门店配送方式标识字段
ALTER TABLE `catering_cloud`.`catering_orders`
  ADD COLUMN `shop_delivery_flag` tinyint(1) DEFAULT '0' COMMENT '门店配送方式标识（0：自配送[默认]；1：达达配送）' AFTER `is_after_sales`;
-- gz菜单表数据

-- ----------------------------
-- Table structure for catering_common_permission
-- ----------------------------
DROP TABLE IF EXISTS `catering_common_permission`;
CREATE TABLE `catering_common_permission`  (
                                             `id` bigint(64) NOT NULL COMMENT '主键id',
                                             `menu_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
                                             `parent_id` bigint(64) DEFAULT NULL COMMENT '父菜单id',
                                             `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单链接',
                                             `type` tinyint(1) DEFAULT NULL COMMENT '菜单类型适用类型：0:商户pc，1：app，2：平台',
                                             `code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标志',
                                             `level` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单级别：page、button',
                                             `sort` int(5) NOT NULL DEFAULT 1 COMMENT '排序',
                                             `is_del` tinyint(1) DEFAULT 0 COMMENT '删除标记:0：正常,1：删除',
                                             `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for catering_common_role
-- ----------------------------
DROP TABLE IF EXISTS `catering_common_role`;
CREATE TABLE `catering_common_role` (
                                        `id` bigint(64) NOT NULL COMMENT '主键id',
                                        `subject_id` bigint(64) NOT NULL DEFAULT '-1' COMMENT '店铺id或平台id（平台用-1表示）',
                                        `role_name` varchar(64) DEFAULT NULL COMMENT '角色名称',
                                        `default_flag` tinyint(1) DEFAULT '2' COMMENT '是否默认2：否1：是(默认不能删除）',
                                        `remark` varchar(128) DEFAULT NULL COMMENT '角色描述',
                                        `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记：0：未删除，1：已删除',
                                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for catering_common_role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `catering_common_role_permission_relation`;
CREATE TABLE `catering_common_role_permission_relation` (
                                                            `id` bigint(64) NOT NULL COMMENT '主键id',
                                                            `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
                                                            `permission_id` bigint(64) DEFAULT NULL COMMENT '权限id',
                                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for catering_common_subject_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `catering_common_subject_role_relation`;
CREATE TABLE `catering_common_subject_role_relation` (
                                                         `id` bigint(64) NOT NULL COMMENT '主键id',
                                                         `subject_id` bigint(64) DEFAULT NULL COMMENT '员工id或adminb表管理员id',
                                                         `role_id` bigint(64) DEFAULT NULL COMMENT '角色id',
                                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of catering_common_permission
-- ----------------------------
INSERT INTO `catering_common_permission` VALUES (1310784072893837313, '员工', 0, '', 0, 'employers', 'page', 101, 0, '2020-09-29 11:30:52', '2020-10-12 16:16:06');
INSERT INTO `catering_common_permission` VALUES (1310784072956751873, '营销', 0, '', 0, 'activity', 'page', 102, 0, '2020-09-29 11:30:52', '2020-10-12 16:16:09');
INSERT INTO `catering_common_permission` VALUES (1310784072956751874, '报表', 0, '', 0, 'reports', 'page', 103, 0, '2020-09-29 11:30:52', '2020-10-12 16:16:15');
INSERT INTO `catering_common_permission` VALUES (1310786781667635201, '员工管理', 1310784072893837313, '', 0, 'employersManage', 'page', 104, 0, '2020-09-29 11:41:38', '2020-10-12 16:16:38');
INSERT INTO `catering_common_permission` VALUES (1310786781667635202, '门店活动', 1310784072956751873, '', 0, 'shopActivityManage', 'page', 105, 0, '2020-09-29 11:41:38', '2020-10-12 16:18:42');
INSERT INTO `catering_common_permission` VALUES (1310786781726355457, '营业概览', 1310784072956751874, '', 0, 'reportsOverview', 'page', 106, 0, '2020-09-29 11:41:38', '2020-10-12 16:19:56');
INSERT INTO `catering_common_permission` VALUES (1310786781734744066, '商品销售', 1310784072956751874, '', 0, 'merchandising', 'page', 107, 0, '2020-09-29 11:41:38', '2020-10-12 16:20:06');
INSERT INTO `catering_common_permission` VALUES (1310786781734744067, '订单流水', 1310784072956751874, '', 0, 'orderFlow', 'page', 108, 0, '2020-09-29 11:41:38', '2020-10-12 16:19:57');
INSERT INTO `catering_common_permission` VALUES (1310790597054017537, '员工列表', 1310786781667635201, '', 0, 'employers', 'page', 109, 0, '2020-09-29 11:56:47', '2020-10-12 16:17:28');
INSERT INTO `catering_common_permission` VALUES (1310790597054017538, '角色列表', 1310786781667635201, '', 0, 'emproles', 'page', 110, 0, '2020-09-29 11:56:47', '2020-10-12 16:17:30');
INSERT INTO `catering_common_permission` VALUES (1310790597054017539, '新建活动', 1315558135130451970, '', 0, 'addMyActivity', 'button', 111, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:17');
INSERT INTO `catering_common_permission` VALUES (1310790597054017540, '查询活动', 1315558135201755137, '', 0, 'checkMyActivity', 'button', 112, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:18');
INSERT INTO `catering_common_permission` VALUES (1310790597054017541, '编辑活动', 1315558135201755137, '', 0, 'editMyActivity', 'button', 113, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:19');
INSERT INTO `catering_common_permission` VALUES (1310790597054017542, '删除活动', 1315558135201755137, '', 0, 'deleteMyActivity', 'button', 114, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:20');
INSERT INTO `catering_common_permission` VALUES (1310790597054017543, '冻结活动', 1315558135201755137, '', 0, 'freezMyActivity', 'button', 115, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:21');
INSERT INTO `catering_common_permission` VALUES (1310790597054017544, '查看详情', 1315558135201755137, '', 0, 'infoMyActivity', 'button', 116, 0, '2020-09-29 11:56:47', '2020-10-12 16:20:31');
INSERT INTO `catering_common_permission` VALUES (1310790597054017545, '查询商品销售', 1310786781734744066, '', 0, 'checkMerchandising', 'button', 117, 1, '2020-09-29 11:56:47', '2020-10-12 16:20:34');
INSERT INTO `catering_common_permission` VALUES (1310790597054017546, '查询订单流水', 1310786781734744067, '', 0, 'checkOrderFlow', 'button', 118, 1, '2020-09-29 11:56:47', '2020-10-12 16:20:36');
INSERT INTO `catering_common_permission` VALUES (1310791871614922754, '新建账号', 1310790597054017537, '', 0, 'addEmployers', 'button', 119, 0, '2020-09-29 12:01:51', '2020-10-12 16:17:54');
INSERT INTO `catering_common_permission` VALUES (1310791871614922755, '编辑账号', 1310790597054017537, '', 0, 'editEmployers', 'button', 120, 0, '2020-09-29 12:01:51', '2020-10-12 16:17:54');
INSERT INTO `catering_common_permission` VALUES (1310791871614922756, '启用/禁用账号', 1310790597054017537, '', 0, 'opEmployers', 'button', 121, 0, '2020-09-29 12:01:51', '2020-10-12 16:17:55');
INSERT INTO `catering_common_permission` VALUES (1310791871614922757, '重置密码', 1310790597054017537, '', 0, 'resetEmployers', 'button', 122, 0, '2020-09-29 12:01:51', '2020-10-12 16:17:56');
INSERT INTO `catering_common_permission` VALUES (1310791871614922758, '查询账号', 1310790597054017537, '', 0, 'checkEmployers', 'button', 123, 0, '2020-09-29 12:01:51', '2020-10-12 16:17:59');
INSERT INTO `catering_common_permission` VALUES (1310791871614922759, '新建角色', 1310790597054017538, '', 0, 'addEmproles', 'button', 124, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:41');
INSERT INTO `catering_common_permission` VALUES (1310791871614922760, '编辑角色', 1310790597054017538, '', 0, 'editEmproles', 'button', 125, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:42');
INSERT INTO `catering_common_permission` VALUES (1310791871614922761, '设置权限', 1310790597054017538, '', 0, 'setEmproles', 'button', 126, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:43');
INSERT INTO `catering_common_permission` VALUES (1310791871614922762, '复制角色', 1310790597054017538, '', 0, 'copyEmployers', 'button', 127, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:44');
INSERT INTO `catering_common_permission` VALUES (1310791871614922763, '删除角色', 1310790597054017538, '', 0, 'deleEmproles', 'button', 128, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:46');
INSERT INTO `catering_common_permission` VALUES (1310791871614922764, '查询角色', 1310790597054017538, '', 0, 'checkEmproles', 'button', 129, 0, '2020-09-29 12:01:51', '2020-10-12 16:20:48');
INSERT INTO `catering_common_permission` VALUES (1310793025438593026, '商品管理', 0, '', 2, 'goodsManage', NULL, 201, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:22');
INSERT INTO `catering_common_permission` VALUES (1310793025438593027, '品牌管理', 0, '', 2, 'shopManage', NULL, 202, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:35');
INSERT INTO `catering_common_permission` VALUES (1310793025438593028, '促销活动', 0, '', 2, 'activityManage', NULL, 203, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:37');
INSERT INTO `catering_common_permission` VALUES (1310793025438593029, '推广管理', 0, '', 2, 'extensionManage', NULL, 204, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:39');
INSERT INTO `catering_common_permission` VALUES (1310793025438593030, '订单管理', 0, '', 2, 'orderManage', NULL, 205, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:41');
INSERT INTO `catering_common_permission` VALUES (1310793025438593031, '用户管理', 0, '', 2, 'userManagement', NULL, 206, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:42');
INSERT INTO `catering_common_permission` VALUES (1310793025438593032, '财务管理', 0, '', 2, 'financialManagement', NULL, 207, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:43');
INSERT INTO `catering_common_permission` VALUES (1310793025438593033, '报表管理', 0, '', 2, 'reportManage', NULL, 208, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:45');
INSERT INTO `catering_common_permission` VALUES (1310793025438593034, '系统管理', 0, '', 2, 'systemManage', NULL, 209, 0, '2020-09-29 12:06:26', '2020-09-29 14:07:46');
INSERT INTO `catering_common_permission` VALUES (1310799936993079297, '商品分类', 1310793025438593026, '', 2, 'goodsClassify', NULL, 210, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:49');
INSERT INTO `catering_common_permission` VALUES (1310799936993079298, '商品标签', 1310793025438593026, '', 2, 'goodsTags', NULL, 211, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:51');
INSERT INTO `catering_common_permission` VALUES (1310799936993079299, '商品库', 1310793025438593026, '', 2, 'goodsList', NULL, 212, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:53');
INSERT INTO `catering_common_permission` VALUES (1310799936993079300, '赠品列表', 1310793025438593026, '', 2, 'giftList', NULL, 213, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:54');
INSERT INTO `catering_common_permission` VALUES (1310799936993079301, '品牌标签', 1310793025438593027, '', 2, 'shopTag', NULL, 214, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:56');
INSERT INTO `catering_common_permission` VALUES (1310799936993079302, '品牌库', 1310793025438593027, '', 2, 'shoplist', NULL, 215, 0, '2020-09-29 12:33:54', '2020-09-29 14:07:58');
INSERT INTO `catering_common_permission` VALUES (1310799936993079303, '入驻商家审核', 1310793025438593027, '', 2, 'shopExamine', NULL, 216, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:00');
INSERT INTO `catering_common_permission` VALUES (1310799936993079304, '自提送赠品', 1310793025438593028, '', 2, 'gift', NULL, 217, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:02');
INSERT INTO `catering_common_permission` VALUES (1310799936993079305, '秒杀场次配置', 1310793025438593028, '', 2, 'seckillConfig', NULL, 218, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:04');
INSERT INTO `catering_common_permission` VALUES (1310799936993079306, '优惠券', 1310793025438593028, '', 2, 'coupon', NULL, 219, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:06');
INSERT INTO `catering_common_permission` VALUES (1310799936993079307, '活动', 1310793025438593028, '', 2, 'activityList', NULL, 220, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:09');
INSERT INTO `catering_common_permission` VALUES (1310799936993079308, '广告管理', 1310793025438593029, '', 2, 'ad', NULL, 221, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:10');
INSERT INTO `catering_common_permission` VALUES (1310799936993079309, '地推员管理', 1310793025438593029, '', 2, 'groupPusher', NULL, 222, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:12');
INSERT INTO `catering_common_permission` VALUES (1310799936993079310, '订单设置', 1310793025438593030, '', 2, 'ordersetting', NULL, 223, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:14');
INSERT INTO `catering_common_permission` VALUES (1310799936993079311, '订单列表', 1310793025438593030, '', 2, 'orderlist', NULL, 224, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:15');
INSERT INTO `catering_common_permission` VALUES (1310799936993079312, '退款列表', 1310793025438593030, '', 2, 'refundList', NULL, 225, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:21');
INSERT INTO `catering_common_permission` VALUES (1310799936993079313, '评价管理', 1310793025438593030, '', 2, 'evaluation', NULL, 226, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:23');
INSERT INTO `catering_common_permission` VALUES (1310799936993079314, '企业用户', 1310793025438593031, '', 2, 'company', NULL, 227, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:24');
INSERT INTO `catering_common_permission` VALUES (1310799936993079315, '个人用户', 1310793025438593031, '', 2, 'person', NULL, 228, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:25');
INSERT INTO `catering_common_permission` VALUES (1310799936993079316, '信息反馈', 1310793025438593031, '', 2, 'information', NULL, 229, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:27');
INSERT INTO `catering_common_permission` VALUES (1310799936993079317, '积分列表', 1310793025438593032, '', 2, 'financial/integral', NULL, 230, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:34');
INSERT INTO `catering_common_permission` VALUES (1310799936993079318, '对账报表', 1310793025438593033, '', 2, 'reportForm', NULL, 231, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:36');
INSERT INTO `catering_common_permission` VALUES (1310799936993079319, '营业概况', 1310793025438593033, '', 2, 'businessDetail', NULL, 232, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:37');
INSERT INTO `catering_common_permission` VALUES (1310799936993079320, '角色管理', 1310793025438593034, '', 2, 'roleManage', NULL, 233, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:39');
INSERT INTO `catering_common_permission` VALUES (1310799936993079321, '操作员列表', 1310793025438593034, '', 2, 'operator', NULL, 234, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:40');
INSERT INTO `catering_common_permission` VALUES (1310799936993079322, '公告管理', 1310793025438593034, '', 2, 'notice', NULL, 235, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:41');
INSERT INTO `catering_common_permission` VALUES (1310799936993079323, '小程序类目管理', 1310793025438593034, '', 2, 'applets', NULL, 236, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:43');
INSERT INTO `catering_common_permission` VALUES (1310799936993079324, '操作日志', 1310793025438593034, '', 2, 'log', NULL, 237, 0, '2020-09-29 12:33:54', '2020-09-29 14:08:44');
INSERT INTO `catering_common_permission` VALUES (1310805391756734466, '添加', 1310799936993079297, '', 2, 'goodsClassifyAdd', NULL, 238, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:47');
INSERT INTO `catering_common_permission` VALUES (1310805391756734467, '查看', 1310799936993079297, '', 2, 'goodsClassifyCheck', NULL, 239, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:48');
INSERT INTO `catering_common_permission` VALUES (1310805391756734468, '编辑', 1310799936993079297, '', 2, 'goodsClassifyEdit', NULL, 240, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:52');
INSERT INTO `catering_common_permission` VALUES (1310805391756734469, '添加', 1310799936993079298, '', 2, 'goodsTagsAdd', NULL, 241, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:54');
INSERT INTO `catering_common_permission` VALUES (1310805391756734470, '查看', 1310799936993079298, '', 2, 'goodsTagsCheck', NULL, 242, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:56');
INSERT INTO `catering_common_permission` VALUES (1310805391756734471, '编辑', 1310799936993079298, '', 2, 'goodsTagsEdit', NULL, 243, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:57');
INSERT INTO `catering_common_permission` VALUES (1310805391756734472, '删除', 1310799936993079298, '', 2, 'goodsTagsDelete', NULL, 244, 0, '2020-09-29 12:55:35', '2020-09-29 14:08:59');
INSERT INTO `catering_common_permission` VALUES (1310805391756734473, '添加', 1310799936993079299, '', 2, 'goodsListAdd', NULL, 245, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:01');
INSERT INTO `catering_common_permission` VALUES (1310805391756734474, '查看', 1310799936993079299, '', 2, 'goodsListCheck', NULL, 246, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:03');
INSERT INTO `catering_common_permission` VALUES (1310805391756734475, '编辑', 1310799936993079299, '', 2, 'goodsListEdit', NULL, 247, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:04');
INSERT INTO `catering_common_permission` VALUES (1310805391756734476, '删除', 1310799936993079299, '', 2, 'goodsListDelete', NULL, 248, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:06');
INSERT INTO `catering_common_permission` VALUES (1310805391756734477, '添加', 1310799936993079300, '', 2, 'giftListAdd', NULL, 249, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:07');
INSERT INTO `catering_common_permission` VALUES (1310805391756734478, '编辑', 1310799936993079300, '', 2, 'giftListEdit', NULL, 250, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:09');
INSERT INTO `catering_common_permission` VALUES (1310805391756734479, '删除', 1310799936993079300, '', 2, 'giftListDelete', NULL, 250, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:11');
INSERT INTO `catering_common_permission` VALUES (1310805391756734480, '添加', 1310799936993079301, '', 2, 'shopTagAdd', NULL, 252, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:13');
INSERT INTO `catering_common_permission` VALUES (1310805391756734481, '查看', 1310799936993079301, '', 2, 'shopTagCheck', NULL, 253, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:14');
INSERT INTO `catering_common_permission` VALUES (1310805391756734482, '编辑', 1310799936993079301, '', 2, 'shopTagEdit', NULL, 254, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:17');
INSERT INTO `catering_common_permission` VALUES (1310805391756734483, '删除', 1310799936993079301, '', 2, 'shopTagDelete', NULL, 255, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:19');
INSERT INTO `catering_common_permission` VALUES (1310805391756734484, '添加', 1310799936993079302, '', 2, 'shoplistAdd', NULL, 256, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:20');
INSERT INTO `catering_common_permission` VALUES (1310805391756734485, '编辑', 1310799936993079302, '', 2, 'shoplistEdit', NULL, 257, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:21');
INSERT INTO `catering_common_permission` VALUES (1310805391756734486, '商品授权', 1310799936993079302, '', 2, 'shopTagAuth', NULL, 258, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:23');
INSERT INTO `catering_common_permission` VALUES (1310805391756734487, '启用', 1310799936993079302, '', 2, 'shopTagStart', NULL, 259, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:24');
INSERT INTO `catering_common_permission` VALUES (1310805391756734488, '禁用', 1310799936993079302, '', 2, 'shopTagDisable', NULL, 260, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:26');
INSERT INTO `catering_common_permission` VALUES (1310805391756734489, '处理', 1310799936993079303, '', 2, 'shopExamineHandle', NULL, 261, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:28');
INSERT INTO `catering_common_permission` VALUES (1310805391756734490, '查看', 1310799936993079303, '', 2, 'shopExamineCheck', NULL, 262, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:31');
INSERT INTO `catering_common_permission` VALUES (1310805391756734491, '添加', 1310799936993079304, '', 2, 'giftAdd', NULL, 263, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:33');
INSERT INTO `catering_common_permission` VALUES (1310805391756734492, '查看', 1310799936993079304, '', 2, 'giftCheck', NULL, 264, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:34');
INSERT INTO `catering_common_permission` VALUES (1310805391756734493, '添加', 1310799936993079305, '', 2, 'seckillConfigAdd', NULL, 265, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:36');
INSERT INTO `catering_common_permission` VALUES (1310805391756734494, '编辑', 1310799936993079305, '', 2, 'seckillConfigEdit', NULL, 266, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:38');
INSERT INTO `catering_common_permission` VALUES (1310805391756734495, '删除', 1310799936993079305, '', 2, 'seckillConfigDelete', NULL, 267, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:40');
INSERT INTO `catering_common_permission` VALUES (1310805391756734496, '添加', 1310799936993079306, '', 2, 'couponAdd', NULL, 268, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:41');
INSERT INTO `catering_common_permission` VALUES (1310805391756734497, '查看', 1310799936993079306, '', 2, 'couponCheck', NULL, 269, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:42');
INSERT INTO `catering_common_permission` VALUES (1310805391756734498, '编辑', 1310799936993079306, '', 2, 'couponEdit', NULL, 270, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:44');
INSERT INTO `catering_common_permission` VALUES (1310805391756734499, '删除', 1310799936993079306, '', 2, 'couponDelete', NULL, 271, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:52');
INSERT INTO `catering_common_permission` VALUES (1310805391756734500, '添加', 1310799936993079307, '', 2, 'activityListAdd', NULL, 272, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:53');
INSERT INTO `catering_common_permission` VALUES (1310805391756734501, '编辑', 1310799936993079307, '', 2, 'activityListEdit', NULL, 273, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:55');
INSERT INTO `catering_common_permission` VALUES (1310805391756734502, '删除', 1310799936993079307, '', 2, 'activityListDelete', NULL, 274, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:56');
INSERT INTO `catering_common_permission` VALUES (1310805391756734503, '复制', 1310799936993079307, '', 2, 'activityListCopy', NULL, 275, 0, '2020-09-29 12:55:35', '2020-09-29 14:09:59');
INSERT INTO `catering_common_permission` VALUES (1310805391756734504, '添加', 1310799936993079308, '', 2, 'adAdd', NULL, 276, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:00');
INSERT INTO `catering_common_permission` VALUES (1310805391756734505, '查看', 1310799936993079308, '', 2, 'adCheck', NULL, 277, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:02');
INSERT INTO `catering_common_permission` VALUES (1310805391756734506, '编辑', 1310799936993079308, '', 2, 'adEdit', NULL, 278, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:04');
INSERT INTO `catering_common_permission` VALUES (1310805391756734507, '删除', 1310799936993079308, '', 2, 'adDelete', NULL, 279, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:07');
INSERT INTO `catering_common_permission` VALUES (1310805391756734508, '添加', 1310799936993079309, '', 2, 'groupPusherAdd', NULL, 280, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:10');
INSERT INTO `catering_common_permission` VALUES (1310805391756734509, '编辑', 1310799936993079309, '', 2, 'groupPusherEdit', NULL, 281, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:13');
INSERT INTO `catering_common_permission` VALUES (1310805391756734510, '查看', 1310799936993079311, '', 2, 'orderlistCheck', NULL, 282, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:15');
INSERT INTO `catering_common_permission` VALUES (1310805391756734511, '导出备餐表', 1310799936993079311, '', 2, 'orderlistExportMeals', NULL, 283, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:17');
INSERT INTO `catering_common_permission` VALUES (1310805391756734512, '导出订单表', 1310799936993079311, '', 2, 'orderlistExportOrder', NULL, 284, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:19');
INSERT INTO `catering_common_permission` VALUES (1310805391756734513, '查看', 1310799936993079312, '', 2, 'refundListCheck', NULL, 285, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:20');
INSERT INTO `catering_common_permission` VALUES (1310805391756734514, '查看', 1310799936993079313, '', 2, 'evaluationCheck', NULL, 286, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:21');
INSERT INTO `catering_common_permission` VALUES (1310805391756734515, '添加', 1310799936993079314, '', 2, 'companyAdd', NULL, 287, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:22');
INSERT INTO `catering_common_permission` VALUES (1310805391756734516, '查看', 1310799936993079314, '', 2, 'companyCheck', NULL, 288, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:23');
INSERT INTO `catering_common_permission` VALUES (1310805391756734517, '编辑', 1310799936993079314, '', 2, 'companyEdit', NULL, 289, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:25');
INSERT INTO `catering_common_permission` VALUES (1310805391756734518, '查看', 1310799936993079315, '', 2, 'personCheck', NULL, 290, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:26');
INSERT INTO `catering_common_permission` VALUES (1310805391895146497, '查看', 1310799936993079316, '', 2, 'informationCheck', NULL, 291, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:28');
INSERT INTO `catering_common_permission` VALUES (1310805391895146498, '明细', 1310799936993079318, '', 2, 'reportFormCheck', NULL, 292, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:31');
INSERT INTO `catering_common_permission` VALUES (1310805391895146499, '导出', 1310799936993079318, '', 2, 'reportFormExport', NULL, 293, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:32');
INSERT INTO `catering_common_permission` VALUES (1310805391895146500, '添加', 1310799936993079320, '', 2, 'roleManageAdd', NULL, 294, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:34');
INSERT INTO `catering_common_permission` VALUES (1310805391895146501, '授权', 1310799936993079320, '', 2, 'roleManageAuth', NULL, 295, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:35');
INSERT INTO `catering_common_permission` VALUES (1310805391895146502, '编辑', 1310799936993079320, '', 2, 'roleManageEdit', NULL, 296, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:36');
INSERT INTO `catering_common_permission` VALUES (1310805391895146503, '删除', 1310799936993079320, '', 2, 'roleManageDelete', NULL, 297, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:38');
INSERT INTO `catering_common_permission` VALUES (1310805391895146504, '添加', 1310799936993079321, '', 2, 'operatorAdd', NULL, 298, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:39');
INSERT INTO `catering_common_permission` VALUES (1310805391895146505, '编辑', 1310799936993079321, '', 2, 'operatorEdit', NULL, 299, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:40');
INSERT INTO `catering_common_permission` VALUES (1310805391895146506, '启用', 1310799936993079321, '', 2, 'operatorStart', NULL, 300, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:47');
INSERT INTO `catering_common_permission` VALUES (1310805391895146507, '禁用', 1310799936993079321, '', 2, 'operatorDisabled', NULL, 301, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:49');
INSERT INTO `catering_common_permission` VALUES (1310805391895146508, '删除', 1310799936993079321, '', 2, 'operatorDelete', NULL, 302, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:51');
INSERT INTO `catering_common_permission` VALUES (1310805391895146509, '添加', 1310799936993079322, '', 2, 'noticeAdd', NULL, 303, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:53');
INSERT INTO `catering_common_permission` VALUES (1310805391895146510, '查看', 1310799936993079322, '', 2, 'noticeCheck', NULL, 304, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:55');
INSERT INTO `catering_common_permission` VALUES (1310805391895146511, '置顶', 1310799936993079322, '', 2, 'noticeTop', NULL, 305, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:56');
INSERT INTO `catering_common_permission` VALUES (1310805391895146512, '删除', 1310799936993079322, '', 2, 'noticeDelete', NULL, 306, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:57');
INSERT INTO `catering_common_permission` VALUES (1310805391895146513, '添加', 1310799936993079323, '', 2, 'appletsAdd', NULL, 307, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:58');
INSERT INTO `catering_common_permission` VALUES (1310805391895146514, '编辑', 1310799936993079323, '', 2, 'appletsEdit', NULL, 308, 0, '2020-09-29 12:55:35', '2020-09-29 14:10:59');
INSERT INTO `catering_common_permission` VALUES (1310805391895146515, '启用', 1310799936993079323, '', 2, 'appletsStart', NULL, 309, 0, '2020-09-29 12:55:35', '2020-09-29 14:11:04');
INSERT INTO `catering_common_permission` VALUES (1310805391920312321, '禁用', 1310799936993079323, '', 2, 'appletsDisabled', NULL, 310, 0, '2020-09-29 12:55:35', '2020-09-29 14:11:06');
INSERT INTO `catering_common_permission` VALUES (1310805391920312322, '删除', 1310799936993079323, '', 2, 'appletsDelete', NULL, 311, 0, '2020-09-29 12:55:35', '2020-09-29 14:11:07');
INSERT INTO `catering_common_permission` VALUES (1310806283918749697, '订单处理', 0, '', 1, 'dealOrder', 'page', 401, 0, '2020-09-29 12:59:07', '2020-10-12 16:24:32');
INSERT INTO `catering_common_permission` VALUES (1310806283918749698, '经营管理', 0, '', 1, 'ManagementManage', 'page', 402, 0, '2020-09-29 12:59:07', '2020-10-12 16:24:33');
INSERT INTO `catering_common_permission` VALUES (1310806283918749699, '门店设置', 0, '', 1, 'shopSetting', 'page', 403, 0, '2020-09-29 12:59:07', '2020-10-12 16:24:41');
INSERT INTO `catering_common_permission` VALUES (1310806494527336450, '查看预订单', 1310806283918749697, '', 1, 'checkPreorder', 'button', 404, 0, '2020-09-29 12:59:57', '2020-10-12 16:24:43');
INSERT INTO `catering_common_permission` VALUES (1310806494527336451, '核销订单', 1310806283918749697, '', 1, 'sureOrder', 'button', 405, 0, '2020-09-29 12:59:57', '2020-10-12 16:24:45');
INSERT INTO `catering_common_permission` VALUES (1310806494527336452, '取消订单', 1310806283918749697, '', 1, 'cancleOrder', 'button', 406, 0, '2020-09-29 12:59:57', '2020-10-12 16:24:46');
INSERT INTO `catering_common_permission` VALUES (1310806494527336453, '处理退款单', 1310806283918749697, '', 1, 'dealRefund', 'button', 407, 0, '2020-09-29 12:59:57', '2020-10-12 16:24:54');
INSERT INTO `catering_common_permission` VALUES (1310806884807323650, '查看营业情况及待核销商品', 1310806283918749698, '', 1, 'checkManageGoods', 'page', 408, 0, '2020-09-29 13:01:31', '2020-10-12 16:25:05');
INSERT INTO `catering_common_permission` VALUES (1310806884807323651, '订单查询', 1310806283918749698, '', 1, 'queryOrder', 'page', 409, 0, '2020-09-29 13:01:31', '2020-10-12 16:25:07');
INSERT INTO `catering_common_permission` VALUES (1310806884807323652, '商品管理', 1310806283918749698, '', 1, 'goodsManage', 'page', 410, 0, '2020-09-29 13:01:31', '2020-10-12 16:25:14');
INSERT INTO `catering_common_permission` VALUES (1310806884807323653, '评价管理', 1310806283918749698, '', 1, 'evaluateManage', 'page', 411, 0, '2020-09-29 13:01:31', '2020-10-12 16:25:15');
INSERT INTO `catering_common_permission` VALUES (1310806884870238210, '营销管理', 1310806283918749698, '', 1, 'saleManage', 'page', 412, 0, '2020-09-29 13:01:31', '2020-10-12 16:25:17');
INSERT INTO `catering_common_permission` VALUES (1310806884870238211, '员工管理', 1310806283918749698, '', 1, 'employeeManage', 'page', 413, 1, '2020-09-29 13:01:31', '2020-10-12 16:25:19');
INSERT INTO `catering_common_permission` VALUES (1310807122121043969, '门店信息', 1310806283918749699, '', 1, 'shopInfo', 'page', 414, 0, '2020-09-29 13:02:27', '2020-10-12 16:25:21');
INSERT INTO `catering_common_permission` VALUES (1310807122129432577, '业务配置', 1310806283918749699, '', 1, 'businessSetting', 'page', 415, 0, '2020-09-29 13:02:27', '2020-10-12 16:25:25');
INSERT INTO `catering_common_permission` VALUES (1310807122129432578, '自提点管理', 1310806283918749699, '', 1, 'selfpickupManage', 'page', 416, 0, '2020-09-29 13:02:27', '2020-10-12 16:25:27');
INSERT INTO `catering_common_permission` VALUES (1310807122129432579, '打印设置', 1310806283918749699, '', 1, 'printSetting', 'page', 417, 0, '2020-09-29 13:02:27', '2020-10-12 16:25:35');
INSERT INTO `catering_common_permission` VALUES (1310807611957030914, '新建商品', 1310806884807323652, '', 1, 'addGoods', 'button', 418, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:37');
INSERT INTO `catering_common_permission` VALUES (1310807611957030915, '编辑商品', 1310806884807323652, '', 1, 'editGoods', 'button', 419, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:38');
INSERT INTO `catering_common_permission` VALUES (1310807611957030916, '上/下架商品', 1310806884807323652, '', 1, 'upGoods', 'button', 420, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:40');
INSERT INTO `catering_common_permission` VALUES (1310807611957030917, '调整库存', 1310806884807323652, '', 1, 'changeInventory', 'button', 421, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:42');
INSERT INTO `catering_common_permission` VALUES (1310807611957030918, '删除商品', 1310806884807323652, '', 1, 'delGoods', 'button', 422, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:44');
INSERT INTO `catering_common_permission` VALUES (1310807611957030919, '商品排序', 1310806884807323652, '', 1, 'sortGoods', 'button', 423, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:46');
INSERT INTO `catering_common_permission` VALUES (1310807611957030920, '新建分类', 1310806884807323652, '', 1, 'addCategory', 'button', 424, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:49');
INSERT INTO `catering_common_permission` VALUES (1310807611957030921, '编辑分类', 1310806884807323652, '', 1, 'editCategory', 'button', 425, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:51');
INSERT INTO `catering_common_permission` VALUES (1310807611957030922, '删除分类', 1310806884807323652, '', 1, 'delCategroy', 'button', 426, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:53');
INSERT INTO `catering_common_permission` VALUES (1310807611957030923, '分类排序', 1310806884807323652, '', 1, 'sortCategory', 'button', 427, 0, '2020-09-29 13:04:24', '2020-10-12 16:25:56');
INSERT INTO `catering_common_permission` VALUES (1310807735760302082, '隐藏评价', 1310806884807323653, '', 1, 'hideEvaluate', 'button', 428, 0, '2020-09-29 13:04:53', '2020-10-12 16:25:57');
INSERT INTO `catering_common_permission` VALUES (1310807735760302083, '回复评价', 1310806884807323653, '', 1, 'replyEvaluate', 'button', 429, 0, '2020-09-29 13:04:53', '2020-10-12 16:25:59');
INSERT INTO `catering_common_permission` VALUES (1310807879553626113, '冻结活动', 1310806884870238210, '', 1, 'frozenActivity', 'button', 430, 0, '2020-09-29 13:05:28', '2020-10-12 16:26:03');
INSERT INTO `catering_common_permission` VALUES (1310808085183574018, '新建员工账号', 1310806884870238211, '', 1, 'addEmployee', 'button', 431, 1, '2020-09-29 13:06:17', '2020-10-12 16:26:05');
INSERT INTO `catering_common_permission` VALUES (1310808085183574019, '编辑员工账号', 1310806884870238211, '', 1, 'editEmployee', 'button', 432, 1, '2020-09-29 13:06:17', '2020-10-12 16:26:07');
INSERT INTO `catering_common_permission` VALUES (1310808085183574020, '删除员工账号', 1310806884870238211, '', 1, 'delEmployee', 'button', 433, 1, '2020-09-29 13:06:17', '2020-10-12 16:26:09');
INSERT INTO `catering_common_permission` VALUES (1310808407494864897, '上传logo', 1310807122121043969, '', 1, 'uploadLogo', 'button', 434, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:10');
INSERT INTO `catering_common_permission` VALUES (1310808407511642114, '编辑门店公告', 1310807122121043969, '', 1, 'editShopNotice', 'button', 435, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:11');
INSERT INTO `catering_common_permission` VALUES (1310808407511642115, '上传门店头图', 1310807122121043969, '', 1, 'uploadShopHeadIcon', 'button', 436, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:14');
INSERT INTO `catering_common_permission` VALUES (1310808407511642116, '编辑门店电话', 1310807122121043969, '', 1, 'editShopPhone', 'button', 437, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:16');
INSERT INTO `catering_common_permission` VALUES (1310808407520030721, '编辑门店地址', 1310807122121043969, '', 1, 'editShopAddress', 'button', 438, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:17');
INSERT INTO `catering_common_permission` VALUES (1310808407520030722, '上传门店照片', 1310807122121043969, '', 1, 'uploadShopPicture', 'button', 439, 0, '2020-09-29 13:07:34', '2020-10-12 16:26:21');
INSERT INTO `catering_common_permission` VALUES (1311201520503623681, '查询', 1310799936993079297, '', 2, 'goodsClassifySearch', NULL, 1, 0, '2020-09-30 15:09:39', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201520570732545, '查询', 1310799936993079298, '', 2, 'goodsTagsSearch', NULL, 1, 0, '2020-09-30 15:09:39', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201520570732546, '查询', 1310799936993079299, '', 2, 'goodsListSearch', NULL, 1, 0, '2020-09-30 15:09:39', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201520570732547, '查询', 1310799936993079300, '', 2, 'giftListSearch', NULL, 1, 0, '2020-09-30 15:09:39', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201943285272578, '查询', 1310799936993079301, '', 2, 'shopTagSearch', NULL, 1, 0, '2020-09-30 15:11:20', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201943285272579, '查询', 1310799936993079302, '', 2, 'shoplistSearch', NULL, 1, 0, '2020-09-30 15:11:20', NULL);
INSERT INTO `catering_common_permission` VALUES (1311201943285272580, '查询', 1310799936993079303, '', 2, 'shopExamineSearch', NULL, 1, 0, '2020-09-30 15:11:20', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202227302567938, '查询', 1310799936993079304, '', 2, 'giftSearch', NULL, 1, 0, '2020-09-30 15:12:28', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202227302567939, '查询', 1310799936993079306, '', 2, 'couponSearch', NULL, 1, 0, '2020-09-30 15:12:28', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202227302567940, '查询', 1310799936993079307, '', 2, 'activityListSearch', NULL, 1, 0, '2020-09-30 15:12:28', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202908428177410, '查询', 1310799936993079308, '', 2, 'extensionManageSearch', NULL, 1, 0, '2020-09-30 15:15:10', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202908428177411, '查询', 1310799936993079309, '', 2, 'groupPusherSearch', NULL, 1, 0, '2020-09-30 15:15:10', '2020-10-09 09:39:43');
INSERT INTO `catering_common_permission` VALUES (1311202908428177412, '查询', 1310799936993079311, '', 2, 'orderlistSearch', NULL, 1, 0, '2020-09-30 15:15:10', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202908428177413, '查询', 1310799936993079312, '', 2, 'refundListSearch', NULL, 1, 0, '2020-09-30 15:15:10', NULL);
INSERT INTO `catering_common_permission` VALUES (1311202908428177414, '查询', 1310799936993079313, '', 2, 'evaluationSearch', NULL, 1, 0, '2020-09-30 15:15:10', NULL);
INSERT INTO `catering_common_permission` VALUES (1311203475095425026, '查询', 1310799936993079314, '', 2, 'companySearch', NULL, 1, 0, '2020-09-30 15:17:25', NULL);
INSERT INTO `catering_common_permission` VALUES (1311203475103813633, '查询', 1310799936993079315, '', 2, 'personSearch', NULL, 1, 0, '2020-09-30 15:17:25', NULL);
INSERT INTO `catering_common_permission` VALUES (1311203475112202241, '查询', 1310799936993079316, '', 2, 'informationSearch', NULL, 1, 0, '2020-09-30 15:17:25', NULL);
INSERT INTO `catering_common_permission` VALUES (1311203475112202242, '查询', 1310799936993079317, '', 2, 'financial/integralSearch', NULL, 1, 0, '2020-09-30 15:17:25', NULL);
INSERT INTO `catering_common_permission` VALUES (1311203475112202243, '查询', 1310799936993079318, '', 2, 'reportFormSearch', NULL, 1, 0, '2020-09-30 15:17:25', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065443713026, '查询', 1310799936993079319, '', 2, 'businessDetailSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065443713027, '查询', 1310799936993079320, '', 2, 'roleManageSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065452101634, '查询', 1310799936993079321, '', 2, 'operatorSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065452101635, '查询', 1310799936993079322, '', 2, 'noticeSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065452101636, '查询', 1310799936993079323, '', 2, 'appletsSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311204065452101637, '查询', 1310799936993079324, '', 2, 'logSearch', NULL, 1, 0, '2020-09-30 15:19:46', NULL);
INSERT INTO `catering_common_permission` VALUES (1311239330883395585, '查看', 1310799936993079307, '', 2, 'activityListCheck', NULL, 276, 0, '2020-09-30 17:39:54', '2020-09-30 17:45:40');
INSERT INTO `catering_common_permission` VALUES (1314385957922803713, '分享', 1310799936993079307, '', 2, 'activityListShare', NULL, 1, 0, '2020-10-09 10:03:27', NULL);
INSERT INTO `catering_common_permission` VALUES (1314386014629793793, '冻结', 1310799936993079307, '', 2, 'activityListFreeze', NULL, 1, 0, '2020-10-09 10:03:40', NULL);
INSERT INTO `catering_common_permission` VALUES (1315558135130451970, '活动中心', 1310786781667635202, '', 0, 'shopActivity', 'page', 1, 0, '2020-10-12 15:41:16', '2020-10-12 16:18:58');
INSERT INTO `catering_common_permission` VALUES (1315558135201755137, '我的活动', 1310786781667635202, '', 0, 'myActivity', 'page', 10, 0, '2020-10-12 15:41:16', '2020-10-12 16:30:25');

-- 初始化shop角色
INSERT into catering_common_role(id,subject_id,role_name,default_flag,remark,is_del)
    (
        SELECT
            id,
            id AS subject_id,
            '店员-默认' AS role_name,
            '1' AS default_flag,
            '000' AS remark,
            '0' AS is_del
        FROM
            catering_shop
        WHERE
                is_del = 0
          AND shop_type != 3
    );
-- 初始化商户默认角色权限数据
INSERT INTO catering_common_role_permission_relation
SELECT
                                                       (
                                                         SELECT
                                                           CONCAT(
                                                               (
                                                                 SELECT
                                                                   SUBSTRING((SELECT t1.id), 1, 15)
                                                               ),
                                                               (
                                                                 SELECT
                                                                   CEILING(RAND() * 9000 + RAND() * 1000)
                                                               )
                                                             )
                                                       ) AS id,
                                                       t1.id AS role_id,
                                                       t2.permission_id
FROM
  catering_common_role t1
    CROSS JOIN (
    SELECT
      1310806494527336450 AS permission_id
    UNION
    SELECT
      1310806494527336451 AS permission_id
    UNION
    SELECT
      1310806494527336452 AS permission_id
    UNION
    SELECT
      1310806494527336453 AS permission_id
  ) t2
WHERE
  t1.subject_id !=- 1;

-- web平台初始角色以及数据关联
INSERT into catering_common_role VALUES (1310843060929695745,-1,'管理员',1,'初始化管理员，含平台所有功能权限',0,now(),now());
INSERT into catering_common_subject_role_relation (SELECT id,id as subject_id,'1310843060929695745' FROM catering_admin WHERE is_del = 0);
INSERT into catering_common_role_permission_relation (SELECT id,'1310843060929695745',id as permission_id FROM catering_common_permission WHERE type = 2 and is_del = 0);


-- ----------------------------
-- Table structure for catering_h5_record
-- ----------------------------
DROP TABLE IF EXISTS `catering_h5_record`;
CREATE TABLE `catering_h5_record`  (
                                     `id` bigint(64) NOT NULL,
                                     `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户手机号',
                                     `activity_id` bigint(64) NOT NULL COMMENT '活动ID',
                                     `ticket_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券ids',
                                     `is_get` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否领取：0-未领取；1-已领取',
                                     `is_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识：0-正常；1-删除',
                                     `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



-- 通联账款分账
drop table if exists `catering_orders_split_bill`;
create table `catering_orders_split_bill`
(
   `id`                   bigint(64) not null comment '主键ID',
   `trading_flow`         varchar(64) comment '系统交易编号',
   `order_id`             bigint(64) comment '订单ID',
   `order_number`         varchar(64) comment '订单编号',
   `pay_user`             varchar(64) comment '付款人',
   `split_amount`         decimal(10,2) comment '分账金额',
   `order_amount`         decimal(10,2) comment '订单金额',
   `subsidy_amount`       decimal(10,2) default 0 comment '补贴金额',
   `trade_code`           varchar(10) comment '业务码',
   `trade_status`         tinyint(1) default 0 comment '交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败；4：待交易）',
   `fail_message`         varchar(255) comment '失败原因',
   `success_time`         datetime comment '成功时间',
   `request_message`      text comment '请求参数信息',
   `is_del`               tinyint(1) default 0 comment '是否删除（0：未删除[默认]；1：已删除）',
   `create_time`          datetime default CURRENT_TIMESTAMP comment '创建时间',
   `create_by`            bigint(64) comment '创建人',
   `update_time`          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   `update_by`            bigint(64) comment '更新人',
   primary key (`id`)
);
alter table catering_orders_split_bill comment '分账表';

drop table if exists `catering_orders_split_bill_order_flow`;
create table `catering_orders_split_bill_order_flow`
(
   `id`                   bigint(64) not null comment '主键ID',
   `split_bill_id`        bigint(64) comment '分账流水表ID',
   `trading_number`       varchar(64) comment '系统交易流水编号',
   `third_trade_number`   varchar(64) comment '第三方交易流水编号',
   `collection_user`      varchar(64) comment '收款人',
   `order_split_amount`   decimal(10,2) comment '订单分账金额',
   `fee_rate`             decimal(10,2) default 0 comment '手续费率（单位：%）',
   `fee`                  decimal(10,2) default 0 comment '手续费',
   `type`                 tinyint(1) comment '分账类型（1：商家可分订单金额；2：订单配送费；3：商家负债分账）',
   `split_type`           tinyint(1) comment '类型（1：分账；2：内扣）',
   `split_rule`           varchar(255) comment '分账规则',
   `trade_status`         tinyint(1) default 0 comment '交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败；4：待交易）',
   `fail_message`         varchar(255) comment '失败原因',
   `success_time`         datetime comment '成功时间',
   `notify_message`       text comment '通知参数信息',
   `is_del`               tinyint(1) default 0 comment '是否删除（0：未删除[默认]；1：已删除）',
   `create_time`          datetime default CURRENT_TIMESTAMP comment '创建时间',
   `create_by`            bigint(64) comment '创建人',
   `update_time`          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   `update_by`            bigint(64) comment '更新人',
   primary key (`id`)
);
alter table catering_orders_split_bill_order_flow comment '订单分账流水表';

drop table if exists `catering_orders_split_bill_subsidy_flow`;
create table `catering_orders_split_bill_subsidy_flow`
(
   `id`                   bigint(64) not null comment '主键ID',
   `split_bill_id`        bigint(64) comment '分账流水表ID',
   `trading_number`       varchar(64) comment '系统交易流水编号',
   `third_trade_number`   varchar(64) comment '第三方交易流水编号',
   `collection_user`      varchar(64) comment '收款人',
   `order_subsidy_amount` decimal(10,2) comment '订单补贴金额',
   `type`                 tinyint(1) comment '补贴类型（1：优惠券；2：配送）',
   `trade_status`         tinyint(1) default 0 comment '交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败；4：待交易；5：交易关闭）',
   `fail_message`         varchar(255) comment '失败原因，关闭原因',
   `success_time`         datetime comment '成功时间',
   `remarks`              varchar(100) comment '备注',
   `request_message`      text comment '请求参数信息',
   `is_del`               tinyint(1) default 0 comment '是否删除（0：未删除[默认]；1：已删除）',
   `create_time`          datetime default CURRENT_TIMESTAMP comment '创建时间',
   `create_by`            bigint(64) comment '创建人',
   `update_time`          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   `update_by`            bigint(64) comment '更新人',
   primary key (`id`)
);
alter table catering_orders_split_bill_subsidy_flow comment '补贴分账流水表';

-- 通联账款分账提现
drop table if exists `catering_orders_split_bill_withdraw_flow`;
create table `catering_orders_split_bill_withdraw_flow`
(
   `id`                   bigint(64) not null comment '主键ID',
   `trading_number`       varchar(64) comment '系统交易流水编号',
   `third_trade_number`   varchar(64) comment '第三方交易流水编号',
   `collection_user`      varchar(64) comment '收款人',
   `collection_account`   varchar(30) comment '收款账号',
   `collection_account_pro` bigint(1) comment '收款账号属性: 0-个人银行卡、1-企业对公账户',
   `withdraw_amount`      decimal(10,2) comment '提现金额',
   `fee_rate`             decimal(10,2) default 0 comment '手续费率（单位：%）',
   `fee`                  decimal(10,2) default 0 comment '手续费',
   `type`                 varchar(15) comment '提现方式（D0:D+0到账  D1:D+1到账  T1customized:T+1到账，仅工作日代付  D0customized:D+0到账，根据平台资金头寸付款）',
   `trade_status`         tinyint(1) default 0 comment '交易状态（0：未交易；1：交易中；2：交易成功；3：交易失败）',
   `fail_message`         varchar(255) comment '失败原因',
   `success_time`         datetime comment '成功时间',
   `request_message`      text comment '请求参数信息',
   `notify_message`       text comment '通知参数信息',
   `is_del`               tinyint(1) default 0 comment '是否删除（0：未删除[默认]；1：已删除）',
   `create_time`          datetime default CURRENT_TIMESTAMP comment '创建时间',
   `create_by`            bigint(64) comment '创建人',
   `update_time`          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   `update_by`            bigint(64) comment '更新人',
   primary key (`id`)
);
alter table catering_orders_split_bill_withdraw_flow comment '分账提现流水表';

-- 商家负债表
drop table if exists catering_orders_shop_debt;
create table catering_orders_shop_debt
(
   id                   bigint(64) not null comment '主键ID',
   shop_id              bigint(64) comment '商户ID',
   amount               decimal(10,2) comment '负债总金额',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id)
);
alter table catering_orders_shop_debt comment '商家负债表';

-- 商家负债明细表
drop table if exists catering_orders_shop_debt_flow;
create table catering_orders_shop_debt_flow
(
   id                   bigint(64) not null comment '主键ID',
   shop_debt_id         bigint(64) comment '负债表ID',
   shop_id              bigint(64) comment '商户ID',
   order_id             bigint(64) comment '订单ID',
   debt_amount          decimal(10,2) comment '负债金额',
   subsidy_amount       decimal(10,2) comment '平台补贴金额',
   amount               decimal(10,2) comment '商家应付/已付金额',
   debt_type            tinyint(1) comment '负债类型（1：配送费）',
   amount_type          tinyint(1) comment '款项类型（1：应付；2：已付）',
   remarks              varchar(100) comment '备注',
   is_del               tinyint(1) default 0 comment '是否删除（0：否；1：是）',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id)
);
alter table catering_orders_shop_debt_flow comment '商家负债明细表';


-- 商品
ALTER TABLE `catering_cloud`.`catering_merchant_goods_sku`
  MODIFY COLUMN `pack_price` decimal(10, 2) NULL DEFAULT -1 COMMENT '打包费' AFTER `enterprise_price`;


ALTER TABLE `catering_cloud`.`catering_shop_goods_sku`
  MODIFY COLUMN `pack_price` decimal(10, 2) NULL DEFAULT -1 COMMENT '打包费' AFTER `takeout_price`;

update catering_merchant_goods_sku set pack_price =-1;
update catering_shop_goods_sku set pack_price =-1;

-- 订单配送
ALTER TABLE `catering_orders_delivery`
ADD COLUMN `immediate_delivery_time`  datetime DEFAULT NULL COMMENT '立即送达时间' AFTER `estimate_end_time`;
-- 修改评价表注释
ALTER TABLE `catering_orders_appraise`
MODIFY COLUMN `service`  tinyint(2) NULL DEFAULT NULL COMMENT '配送评分' AFTER `taste`;



ALTER TABLE `catering_cloud`.`catering_user_ticket`
  MODIFY COLUMN `ticket_activity_id` bigint(64) DEFAULT NULL COMMENT '优惠券活动id,为null表示为地推扫码发券' AFTER `ticket_id`,
  MODIFY COLUMN `ticket_rule_record_id` bigint(64) DEFAULT NULL COMMENT '平台活动优惠券规则记录表id，为null表示为地推扫码发券或品牌发券' AFTER `ticket_activity_id`;



-- 店铺扩展信息
CREATE TABLE `catering_shop_ext` (
  `id` bigint(64) NOT NULL,
  `shop_id` bigint(64) DEFAULT NULL COMMENT '门店id',
  `contract_no` varchar(150) DEFAULT NULL COMMENT '与通联绑定合同号',
  `sign_status` int(1) DEFAULT NULL COMMENT '门店与通联签约状态： 1：签约，2：未签约',
  `audit_status` int(1) DEFAULT NULL COMMENT '店铺与通联绑定状态:1-实名认证、2-电子签约、3-绑定银行卡、4-完成、5、手机号绑定完成',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户通联注册真实姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '用户通联注册真实身份证号',
  `bank_card_pro` bigint(2) DEFAULT NULL COMMENT '通联会员/银行卡属性: 0-个人银行卡、1-企业对公账户',
  `tl_phone` varchar(100) DEFAULT NULL COMMENT '与通联绑定预留手机号',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(100) DEFAULT NULL COMMENT '开户支行',
  `bank_card_number` varchar(100) DEFAULT NULL COMMENT '开户行卡号',
  `business_license` varchar(150) DEFAULT NULL COMMENT '营业执照',
  `food_business_license` varchar(150) DEFAULT NULL COMMENT '食品经营许可证',
  `id_card_positive` varchar(150) DEFAULT NULL COMMENT '身份证正面',
  `id_card_back` varchar(150) DEFAULT NULL COMMENT '身份证反面',
  `id_card_withhand` varchar(255) DEFAULT NULL COMMENT '手持身份证',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '是否删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺扩展表';

-- 店铺通联支付绑定银行卡
CREATE TABLE `catering_shop_bank` (
  `id` bigint(64) NOT NULL,
  `shop_id` bigint(64) DEFAULT NULL COMMENT '店铺id',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行名称  ',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `id_card` varchar(150) DEFAULT NULL COMMENT '用户真实身份证号',
  `phone` varchar(50) DEFAULT NULL COMMENT '银行预留手机号码',
  `bank_card_no` varchar(150) DEFAULT NULL COMMENT '银行卡号',
  `bank_card_pro` bigint(1) DEFAULT NULL COMMENT '银行卡/账户属性: 0-个人银行卡、1-企业对公账户',
  `bank_city_no` varchar(1) DEFAULT NULL COMMENT '开户行地区代码:根据中国地区代码',
  `branch_bank_name` varchar(255) DEFAULT NULL COMMENT '开户行支行名称',
  `union_bank` varchar(100) DEFAULT NULL COMMENT '支付行号',
  `audit_status` int(1) DEFAULT NULL COMMENT '签约状态：1-实名认证、2-电子签约、3-绑定银行卡',
  `is_del` tinyint(1) DEFAULT NULL COMMENT '是否删除：true : 是、false ：否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺通联绑定银行卡';

-- 打印配置相关字段添加
ALTER TABLE `catering_cloud`.`catering_shop_printing_config`
ADD COLUMN `device_key` varchar(64) NULL COMMENT '设备密钥' AFTER `device_number`,
MODIFY COLUMN `printing_times` int(2) NULL DEFAULT NULL COMMENT '外卖单每次打印份数' AFTER `device_number`,
ADD COLUMN `cook_times` integer(1) NULL COMMENT '厨打单每次打印份数' AFTER `printing_times`;

ALTER TABLE `catering_cloud`.`catering_shop_printing_config`
ADD COLUMN `device_name` varchar(50) NULL COMMENT '打印机名称（默认为打印机设备号）' AFTER `shop_id`,
MODIFY COLUMN `device_key` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备密钥' AFTER `device_number`,
ADD COLUMN `device_status` integer(1) NULL COMMENT '设备状态：1:在线，2:缺纸，0：离线' AFTER `device_key`;

-- 店铺是否营业默认值处理
UPDATE catering_shop SET business_status = 1;
ALTER TABLE `catering_cloud`.`catering_shop`
MODIFY COLUMN `address_detail` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址' AFTER `address_area`,
MODIFY COLUMN `address_full` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '完整地址' AFTER `address_detail`,
ADD COLUMN `delivery_type` integer(1) NULL COMMENT '配送类型： 1-自配送、2-达达配送' AFTER `change_good_price`;


-- 商户入驻申请表 -- herui
CREATE TABLE `catering_shop_apply` (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `apply_user_id` bigint(64) DEFAULT NULL COMMENT '申请人用户ID',
  `shop_name` varchar(32) DEFAULT NULL COMMENT '店铺名称',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '申请时的手机号码',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `id_card_positive` varchar(255) DEFAULT NULL COMMENT '身份证正面',
  `id_card_back` varchar(255) DEFAULT NULL COMMENT '身份证反面',
  `id_card_withhand` varchar(255) DEFAULT NULL COMMENT '手持身份证',
  `business_license` varchar(100) DEFAULT NULL COMMENT '营业执照',
  `food_business_license` varchar(100) DEFAULT NULL COMMENT '食品经营许可证',
  `address_province_code` varchar(32) DEFAULT NULL COMMENT '省',
  `address_city_code` varchar(32) DEFAULT NULL COMMENT '市',
  `address_area_code` varchar(32) DEFAULT NULL COMMENT '区',
  `address_province` varchar(32) DEFAULT NULL COMMENT '地址省',
  `address_city` varchar(32) DEFAULT NULL COMMENT '地址市',
  `address_area` varchar(32) DEFAULT NULL COMMENT '地址区',
  `address_detail` varchar(400) DEFAULT NULL COMMENT '详细地址',
  `address_full` varchar(500) DEFAULT NULL COMMENT '完整地址',
  `map_coordinate` varchar(64) DEFAULT NULL COMMENT '经纬度',
  `door_number` varchar(50) DEFAULT NULL COMMENT '门牌号',
  `bank_name` varchar(50) DEFAULT NULL COMMENT '开户银行',
  `bank_branch` varchar(50) DEFAULT NULL COMMENT '开户支行',
  `bank_card_number` varchar(32) DEFAULT NULL COMMENT '开户银行卡号',
  `handled` tinyint(1) DEFAULT NULL COMMENT '是否处理(1:待处理,2:已处理)',
  `status` tinyint(1) DEFAULT NULL COMMENT '处理状态(1:审核通过,2:审核不通过)',
  `reasons_for_failure` varchar(255) DEFAULT NULL COMMENT '审核不通过原因',
  `update_by` bigint(64) DEFAULT NULL COMMENT '审核人ID',
  `shop_id` bigint(64) DEFAULT NULL COMMENT '店铺ID',
  `merchant_id` bigint(64) DEFAULT NULL COMMENT '品牌ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序商铺申请入驻表';

ALTER TABLE `catering_cloud`.`catering_ticket_data_record`
  ADD COLUMN `discount_before_fee` decimal(18, 4) COMMENT '订单优惠前金额' AFTER `order_amount`;
ALTER TABLE `catering_cloud`.`catering_ticket_data_record`
  ADD COLUMN `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `create_time`;
UPDATE  catering_ticket_data_record set update_time = create_time;
UPDATE catering_ticket_data_record t1 JOIN catering_orders t2 on t1.order_id = t2.id
set t1.discount_before_fee = t2.discount_before_fee;

-- 广告
ALTER TABLE `catering_cloud`.`catering_advertising`
MODIFY COLUMN `link_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '跳转类型 1:内部地址 2:自定义地址 0:无' AFTER `goods_id`;

-- 广告字典
INSERT INTO `catering_dict_group` VALUES (54, '广告跳转类型', 'advertising_link_type', '2020-10-16 09:15:10', '2020-10-16 09:15:10', 0);
INSERT INTO `catering_dict_group_item` VALUES (251, 54, '0', '无', NULL, '2020-10-16 09:16:03', '2020-10-16 09:16:03', 0);
INSERT INTO `catering_dict_group_item` VALUES (252, 54, '1', '跳转至内部地址', NULL, '2020-10-16 09:16:44', '2020-10-16 09:16:44', 0);
INSERT INTO `catering_dict_group_item` VALUES (253, 54, '2', '自定义跳转页内容', NULL, '2020-10-16 09:17:13', '2020-10-16 09:17:13', 0);

-- catering_orders_transaction_flow 表添加order_id 索引
CREATE INDEX order_id_index ON catering_orders_transaction_flow ( order_id );

-- 通联异常退款表
CREATE TABLE `catering_allin_refund_order` (
  `id` bigint(64) NOT NULL COMMENT '主键ID',
  `order_id` bigint(64) DEFAULT NULL COMMENT '订单ID',
  `trading_flow` varchar(64) DEFAULT NULL COMMENT '系统交易流水编号',
  `third_trade_number` varchar(64) DEFAULT NULL COMMENT '第三方交易流水编号',
  `refund_no` varchar(64) DEFAULT NULL COMMENT '退款单号',
  `store_id` bigint(64) DEFAULT NULL COMMENT '门店ID',
  `member_id` bigint(64) DEFAULT NULL COMMENT '下单客户ID',
  `amount` bigint(10) DEFAULT NULL COMMENT '退款金额(分)',
  `source` tinyint(1) DEFAULT NULL COMMENT '退款来源',
  `status` tinyint(1) DEFAULT '1' COMMENT '确认退款状态(1:待确认  2.退款成功  3.退款失败)',
  `remarks` varchar(64) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通联异常退款表';

