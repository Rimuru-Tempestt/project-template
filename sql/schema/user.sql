CREATE TABLE `user` (
                        `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `username` varchar(15) NOT NULL COMMENT '用户名',
                        `password` varchar(100) NOT NULL COMMENT '编码后的密码',
                        `is_expired` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '账号是否过期',
                        `is_locked` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '账号是否被锁定',
                        `is_delete` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否被删除',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3

