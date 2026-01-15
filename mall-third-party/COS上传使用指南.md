# 腾讯云COS上传使用指南

本项目提供两种文件上传方式，可根据不同场景选择使用。

## 方式一：服务端直传（现有方式）

### 适用场景
- 后台管理系统
- 需要对文件进行安全检查（病毒扫描、敏感词过滤）
- 需要对文件进行处理（压缩、转码、加水印）
- 文件较小（< 1MB）

### 流程图
```
前端 → 上传文件 → 后端服务器 → 腾讯云COS
                  (文件经过后端)
```

### 接口
```
POST /thirdparty/cos/upload
Content-Type: multipart/form-data
```

### 前端示例

#### JavaScript (原生)
```javascript
// HTML
<input type="file" id="fileInput" accept="image/*">
<button onclick="uploadFile()">上传</button>

// JavaScript
async function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert('请选择文件');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('http://localhost:8400/thirdparty/cos/upload', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.code === 0) {
            console.log('上传成功！');
            console.log('文件URL:', result.data.url);
            alert('上传成功！文件URL: ' + result.data.url);
        } else {
            alert('上传失败: ' + result.msg);
        }
    } catch (error) {
        console.error('上传失败:', error);
        alert('上传失败: ' + error.message);
    }
}
```

#### Vue 3
```vue
<template>
  <div>
    <input type="file" @change="handleFileChange" accept="image/*">
    <button @click="uploadFile" :disabled="!selectedFile || uploading">
      {{ uploading ? '上传中...' : '上传' }}
    </button>
    <div v-if="uploadedUrl">
      <p>上传成功！</p>
      <img :src="uploadedUrl" style="max-width: 300px">
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const selectedFile = ref(null)
const uploading = ref(false)
const uploadedUrl = ref('')

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0]
}

const uploadFile = async () => {
  if (!selectedFile.value) {
    alert('请选择文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  uploading.value = true

  try {
    const response = await axios.post(
      'http://localhost:8400/thirdparty/cos/upload',
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )

    if (response.data.code === 0) {
      uploadedUrl.value = response.data.data.url
      alert('上传成功！')
    } else {
      alert('上传失败: ' + response.data.msg)
    }
  } catch (error) {
    console.error('上传失败:', error)
    alert('上传失败: ' + error.message)
  } finally {
    uploading.value = false
  }
}
</script>
```

#### React
```jsx
import React, { useState } from 'react';
import axios from 'axios';

function FileUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [uploadedUrl, setUploadedUrl] = useState('');

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const uploadFile = async () => {
    if (!selectedFile) {
      alert('请选择文件');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    setUploading(true);

    try {
      const response = await axios.post(
        'http://localhost:8400/thirdparty/cos/upload',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      );

      if (response.data.code === 0) {
        setUploadedUrl(response.data.data.url);
        alert('上传成功！');
      } else {
        alert('上传失败: ' + response.data.msg);
      }
    } catch (error) {
      console.error('上传失败:', error);
      alert('上传失败: ' + error.message);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <input type="file" onChange={handleFileChange} accept="image/*" />
      <button onClick={uploadFile} disabled={!selectedFile || uploading}>
        {uploading ? '上传中...' : '上传'}
      </button>
      {uploadedUrl && (
        <div>
          <p>上传成功！</p>
          <img src={uploadedUrl} alt="uploaded" style={{ maxWidth: '300px' }} />
        </div>
      )}
    </div>
  );
}

export default FileUpload;
```

---

## 方式二：前端直传（服务端签名）⭐ 推荐

### 适用场景
- 面向C端用户的应用
- 大文件上传
- 高并发上传
- 需要节省服务器带宽和资源

### 优势
- ✅ 上传速度更快（直连COS）
- ✅ 节省后端服务器带宽
- ✅ 降低服务器负载
- ✅ 支持更大并发量
- ✅ 前端永远看不到密钥，更安全

### 流程图
```
步骤1: 前端 → 请求签名 → 后端生成签名 → 返回签名URL
步骤2: 前端 → 直接上传 → 腾讯云COS（不经过后端）
步骤3: 前端 → 通知后端（可选）
```

### 接口

#### 获取上传签名
```
POST /thirdparty/cos/getUploadSign
Content-Type: application/json

请求体:
{
  "fileName": "avatar.jpg",
  "contentType": "image/jpeg",
  "fileSize": 102400,
  "customDir": "avatar"  // 可选
}

响应:
{
  "code": 0,
  "data": {
    "uploadUrl": "https://bucket.cos.region.myqcloud.com/path?sign=xxx",
    "objectKey": "avatar/2025/01/14/xxx.jpg",
    "accessUrl": "https://cdn.example.com/avatar/2025/01/14/xxx.jpg",
    "expiration": "2025-01-14T12:00:00",
    "contentType": "image/jpeg",
    "maxSize": 10485760
  }
}
```

### 前端示例
前端直传会有**跨域问题**，可参考官方文档配置[Web 端直传实践](https://cloud.tencent.com/document/product/436/9067)

#### JavaScript (原生)
```javascript
async function uploadFileWithPresignedUrl() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert('请选择文件');
        return;
    }

    try {
        // 步骤1: 获取上传签名
        console.log('正在获取上传签名...');
        const signResponse = await fetch('http://localhost:8400/thirdparty/cos/getUploadSign', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fileName: file.name,
                contentType: file.type,
                fileSize: file.size,
                customDir: 'test'  // 可选
            })
        });

        const signResult = await signResponse.json();

        if (signResult.code !== 0) {
            alert('获取签名失败: ' + signResult.msg);
            return;
        }

        const { uploadUrl, accessUrl, contentType } = signResult.data;
        console.log('获取签名成功，开始上传...');

        // 步骤2: 直接上传到COS
        const uploadResponse = await fetch(uploadUrl, {
            method: 'PUT',
            headers: {
                'Content-Type': contentType
            },
            body: file
        });

        if (uploadResponse.ok) {
            console.log('上传成功！');
            console.log('文件URL:', accessUrl);
            alert('上传成功！文件URL: ' + accessUrl);

            // 步骤3（可选）: 通知后端保存文件记录
            // await saveFileRecord(accessUrl);
        } else {
            alert('上传失败: ' + uploadResponse.status);
        }

    } catch (error) {
        console.error('上传失败:', error);
        alert('上传失败: ' + error.message);
    }
}
```

#### Vue 3 (完整示例)
```vue
<template>
  <div class="upload-container">
    <input
      type="file"
      @change="handleFileChange"
      accept="image/*"
      :disabled="uploading"
    >
    <button
      @click="uploadFile"
      :disabled="!selectedFile || uploading"
    >
      {{ uploading ? `上传中 ${uploadProgress}%` : '上传' }}
    </button>

    <div v-if="uploadedUrl" class="result">
      <p>上传成功！</p>
      <p>文件URL: {{ uploadedUrl }}</p>
      <img :src="uploadedUrl" style="max-width: 300px">
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8400'

const selectedFile = ref(null)
const uploading = ref(false)
const uploadProgress = ref(0)
const uploadedUrl = ref('')

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0]
  uploadedUrl.value = ''
}

const uploadFile = async () => {
  if (!selectedFile.value) {
    alert('请选择文件')
    return
  }

  uploading.value = true
  uploadProgress.value = 0

  try {
    // 步骤1: 获取上传签名
    console.log('正在获取上传签名...')
    const signResponse = await axios.post(`${API_BASE_URL}/thirdparty/cos/getUploadSign`, {
      fileName: selectedFile.value.name,
      contentType: selectedFile.value.type,
      fileSize: selectedFile.value.size,
      customDir: 'avatar'  // 可选：自定义目录
    })

    if (signResponse.data.code !== 0) {
      throw new Error(signResponse.data.msg)
    }

    const { uploadUrl, accessUrl, contentType } = signResponse.data.data
    console.log('获取签名成功，开始上传到COS...')

    // 步骤2: 直接上传到COS（带进度）
    await axios.put(uploadUrl, selectedFile.value, {
      headers: {
        'Content-Type': contentType
      },
      onUploadProgress: (progressEvent) => {
        uploadProgress.value = Math.round(
          (progressEvent.loaded * 100) / progressEvent.total
        )
      }
    })

    console.log('上传成功！')
    uploadedUrl.value = accessUrl
    alert('上传成功！')

    // 步骤3（可选）: 通知后端保存文件记录
    // await saveFileRecord(accessUrl)

  } catch (error) {
    console.error('上传失败:', error)
    alert('上传失败: ' + error.message)
  } finally {
    uploading.value = false
    uploadProgress.value = 0
  }
}

// 可选：保存文件记录到后端数据库
const saveFileRecord = async (fileUrl) => {
  try {
    await axios.post(`${API_BASE_URL}/api/files/record`, {
      url: fileUrl,
      fileName: selectedFile.value.name,
      fileSize: selectedFile.value.size,
      fileType: selectedFile.value.type
    })
    console.log('文件记录已保存')
  } catch (error) {
    console.error('保存文件记录失败:', error)
  }
}
</script>

<style scoped>
.upload-container {
  padding: 20px;
}

.result {
  margin-top: 20px;
  padding: 10px;
  background: #f0f0f0;
  border-radius: 4px;
}
</style>
```

#### React (完整示例)
```jsx
import React, { useState } from 'react';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8400';

function PresignedUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploadedUrl, setUploadedUrl] = useState('');

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
    setUploadedUrl('');
  };

  const uploadFile = async () => {
    if (!selectedFile) {
      alert('请选择文件');
      return;
    }

    setUploading(true);
    setUploadProgress(0);

    try {
      // 步骤1: 获取上传签名
      console.log('正在获取上传签名...');
      const signResponse = await axios.post(
        `${API_BASE_URL}/thirdparty/cos/getUploadSign`,
        {
          fileName: selectedFile.name,
          contentType: selectedFile.type,
          fileSize: selectedFile.size,
          customDir: 'avatar'  // 可选
        }
      );

      if (signResponse.data.code !== 0) {
        throw new Error(signResponse.data.msg);
      }

      const { uploadUrl, accessUrl, contentType } = signResponse.data.data;
      console.log('获取签名成功，开始上传到COS...');

      // 步骤2: 直接上传到COS（带进度）
      await axios.put(uploadUrl, selectedFile, {
        headers: {
          'Content-Type': contentType
        },
        onUploadProgress: (progressEvent) => {
          const progress = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          setUploadProgress(progress);
        }
      });

      console.log('上传成功！');
      setUploadedUrl(accessUrl);
      alert('上传成功！');

      // 步骤3（可选）: 通知后端保存文件记录
      // await saveFileRecord(accessUrl);

    } catch (error) {
      console.error('上传失败:', error);
      alert('上传失败: ' + error.message);
    } finally {
      setUploading(false);
      setUploadProgress(0);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <input
        type="file"
        onChange={handleFileChange}
        accept="image/*"
        disabled={uploading}
      />
      <button onClick={uploadFile} disabled={!selectedFile || uploading}>
        {uploading ? `上传中 ${uploadProgress}%` : '上传'}
      </button>

      {uploadedUrl && (
        <div style={{ marginTop: '20px', padding: '10px', background: '#f0f0f0' }}>
          <p>上传成功！</p>
          <p>文件URL: {uploadedUrl}</p>
          <img src={uploadedUrl} alt="uploaded" style={{ maxWidth: '300px' }} />
        </div>
      )}
    </div>
  );
}

export default PresignedUpload;
```

---

## 两种方式对比

| 特性 | 服务端直传 | 前端直传（服务端签名）⭐ |
|------|-----------|----------------------|
| 上传速度 | 较慢 | 快 |
| 服务器带宽 | 占用大 | 占用小 |
| 服务器负载 | 高 | 低 |
| 并发能力 | 受限 | 强 |
| 安全性 | 高（可检查文件） | 高（签名有过期时间） |
| 文件处理 | 可以 | 不可以 |
| 实现复杂度 | 简单 | 中等 |
| 适用场景 | 后台管理 | C端应用 |

---

## 配置说明

### Nacos配置（cos.yml）
```yaml
tencent:
  cos:
    secret-id: your-secret-id
    secret-key: your-secret-key
    region: ap-guangzhou
    bucket-name: your-bucket-name
    cdn-url: https://your-bucket.cos.ap-guangzhou.myqcloud.com
```

### 服务端口
- mall-third-party: 8400

---

## 测试

### 服务端直传测试
```bash
cd mall-third-party
mvn test -Dtest=CosUploadTest#testUploadImage
```

### 前端直传测试
```bash
# 1. 启动 mall-third-party 服务
cd mall-third-party
mvn spring-boot:run

# 2. 运行测试
mvn test -Dtest=CosPresignedUploadTest#testPresignedUpload
```

---

## 常见问题

### Q: 签名过期时间是多少？
A: 默认15分钟。前端需要在获取签名后15分钟内完成上传。

### Q: 支持哪些��件类型？
A: 前端直传方式目前仅支持图片（image/*），可在后端配置中修改。

### Q: 文件大小限制是多少？
A: 默认最大10MB，可在后端代码中修改。

### Q: 前端直传失败怎么办？
A: 检查：
1. 签名是否过期
2. Content-Type是否正确
3. 文件大小是否超限
4. 网络连接是否正常

### Q: 是否需要CORS配置？
A: 前端直传需要在腾讯云COS控制台配置CORS规则，允许跨域请求。

---

## 建议

- **后台管理系统**: 使用方式一（服务端直传）
- **用户头像上传**: 使用方式二（前端直传）
- **商品评价图片**: 使用方式二（前端直传）
- **需要敏感词过滤**: 使用方式一（服务端直传）
- **大文件上传**: 使用方式二（前端直传）