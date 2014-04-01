package com.test;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.entity.util.TermUnitStream;
import com.zel.util.TimeUtil;

public class LuceneUITest extends TestCase {
	// 做日志用
	private static Logger logger = Logger.getLogger(LuceneUITest.class);

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		// LibraryManager.makeTrie();
		long end = System.currentTimeMillis();
		// System.out.println("加载词典用时---"
		// + TimeUtil.getMinuteAndSecond(end - begin));

		LinkedList<String> strList = new LinkedList<String>();

		String content = "";
		
		strList.add("成功");
		// strList.add("eclipse是非常好用的集成环境，价格为30元!");
		// strList.add("下载");
		// strList.add("淘宝网店");
		// strList.add("他说的确实在理");
		// strList.add("这的确是真的");
//		strList.add(null);
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
//		 strList.add("一次性交多少钱");
		// strList.add("两块五一套，三块八一斤，四块七一本，五块六一条");
		// strList.add("小和尚留了一个像大和尚一样的和尚头");
//		strList.add("我是中华人民共和国公民;我爸爸是共和党党员; 地铁和平门站");
		// strList.add("二次元乳量，养眼美女，我在泰国用微信");
		// strList.add("周天亮一次姓交了200元");

		StandardAnalyzer analyzer = new StandardAnalyzer();

		List<TermUnit> list = null;

		begin = System.currentTimeMillis();
		int length = 0;

		for (String temp : strList) {
			if (temp == null) {
				continue;
			}
			length += temp.length();

			StringReader sr = new StringReader(temp);

			TermUnitStream termUnitStream = analyzer.getSplitResult(sr);
			TermUnit termUnit = null;
			while (termUnitStream.hasNext()) {
				termUnit = termUnitStream.getNext();
				System.out.print(termUnit.getValue()+",");
			}

			System.out.println();

		}

		end = System.currentTimeMillis();
		System.out.println("分词共用时" + TimeUtil.getMinuteAndSecond(end - begin));
		System.out.println("分词速率为: "
				+ TimeUtil.getSplitSpeed(length, end - begin) + " 字/秒");
		System.out.println("content length----" + content.length());
	}
}
