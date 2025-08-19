Docker 容器化后端部署指南
环境准备
必备软件


Docker Engine (20.10.x 或更高版本)
Docker Compose (v2.x 或更高版本)
Git
JDK 11+
Maven 3.6+
环境检查


bash
docker --version
docker-compose --version
java -version
mvn -version

项目结构


部署前请确保项目结构如下：


plaintext
survey-backend/
├── src/                  # Spring Boot源代码
├── pom.xml               # Maven配置文件
├── Dockerfile            # 后端服务Dockerfile
├── docker-compose.yml    # 容器编排配置
├── nginx.conf            # Nginx配置
├── init.sql              # 数据库初始化脚本
└── README.md             # 项目说明文档

部署步骤
1. 获取代码


bash
git clone <项目仓库地址>
cd survey-backend

2. 构建 Spring Boot 项目


bash
mvn clean package -DskipTests

3. 启动 Docker 容器


bash
docker-compose up -d

4. 验证服务状态


bash
# 查看运行中的容器
docker-compose ps

# 查看服务日志
docker-compose logs -f backend

# 检查数据库连接
docker-compose exec db mysql -uroot -ppassword survey_db -e "SELECT COUNT(*) FROM regions;"

API 测试
使用 curl 测试 API


bash
# 创建区域
curl -X POST http://localhost:80/api/regions \
  -H "Content-Type: application/json" \
  -d '{"projectId":"PROJ002","type":"rectangle","dimensions":{"length":6.5,"width":4.2}}'

# 获取所有区域
curl http://localhost:80/api/regions

# 获取特定区域
curl http://localhost:80/api/regions/1

常见问题排查
容器启动失败


检查端口是否被占用


bash
netstat -tulpn | grep 8080
netstat -tulpn | grep 3306



查看详细日志


bash
docker-compose logs backend
docker-compose logs db

数据库连接问题


确保 docker-compose.yml 中的数据库连接参数正确
检查数据库容器是否正常启动
验证数据库初始化脚本是否执行成功


bash
docker-compose exec db mysql -uroot -ppassword survey_db -e "SHOW TABLES;"

API 访问问题


检查 Nginx 配置是否正确
验证后端服务是否正常响应


bash
docker-compose exec backend curl http://localhost:8080/actuator/health

开发环境配置
热部署设置


在 pom.xml 中添加 devtools 依赖


xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>



启动开发模式


bash
mvn spring-boot:run

备份与更新
数据库备份


bash
docker-compose exec db mysqldump -uroot -ppassword survey_db > backup_$(date +%Y%m%d).sql

更新服务


bash
# 拉取最新代码
git pull

# 重新构建并启动
mvn clean package -DskipTests
docker-compose down
docker-compose up -d --build

生产环境注意事项


安全配置
修改默认密码（数据库、应用账号）
配置 HTTPS（修改 Nginx 配置添加 SSL 证书）
限制容器网络访问权限
性能优化
根据服务器配置调整 JVM 参数
配置数据库连接池参数
添加 Redis 缓存提高性能
监控配置
集成 Prometheus 和 Grafana 监控容器和应用
配置日志聚合（ELK Stack）
设置关键指标告警机制
联系方式
