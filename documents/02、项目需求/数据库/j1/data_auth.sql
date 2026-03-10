

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_auth
-- ----------------------------
DROP TABLE IF EXISTS `func_perm`;
CREATE TABLE `func_perm`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `mid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属菜单id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能权限名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问地址',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `is_enable` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用 1表示启用 0表示禁用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '功能权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of func_perm
-- ----------------------------
INSERT INTO `func_perm` VALUES ('1', '035', '修改菜单', '/modify-menu', 1, '1', '11');
INSERT INTO `func_perm` VALUES ('2', '035', '添加菜单', '/add-menu', 2, '1', '22');


SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE role ADD COLUMN keyword VARCHAR(24) NOT NULL COMMENT '关键词';
