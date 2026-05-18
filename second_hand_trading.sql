/*
 Navicat Premium Data Transfer

 Source Server         : lijiapp
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : second_hand_trading

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 23/06/2025 22:19:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sh_address
-- ----------------------------
DROP TABLE IF EXISTS `sh_address`;
CREATE TABLE `sh_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `consignee_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人手机号',
  `province_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省',
  `city_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '市',
  `region_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区',
  `detail_address` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `default_flag` tinyint NOT NULL COMMENT '是否默认地址',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_address
-- ----------------------------
INSERT INTO `sh_address` VALUES (42, '黑八', '19878675646', '男生宿舍', '10号楼', '一层', '438宿舍靠近饮水机', 1, 43);
INSERT INTO `sh_address` VALUES (43, '黑九八', '19878675646', '男生宿舍', '10号楼', '三层', '336靠近卫生间旁边', 0, 43);
INSERT INTO `sh_address` VALUES (44, '李思思', '15678675634', '女生宿舍', '7号楼', '三层', '218宿舍靠近洗手池', 1, 44);
INSERT INTO `sh_address` VALUES (45, '张三三', '19878675644', '女生宿舍', '8号楼', '二层', '靠近卫生间', 1, 36);
INSERT INTO `sh_address` VALUES (46, '123456', '15380678506', '女生宿舍', '7号楼', '二层', '3-330', 1, 49);

-- ----------------------------
-- Table structure for sh_admin
-- ----------------------------
DROP TABLE IF EXISTS `sh_admin`;
CREATE TABLE `sh_admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `account_number` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员账号',
  `admin_password` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `admin_name` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员名字',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员手机号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员邮箱',
  `birth` date NULL DEFAULT NULL COMMENT '出生日期',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_number`(`account_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_admin
-- ----------------------------
INSERT INTO `sh_admin` VALUES (1, 'admin', '123456', '超级管理员', '15312345678', '123@qq.com', '2025-06-04', '女');
INSERT INTO `sh_admin` VALUES (2, 'admin1', '123456', '超级管理员二号', '18212345678', '456@163.com', '2025-06-13', '男');
INSERT INTO `sh_admin` VALUES (3, '55', '55', '55', '16312345698', '12365@163.com', '2025-06-10', '女');
INSERT INTO `sh_admin` VALUES (4, '12345678910', '123456', '小易', '12345677777', '11111111@qq.com', '2025-06-25', '男');
INSERT INTO `sh_admin` VALUES (5, 'admin2', '12345678', '1234512', '12345678911', '123456789@qq.com', '2025-06-17', '男');
INSERT INTO `sh_admin` VALUES (15, 'admin3', '12345678', '123456', '15345678912', '123456@qq.com', '2025-06-24', '男');

-- ----------------------------
-- Table structure for sh_favorite
-- ----------------------------
DROP TABLE IF EXISTS `sh_favorite`;
CREATE TABLE `sh_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
  `create_time` datetime NOT NULL COMMENT '加入收藏的时间',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  `idle_id` bigint NOT NULL COMMENT '闲置物主键id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC, `idle_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收藏信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_favorite
-- ----------------------------
INSERT INTO `sh_favorite` VALUES (61, '2023-06-18 00:11:56', 44, 196);
INSERT INTO `sh_favorite` VALUES (62, '2023-06-18 04:58:49', 37, 194);
INSERT INTO `sh_favorite` VALUES (63, '2023-06-18 05:13:50', 36, 192);
INSERT INTO `sh_favorite` VALUES (66, '2025-06-10 17:08:33', 36, 190);
INSERT INTO `sh_favorite` VALUES (67, '2025-06-10 20:24:29', 36, 193);

-- ----------------------------
-- Table structure for sh_idle_item
-- ----------------------------
DROP TABLE IF EXISTS `sh_idle_item`;
CREATE TABLE `sh_idle_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `idle_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '闲置物名称',
  `idle_details` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详情',
  `picture_list` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图集',
  `idle_price` decimal(10, 2) NOT NULL COMMENT '价格',
  `idle_place` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发货地区',
  `idle_label` int NOT NULL COMMENT '分类标签',
  `release_time` datetime NOT NULL COMMENT '发布时间',
  `idle_status` tinyint NOT NULL COMMENT '状态（发布1、下架2、删除0）',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '二手商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_idle_item
-- ----------------------------
INSERT INTO `sh_idle_item` VALUES (190, '《我只愿面朝大海，春暖花开》', '品名诗名画，享诗意人生！！！\n一本最唯美、最感伤、最具忧郁情怀的诗集经典（双封面+双封面+四色精美彩绘+赠送书签）\n《我只愿面朝大海，春暖花开》——海子经典诗集，该作品共十辑，收录了海子经典诗集177首，其经典篇目有：《面朝大海，春暖花开》《亚洲铜》《以梦为马》等等。\n海子是最受青年人喜爱的当代诗人之一，是最具传奇色彩和最受正义的当代诗人之一，是影响几代人的当代诗人。他擅长写抒情诗歌，他抒情的声音是他众多声音中最响亮、最尖锐的一种，是他被传诵得最远的声音。', '[\"http://sx737w976.hn-bkt.clouddn.com/FuOpfeDhs2hgUODet2nvHqG-PGuq\"]', 30.00, '11号楼', 1, '2023-06-17 16:51:23', 1, 43);
INSERT INTO `sh_idle_item` VALUES (191, '苦难辉煌', '《苦难辉煌》主要内容：20世纪在世界东方，莫过于中华民族从东亚病夫到东方巨龙、从百年沉沦到百年复兴这一历史命运的大落大起。在这一命运形成之初，中国国民党、中国共产党、联共（布）与共产国际、日本昭和军阀集团这四大力量，以中国大地为舞台发生了猛烈碰撞。内外矛盾冲突空前尖锐，相互斗争局面极其复杂，各派力量的策略转换空前迅速；每一方的领袖和将领皆在较量中淋沥尽致地展现自己全部能量，从而在历史中留下深深的印痕。外部的围追堵截，内部的争论与妥协、以及不尽的跋涉、惊人的牺牲、大量的叛变，中国共产党人正是经历了如此的地狱之火，带领中华民族探测到了前所未有的历史深度和时代宽度，最终完成了中国历史中最富史诗意义的壮举，中国革命也由此成为一只火中凤凰，从苦难走向辉煌。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fol8eKLiqMf9HGp5_O4Urq6Ob3Uk\"]', 46.00, '11号楼', 1, '2023-06-17 16:52:46', 1, 43);
INSERT INTO `sh_idle_item` VALUES (192, '注塑制品成型缺陷图集', '《注塑制品成型缺陷图集》收集了数百张注塑产品缺陷图，针对各类产品缺陷给出排查步骤和材料、工艺、设备、模具等方面的解决方法，并分析了多级注塑、微型产品和超薄产品等典型案例。《注塑制品成型缺陷图集》可供注塑企业技术工人和技术人员培训学习使用，也可供相关行业参考。', '[\"http://sx737w976.hn-bkt.clouddn.com/FsFUPiU0eQPPc8O_gsiRcQq1oycW\"]', 34.00, '8号楼', 2, '2023-06-17 16:54:28', 1, 40);
INSERT INTO `sh_idle_item` VALUES (193, '世界上最伟大的推销员', '（风靡当今西方世界的商业圣经  营销大师无往而不胜的智慧之源）\n销量破千万册 唯一授权完整版\n这本书记载了一则感人肺腑的传奇故事。一个名叫海菲的牧童，从他的主人那里幸运地得到十道神秘的羊皮卷，遵循卷中的原则，他执着创业，最终成为了一名伟大的推销员。建立起了一座浩大的商业王国……\n这是一本在全世界范围内影响巨大的书，适合任何附层的人阅读。它振雷人心，激励斗志，改变了许多人的命运……\n本书是一经问世，英文版销量当年突破100万，讯即被译成18种文字，每年销量有增无减。', '[\"http://sx737w976.hn-bkt.clouddn.com/FuIUbqT95NwhcAxx1z-VkYvAzp8E\"]', 55.00, '8号楼', 2, '2023-06-17 16:55:13', 1, 40);
INSERT INTO `sh_idle_item` VALUES (194, '创业在路上', '★ 罗永浩的人生经历就是一本典型的创业宝典，对于如今的年轻人，具有可借鉴的实用价值。\n★ 还原了真实的、区别于公众形象的罗永浩。\n★ 送给创业者满满的干货：\n比如“并不是所有人都适合创业。”“管控公司zui重要的三件事：找钱、找人和定战略方向。”“不要试图去补短板，一定不要自己去补自己不擅长的”等\n★ 《创业在路上》集结了2017年超长对话栏目《长谈》、罗永浩的创业分享课的精华内容，凝结了两位对话人睿智有趣的思考方式，首度公开更多幕后花絮和创业心得。', '[\"http://sx737w976.hn-bkt.clouddn.com/FmWuNbS_sN29Mk2h8LfF_MYCwDlw\"]', 128.00, '7号楼', 3, '2023-06-18 00:04:47', 1, 39);
INSERT INTO `sh_idle_item` VALUES (195, ' 从零开始学创业大全集', '为了让每一个怀揣梦想走上创业之路的有志者能在最短的时间内叩开创业的大门，了解创业的流程和方法，从而找到适合自己的创业之路，我们精心编写了这本《从零开始学创业大全集》。阳飞扬编著的《从零开始学创业大全集（超值白金版）》从创业准备、创业团队的组建、创业项目和商业模式的选择、创业计划书的制作、创业资金的筹集、企业的经营策略、资本运作以及产品营销方法、危机应对策略等方面，全面系统地阐述了创业的基本理论与实践，探讨和总结了创业活动的一般规律和关键问题，堪称我国当前的最佳创业指导书。与市场E同类创业类图书最大的不同是。《从零开始学创业大全集（超值白金版）》对创业者所要遇到的各方面问题都做了细致的阐释，不是机械教条式的说教，而是用一些国内外优秀创业者的故事和经历，来启发你的创业智慧，内容新颖、全面，可读性强。同时，为了便于你在创业的过程中操作，我们还把这些经验进行了总结和归纳，希望给你创业提供贴心的帮助和保姆式的服务。在编写过程中，我们既注重实用性、时效性，以丰富读者的相关创业知识为目标，又注重系统性、理论性，力求提升创业者对创新与创业精神、创业内涵的理解。    《从零开始学创业大全集（超值白金版）》既可作为创业教育的培训用书或参考书，同时也适合于各阶层创业者和有志于创业的人士阅读。尤其在当前严峻的就业形势下，越来越多的大学生也选择了创业作为实现就业的手段，国家也出台了相关的政策扶持和帮助大学生自主创业，社会也通过舆论引导大学生健康创业，因此，《从零开始学创业大全集（超值白金版）》对指导大学生创业也具有重要的理论和实践意义。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fh2y0TMek1WftZ_jKh-idJUxMimT\"]', 156.00, '6号楼', 3, '2023-06-18 00:05:39', 1, 39);
INSERT INTO `sh_idle_item` VALUES (196, '一个国王的爱情故事', '你想当国王、想拥有荣华富贵、名闻天下吗？你希望无论走到哪里都成为众人的中心吗？你希望自己每时每刻、每一天都是人们关注的对象吗？\n国王永远不会独自一人。每时每刻总有人注视着他——有时是他的保镖，有时是街上成千的民众。他永远不会独一人；每个人都认识他的面孔。他做事必须检点，因为他的所做所为是无法保密的。\n国王今天说了什么，明天全世界的人都会知道。他说话得谨慎；因为总有人在听。\n这可不像一般的工作那样，5点钟就可以下班。国王没有假期。国王永远是国王——每时每刻都是国王。\n国王永远不会独自一人，但他总是感到孤独。谁会是国王的朋友呢？谁会与他共同分担那份孤独呢？', '[\"http://sx737w976.hn-bkt.clouddn.com/Fgksjia1IBaykbMOPySTOx8zwr0x\"]', 13.00, '14号楼', 1, '2023-06-18 00:07:26', 0, 38);
INSERT INTO `sh_idle_item` VALUES (198, '风景杂志', '风景杂志风景杂志风景杂志风景杂志风景杂志风景杂志', '[\"http://sx737w976.hn-bkt.clouddn.com/FoincR_uHiKb6nR-Qi2ErDWpsFP9\"]', 77.00, '7号楼', 4, '2023-06-18 05:11:18', 2, 36);
INSERT INTO `sh_idle_item` VALUES (200, '深入理解JVM字节码', '这是一本揭示JVM字节码“黑科技”的著作，它从原理和应用两个维度深入剖析了JVM字节码。书中内容涉及JVM字节码的大部分应用场景，如Java性能优化、软件防护与破解、APM等，通过大量实战案例讲解了它在这些场景中的实操技巧。\n本书共 12 章，从逻辑上分为两大部分。\n第一部分：原理篇（第1~8章）\n第1章详细剖析了class文件的内部结构；第2章介绍了字节码的概念以及Java虚拟机栈和栈帧的相关内容；第3章介绍了字节码的进阶知识，包括泛型擦除、synchronized关键字、反射的底层实现原理；第4章介绍了javac编译器的原理，以及javac编译的七大阶段和各阶段的作用；第5章从字节码的角度介绍Kotlin的常见语法糖、coroutine等原理；第6章介绍了ASM和Javassist这两个字节码操作工具；第7章介绍了Java Instrumentation的原理；第8章介绍了JSR 269插件化注解处理原理。\n第二部分：应用篇（第9~12章）\n第9章介绍了字节码在cglib、Fastjson等框架上的应用；第10章介绍了反编译、破解、防破解和逆向工程的相关内容；第11章介绍了APM的概况、分布式跟踪的基本原理等；第12章详细介绍了Android dex文件的组成结构，以及Android字节码指令与Java字节码指令的区别。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fu7x3w9xOnkw2Z4WEIJWdoJGEuU8\"]', 56.00, '12号楼', 1, '2025-06-15 18:24:04', 1, 39);
INSERT INTO `sh_idle_item` VALUES (201, '红星照耀中国', '本书真实记录了斯诺自1936年6月至10月在中国西北革命根据地进行实地采访的所见所闻，向全世界报道了中国和中国工农红军以及许多红军领袖、红军将领的情况。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fgc9rP-UqDx_BeLbxAwKl7iTza5-\"]', 20.00, '6号楼', 1, '2025-06-15 18:37:52', 1, 36);
INSERT INTO `sh_idle_item` VALUES (202, '寻找家园', '高尔泰用一本书书写一生，苍莽浑厚、精洁优美。他的文字是历史的真实回忆，更是对人性的深层揭示、对灵魂的深度挖掘。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fnk3L51dyYL7GOfmCG0ryuDWcEPe\"]', 105.00, '11号楼', 1, '2025-06-15 18:41:28', 2, 39);
INSERT INTO `sh_idle_item` VALUES (203, '公司战略透视', '波士顿顾问公司（BCG）是 全球公司战略的创新者。BCG及 其创始人布鲁斯D亨德森因 创建公司战略学而举世闻名。诸 如“金牛”、“经验曲线”、“一人 细分市场营销”、“以时间为基础 的竞争”和“以能力为基础的竞 争”等创新的经营概念均由BCG 首创。 BCG与世人分享其战略思考 的一种方式是出版一系列的《管 理新视野》。《管理新视野》主题 高度集中，讨论从实际客户案例 或内部进行的专案研究中，所发 现、分析和验、和证的战略观点，一 般不超过12不00字，出版后，分送到 世界各地的主各管手中。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fq6_2bbiZSClBfPMvRJt_qa1RmvZ\"]', 55.00, '10号楼', 3, '2025-06-15 18:47:37', 1, 38);
INSERT INTO `sh_idle_item` VALUES (204, '秘书礼仪实务(人文社科类规划教材)', '李霞、胡红霞、甘琛主编的《秘书礼仪实务》共十四章内容，每章借助思维导图，把每一章的内容通过知识指导和技能实训清晰呈现，名言警句以“思想者”的方式融入，以“导入案例”方式循循善诱和启发学生，相关知识以“温馨小贴士”巧妙衔接，同时很多内容图文并茂，给学习者以愉快、美好的分享，学生详细阅读书中内容和精彩案例，一定能够找到他们的良师益友。', '[\"http://sx737w976.hn-bkt.clouddn.com/FlReXwWp_D7SQu8VcKKwnlDZliV1\"]', 25.00, '8号楼', 2, '2025-06-15 18:54:50', 1, 44);
INSERT INTO `sh_idle_item` VALUES (205, '中国考古学 旧石器时代晚期到早期青铜时代', '·一部考古学经典，讲述文字出现之前的中国史。\n·两位权威学者，穿越旧石器时代狩猎采集人群、新石器时代的农业村落，再到青铜时代商王朝，带你去看早期中国。\n·八千年的考古发现，揭示文明诞生的历程，指明中国通往早期国家之路。', '[\"http://sx737w976.hn-bkt.clouddn.com/FgM2G1qbKOHZXPw4D3ug5Y7v3j1L\"]', 55.00, '8号楼', 2, '2025-06-15 18:57:54', 1, 44);
INSERT INTO `sh_idle_item` VALUES (206, '后陡门的夏', '后陡门的夏天会结束，后陡门的夏却不会落幕。\n祝愿你我在每个夏天都热闹且幸福。', '[\"http://sx737w976.hn-bkt.clouddn.com/Fvj0UlhXLED4oOJFFvycIrmQTfms\"]', 30.00, '10号楼', 4, '2025-06-15 19:05:38', 1, 48);
INSERT INTO `sh_idle_item` VALUES (207, '四季花传书', '继《一日一花》之后，日本当红花道大师川濑敏郎的经典花艺著作《四季花传书》终于在国内上市。山茶花、紫罗兰、水仙、芭蕉、朝颜……川濑敏郎以每月一花的形式，花了2年时间，从历史、文化、艺术、美学的角度阐述他的花艺。同时从四季不同花草的插法，到花剪的使用、器皿的选择，细致入微地介绍了生活中的插花指南。\n　　川濑敏郎的插花经过岁月的磨砺，从年轻时代华丽的形式美精炼到后期侘寂美学的质朴。在作品中强调一种“不依托于外在”的缺、拙、涩之意境，正如日本花道回归人的内在，呈现完整的生命之美。\n　　如果说《一日一花》带给我们更多的是视觉上的审美享受，那这本《四季花传书》则是一次对花道文化的启蒙与实用指南。\n　　花对于日本人来说是一种信仰。它是一种用来供奉的东西，它的重要，就好像是人的丹田。如果没有花，人不能感到生命的根源。\n　　我们欣赏一朵花在自然中的样子。自然的美，是无私的美。但插花不同。它带着看花的人的心情——看花的时候，仿如眉心落下一滴清净的水通过了身体。花道大概需要在形式上学习，但插花本身不需要学习，而是习惯。因为插花只是把心情表现出来而已。', '[\"http://sx737w976.hn-bkt.clouddn.com/FkDPNGmHIo0ZLc2yf-tCcTa_hPBW\"]', 20.00, '13号楼', 4, '2025-06-15 19:11:25', 2, 46);
INSERT INTO `sh_idle_item` VALUES (208, '毕业季啦！', '书籍丰富，可尽情选购！', '[\"http://sx737w976.hn-bkt.clouddn.com/FpOMRvI6HbDHIvTABXUHYs9-dW84\"]', 0.00, '7号楼', 5, '2025-06-19 17:28:15', 1, 46);
INSERT INTO `sh_idle_item` VALUES (209, '关于谨防上当受骗的公告', '尊敬的用户们：大家好！随着二手书交易的日益活跃，我们的平台也成为了众多爱书之人交流与共享书籍的重要场所。然而，近期我们发现有不法分子试图利用平台进行欺诈行为，给用户的财产安全和交易体验带来了潜在风险。为了保障广大用户的合法权益，维护平台的健康秩序，特此发布此公告，提醒大家在交易过程中提高警惕，谨防上当受骗。', '[\"http://sx737w976.hn-bkt.clouddn.com/FvPTM4HDCGVz7kMQ0wfM6WDtt-Bp\"]', 0.00, '12号楼', 5, '2025-06-19 17:31:16', 1, 36);
INSERT INTO `sh_idle_item` VALUES (210, '123456', '123456', '[\"http://sx737w976.hn-bkt.clouddn.com/Fod1jER_tdCW6b7bt6gkTgyJElcL\"]', 20.00, '7号楼', 4, '2025-06-22 09:45:39', 0, 49);
INSERT INTO `sh_idle_item` VALUES (211, '123456', '123456', '[\"http://sx737w976.hn-bkt.clouddn.com/Fod1jER_tdCW6b7bt6gkTgyJElcL\",\"http://sx737w976.hn-bkt.clouddn.com/FoP-Tcww1ZoP46vsEOXEhyufYAHP\"]', 20.00, '7号楼', 4, '2025-06-22 11:17:41', 1, 49);

-- ----------------------------
-- Table structure for sh_message
-- ----------------------------
DROP TABLE IF EXISTS `sh_message`;
CREATE TABLE `sh_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  `idle_id` bigint NOT NULL COMMENT '闲置主键id',
  `content` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言内容',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `to_user` bigint NOT NULL COMMENT '所回复的用户',
  `to_message` bigint NULL DEFAULT NULL COMMENT '所回复的留言',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id` ASC) USING BTREE,
  INDEX `idle_id_index`(`idle_id` ASC) USING BTREE,
  INDEX `to_user_index`(`to_user` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '留言表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_message
-- ----------------------------
INSERT INTO `sh_message` VALUES (60, 44, 197, '这本书可以便宜吗。或者送货上门', '2023-06-18 05:02:48', 37, NULL);
INSERT INTO `sh_message` VALUES (62, 37, 197, '可以便宜的', '2023-06-18 05:04:16', 44, 60);
INSERT INTO `sh_message` VALUES (63, 36, 194, '这本书可以便宜吗。想要', '2023-06-18 05:09:43', 39, NULL);
INSERT INTO `sh_message` VALUES (64, 39, 194, '可以可以的兄弟。你出多少', '2023-06-18 05:10:18', 36, 63);
INSERT INTO `sh_message` VALUES (65, 49, 209, '好的', '2025-06-22 09:43:36', 36, NULL);
INSERT INTO `sh_message` VALUES (66, 36, 209, '小心受骗，切勿脱离平台操作', '2025-06-22 09:46:50', 49, 65);

-- ----------------------------
-- Table structure for sh_order
-- ----------------------------
DROP TABLE IF EXISTS `sh_order`;
CREATE TABLE `sh_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `order_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  `idle_id` bigint NOT NULL COMMENT '闲置物品主键id',
  `order_price` decimal(10, 2) NOT NULL COMMENT '订单总价',
  `payment_status` tinyint NOT NULL COMMENT '支付状态',
  `payment_way` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `order_status` tinyint NOT NULL COMMENT '订单状态',
  `is_deleted` tinyint NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_order
-- ----------------------------
INSERT INTO `sh_order` VALUES (142, '168704734334110002', 44, 196, 13.00, 0, NULL, '2023-06-18 00:15:43', NULL, 4, 0);
INSERT INTO `sh_order` VALUES (143, '168704747296510003', 44, 196, 13.00, 1, '支付宝', '2023-06-18 00:17:53', '2023-06-18 00:18:03', 3, 0);
INSERT INTO `sh_order` VALUES (145, '174946300625710002', 36, 194, 128.00, 1, '支付宝', '2025-06-09 17:56:46', '2025-06-09 17:56:55', 1, 0);
INSERT INTO `sh_order` VALUES (146, '174947159526110002', 36, 193, 55.00, 0, NULL, '2025-06-09 20:19:55', NULL, 4, 0);
INSERT INTO `sh_order` VALUES (147, '174955659589210002', 36, 190, 30.00, 1, '支付宝', '2025-06-10 19:56:36', '2025-06-10 19:56:39', 1, NULL);
INSERT INTO `sh_order` VALUES (148, '174955975229110002', 36, 193, 55.00, 1, '支付宝', '2025-06-10 20:49:12', '2025-06-10 20:49:15', 1, 0);
INSERT INTO `sh_order` VALUES (149, '174956147747210003', 36, 191, 46.00, 0, NULL, '2025-06-10 21:17:57', NULL, 0, NULL);
INSERT INTO `sh_order` VALUES (150, '174956169939510004', 36, 190, 30.00, 0, NULL, '2025-06-10 21:21:39', NULL, 0, NULL);
INSERT INTO `sh_order` VALUES (151, '174956172109710005', 36, 196, 13.00, 0, NULL, '2025-06-10 21:22:01', NULL, 0, NULL);
INSERT INTO `sh_order` VALUES (152, '174997828967610002', 36, 194, 128.00, 0, '支付宝', '2025-06-15 17:04:50', '2025-06-15 17:04:57', 0, NULL);
INSERT INTO `sh_order` VALUES (153, '174997832528010003', 36, 195, 156.00, 0, NULL, '2025-06-15 17:05:25', NULL, 0, NULL);
INSERT INTO `sh_order` VALUES (154, '174998100570310004', 36, 195, 156.00, 0, '支付宝', '2025-06-15 17:50:06', '2025-06-15 17:50:10', 0, NULL);
INSERT INTO `sh_order` VALUES (155, '174998144344510005', 36, 193, 55.00, 0, '支付宝', '2025-06-15 17:57:23', '2025-06-15 17:57:28', 0, NULL);
INSERT INTO `sh_order` VALUES (156, '174998970330810003', 36, 206, 30.00, 0, '支付宝', '2025-06-15 20:15:03', '2025-06-15 20:15:06', 4, NULL);
INSERT INTO `sh_order` VALUES (157, '175055662542610002', 49, 198, 77.00, 0, NULL, '2025-06-22 09:43:45', NULL, 0, NULL);
INSERT INTO `sh_order` VALUES (158, '175055668321310003', 49, 198, 77.00, 0, '支付宝', '2025-06-22 09:44:43', '2025-06-22 09:44:49', 0, NULL);

-- ----------------------------
-- Table structure for sh_order_address
-- ----------------------------
DROP TABLE IF EXISTS `sh_order_address`;
CREATE TABLE `sh_order_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `consignee_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `consignee_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `detail_address` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderId`(`order_id` ASC) USING BTREE,
  INDEX `order_id_index`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 139 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_order_address
-- ----------------------------
INSERT INTO `sh_order_address` VALUES (124, 142, '李思思', '15678675634', '女生宿舍7号楼三层218宿舍靠近洗手池');
INSERT INTO `sh_order_address` VALUES (125, 143, '李思思', '15678675634', '女生宿舍7号楼三层218宿舍靠近洗手池');
INSERT INTO `sh_order_address` VALUES (126, 144, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (127, 145, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (128, 146, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (129, 147, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (130, 148, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (131, 149, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (132, 150, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (133, 151, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (134, 152, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (135, 153, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (136, 154, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (137, 155, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (138, 156, '张三三', '19878675644', '女生宿舍8号楼二层靠近卫生间');
INSERT INTO `sh_order_address` VALUES (139, 158, '123456', '15380678506', '女生宿舍7号楼二层3-330');

-- ----------------------------
-- Table structure for sh_user
-- ----------------------------
DROP TABLE IF EXISTS `sh_user`;
CREATE TABLE `sh_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号（手机号）',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `user_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `nickname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像',
  `sign_in_time` datetime NOT NULL COMMENT '注册时间',
  `user_status` tinyint NULL DEFAULT NULL COMMENT '状态（1代表封禁）',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `birth` date NULL DEFAULT NULL COMMENT '出生日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_number`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sh_user
-- ----------------------------
INSERT INTO `sh_user` VALUES (36, '19878675641', '3328530966@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '张三三', 'http://sx737w976.hn-bkt.clouddn.com/FknP5xCkzl5tNICn7kPsxxvythqS', '2025-06-22 09:46:24', 0, '女', '2010-06-30');
INSERT INTO `sh_user` VALUES (37, '19878675642', '7493544596@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'http://localhost:8888/image?imageName=file168702033110410022.jpg', '2025-06-15 17:51:28', 1, '男', '2025-06-10');
INSERT INTO `sh_user` VALUES (38, '19878675643', '2345678345@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '王五', 'http://sx737w976.hn-bkt.clouddn.com/Fvs0WFzF1eTSACfTXH_O83UJTQ2d', '2025-06-15 18:42:37', 1, '男', '2025-06-10');
INSERT INTO `sh_user` VALUES (39, '19878675644', '6637367898@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 'http://sx737w976.hn-bkt.clouddn.com/Fvs0WFzF1eTSACfTXH_O83UJTQ2d', '2025-06-15 18:38:55', 0, '男', '2025-06-09');
INSERT INTO `sh_user` VALUES (40, '19878675645', '9078354676@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '田七', 'http://sx737w976.hn-bkt.clouddn.com/Fvs0WFzF1eTSACfTXH_O83UJTQ2d', '2025-06-15 18:25:33', 0, '男', '2025-04-14');
INSERT INTO `sh_user` VALUES (43, '19878675646', '6666666666@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '黑八', 'http://sx737w976.hn-bkt.clouddn.com/FoP-Tcww1ZoP46vsEOXEhyufYAHP', '2025-06-15 18:05:38', 0, '男', '2025-01-13');
INSERT INTO `sh_user` VALUES (44, '15678675634', '7597298318@163.com', 'e10adc3949ba59abbe56e057f20f883e', '李思思', 'http://sx737w976.hn-bkt.clouddn.com/FmyBlFrNvCL-w86eG9y1UuGdqIO4', '2025-06-15 18:50:19', 0, '女', '1999-06-08');
INSERT INTO `sh_user` VALUES (45, '17867564523', '2787364732@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '张一', 'http://sx737w976.hn-bkt.clouddn.com/FramZhPKLd1PzEfb3RuA_GZUWCvf', '2025-06-15 19:14:03', 0, '男', '1994-07-14');
INSERT INTO `sh_user` VALUES (46, '15380678506', '1238903324@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '王七', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', '2025-06-22 08:51:22', 0, '男', '2025-06-25');
INSERT INTO `sh_user` VALUES (47, '18634670987', '3328530956@qq.com', 'e10adc3949ba59abbe56e057f20f883e', 'hyw', 'http://sx737w976.hn-bkt.clouddn.com/FmyBlFrNvCL-w86eG9y1UuGdqIO4', '2025-06-15 19:16:40', 1, '女', '2000-06-30');
INSERT INTO `sh_user` VALUES (48, '16457802391', '5542558739@qq.com', 'e10adc3949ba59abbe56e057f20f883e', '十个勤天', 'http://sx737w976.hn-bkt.clouddn.com/FpY4-SQjW_V9r_nY6bvjFgbfPDoQ', '2025-06-15 18:59:28', 0, '男', '2022-06-06');
INSERT INTO `sh_user` VALUES (49, NULL, 'wangwen02020506@163.com', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'http://sx737w976.hn-bkt.clouddn.com/FoP-Tcww1ZoP46vsEOXEhyufYAHP', '2025-06-22 11:15:53', 0, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
