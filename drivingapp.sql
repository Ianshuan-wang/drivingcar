use datebase test;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS psysiological_parameter;
DROP TABLE IF EXISTS path;
DROP TABLE IF EXISTS driving_state;
DROP TABLE IF EXISTS driving_record;
DROP TABLE IF EXISTS collect;
DROP TABLE IF EXISTS footmark;
DROP TABLE IF EXISTS card_coupon;
DROP TABLE IF EXISTS sign_in_record;
DROP TABLE IF EXISTS user_suggestion;
DROP TABLE IF EXISTS tip;
DROP TABLE IF EXISTS butler_advice;
DROP TABLE IF EXISTS goods;
DROP TABLE IF EXISTS equipment;

-- ----------------------------
-- 1、用户表
-- ----------------------------

CREATE TABLE user(
    id                      VARCHAR(255)                COMMENT '主键id',
    head_protrait           VARCHAR(255)                COMMENT '头像',
    username                VARCHAR(50)                 COMMENT '用户名',
    phone           		VARCHAR(20)                 COMMENT '手机号',
    email                 	VARCHAR(50)                 COMMENT '邮箱',
    qq                      VARCHAR(255)                COMMENT 'QQ',
    wechat                  VARCHAR(255)                COMMENT '微信',
    `password`              VARCHAR(50)                 COMMENT '密码',
    expirical_value         INT                         COMMENT '经验值',
    registration_time       DATE                        COMMENT '注册时间',
		UNIQUE (phone),
		UNIQUE (email),
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '用户表表';

-- ----------------------------
-- 5、 驾驶记录
-- ----------------------------
CREATE TABLE driving_record (
    id                       BIGINT         AUTO_INCREMENT       COMMENT '主键id',
    user_id                  VARCHAR(255)                        COMMENT '用户id-外键',
    `time`                   DATE                                COMMENT '时间',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=INNODB COMMENT = '驾驶记录表';


-- ----------------------------
-- 2、 生理参数
-- ----------------------------
CREATE TABLE physiological_parameter (
    id                       BIGINT         AUTO_INCREMENT       COMMENT '主键id',
    driving_record_id        BIGINT                              COMMENT '驾驶记录id-外键',
    heart_rate               INT                                 COMMENT '心率',
    Blood_oxygen             INT                                 COMMENT '血氧',
    hypertension             INT                                 COMMENT '高血压',
    hypopiesis               INT                                 COMMENT '低血压',
    time_stamp               DATE                                COMMENT '时间戳',
    PRIMARY KEY (id),
    FOREIGN KEY (driving_record_id) REFERENCES driving_record(id)
) ENGINE=INNODB COMMENT = '生理参数表';

-- ----------------------------
-- 3、 单次路径记录
-- ----------------------------
CREATE TABLE path (
    id                       BIGINT         AUTO_INCREMENT     COMMENT '主键id',
    driving_record_id        BIGINT                            COMMENT '驾驶记录id-外键',
    coordinate_point         LONGTEXT                          COMMENT '坐标点（坐标点集合）',
    mileage                  DOUBLE                            COMMENT '里程',
    max_speed                DOUBLE                            COMMENT '最大速度',
    min_speed                DOUBLE                            COMMENT '最小速度',
    average_speed            DOUBLE                            COMMENT '平均速度',
    PRIMARY KEY (id),
    FOREIGN KEY (driving_record_id) REFERENCES driving_record(id)
) ENGINE=INNODB COMMENT = '路径';


-- ----------------------------
-- 4、 驾驶状态
-- ----------------------------
CREATE TABLE driving_state (
    id                       BIGINT         AUTO_INCREMENT       COMMENT '主键id',
    driving_record_id        BIGINT                              COMMENT '驾驶记录id-外键',
    mood               	     INT                                 COMMENT '情绪',
    distract                 INT                                 COMMENT '分心',
    tired                    INT                                 COMMENT '疲劳',
    TIME                     DATE                                COMMENT '时间',
    PRIMARY KEY (id),
    FOREIGN KEY (driving_record_id) REFERENCES driving_record(id)
) ENGINE=INNODB COMMENT = '驾驶状态表';




-- ----------------------------
-- 6、 收藏   
-- ----------------------------
CREATE TABLE collect (
    id                      INT       AUTO_INCREMENT          COMMENT '主键id',
    user_id        	       	VARCHAR(255)                      COMMENT '用户id-外键',
    poi_id        			VARCHAR(255)                      COMMENT '坐标点',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USER(id)
) ENGINE=INNODB COMMENT = '收藏表';


-- ----------------------------
-- 7、 足迹
-- ----------------------------
CREATE TABLE footmark (
    id                INT                        COMMENT '主键id',
    user_id           VARCHAR(255)               COMMENT '用户id-外键',
    path              VARCHAR(255)               COMMENT '路径（坐标点集合）',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USER(id)
) ENGINE=INNODB COMMENT = '足迹表';

-- ----------------------------
-- 8、 卡券
-- ----------------------------
CREATE TABLE card_coupon (
    id                   INT         AUTO_INCREMENT       COMMENT '主键id',
    `name`               VARCHAR(255)                              COMMENT '驾驶记录id-外键',
    picture              VARCHAR(255)                                 COMMENT '心率',
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '卡券表';


-- ----------------------------
-- 9、 签到记录 
-- ----------------------------
CREATE TABLE sign_in_record (
    id                       INT                   COMMENT '主键id',
    user_id                  VARCHAR(255)          COMMENT '用户id-外键',
    sign_in_date             DATE                  COMMENT '日期',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USER(id)
) ENGINE=INNODB COMMENT = '签到记录表';


-- ----------------------------
-- 10、 用户建议
-- ----------------------------
CREATE TABLE user_suggestion (
    id                  INT   AUTO_INCREMENT    COMMENT '主键id',
    user_id             VARCHAR(255)            COMMENT '用户id-外键',
	type				VARCHAR(50)             COMMENT '种类',
    `time`              DATE                    COMMENT '时间',
    content             TEXT                    COMMENT '内容',
    picture             VARCHAR(255)            COMMENT '图片',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=INNODB COMMENT = '用户建议表';


-- ----------------------------
-- 11、 贴士
-- ----------------------------
CREATE TABLE tip (
    id             INT            COMMENT '主键id',
    content        TEXT           COMMENT '内容',
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '贴士表';


-- ----------------------------
-- 12、 管家建议
-- ----------------------------
CREATE TABLE butler_advice (
    id                   INT                  COMMENT '主键id',
    content              TEXT                 COMMENT '内容',
    audio_frequency      VARCHAR(255)         COMMENT '音频（路径）',
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '生理参数表';


-- ----------------------------
-- 13、 商品（待定）
-- ----------------------------
CREATE TABLE goods (
    id                     INT                           COMMENT '主键id',
    `name`                   VARCHAR(255)                  COMMENT '名称',
    picture                VARCHAR(255)                  COMMENT '图片',
    brief_introduction     TEXT                          COMMENT '简介',
    price                  DOUBLE                        COMMENT '价格',
    producing_area         VARCHAR(255)                  COMMENT '产地',
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '商品（待定）';


-- ----------------------------
-- 14、 设备（待定）
-- ----------------------------
CREATE TABLE equipment (
    id              INT                            COMMENT '主键id',
    `name`            VARCHAR(255)                 COMMENT '设备名',
    PRIMARY KEY (id)
) ENGINE=INNODB COMMENT = '设备（待定）';