# springboot-htl
基于Spring Boot & MyBatis & redis的maven种子系统，用于快速构建中小型API、RESTful API项目。
主要版本：springboot采用2.x版本、mybatis3.4X版本、通用mapper使用4.X版本

1、集成MyBatis、通用Mapper插件、PageHelper分页插件，实现单表业务零SQL，可以说学会使用这套架构将节约你百分之五十的开发时间！
2、集成Druid数据库连接池与监控，默认用户名admin、密码123456，用于监控业务系统的sql使用情况等
3、使用FastJsonHttpMessageConverter，提高JSON序列化速度，用于redis对象的转换等
4、提供基础方法基础服务的封装，对于单表的增删查改，包括多条件查询，分页查询都已经封装好，根据代码生成器生成对应的Model、Mapper、
MapperXML、Service、ServiceImpl、Controller等基础代码，另外，使用模板也有助于保持团队代码风格的统一
5、统一响应结果封装及生成工具、统一异常处理、简单的接口签名认证
6、使用redis作为系统的缓存架构
7、拦截器、过滤器、监听器等实现跨域、签名、token认证等
8、websocket的使用事例
9、定时器scheduling的使用
10、系统日志的记录，可根据日志级别，打印sql日志，用于快速线上定位问题
11、引入HuTool作为通用工具包
12、阿里云短信工具
13、itext进行pdf打印
14、邮件服务
15、乐观锁的使用

<p><a href="https://mapperhelper.github.io/docs/7.use330/" rel="nofollow">通用Mapper文档</a><br>
<a href="http://www.mybatis.org/mybatis-3/zh/index.html" rel="nofollow">MyBatis查看官方中文文档 </a><br>
<a href="https://pagehelper.github.io/" rel="nofollow">MyBatis 分页插件 PageHelper</a><br>
<a href="https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter/">Druid Spring Boot Starter</a><br>
<a href="https://github.com/Alibaba/fastjson/wiki/%E9%A6%96%E9%A1%B5">Fastjson</a><br>
<a href="https://www.jianshu.com/p/576dbf44b2ae" rel="nofollow">前端参数签名怎么生成，建议使用 JWT</a><br>
<a href="http://hutool.mydoc.io/" rel="nofollow">Hutool</a><br>
<a href="https://github.com/abel533/Mapper/wiki/changelog">通用mapper更新地址</a></p>