# Cloudflare R2 对象存储集成使用说明

## 简介

本项目集成了 Cloudflare R2 对象存储服务，使用 AWS S3 Java SDK 与 R2 进行交互。Cloudflare R2 提供与 AWS S3 兼容的 API，因此可以使用标准的 S3 SDK 进行操作。

## 配置步骤

### 1. 创建 Cloudflare R2 存储桶

1. 登录 Cloudflare Dashboard
2. 导航到 R2 Object Storage
3. 创建一个新的存储桶
4. 记录存储桶名称和账户 ID

### 2. 创建 API Token

1. 在 R2 页面中，点击 "Manage R2 API tokens"
2. 创建新的 API Token
3. 设置权限（建议给予 Object Read and Write 权限）
4. 记录 Access Key ID 和 Secret Access Key

### 3. 配置应用程序

在 `application.yml` 或 `application-r2.yml` 中配置：

```yaml
cloudflare:
  r2:
    access-key: "your-r2-access-key"
    secret-key: "your-r2-secret-key"
    endpoint: "https://your-account-id.r2.cloudflarestorage.com"
    bucket-name: "your-bucket-name"
```

### 4. 端点地址格式

Cloudflare R2 的端点地址格式为：

- `https://<account-id>.r2.cloudflarestorage.com`

其中 `<account-id>` 是你的 Cloudflare 账户 ID。

## 功能特性

### 1. 文件上传

#### 单文件上传

```java
@Autowired
private CloudflareR2Service r2Service;

public String uploadFile(MultipartFile file) {
    return r2Service.uploadFile(file, "uploads");
}
```

#### 字节数组上传

```java
public String uploadBytes(byte[] data, String fileName) {
    return r2Service.uploadBytes(data, fileName, "image/jpeg", "images");
}
```

### 2. 文件管理

#### 删除文件

```java
public boolean deleteFile(String fileName) {
    return r2Service.deleteFile(fileName);
}
```

#### 批量删除

```java
public void deleteMultipleFiles(List<String> fileNames) {
    r2Service.deleteFiles(fileNames);
}
```

#### 列出文件

```java
public List<S3Object> listFiles(String prefix) {
    return r2Service.listFiles(prefix);
}
```

#### 检查文件是否存在

```java
public boolean checkFile(String fileName) {
    return r2Service.fileExists(fileName);
}
```

## API 端点

### 1. 上传文件

```
POST /api/files/upload
Content-Type: multipart/form-data

参数：
- file: 要上传的文件
- folder: 可选的文件夹名称
```

### 2. 批量上传

```
POST /api/files/upload-batch
Content-Type: multipart/form-data

参数：
- files: 要上传的文件列表
- folder: 可选的文件夹名称
```

### 3. 列出文件

```
GET /api/files/list?prefix=folder/

参数：
- prefix: 可选的文件前缀（用于筛选）
```

### 4. 删除文件

```
DELETE /api/files/delete?fileName=file-name

参数：
- fileName: 要删除的文件名
```

### 5. 检查文件存在

```
GET /api/files/exists?fileName=file-name

参数：
- fileName: 要检查的文件名
```

## 自定义域名配置

为了获得更好的访问体验，建议配置自定义域名：

1. 在 Cloudflare Dashboard 中为 R2 存储桶配置自定义域名
2. 更新 `CloudflareR2Service.buildFileUrl()` 方法中的域名
3. 或在配置文件中添加自定义域名配置

## 文件名生成规则

系统会自动为上传的文件生成唯一的文件名，格式为：

```
[folder/]yyyyMMdd-HHmmss-uuid.extension
```

例如：

- `uploads/20241225-143052-a1b2c3d4.jpg`
- `documents/20241225-143052-e5f6g7h8.pdf`

## 错误处理

所有的文件操作都包含适当的错误处理：

- 文件上传失败时会抛出 `RuntimeException`
- 删除不存在的文件时会返回 `false`
- 网络错误时会记录详细的错误日志

## 性能优化建议

1. **文件大小限制**：建议设置合理的文件大小限制
2. **并发上传**：对于大量文件上传，考虑使用异步处理
3. **CDN 加速**：使用 Cloudflare CDN 加速文件访问
4. **缓存策略**：对于静态文件设置适当的缓存头

## 安全注意事项

1. **API Token 安全**：妥善保管 Access Key 和 Secret Key
2. **权限控制**：只授予必要的 R2 权限
3. **文件类型验证**：上传前验证文件类型和大小
4. **访问控制**：合理设置存储桶的访问策略

## 监控和日志

系统提供详细的日志记录：

- 文件上传成功/失败日志
- 文件删除操作日志
- 错误详情记录

可以通过配置日志级别来调整日志详细程度：

```yaml
logging:
  level:
    robin.discordbot.service.CloudflareR2Service: DEBUG
```

## 故障排除

### 常见问题

1. **连接失败**

   - 检查端点地址是否正确
   - 验证 API Token 权限
   - 确认网络连接

2. **上传失败**

   - 检查文件大小限制
   - 验证文件类型支持
   - 查看错误日志

3. **权限错误**
   - 确认 API Token 权限
   - 检查存储桶访问策略

### 调试步骤

1. 启用 DEBUG 日志级别
2. 检查网络连接
3. 验证配置参数
4. 查看详细错误信息
