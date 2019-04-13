# LoginServer
一个示例登陆服务器，使用AES加密，可选DES，需要数据库配合
===
默认数据库名为login_server，其中有用户表users，属性为username和hash。显然username对应用户名，hash对应用户名和密码的散列值。

特性：
- 本程序支持修改密码功能
- 密码全程无明文传输过程

---

更新：
- 哈希函数换用SHA256
