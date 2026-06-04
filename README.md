# Chatting In Web

[![Build](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/your/repo)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Language](https://img.shields.io/badge/language-Java-orange.svg)]()

一句话说明：基于 Spring Boot 的示例即时聊天项目，集成 WebSocket、MyBatis、Redis（可选）及 AI 聊天演示

---

## 目录
- 关于本项目
- 功能特性
- 快速开始
- 项目结构
- 架构 / 思维导图（Mermaid）
- 配置说明（占位符）
- 开发与调试（PowerShell 命令）
- 测试
- 贡献指南
- 许可与行为规范
- 常见问题（FAQ）

---

## 关于本项目
本项目演示如何构建一个简单的即时聊天服务，主要技术栈包括：
- Spring Boot（后端框架）
- WebSocket（实时通信）
- MyBatis / Mapper（数据持久化）
- Redis（可选，用于缓存/会话）
- Thymeleaf / 静态页面（前端演示）
- AI 聊天模块（演示如何接入外部 AI 服务）

主入口（Java 类）：`src/main/java/com/example/chatting_in_web/ChattingInWebApplication.java`  
构建文件：`pom.xml`（Maven）

---

## 功能特性
- 用户注册 / 登录（示例）
- 私聊 / 群聊（基于 WebSocket）
- 消息持久化（MyBatis 映射）
- 可选 Redis 缓存支持
- AI 聊天接口演示（可接入第三方 AI）
- 简单前端页面示例（位于 `src/main/resources/templates`）

---

## 快速开始（最低要求）
建议环境：
- JDK 11 或更高（以 `pom.xml` 为准）
- Maven 3.6+
- 可选：本地或远程数据库（如 MySQL）、Redis（若启用）

在项目根目录运行（Windows PowerShell）：

```powershell
# 编译并打包
mvn clean package

# 运行（请根据 target 中实际 jar 名称替换）
java -jar .\target\chatting_in_web-0.0.1-SNAPSHOT.jar

# 或在开发模式直接运行
mvn spring-boot:run




---

chatting_in_web/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/example/chatting_in_web/
│  │  │     ├─ ChattingInWebApplication.java
│  │  │     ├─ config/
│  │  │     │  ├─ AiChatHandShake.java
│  │  │     │  ├─ ChatHandShake.java
│  │  │     │  ├─ RedisConfig.java
│  │  │     │  └─ WebSocketConfig.java
│  │  │     ├─ controller/
│  │  │     │  ├─ AIChatController.java
│  │  │     │  ├─ ChatController.java
│  │  │     │  ├─ LoginController.java
│  │  │     │  └─ UserController.java
│  │  │     ├─ dao/
│  │  │     │  ├─ ChatDao.java
│  │  │     │  └─ UserDao.java
│  │  │     ├─ entity/
│  │  │     │  ├─ ChatMessage.java
│  │  │     │  └─ Group.java
│  │  │     ├─ service/
│  │  │     ├─ task/
│  │  │     └─ util/
│  │  └─ resources/
│  │     ├─ application.properties
│  │     ├─ application.yaml
│  │     ├─ mapper/
│  │     │  ├─ ChatMapper.xml
│  │     │  └─ UserMapper.xml
│  │     └─ templates/
│  │        ├─ aichat.html
│  │        ├─ chat.html
│  │        ├─ login.html
│  │        └─ register.html
└─ target/  （构建产物）

Redisson
RabbitMQ
MinIO



