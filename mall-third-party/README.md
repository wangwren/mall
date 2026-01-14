# 腾讯云COS对象存储接入说明

## 项目结构

已创建独立的 `mall-third-party` 微服务，用于处理第三方服务集成（目前包含腾讯云COS文件上传）。

```
mall-third-party/
├── src/main/java/com/core/mall/thirdparty/
│   ├── MallThirdPartyApplication.java      # 主启动类
│   ├── config/
│   │   ├── CosConfig.java                  # COS客户端配置
│   │   └── CosProperties.java              # COS配置属性绑定
│   ├── service/
│   │   └── CosService.java                 # 文件上传服务
│   └── controller/
│       └── CosController.java              # 文件上传API控制器
└── src/main/resources/
    └── application.yml                      # 服务配置
```

## 配置步骤

### 1. 在Nacos配置中心添加COS配置

登录Nacos控制台 (http://127.0.0.1:8848/nacos)，在 `third-party` 命名空间中添加配置：

**Data ID**: `mall-third-party.yml`
**Group**: `DEFAULT_GROUP`

**配置内容**:
```yaml
# 腾讯云COS配置
tencent:
  cos:
    # 访问密钥ID
    secret-id: your-secret-id
    # 访问密钥Key
    secret-key: your-secret-key
    # 地域（如：ap-guangzhou、ap-beijing等）
    region: ap-guangzhou
    # 存储桶名称
    bucket-name: your-bucket-name
    # CDN访问域名（如：https://your-bucket.cos.ap-guangzhou.myqcloud.com）
    cdn-url: https://your-bucket.cos.ap-guangzhou.myqcloud.com
```

### 2. 获取腾讯云COS配置信息

1. 登录 [腾讯云控制台](https://console.cloud.tencent.com/)
2. 进入 [对象存储COS](https://console.cloud.tencent.com/cos)
3. 创建存储桶（Bucket）
   - 选择地域（如：广州 - ap-guangzhou）
   - 设置访问权限（建议：私有读写，通过程序控制访问）
4. 获取密钥
   - 进入 [访问管理-API密钥](https://console.cloud.tencent.com/cam/capi)
   - 创建或查看 SecretId 和 SecretKey

### 3. 启动服务

```bash
# 确保Nacos已启动
cd mall-third-party
mvn spring-boot:run
```

服务将运行在端口: **30000**

## API接口

### 上传文件

**请求方式**: POST
**直接访问**: `http://localhost:30000/thirdparty/cos/upload`
**网关访问**: `http://localhost:8060/app/thirdparty/cos/upload`

**请求参数**:
- `file`: 上传的文件（multipart/form-data）

**响应示例**:
```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "url": "https://your-bucket.cos.ap-guangzhou.myqcloud.com/2024/01/14/abc123.jpg",
    "filename": "example.jpg",
    "size": 102400
  }
}
```

## 使用示例

### 前端上传示例 (Vue3 + Element Plus)

```vue
<template>
  <el-upload
    action="http://localhost:8060/app/thirdparty/cos/upload"
    :on-success="handleSuccess"
    :on-error="handleError"
  >
    <el-button type="primary">点击上传</el-button>
  </el-upload>
</template>

<script setup>
const handleSuccess = (response) => {
  if (response.code === 0) {
    console.log('文件URL:', response.data.url)
  }
}

const handleError = (error) => {
  console.error('上传失败:', error)
}
</script>
```

### cURL示例

```bash
curl -X POST http://localhost:30000/thirdparty/cos/upload \
  -F "file=@/path/to/your/file.jpg"
```

## 文件存储规则

上传的文件按照以下规则组织:
- 路径格式: `年/月/日/UUID.扩展名`
- 示例: `2024/01/14/a1b2c3d4e5f6.jpg`

这样的组织方式有以下优点:
- 避免文件名冲突
- 便于按日期查找和管理
- 支持按日期进行存储统计

## 服务注册与发现

- 服务名: `mall-third-party`
- 注册到Nacos: `127.0.0.1:8848`
- 命名空间: `third-party`
- 端口: `30000`

## 网关路由配置

已在 `mall-gateway` 中配置路由规则:
- 前端请求: `/app/thirdparty/**`
- 转发到: `lb://mall-third-party`
- 路径重写: `/app/thirdparty/xxx` → `/thirdparty/xxx`

## 安全建议

1. **访问控制**: 建议存储桶设置为私有读写，通过应用程序控制访问
2. **密钥管理**: SecretId 和 SecretKey 保存在Nacos配置中心，不要提交到代码仓库
3. **文件验证**: 在生产环境中，建议添加文件类型、大小限制
4. **防盗链**: 在腾讯云COS控制台配置防盗链规则
5. **CDN加速**: 对于高访问量场景，建议配置CDN加速域名

## 后续扩展

如需添加更多功能，可在 `CosService` 中实现:
- 文件删除
- 文件列表查询
- 生成临时访问URL
- 批量上传
- 图片处理（裁剪、压缩等）

## 常见问题

### 1. 服务启动失败
- 检查Nacos是否正常运行
- 确认 `third-party` 命名空间已创建
- 验证配置文件是否正确

### 2. 上传失败
- 检查COS配置信息是否正确
- 确认存储桶访问权限
- 查看服务日志获取详细错误信息

### 3. 文件无法访问
- 检查存储桶访问权限设置
- 验证CDN域名是否正确配置
- 确认文件路径是否正确
