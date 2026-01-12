# 项目结构文档

## 项目整体结构

```
mall/
├── mall-product/          # 商品管理服务
├── mall-order/            # 订单处理服务
├── mall-member/           # 会员管理服务
├── mall-coupon/           # 优惠券促销服务
├── mall-ware/             # 仓储库存服务
├── mall-gateway/          # API 网关服务
├── mall-commons/          # 公共模块（工具类、基础类）
├── renren-security-master/ # 后台管理系统
│   ├── renren-admin/      # 管理后台主模块
│   ├── renren-api/        # API 服务模块
│   ├── renren-common/     # 公共模块
│   ├── renren-generator/  # 代码生成器
│   ├── renren-dynamic-datasource/ # 动态数据源
│   └── renren-ui/         # Vue3 前端工程
├── docs/                  # 项目文档
│   └── ai-context/        # AI 上下文文档
├── pom.xml                # 父 POM 文件
└── CLAUDE.md              # Claude Code 指导文件
```

## 技术栈详情

### 后端技术栈
- **Java**: 21
- **Spring Boot**: 3.5.8
- **Spring Cloud**: 2025.0.0
- **Spring Cloud Alibaba**: 2025.0.0.0
  - Nacos: 服务发现和配置中心
  - Sentinel: 流量控制和熔断降级（可选）
- **Spring Cloud Gateway**: API 网关
- **MyBatis-Plus**: 3.5.10（持久层框架）
- **Dynamic Datasource**: 4.3.1（多数据源支持）
- **数据库**: PostgreSQL
- **连接池**: Hikari（Spring Boot 默认）
- **工具库**: Hutool 5.8.38
- **API 文档**:
  - Knife4j: 4.5.0
  - SpringDoc: 2.8.4
- **参数校验**: Hibernate Validator
- **Lombok**: 简化 Java 代码

### 后台管理系统技术栈
- **安全框架**: Apache Shiro 1.12
- **定时任务**: Quartz 2.3
- **前端**: Vue 3 + TypeScript + Element Plus + Vite

## 微服务模块结构

每个微服务（mall-product、mall-order、mall-member、mall-coupon、mall-ware）遵循相同的标准结构：

```
mall-{service}/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/core/mall/{service}/
│   │   │       ├── controller/        # REST 控制器
│   │   │       ├── service/           # 业务逻辑接口
│   │   │       │   └── impl/          # 业务逻辑实现
│   │   │       ├── dao/               # 数据访问层（MyBatis Mapper）
│   │   │       ├── entity/            # 数据库实体类
│   │   │       ├── dto/               # 数据传输对象
│   │   │       ├── feign/             # Feign 客户端（服务间调用）
│   │   │       └── Mall{Service}Application.java  # 启动类
│   │   └── resources/
│   │       ├── mapper/                # MyBatis XML 映射文件
│   │       │   └── {service}/
│   │       └── application.yml        # 配置文件（Nacos 连接）
│   └── test/
│       └── java/                      # 测试代码
└── pom.xml                            # 模块 POM 文件
```

### 包结构说明

#### Controller 层
- 使用 `@RestController` 注解
- 路径格式: `/{service}/{entity}`
- 标准 CRUD 端点:
  - `GET /page` - 分页查询
  - `GET /{id}` - 根据 ID 查询
  - `POST /` - 新增
  - `PUT /` - 更新
  - `DELETE /` - 删除
- 返回统一的 `Result<T>` 包装类

#### Service 层
- 接口定义业务方法
- 实现类继承 `CrudServiceImpl<M, T, D>`
  - M: Mapper 类型
  - T: Entity 类型
  - D: DTO 类型
- 必须实现 `getWrapper(Map<String, Object> params)` 方法构建查询条件

#### DAO 层
- 接口继承 `BaseMapper<T>`
- 配合 XML mapper 文件定义复杂查询
- 位置: `resources/mapper/{service}/`

#### Entity 层
- 数据库表映射实体
- 继承 `BaseEntity`（提供 id、createDate、updateDate 等公共字段）
- 使用 MyBatis-Plus 注解: `@TableName`、`@TableId`、`@TableField`

#### DTO 层
- API 层数据传输对象
- 包含验证注解（`@NotNull`、`@NotBlank` 等）
- 使用验证分组: `AddGroup`、`UpdateGroup`、`DefaultGroup`

## mall-commons 公共模块

```
mall-commons/
└── src/main/java/com/core/mall/common/
    ├── aspect/              # AOP 切面（如 Redis 切面）
    ├── constant/            # 常量定义
    ├── convert/             # 类型转换器
    ├── dao/                 # 基础 DAO
    │   └── BaseDao.java
    ├── entity/              # 基础实体
    │   └── BaseEntity.java
    ├── exception/           # 异常处理
    │   ├── ErrorCode.java
    │   ├── RenException.java
    │   └── ExceptionUtils.java
    ├── page/                # 分页封装
    │   └── PageData.java
    ├── redis/               # Redis 配置和工具
    │   ├── RedisConfig.java
    │   ├── RedisKeys.java
    │   └── RedisUtils.java
    ├── service/             # 基础服务
    │   ├── BaseService.java
    │   ├── CrudService.java
    │   └── impl/
    │       ├── BaseServiceImpl.java
    │       └── CrudServiceImpl.java
    ├── utils/               # 工具类
    │   ├── ConvertUtils.java      # 对象转换
    │   ├── DateUtils.java         # 日期工具
    │   ├── HttpContextUtils.java  # HTTP 上下文
    │   ├── IpUtils.java           # IP 工具
    │   ├── JsonUtils.java         # JSON 工具
    │   ├── MessageUtils.java      # 国际化消息
    │   ├── Result.java            # 统一响应结果
    │   ├── SpringContextUtils.java # Spring 上下文
    │   └── TreeUtils.java         # 树形结构工具
    ├── validator/           # 验证器
    │   ├── AssertUtils.java
    │   ├── ValidatorUtils.java
    │   └── group/           # 验证分组
    │       ├── AddGroup.java
    │       ├── UpdateGroup.java
    │       ├── DefaultGroup.java
    │       └── Group.java
    └── xss/                 # XSS 防护
        └── SqlFilter.java
```

## mall-gateway 网关模块

```
mall-gateway/
├── src/main/
│   ├── java/com/core/mall/gateway/
│   │   └── MallGatewayApplication.java
│   └── resources/
│       └── application.yml    # 网关路由配置
└── pom.xml
```

### 网关配置特点
- 端口: 8060
- 使用 Spring Cloud Gateway
- 路由规则配置在 `application.yml` 中
- 默认使用 `StripPrefix=1` 去除第一段路径
- 通过 `lb://service-name` 实现负载均衡

## renren-security-master 后台管理系统

```
renren-security-master/
├── renren-admin/           # 管理后台主模块
│   ├── db/                 # 数据库脚本
│   │   ├── mysql.sql
│   │   ├── oracle.sql
│   │   ├── postgresql.sql
│   │   ├── sqlserver.sql
│   │   └── dm8.sql
│   ├── src/main/
│   │   ├── java/io/renren/
│   │   │   ├── common/     # 公共组件
│   │   │   │   ├── annotation/  # 注解
│   │   │   │   ├── aspect/      # 切面
│   │   │   │   ├── config/      # 配置
│   │   │   │   ├── exception/   # 异常处理
│   │   │   │   ├── handler/     # 处理器
│   │   │   │   ├── interceptor/ # 拦截器
│   │   │   │   ├── utils/       # 工具类
│   │   │   │   └── validator/   # 验证器
│   │   │   └── modules/    # 功能模块
│   │   │       ├── job/         # 定时任务
│   │   │       ├── log/         # 日志管理
│   │   │       ├── oss/         # 文件存储
│   │   │       ├── security/    # 安全模块
│   │   │       └── sys/         # 系统管理
│   │   └── resources/
│   │       ├── mapper/     # MyBatis 映射文件
│   │       ├── public/     # 静态资源
│   │       └── application.yml
│   └── Dockerfile
│
├── renren-generator/       # 代码生成器
│   └── src/main/resources/
│       ├── mapper/
│       ├── template/       # 代码模板
│       ├── application.yml
│       └── generator.properties  # 生成器配置
│
├── renren-api/             # API 服务模块
├── renren-common/          # 公共模块
├── renren-dynamic-datasource/  # 动态数据源
└── renren-ui/              # Vue3 前端
    ├── src/
    │   ├── api/            # API 接口
    │   ├── assets/         # 静态资源
    │   ├── components/     # 组件
    │   ├── router/         # 路由
    │   ├── store/          # Pinia 状态管理
    │   ├── utils/          # 工具类
    │   └── views/          # 页面
    ├── public/
    ├── package.json
    └── vite.config.ts
```

## 配置管理架构

### Nacos 配置中心
- **地址**: `127.0.0.1:8848`
- **配置文件**:
  - `application.yml` - 应用通用配置
  - `mybatis.yml` - MyBatis 配置
  - `datasource.yml` - 数据源配置

### 各服务命名空间
- `product` - 商品服务
- `order` - 订单服务
- `member` - 会员服务
- `coupon` - 优惠券服务
- `ware` - 仓储服务
- `gateway` - 网关服务

### 本地配置文件
每个服务的 `application.yml` 仅包含：
- Nacos 服务器地址
- 配置文件格式
- 命名空间
- 配置分组
- 要导入的配置文件列表

## 服务间通信

### Feign 客户端
- 位置: `{service}/feign/` 包
- 使用 `@FeignClient(name = "service-name")` 注解
- 方法签名与目标服务 Controller 一致
- 自动负载均衡和服务发现

### 示例
```java
@FeignClient(name = "mall-product")
public interface ProductService {
    @GetMapping("product/brand/all")
    Result queryAllBrand();
}
```

## 数据库设计

### 表命名规范
- 商品服务: `pms_*` (Product Management System)
- 订单服务: `oms_*` (Order Management System)
- 会员服务: `ums_*` (User Management System)
- 优惠券服务: `sms_*` (Sale Management System)
- 仓储服务: `wms_*` (Warehouse Management System)

### 公共字段
- `id`: 主键（Long）
- `create_date`: 创建时间
- `update_date`: 更新时间
- `creator`: 创建者
- `updater`: 更新者

## 开发环境要求

- **JDK**: 21+
- **Maven**: 3.6+
- **数据库**: PostgreSQL 9.4+（或 MySQL 8.0+）
- **Nacos**: 2.x
- **IDE**: IntelliJ IDEA（推荐）
- **Node.js**: 16+（前端开发）

## 端口分配

- **mall-gateway**: 8060
- **mall-product**: 动态分配（Nacos 管理）
- **mall-order**: 动态分配（Nacos 管理）
- **mall-member**: 动态分配（Nacos 管理）
- **mall-coupon**: 动态分配（Nacos 管理）
- **mall-ware**: 动态分配（Nacos 管理）
- **renren-admin**: 8080
- **Nacos**: 8848
