## 项目类别

spring boot+maven的模板项目，Java版本17以上

```
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.4</version>
        <relativePath/>
    </parent>
```

## xml配置

依赖包括

1.spring-web启动器

2.mysql数据库连接依赖

3.lombok注解器

这里推两个学习lombok的网址：

官网：[Project Lombok](https://projectlombok.org/)

中文（推荐）：[Java 插件Lombok用法 | Baeldung中文网](https://www.baeldung-cn.com/intro-to-project-lombok)

4.hutool 糊涂工具库（[Hutool](https://doc.hutool.cn/)）

5.knife4j接口文档（[Knife4j · 集Swagger2及OpenAPI3为一体的增强解决方案. | Knife4j](https://doc.xiaominfo.com/)）

6.mybatis-flex（[MyBatis-Flex - MyBatis-Flex 官方网站](https://mybatis-flex.com/)）

\7. 切面编程AOP（[微服务——SpringBoot使用归纳——Spring Boot中的切面AOP处理——Spring Boot 中的 AOP 处理-阿里云开发者社区](https://developer.aliyun.com/article/1658183)）

8.caffeine java自带的本地缓存库

9.Spring Session + Redis

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope> <!-- 仅在测试环境生效，不打包到生产环境 -->
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.36</version>
            <optional>true</optional>
        </dependency>
        <!-- Hutool工具库 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.38</version>
        </dependency>

        <!-- knife4j接口文档 -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>4.2.0</version>
        </dependency>

        <!-- mybatis-flex -->
        <dependency>
            <groupId>com.mybatis-flex</groupId>
            <artifactId>mybatis-flex-spring-boot3-starter</artifactId>
            <version>1.10.0</version>
        </dependency>

        <!-- 代码生成模块 -->
        <dependency>
            <groupId>com.mybatis-flex</groupId>
            <artifactId>mybatis-flex-codegen</artifactId>
            <version>1.11.0</version>
        </dependency>
        <!-- 数据库连接池 -->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
        </dependency>

        <!--切面编程-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
            <version>3.0.5</version>
        </dependency>
        <!--caffeine java自带的 本地缓存库-->
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>

        <!-- Spring Session + Redis -->
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
```

## 代码生成器

在根包下新建generator包，新建MyBatisCodeGenerator 类，编写代码生成器。可以从官网复制实例代码，然后按需求更改即可。

在Mybatis Flex的代码生成器中，支持如下 8 种类型的产物生成，目前只有前6个最基础的

- Entity  实体类 ✅
- Mapper  映射类 ✅
- Service   服务类 ✅
- ServiceImpl  服务实现类 ✅
- Controller  控制类 ✅
- MapperXml  文件 ✅
- TableDef  表定义辅助类
- package-info.java  文件

```
import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

/**
MyBatis Flex 代码生成器s
**/

@Slf4j
public class MyBatisCodeGenerator {

    // 需要生成的表名
    private static final String[] TABLE_NAMES = {"user"};

    public static void main(String[] args) {

        // 获取数据源信息
        Dict dict = YamlUtil.loadByPath("application.yml");
        Map<String, Object> dataSourceConfig = dict.getByPath("spring.datasource");
        String url = String.valueOf(dataSourceConfig.get("url"));
        String username = String.valueOf(dataSourceConfig.get("username"));
        String password = String.valueOf(dataSourceConfig.get("password"));
        // 配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 创建配置内容
        GlobalConfig globalConfig = createGlobalConfig();

        // 通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        // 生成代码
        generator.generate();
    }

    // 详细配置见：https://mybatis-flex.com/zh/others/codegen.html
    public static GlobalConfig createGlobalConfig() {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        // 设置根包，建议先生成到一个临时目录下，生成代码后，再移动到项目目录下
        globalConfig.getPackageConfig()
                .setBasePackage("com.example.genresult");//临时路径

        // 设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                // 设置逻辑删除的默认字段名称
                .setLogicDeleteColumn("isDelete");

        // 设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        // 设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();

        // 设置生成 service
        globalConfig.enableService();
        globalConfig.enableServiceImpl();

        // 设置生成 controller
        globalConfig.enableController();

        // 设置生成时间和字符串为空，避免多余的代码改动
        globalConfig.getJavadocConfig()
                .setAuthor("<a href=\"\">程序员</a>")
                .setSince("");
        return globalConfig;
    }
}
```

### 关键点使用方法

1.user是数据库表的名字，根据自己项目数据库名表名进行修改

2.临时路径com.example.genresult  这个路径根据自己的项目改变

3.设置生成时间和字符串为空，避免多余的代码改动，和作者名定义

```
    // 需要生成的表名
    private static final String[] TABLE_NAMES = {"user"};

// 设置根包，建议先生成到一个临时目录下，生成代码后，再移动到项目目录下
        globalConfig.getPackageConfig()
                .setBasePackage("com.example.yuaicodemother.genresult");//临时路径
                
// 设置生成时间和字符串为空，避免多余的代码改动，和作者名定义
        globalConfig.getJavadocConfig()
                .setAuthor("<a href=\"\">程序员</a>")
                .setSince("");
        return globalConfig;
```

## 启动类Been注解添加

```
@MapperScan("com.example.mapper")
```

## yml基础配置

```
spring:
  application:
    name: 项目名
  # mysql
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/数据库名
    username: root
    password: 密码

server:
  port: 8123 //自定义运行端口
  servlet:
    context-path: /api


# springdoc-openapi
springdoc:
  group-configs:
    - group: 'default'
      paths-to-match: '/**'
      packages-to-scan: com.example.generaltemplate.controller
# knife4j
knife4j:
  enable: true
  setting:
    language: zh_cn
```

### 执行

修改代码生成器，运行一次MyBatisCodeGenerator 类通过访问 http://localhost:8123/api/doc.html#/home查看*knife4j接口测试，如果能看到如图的  user-controller  表示成功，调试人员可以根据，实现服务方法，根据业务逻辑测试接口稳定性。*

![img](https://jcnlauhka1t3.feishu.cn/space/api/box/stream/download/asynccode/?code=MjUyNDEzNmY0YWRlNGU1NDFiMjQ0MmY0ZmQ0MjU4YTVfVFJYNDlzcFg4enJaS21vRWVtUlVqbVB2Z1U3UHhrU0JfVG9rZW46TGlSd2JDMVFkb04zNkd4TUpnQmNGRWRablpjXzE3NzA2MjE5NzQ6MTc3MDYyNTU3NF9WNA)

注意

每个Java版本和spring boot版本太低会不支持本文中的依赖版本，需要根据需求更新版本，不清楚可以去调用AI了解。