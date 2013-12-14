2011.11.06 
1、将免费版改为全功能版。【ok】
2、验证通讯录结构是否变化。【】通讯录的表结构发生了变化。需要进一步测验
3、各文本增加版本号信息。 【ok】

删除软件：Settings->Applications->Manage applications
设置中文：Settings->Locale & text->Select locale->中文(简体)

在 test_phonebook项目中检测通讯录的数据库表的变化情况，
将全部属性已经提取出来，现在遇到的问题是qq号信息读取失败。
下一步工作定位原因。

生成version 1.01

2011.11.12
通讯录中，下列信息无法获得。需要进一步分析。 
QQ号
昵称：Fffff没有
网站：gggggg
互联网电话：kkkkkkkkk
备注：dddd  是note字段

获取通讯录的方式出现了新的方式，暂时还不清楚，具体的方式。

