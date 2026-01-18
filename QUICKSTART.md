# LifeIndex 项目快速开始指南

## 📱 项目概述

LifeIndex 是一个基于 Android 的个人物品识别与管理系统，采用 Material 3 设计，支持拍照识别、智能标签、快速搜索等功能。

## 🏗️ 项目架构

本项目采用 MVVM 架构 + Clean Architecture 原则：

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  (Compose + Material3 + Navigation)    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          ViewModel Layer                │
│  (State Management + Business Logic)   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          Data Layer                     │
│  (Repository + DAO + Database)         │
└─────────────────────────────────────────┘
```

## 📁 项目结构说明

### 核心模块

1. **data/** - 数据层
   - `entity/` - 数据库实体（ItemEntity）
   - `dao/` - 数据访问对象（ItemDao）
   - `database/` - Room 数据库配置（AppDatabase）
   - `repository/` - 数据仓库（ItemRepository）

2. **ui/** - UI 层
   - `screens/` - 各个屏幕的 UI 和 ViewModel
     - `home/` - 首页（物品列表、搜索、筛选）
     - `capture/` - 拍照识别页
     - `detail/` - 物品详情页
   - `components/` - 可复用 UI 组件
   - `navigation/` - 导航配置
   - `theme/` - 主题配置

3. **di/** - 依赖注入
   - `ViewModelFactory` - ViewModel 工厂类

4. **MainActivity.kt** - 应用入口
5. **LifeIndexApplication.kt** - Application 类

## 🚀 快速开始

### 前置要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.2

### 构建步骤

1. **克隆项目**
   ```bash
   git clone <your-repo-url>
   cd LifeIndex
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目目录
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 Run 按钮 (或按 Shift + F10)
   - 或使用命令行：
     ```bash
     ./gradlew installDebug
     ```

## 🎯 核心功能流程

### 1. 添加物品流程

```
点击 + 按钮
    ↓
启动相机拍照
    ↓
保存照片到临时文件
    ↓
AI 分析（模拟）
    ↓
显示确认页面
    ↓
用户编辑信息
    ↓
保存到数据库
```

### 2. 数据流向

```
Camera Intent
    ↓
FileProvider (URI 转换)
    ↓
CaptureViewModel (状态管理)
    ↓
ItemRepository (数据处理)
    ↓
Room Database (持久化)
```

## 📸 相机集成说明

### FileProvider 配置

项目已配置好 FileProvider，用于安全地共享文件：

1. **AndroidManifest.xml** - 声明 FileProvider
2. **res/xml/file_paths.xml** - 配置文件路径
3. **CaptureViewModel** - 创建和管理照片 URI

### 拍照流程（待实现）

当前版本的拍照 UI 已完成，但需要添加相机启动代码。参考实现：

```kotlin
// 在 CaptureScreen 中添加
val cameraLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.TakePicture()
) { success ->
    if (success) {
        viewModel.analyzeImage(currentPhotoPath)
    }
}
```

## 🔧 关键技术点

### 1. Room 数据库

- **Entity**: `ItemEntity` - 物品数据模型
- **DAO**: `ItemDao` - 数据库操作接口
- **Database**: `AppDatabase` - 数据库配置

### 2. Jetpack Compose

- 使用 `@Composable` 函数构建 UI
- `StateFlow` 用于状态管理
- `LaunchedEffect` 处理副作用

### 3. Material 3

- `Scaffold` - 页面框架
- `Card` - 卡片组件
- `SearchBar` - 搜索栏
- `FilterChip` - 筛选芯片
- `NavigationBar` - 底部导航

### 4. Navigation Compose

- 类型安全的导航路由
- 参数传递（itemId）
- 返回栈管理

## 📝 开发待办事项

### 当前已完成 ✅

- [x] 项目架构搭建
- [x] Room 数据库配置
- [x] UI 界面实现（Home, Capture, Detail）
- [x] ViewModel 和状态管理
- [x] 基础导航配置
- [x] FileProvider 配置

### 待实现功能 🚧

1. **相机集成** - 完善相机启动和回调处理
2. **真实 AI 集成** - 替换模拟 AI 为真实识别
3. **编辑功能** - 允许编辑已保存的物品
4. **删除功能** - 删除物品及其图片
5. **权限处理** - 动态请求相机权限
6. **图片优化** - 压缩和缓存优化
7. **错误处理** - 完善异常处理和用户提示
8. **单元测试** - 添加测试用例

## 🐛 已知问题

1. **相机功能未完全实现** - UI 已就绪，需要添加相机启动逻辑
2. **AI 识别为模拟** - 目前使用 2 秒延迟模拟，未接入真实 AI
3. **权限请求缺失** - 需要添加运行时权限请求

## 🔐 权限说明

应用需要以下权限：

- `CAMERA` - 拍照功能
- `WRITE_EXTERNAL_STORAGE` - 保存图片（Android 9 及以下）

## 📊 数据模型

```kotlin
data class ItemEntity(
    id: Long,              // 唯一标识
    imagePath: String,     // 图片本地路径
    title: String,         // 物品名称
    category: String,      // 分类
    tagsJson: String,      // 标签（逗号分隔）
    createTime: Long,      // 创建时间戳
    analysis: String       // AI 分析结果
)
```

## 🎨 自定义主题

主题配置位于 `ui/theme/Theme.kt`，可以自定义：

- 颜色方案（ColorScheme）
- 字体样式（Typography）
- 形状样式（Shapes）

## 📚 依赖库版本

主要依赖：

- Compose BOM: 2023.10.01
- Material 3: 最新稳定版
- Room: 2.6.1
- Coil: 2.5.0
- Navigation: 2.7.5

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

MIT License - 详见 LICENSE 文件

## 👨‍💻 作者

Samlay

## 📮 联系方式

如有问题或建议，欢迎提 Issue！

---

**提示**: 首次运行可能需要下载 Gradle 和依赖，请确保网络连接稳定。
