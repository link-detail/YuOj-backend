
# --用户表

CREATE TABLE user(
                     id BIGINT auto_increment PRIMARY key COMMENT 'id',
                     userName VARCHAR(256) NULL COMMENT '用户昵称',
                     userAccount VARCHAR(256) NOT NULL COMMENT '账号',
                     userAvatar VARCHAR(1024) NULL COMMENT '头像',
                     gender TINYINT NULL COMMENT '性别',
                     userRole VARCHAR(256) DEFAULT 'user' COMMENT '用户角色:user/ admin/ ban',
                     userPassword VARCHAR(512) NOT NULL COMMENT '密码',
                     userProfile  varchar(512)                           null comment '用户简介',
                     createTime DATETIME DEFAULT CURRENT_TIMESTAMP not NULL COMMENT '创建时间',
                     updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON update CURRENT_TIMESTAMP COMMENT '更新时间',
                     isDelete TINYINT DEFAULT 0 not NULL  COMMENT '是否删除 0/1',
#                      约束条件 （账号信息不可以一样的）
                     CONSTRAINT uni_userAccount UNIQUE (userAccount)
)COMMENT '用户表' collate = utf8mb4_unicode_ci;


# --题目表
CREATE TABLE question
(
    id bigint auto_increment comment 'id' primary key ,
    title varchar(256)  NULL COMMENT '标题',
    content text  null comment '内容',
    tags varchar(256)  null comment '标签（json数组）',
    answer text  null comment '答案',
    submitNum int default 0 not null comment '题目提交数',
    acceptNum int default 0 not null comment '题目通过数',
    judgeCase text null comment '判题用例（json数组）',
    judgeConfig text null comment '判题配置（json数组）',
    thumbNum int default 0 not null comment '点赞数',
    favourNum int default 0 not null comment '收藏数',
    userId bigint not null comment '创建用户id',
    createTime datetime default current_timestamp NOT NULL comment '创建时间',
    updateTime datetime default current_timestamp not null on update CURRENT_TIMESTAMP comment '删除时间',
    isDelete tinyint default 0 not null comment '是否删除',
    # 索引
    index idx_userId(userId)


)COMMENT '题目表';


# --题目提交表

CREATE TABLE question_submit
(
    id bigint auto_increment comment 'id' primary key ,
    userId bigint not null comment '提交题目用户id',
    questionId bigint not null comment '题目id',
    language varchar(256) not null comment '编程语言',
    code text not null comment '用户代码',
    status int default 0 not null comment '判题状态（0-待判题 1-判题中 2-成功 3-失败）',
    judgeInfo text  null comment '判题信息（json对象）',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '删除时间',
    isDelete tinyint default 0 comment '是否删除',
    # 索引
    index idx_questionId(questionId),
    index idx_userId(userId)
)COMMENT '题目提交表';

