# LifeIndex - 个人物品识别与管理系统

一个极简主义的 Android 应用，利用 AI 辅助用户管理和查找个人物品。

## 功能特点

- 📸 **智能拍照识别** - 使用相机拍摄物品，AI 自动识别物品特征
- 🏷️ **自动标签生成** - AI 自动生成物品名称、分类和标签
- 🔍 **快速搜索** - 通过名称或标签快速找到物品
- 📱 **Material 3 设计** - 现代化的用户界面，遵循 Material Design 3 规范
- 💾 **本地存储** - 所有数据存储在本地，保护隐私

## 技术栈

- **Kotlin** - 主要编程语言
- **Jetpack Compose** - 现代 UI 工具包
- **Material 3** - 设计系统
- **Room Database** - 本地数据库
- **Navigation Component** - 导航管理
- **Coil** - 图片加载库
- **Coroutines & Flow** - 异步编程

## 项目结构

```
app/src/main/java/com/lifeindex/
├── data/
│   ├── database/        # Room 数据库
│   ├── dao/             # 数据访问对象
│   ├── entity/          # 数据库实体
│   └── repository/      # 数据仓库
├── ui/
│   ├── screens/
│   │   ├── home/        # 首页屏幕
│   │   ├── capture/     # 拍照和识别屏幕
│   │   └── detail/      # 详情屏幕
│   ├── navigation/      # 导航配置
│   ├── components/      # 可复用组件
│   └── theme/           # 主题配置
├── di/                  # 依赖注入
└── MainActivity.kt      # 主活动
```

## 开发路线

### Phase 1: 基础设施 ✅
- [x] Room 数据库设置
- [x] Repository 层
- [x] Navigation Graph

### Phase 2: 核心功能 - 拍照数据流 ✅
- [x] FileProvider 配置
- [x] 相机集成
- [x] 图片存储管理

### Phase 3: 模拟 AI 识别 ✅
- [x] 模拟 AI 分析流程
- [x] UI 状态管理
- [ ] 真实 AI 集成（待实现）

### Phase 4: 数据持久化 ✅
- [x] 物品保存到数据库
- [x] 图片文件管理
- [x] 搜索功能

### Phase 5: 完善与优化
- [x] 图片加载优化
- [x] 搜索实现
- [ ] 编辑功能
- [ ] 删除功能
- [ ] 设置页面

## 构建项目

### 要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.2

### 步骤

1. 克隆仓库
```bash
git clone <repository-url>
cd LifeIndex
```

2. 在 Android Studio 中打开项目

3. 同步 Gradle 文件

4. 运行应用
```bash
./gradlew installDebug
```

## 使用说明

### 添加物品
1. 点击首页的 + 按钮
2. 拍摄物品照片
3. 等待 AI 识别（当前为模拟）
4. 编辑名称、分类和标签
5. 点击"保存并归档"

### 搜索物品
- 使用顶部搜索栏输入关键词
- 或使用分类筛选芯片

### 查看详情
- 点击任意物品卡片查看详细信息

## 未来改进

- [ ] 集成真实 AI 模型（TFLite / OpenAI API）
- [ ] 云端同步
- [ ] 多语言支持
- [ ] 导出功能
- [ ] 统计分析
- [ ] 分享功能

## 许可证

MIT License

## 作者

Samlay

---

**注意**: 当前版本为 MVP (Minimum Viable Product)，AI 识别功能为模拟实现。真实 AI 集成将在后续版本中实现。
