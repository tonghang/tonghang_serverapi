package com.tonghang.web.common.util;

/**
 * 涵盖了server端要使用的所有常量，其中继承了环信Util,负责初始化获得 app管理员的TOKEN
 * @author Administrator
 *
 */
public class Constant extends HuanXinUtil{

	private Constant(){
		
	}
	
	public static final String ISOLATED = "您已被同行管理员封号，如有疑问请联系我们";
	public static final String PASSWORD_INVALID = "邮箱或密码不正确";
	public static final String NOUSER = "用户不存在";
	public static final String EMAIL = "邮件已发送";
	public static final String EXIST = "该邮箱已被注册";
	public static final String AGREE_ADD_MSG = "同意添加您为好友"; 
	public static final String REFUSE_ADD_MSG = "拒绝添加您为好友";
	public static final String INVITATION_MSG = "请求添加您为好友";
	public static final String NEWBE_MSG = "是和您行业相近的新用户";
	public static final String ISOLATE_MSG = "您已被管理员封号，详情请咨询客服tonghangtinghang@126.com";
	public static final String DEISOLATE_MSG = "您已被管理员解封，欢迎您再次使用";
	public static final String DELETE_TOPIC_MSG = "该话题已被房主解散";
	public static final String PICTURE_SERVER = "http://114.215.143.83:8080/tonghang-serverapi";
	public static final String ADV_SERVER = "http://114.215.143.83:8080/tonghang";
	public static final String NATIVE_ADV_SERVER = "http://192.168.23.1:8080/tonghang";
	public static final String HUANXIN_URL = "http://a1.easemob.com/tonghang/tonghang/";
	public static final String IMAGE_PATH = "/images/";
	public static final String IMAGE_NAME = "sign.jpg";
	public static final String ADV_NAME = "advertise_";
	public static final String ADV_PATH = "/resources/";
	
	public static String JPushBasic="ZWI0ZTc5YzRhYjE4MmQ3MjVlYzJmZjE1OmVkMzIxNjdhODY0MWFiMWVlODY1OGIzYQ==";
	public static final String CLIENT_ID = "YXA6zpmeoHX8EeS5LFOLSMeZrA";
	public static final String CLIENT_SECRET = "YXA62xaG_k1OsmSdGYtjIKE3XbO0ahw";
	public static final String CHANGER_ID = "0ba20eab27544eb8867e543b4db8c13e";
	public static final int PAGESIZE = 10;
	
	public static String CREATEGROUP = "server create group";
	public static String HUANXIN_SALT = "HUANXIN-SECRET";
	
	/** METHOD_DELETE value:GET */
	public static String METHOD_GET = "GET";

	/** METHOD_DELETE value:POST */
	public static String METHOD_POST = "POST";

	/** METHOD_DELETE value:PUT */
	public static String METHOD_PUT = "PUT";

	/** METHOD_DELETE value:DELETE */
	public static String METHOD_DELETE = "DELETE";
	

	/** fail code*/
	public static int ERROR = 500;
	public static int SUCCEES = 200;
	
	/**JPUSH Type*/
	public static final String INVITATION = "0";
	public static final String AGREE_ADD_FRIEND = "1";
	public static final String REFUSE_ADD_FRIEND = "2";
	public static final String RECOMMEND_NEWBE = "3";
	public static final String ISOLATE = "4";
	public static final String DEISOLATE = "5";
	public static final String DELETE_TOPIC = "6";
}
