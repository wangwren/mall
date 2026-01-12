# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 提供在此代码仓库中工作的指导。

## 项目结构

**⚠️ 重要：在执行任何任务前，请先阅读 [项目结构文档](/docs/ai-context/project-structure.md)，了解完整的技术栈、目录结构和项目组织方式。**

## 项目概览

这是一个基于 Spring Cloud 微服务架构的电商平台（mall），包含以下服务：
- **mall-product**: 商品管理服务
- **mall-order**: 订单处理服务
- **mall-member**: 会员管理服务
- **mall-coupon**: 优惠券和促销服务
- **mall-ware**: 仓储和库存管理服务
- **mall-gateway**: API 网关（Spring Cloud Gateway）
- **mall-commons**: 公共工具类和基础类
- **renren-security-master**: 后台管理系统（含代码生成器）

## 技术栈

- **Java**: 21
- **Spring Boot**: 3.5.8
- **Spring Cloud**: 2025.0.0
- **Spring Cloud Alibaba**: 2025.0.0.0（使用 Nacos 作为服务发现和配置中心）
- **MyBatis-Plus**: 3.5.10
- **数据库**: PostgreSQL（使用 Hikari 连接池）
- **API 文档**: Knife4j 4.5.0, SpringDoc 2.8.4
- **工具库**: Hutool 5.8.38
- **构建工具**: Maven 3.6+

## 构建和运行命令

### 构建整个项目
```bash
mvn clean install
```

### 构建特定模块
```bash
cd mall-product  # 或其他模块
mvn clean package
```

### 运行微服务
```bash
# 在模块目录下
mvn spring-boot:run

# 或在 IDE 中直接运行主类
# 例如: MallProductApplication.java
```

### 运行测试
```bash
# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=ClassName

# 运行单个测试方法
mvn test -Dtest=ClassName#methodName
```

## 架构模式

### 服务层架构
所有服务遵循统一的四层架构：
1. **Controller 层**: REST 端点，使用 `@RestController`，返回 `Result<T>` 包装类
2. **Service 层**: 业务逻辑接口
3. **ServiceImpl 层**: 继承 `CrudServiceImpl<M, T, D>` 实现标准 CRUD 操作
4. **DAO 层**: MyBatis-Plus `BaseMapper<T>` 接口，配合 XML mapper 文件

### 基础类（mall-commons）
- **CrudServiceImpl**: 抽象基类，提供 CRUD 操作和自动 DTO/Entity 转换
- **BaseServiceImpl**: 更底层的基类，集成 MyBatis-Plus
- **Result**: 标准 API 响应包装类，提供 `ok()` 和错误处理
- **PageData**: 分页数据包装类

### 核心模式
- **DTO 模式**: API 层使用 DTO，持久层使用 Entity
- **验证分组**: `AddGroup`、`UpdateGroup`、`DefaultGroup` 用于不同场景的验证
- **Feign 客户端**: 服务间通信（例如 `mall-order` → `mall-product`）
- **泛型类型解析**: Service 使用反射在运行时确定 Entity/DTO 类型

### 配置管理
- **Nacos 配置中心**: 所有服务从 Nacos 加载配置
- 每个服务有独立的命名空间（如 `product`、`gateway`）
- 通用配置文件: `application.yml`、`mybatis.yml`、`datasource.yml`
- 本地 `application.yml` 仅包含 Nacos 连接信息

## 代码生成器（renren-generator）

项目包含代码生成器，可生成：
- Entity 实体类
- DAO 接口和 XML mapper
- Service 接口和实现类
- DTO 数据传输对象
- Controller 控制器
- Vue 前端代码

配置文件: `renren-security-master/renren-generator/src/main/resources/generator.properties`

## 开发工作流

### 添加新实体/表
1. 创建数据库表
2. 使用 renren-generator 生成样板代码
3. 自定义生成的 `ServiceImpl.getWrapper()` 实现查询逻辑
4. 在 DTO 上添加验证注解
5. 在 Service 层实现自定义业务逻辑

### 添加新接口
1. 在 Controller 中添加方法，使用相应的 `@GetMapping/@PostMapping/@PutMapping/@DeleteMapping`
2. 列表/分页查询使用 `@RequestParam Map<String, Object> params`
3. 使用 `ValidatorUtils.validateEntity(dto, ValidationGroup.class)` 进行验证
4. 成功时返回 `new Result<T>().ok(data)`

### 服务间调用
1. 在消费方服务中创建 Feign 客户端接口
2. 使用 `@FeignClient(name = "service-name")` 注解
3. 定义方法，映射到提供方的 Controller 端点
4. 像普通 Spring Bean 一样注入使用

## 重要约定

### 请求路径规范
- 路径包含模块前缀: `/product/brand`、`/order/item`
- 标准 CRUD 路径: `GET /page`、`GET /{id}`、`POST /`、`PUT /`、`DELETE /`

### 数据验证
- Controller 中始终使用 `ValidatorUtils.validateEntity()` 验证 DTO
- 使用验证分组区分新增和更新的不同要求
- 使用 `AssertUtils.isArrayEmpty()` 检查数组参数

### 服务发现
- 所有微服务注册到 Nacos（`127.0.0.1:8848`）
- Gateway 根据服务名路由请求（如 `lb://mall-order`）
- 主类上使用 `@EnableDiscoveryClient` 注解

### MyBatis-Plus
- Entity 继承 `BaseEntity`（提供 id、createDate 等公共字段）
- DAO 继承 `BaseMapper<T>`（提供标准 CRUD 方法）
- 自定义查询写在 `resources/mapper/{module}/` 下的 XML mapper 文件中
- 在 `getWrapper()` 方法中使用 `QueryWrapper<T>` 构建动态查询

## 常见问题

### Nacos 连接
启动任何服务前，确保 Nacos 服务器运行在 `127.0.0.1:8848`。没有 Nacos，服务将无法启动。

### 数据库配置
数据库凭据从 Nacos 配置中心加载，不在本地文件中。需要在 Nacos 中更新 `datasource.yml`，而非项目文件。

### 网关路由
Gateway 默认去除第一段路径（`StripPrefix=1`）。请求 `/mall-order/order/list` 会路由到 `mall-order` 服务的 `/order/list`。

### 泛型类型解析
继承 `CrudServiceImpl` 时，确保类型参数顺序正确: `<Mapper, Entity, DTO>`。框架使用反射在运行时确定这些类型。
