# 快速使用

1. 下载：<https://maven.apache.org/download.cgi>
2. 配置环境变量：
   1. 运行 ->  `SYSDM.CPL` -> 环境变量 —> 系统变量中新建：`MAVEN_HOME=C:\Program Files\Maven\apache-maven-3.8.1` -> 系统变量path中添加：`%MAVEN_HOME%\bin`
3. 配置maven，打开conf下的settings.xml，修改以下配置
   1. 修改仓库位置
      ```xml
      <localRepository>C:/Program Files/Maven/mavenRepository</localRepository>
      ```
   2. 配置镜像源
   3. 配置profile

配置完成后，dos下输入`mvn help:system`检测是否配置成功

# settings.xml


# pom.xml


## plugins

[maven-plugins](https://maven.apache.org/plugins/index.html)

### maven-site-plugin






# 常用命令

| 命令 | 命令说明 | 
| ---- | ---- |
|mvn -v      | 查看maven版本|
|mvn compile | 编译|
|mvn test    | 测试|
|mvn package | 打包|
|mvn clean   | 删除target|
|mvn install | 将代码打包成jar包到本地仓库中|
|mvn archetype:generate | 创建maven目录架构|
|mvn archetype:generate -DgroupId=组织名，公司网址的反写+项目名 -DartifactId=项目名-模块名 -Dversion=版本号 -Dpackage=代码所存在的包名| |
|mvn clean dependency:copy-dependencies -DoutputDirectory=lib| 把当前的pom文件中的依赖给下载下来|
| mvn install:intall-file -Dfile=non-maven-proj.jar -DgroupId=som.group -DartifactId=non-maven-proj -Dversion=1 | 安装插件到本地库 |

# 解决方案

## maven仓库无，不支持的非开源包

1. 安装到本地仓库
2. nexus搭建自己的maven私服并部署：`deploy:deploy-file`
3. 设置scope为system，指定系统路径

# Reference

[Apache Maven guide](https://maven.apache.org/guides/mini/guide-configuring-plugins.html)

[我爱学习网：Maven plugin guide](https://www.5axxw.com/wiki/content/7gol50)

[Maven学习之 插件plugin](http://t.zoukankan.com/larryzeal-p-6181555.html)

[插件jar下载](https://repo.maven.apache.org/maven2/org/apache/maven/plugins/)

[参考学习](https://blog.csdn.net/qq_25667339/article/details/44677429)