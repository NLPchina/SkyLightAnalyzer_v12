package com.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.entity.WordPojo;
import com.zel.manager.LibraryManager;
import com.zel.util.SystemParas;
import com.zel.util.TimeUtil;

public class MainTest extends TestCase {
	// 做日志用
	private static Logger logger = Logger.getLogger(MainTest.class);

	public static void testStringList(List<String> strList) {
		long begin = 0;
		long end = 0;

		String content = "";

		StandardAnalyzer analyzer = new StandardAnalyzer();

		Map<String, List<WordPojo>> wordMap = LibraryManager.wordGroupMap;
		Set<String> keySet = wordMap.keySet();
		List<TermUnit> list = null;

		int length = 0;
		int repeat = 1;
		int i = 0;
		begin = System.currentTimeMillis();
		for (String temp : strList) {
			if (temp == null) {
				continue;
			}
			length += temp.length();
			// list = analyzer.getSplitResult(temp);
			// list = analyzer.getSplitPOSResult(temp);
			list = analyzer.getSplitMergePOSResult(temp);
			if (list != null) {
				for (TermUnit term : list) {
					System.out.print(term.getValue()
							+ "/"
							+ term.getNatureTermUnit().getTermNatureItem()
									.getNatureItem().getName() + ",");
				}
			}
			System.out.println();
		}

		end = System.currentTimeMillis();
		System.out.println("分词共用时" + TimeUtil.getMinuteAndSecond(end - begin));
		System.out.println("分词速率为: "
				+ TimeUtil.getSplitSpeed(length, end - begin) + " 字/秒");
		System.out.println("content length----" + length);
	}

	public static void startCommand() throws Exception {
		// 判断是否开启命令行测试
		if (!SystemParas.console_input_enable) {
			return;
		}
		logger.info("已开启命令行，可以进行命令行测试!");
		logger.info("请输入你的分词测试语句,输入quit或是exit则退出系统!");
		long begin = 0;
		long end = 0;

		String content = "";

		StandardAnalyzer analyzer = new StandardAnalyzer();

		Map<String, List<WordPojo>> wordMap = LibraryManager.wordGroupMap;
		List<TermUnit> list = null;

		int i = 0;
		begin = System.currentTimeMillis();

		InputStream consoleStream = System.in;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				consoleStream, SystemParas.console_input_encoding));
		String temp = null;

		while ((temp = br.readLine()) != null) {
			if (temp == null || temp.trim().length() == 0) {
				continue;
			}
			if (temp.trim().toLowerCase().equals("quit")
					|| temp.trim().toLowerCase().equals("exit")) {
				br.close();
				System.exit(0);
			}
			list = analyzer.getSplitPOSResult(temp);
			if (list != null) {
				for (TermUnit term : list) {
					System.out.print(term.getValue()
							+ "/"
							+ term.getNatureTermUnit().getTermNatureItem()
									.getNatureItem().getName() + ",");
				}
			}
			System.out.println();
		}
		br.close();
	}

	public static void testDic() {
		long begin = 0;
		long end = 0;
		LinkedList<WordPojo> strList = new LinkedList<WordPojo>();
		StandardAnalyzer analyzer = new StandardAnalyzer();

		Map<String, List<WordPojo>> wordMap = LibraryManager.wordGroupMap;
		Set<String> keySet = wordMap.keySet();
		for (String tmp : keySet) {
			strList.addAll(wordMap.get(tmp));
		}
		List<TermUnit> list = null;

		int length = 0;
		int repeat = 1;
		int i = 0;
		begin = System.currentTimeMillis();

		int line_count = 0;
		int all_count = 0;
		for (WordPojo temp : strList) {
			if (temp == null) {
				continue;
			}
			length += temp.getWord().length();
			// list = analyzer.getSplitResult(temp);
			list = analyzer.getSplitPOSResult(temp.getWord());
			if (list != null) {
				if (list.size() > 1) {
					for (TermUnit term : list) {
						System.out.print(term.getValue()
								+ "/"
								+ term.getNatureTermUnit().getTermNatureItem()
										.getNatureItem().getName() + ",");
					}
					line_count++;
					System.out.println();
				}
				all_count++;
			}
		}
		System.out.println("total not valid list size---" + line_count);
		System.out.println("all_count---" + all_count);

		end = System.currentTimeMillis();
		System.out.println("分词共用时" + TimeUtil.getMinuteAndSecond(end - begin));
		System.out.println("分词速率为: "
				+ TimeUtil.getSplitSpeed(length, end - begin) + " 字/秒");
		System.out.println("content length----" + length);
	}

	public static void main(String[] args) throws Exception {
		LinkedList<String> strList = new LinkedList<String>();
		String content = "";
		// strList.add("中国");
		// strList.add("中国人民");
		// strList.add("中华人民共和国");
		// strList.add("中国台北");
		// strList.add("我家门前有一条河，下雨天很难过。");
		// strList
		// .add("【微言大义】 南京市溧水区的筒子们，现在无论是走123省道还是走宁高高速或者宁杭高速去主城，过路费是必不可少滴。但素，在不久滴将来，246省道开通后，嫩们就可以免费去主城啦，这也是唯一一条进入主城完全不收费的道路");
		// strList.add("伊斯坦布尔");
		 strList.add("简直就是中国人的耻辱再看看那怂样。。还抽烟我就想扇两巴掌");
		 strList.add("的确是不错!");
		 strList.add("我认为你是对的，高个子");
		// strList.add("他说的确实在理");
		// strList.add("这的确是真的");
		// strList.add("北京 朝阳区");
		// strList.add("长春市长春节讲话");
		// strList.add("结婚的和尚未结婚的");
		// strList.add("和尚未必幸福");
		// strList.add("结婚的和尚未必幸福");
		// strList.add("一个和尚未能结婚");
		// strList.add("长春市长春节讲话");
		// strList.add("中国人民");
		// strList.add("韩亚航空坠机事件影响很恶劣。");
		// strList.add("结合成分子时");
		// strList.add("旅游和服务是最好的");
		// strList.add("邓颖超生前最喜欢的一个东西");
		// strList.add("中国航天官员应邀到美国与太空总署官员开会");
		// strList.add("上海大学城书店");
		// strList.add("北京大学生前来应聘");
		// strList.add("中外科学名著");
		// strList.add("为人民服务");
		// strList.add("独立自主和平等互利的原则");
		// strList.add("为人民办公益");
		// strList.add("这事的确定不下来");
		// strList.add("费孝通向人大常委会提交书面报告");
		// strList.add("aaa分事实上发货丨和无哦喝完酒");
		// strList.add("不好意思清清爽爽");
		// strList.add("长春市春节讲话");
		// strList.add("中华人民共和国万岁万岁万万岁");
		// strList.add("检察院鲍绍检察长就是在世诸葛.像诸葛亮一样聪明");
		// strList.add("长春市长春药店");
		// strList.add("乒乓球拍卖完了");
		// strList.add("计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸");
		// strList.add("长春市长春药店");
		// strList.add("胡锦涛与神九航天员首次实现天地双向视频通话");
		// strList.add("mysql不支持 同台机器两个mysql数据库之间做触发器");
		// strList
		// .add("孙建是一个好人.他和蔡晴是夫妻两 ,对于每一本好书他都原意一一读取..他们都很喜欢元宵.康燕和他们住在一起.我和马春亮,韩鹏飞都是好朋友,不知道什么原因");
		// strList
		// .add("一年有三百六十五个日出 我送你三百六十五个祝福 时钟每天转了一千四百四十圈我的心每天都藏着 一千四百四十多个思念 每一天都要祝你快快乐乐  每一分钟都盼望你平平安安 吉祥的光永远环绕着你 像那旭日东升灿烂无比 ");
		// strList.add(" 一年有三百六十五个日出");
		// strList.add("学校学费要一次性交一千元");
		// strList.add("发展中国家庭养猪事业");
		// strList.add("安徽省是一个发展中的省");
		// strList.add("北京理工大学办事处");
		// strList.add("上海大学城");
		// strList.add("脚下的一大块方砖地面");
		// strList.add("程序员祝海林和朱会震是在孙健的左面和右面.范凯在最右面.再往左是李松洪");
		// strList.add("中文分词 是一个实现,中文分词是一个实现");
		// strList.add("吃葡萄牙酸");
		// strList.add("葡萄牙的进攻");
		// strList.add("凭医生处方才可购买");
		// strList.add("工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作");
		// strList.add("门把手坏了,门把手夹了");
		// strList.add("一个和尚挑水喝,两个和尚抬水喝,三个和尚无水喝 , 一次性交易就毁了施水才");
		// strList.add("老乡们决定把他重新点燃起来");
		// strList.add("我的同事张三和李四是夫妻");
		// strList.add("下面就为大家整理了央视主持人的帅仔 靓女曝光照");
		// strList.add("两毛五一斤.一斤八两");
		// strList.add("在训练中将知识巩固");
		// strList.add("中将孙建很厉害");
		// strList.add("审讯室里一直陪着我们的两个警察");
		// strList.add("一只胳膊两个警察");
		// strList.add("c语言怎么读写ini文件");
		// strList.add("i am a good boy,do you think so?");
		// strList.add("关卡编辑器");
		// strList.add("eclipse 多项目依赖");
		// strList.add("张媛:猩猩的娃叫陈美希吗？不知为什么让我突然想起了林明美");
		// strList.add("李建民工作了一天");
		// strList.add("李民工作了一天");
		// strList.add("李民工作了爸爸");
		// strList.add("井冈山党建信息化服务新平台");
		// strList
		// .add("如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常。如果您认为不应该使用代理服务器，请调整您的代理设置：转至扳手菜单 > 设置 > 显示高级设置... > 更改代理服务器设置... > LAN 设置，取消选中“为 LAN 使用代理服务器”复选框");
		// strList.add("在业内闻名");
		// strList.add("这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。");
		// strList.add("我不喜欢日本和服。");
		// strList.add("雷猴回归人间。");
		// strList.add("工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作");
		// strList.add("我需要廉租房");
		// strList.add("永和服装饰品有限公司");
		// strList.add("我爱北京天安门");
		// strList.add("abc");
		// strList.add("隐马尔可夫");
		// strList.add("雷猴是个好网站");
		// strList.add("“Microsoft”一词由“MICROcomputer（微型计算机）”和“SOFTware（软件）”两部分组成");
		// strList.add("草泥马和欺实马是今年的流行词汇");
		// strList.add("伊藤洋华堂总府店");
		// strList.add("中国科学院计算技术研究所");
		// strList.add("罗密欧与朱丽叶");
		// strList.add("我购买了道具和服装");
		// strList.add("PS: 我觉得开源有一个好处，就是能够敦促自己不断改进，避免敞帚自珍");
		// strList.add("这幅画真的很漂亮，喜欢的不行");
		// strList.add("湖北省石首市");
		// strList.add("湖北省十堰市");
		// strList.add("总经理完成了这件事情");
		// strList.add("电脑修好了");
		// strList.add("做好了这件事情就一了百了了");
		// strList.add("人们审美的观点是不同的");
		// strList.add("我们买了一个美的空调");
		// strList.add("线程初始化时我们要注意");
		// strList.add("一个分子是由好多原子组织成的");
		// strList.add("祝你马到功成");
		// strList.add("他掉进了无底洞里");
		// strList.add("中国的首都是北京");
		// strList.add("孙君意");
		// strList.add("外交部发言人马朝旭");
		// strList.add("领导人会议和第四届东亚峰会");
		// strList.add("在过去的这五年");
		// strList.add("还需要很长的路要走");
		// strList.add("60周年首都阅兵");
		// strList.add("你好人们审美的观点是不同的");
		// strList.add("买水果然后来世博园");
		// strList.add("买水果然后去世博园");
		// strList.add("但是后来我才知道你是对的");
		// strList.add("存在即合理");
		// strList.add("的的的的的在的的的的就以和和和");
		// strList.add("I love你，不以为耻，反以为rong");
		// strList.add("hello你好人们审美的观点是不同的");
		// strList.add("很好但主要是基于网页形式");
		// strList.add("hello你好人们审美的观点是不同的");
		// strList.add("为什么我不能拥有想要的生活");
		// strList.add("后来我才");
		// strList.add("此次来中国是为了");
		// strList.add("使用了它就可以解决一些问题");
		// strList.add(",使用了它就可以解决一些问题");
		// strList.add("其实使用了它就可以解决一些问题");
		// strList.add("好人使用了它就可以解决一些问题");
		// strList.add("是因为和国家");
		// strList.add("老年搜索还支持");
		// strList
		// .add("干脆就把那部蒙人的闲法给废了拉倒！RT @laoshipukong : 27日，全国人大常委会第三次审议侵权责任法草案，删除了有关医疗损害责任“举证倒置”的规定。在医患纠纷中本已处于弱势地位的消费者由此将陷入万劫不复的境地。 ");
		// strList.add("他说的确实在理");
		// strList.add("长春市长春节讲话");
		// strList.add("结婚的和尚未结婚的");
		// strList.add("结合成分子时");
		// strList.add("旅游和服务是最好的");
		// strList.add("这件事情的确是我的错");
		// strList.add("供大家参考指正");
		// strList.add("哈尔滨政府公布塌桥原因");
		// strList.add("我在机场入口处");
		// strList.add("邢永臣摄影报道");
		// strList.add("BP神经网络如何训练才能在分类时增加区分度？");
		// strList.add("南京市长江大桥");
		// strList.add("应一些使用者的建议，也为了便于利用NiuTrans用于SMT研究");
		// strList.add("长春市长春药店");
		// strList.add("邓颖超生前最喜欢的衣服");
		// strList.add("胡锦涛是热爱世界和平的政治局常委");
		// strList.add("程序员祝海林和朱会震是在孙健的左面和右面, 范凯在最右面.再往左是李松洪");
		// strList.add("一次性交多少钱");
		// strList.add("两块五一套，三块八一斤，四块七一本，五块六一条");
		// strList.add("小和尚留了一个像大和尚一样的和尚头");
		// strList.add("我是中华人民共和国公民;我爸爸是共和党党员; 地铁和平门站");
		// strList.add("二次元乳量，养眼美女，我在泰国用微信");
		// strList.add("周天亮一次姓交了200元。.");
		// strList.add("羊质虎皮");
		// strList.add("明确");
		// strList.add("文过饰非");
		// strList.add("明确");
		// strList.add("搞屁啊");
		// strList.add("绷着脸");
		// strList.add("大海捞针");
		// strList.add("食古不化");
		// strList.add("性情急躁");
		// strList.add("铁公鸡");
		// strList.add("人事不省");
		// strList.add("令人恶心");
		// strList.add("死爱面子");
		// strList.add("鸡蛋里挑骨头");
		// strList.add("大学生");
		// strList.add("淘宝网店");
		// strList.add("磁悬液");
		// strList.add("科涅夫");
		// strList.add("胡乾文，可憐人,也許這就是愛情,和平人結果。");

		// strList.add("变髒");
		// strList.add("一塌煳涂");
		// strList.add("什么东西");
		// strList.add("不是东西");
		// strList.add("操蛋");
		// strList.add("蛋疼的大师");
		// strList.add("不曾失望");

		// strList.add("真心不好的回忆");
		// strList.add("你真的好坏");
		// strList.add("好坏都分不出来");
		// strList.add("你是真好");

		// strList.add("他是真的好悲催");
		// strList.add("黑莓手机");
		// strList.add("但也很纠结");
		// strList.add("进入总决赛已经很不错了");
		// strList.add("暖暖的包裹着你。浅浅的微笑");
		// strList.add("真是傻逼百度");
		// strList.add("傻逼百度");
		// strList.add("好样的百度");
		// strList.add("百度好样的");
		// strList.add("真的很好");
		// strList.add("真的很好色");
		// strList.add("真的很好看");
		// strList.add("真的好复杂");
		// strList.add("真的好难");
		// strList.add("真好");
		// strList.add("好色");
		// strList.add("好难看");
		// strList.add("真心叫好");
		// strList.add("好日子");
		// strList.add("上海吕品");
		// strList.add("人人天地");
		// strList.add("睡眠精灵");
		// strList.add("真的很難確定,難道要誠信為本嗎？");

		// strList.add("可憐之人");
		// strList.add("天天向上生機勃發,很可愛,犯賤的人是沒有辦法的");
		// strList.add("1942年");
		// strList.add("一九四二年");
		// strList.add("30元整");
		// strList.add("eclipse是非常好用的集成的环境，价格为30元");
		// strList.add("八百余");
		// strList.add("九百余");
		// strList.add("一千二百多");
		// strList.add("百分之八十");
		// strList.add("十分之八十");
		// strList.add("千分之八十");
		// strList.add("千分之十");
		// strList.add("千分之八");
		// strList.add("九分之八");

		// strList.add("九分之七的人口都没有了");
		// strList.add("四分之二的比例是好的");
		// strList.add("九分之八");
		// strList.add("数十个");
		// strList.add("一千三百三十二分之二百一十三是没问题的");

		// strList.add("40.3%");
		// strList.add("3/4");

		// strList.add("三五十个人算是多的");
		// strList.add("2014年12月19日");
		// strList.add("30天");
		// strList.add("30天气真不错");

		// strList.add("你好x6");
		// strList.add("真的很历害6s");

//		strList.add("二十四日电");
//		strList.add("二千多家");
//		strList.add("成品一百五十万吨");

		strList.add("姚晨");
		strList.add("凌潇肃");
//		strList.add("之");
		
//		strList.add("七亿多美元");
//		strList.add("排毒可以减肥吗");
//		strList.add("怎么可以排毒");
//		strList.add("纤美瘦身排毒精油 ");

//		strList.add("产后小腹凸起");
//		strList.add("少吃多运动");
		// strList.add("分开给予别人使用");
		// testDic();
		testStringList(strList);
		// startCommand();
	}
}