
# --用户表

CREATE TABLE user(
                     id BIGINT auto_increment PRIMARY key COMMENT 'id',
                     userName VARCHAR(256) NULL COMMENT '用户昵称',
                     userAccount VARCHAR(256) NOT NULL COMMENT '账号',
                     userAvatar VARCHAR(1024) NULL COMMENT '头像',
                     gender TINYINT NULL COMMENT '性别',
                     userRole VARCHAR(256) DEFAULT 'user' COMMENT '用户角色:user/ admin',
                     userPassword VARCHAR(512) NOT NULL COMMENT '密码',
                     createTime DATETIME DEFAULT CURRENT_TIMESTAMP not NULL COMMENT '创建时间',
                     updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON update CURRENT_TIMESTAMP COMMENT '更新时间',
                     isDelete TINYINT DEFAULT 0 not NULL  COMMENT '是否删除 0/1',
                     CONSTRAINT uni_userAccount UNIQUE (userAccount)
)COMMENT '用户表'