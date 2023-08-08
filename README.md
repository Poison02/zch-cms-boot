# 项目简介
项目基于SpringBoot3、SpringSecurity6、JWT、Redis、Knife4j、Mybatis-Plus等搭建的前后端分离后台权限管理系统。
# 项目特色
- 使用新版的SpringBoot3.x；
- 使用SpringSecurity和JWT进行认证鉴权；
- 基于RBAC模型的权限设计，细粒度接口方法，按钮级别权限控制；
- 使用Knife4j接口文档，清晰通俗易懂。
# 运行环境
- JDK17+
- IDEA lombok插件
- MySQL8.0+
- Redis7.0+
- IDEA MapStruct Support插件
# 项目地址
后端：[https://github.com/Poison02/zch-cms-boot](https://github.com/Poison02/zch-cms-boot)
# 接口文档
- `knife4j`接口文档：[http://localhost:8989/doc.html](http://localhost:8989/doc.html)
- `swagger`接口文档：[http://localhost:8989/swagger-ui/index.html](http://localhost:8989/swagger-ui/index.html)
# 项目运行
1. 执行SQL文件
2. 修改 `application.yml`数据库配置
3. 启动 `ZchCmsBootApplication`
4. 访问 [http://localhost:8989/doc.html](http://localhost:8989/doc.html)