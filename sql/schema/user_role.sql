CREATE TABLE `user_role` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `user_id` bigint unsigned NOT NULL COMMENT '用户id',
                             `role_id` int unsigned NOT NULL COMMENT '角色id',
                             `is_delete` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3

