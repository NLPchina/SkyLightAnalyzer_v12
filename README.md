SkyLightAnalyzer_v12
====================

天亮分词器第12个小版本

1、基于双数组(DAT,Double-Array Trie)实现的的trie树结构一个分词组件，对词典有一定的依赖性，对“纯字母”、“纯数字”的字符串截断式匹配有过滤功能

2、该版本的分词的准确率主要依赖于词典的完整性，并未做未登录词、人名、地名的识别，但对“纯字母”和“纯数字”的
    截断式匹配进行了过滤。

3、词典词汇量为23万，trie树构建用时2分钟，项目中已经做了相应的trie树的文件加载功能，无需用一次构建一次。

4、分词速率约为150万字符/s，准确率为94%。

5、测试demo类为MainTest.java，可以将项目直接down到本地，导入MyEclipse8.5及以上版本即可。

6、很大程序借鉴了ansj分词的设计思路与做法并进行了重构，词典选用ansj_seg分词所提供的词典，感谢ansj兄对本项目的技术支持。

7、欢迎加入爬虫、自然语言处理技术群320349384，交流促进成长，开源成就未来。
