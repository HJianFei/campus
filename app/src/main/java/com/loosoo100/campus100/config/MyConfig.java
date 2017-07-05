package com.loosoo100.campus100.config;

import com.loosoo100.campus100.utils.MyUtils;

/**
 * @author yang 存放常量
 */
public class MyConfig {
    /**
     * 微信支付APPID
     */
    public static final String WEIXIN_APP_ID = "wxd2e31944db9cf56f";
    /**
     * 商户id
     */
//    public static final String WEIXIN_API_KEY = "loosooAcampus100loosooAcampus100";
    /**
     * 微信支付partnerID
     */
//    public static final String WEIXIN_PARTNER_ID = "1304633401";
    /**
     * 微信支付商户号
     */
//    public static final String WEIXIN_MCH_ID = "1304633401";
    /**
     * 服务器地址 http://www.campus100.cn/
     * <p>
     * 小刘本地服务器地址 http://192.168.199.140/
     * <p>
     * 小丽本地服务器地址 http://192.168.199.169:8080/
     * <p>
     * 小丽本地服务器地址 http://192.168.2.115:8080/
     */
    public static final String URL = "http://www.campus100.cn/";
    /**
     * 社团根路径
     */
    // public static final String URL_COMMUNITY_ROOT =
    // "http://192.168.199.169/PAYS/newshopsystem100/index.php/home/Apicommunity1/";
    public static final String URL_COMMUNITY_ROOT = URL
            + "App/index.php/Home/Apicommunity1/";
    /**
     * 借款根路径
     */
    // public static final String URL_CREDIT_ROOT =
    // "http://192.168.199.169/index.php/Home/Apidisposable/";
    public static final String URL_CREDIT_ROOT = URL
            + "App/index.php/Home/Apidisposable/";
    /**
     * 礼物盒子根路径
     */
    public static final String URL_ROOT = URL + "App/index.php/Home/";
    // public static final String URL_GIFT_ROOT =
    // "http://192.168.199.140:81/App/index.php/Home/";
    /**
     * 提现根目录
     */
    public static final String URL_CASH_ROOT = URL
            + "App/index.php/Home/Apitixian/";
    /**
     * 分享送给Ta
     */
    public static final String SHARE_SEND = URL_ROOT
            + "ApiSendSomeOne/benediction";
    /**
     * 凑一凑分享,后面添加订单ID和type(活动凑一凑为2，礼物盒子凑一凑为1)
     */
    public static final String SHARE_PAY_TOGETHER = "www.campus100.cn/App/Home/ApisharePay/shouquan?id=";
    /**
     * 判断用户是否完善资料(post:uid 用户id，type（1企业0学生）)
     */
    public static final String URL_POST_IS_INFO_PERFECT = URL
            + "App/Home/Apiindex/info";
    /**
     * 获取短信验证码
     */
    public static final String URL_POST_MESSAGE = URL_ROOT + "ApiSms/duanxin";
    /**
     * 获取版本信息
     */
    public static final String URL_JSON_VERSION = URL_ROOT + "ApiVersion/num";
    /**
     * 用户注册、修改登录密码、修改支付密码、修改手机号、找回密码
     */
    public static final String URL_POST_REGISTER = URL_ROOT + "ApiSms/reg";
    /**
     * 是否设置过支付密码
     */
    public static final String URL_POST_ISSETTING_PAYPW = URL_ROOT
            + "Apicommunity1/setPwd";
    /**
     * 用户是否存在
     */
    public static final String URL_POST_ACCOUNT_ISEXISTS = URL_ROOT
            + "ApiSms/phonelist?phone=";
    /**
     * 用户登录
     */
    public static final String URL_POST_LOGIN = URL_ROOT + "Apiuser/login";
    /**
     * 企业登录
     */
    public static final String URL_POST_LOGIN_BOSS = URL_ROOT
            + "Apicompany/login";
    /**
     * 用户注册完善信息
     */
    public static final String URL_POST_REGISTER_PERFECT = URL_ROOT
            + "Apiuser/userEdit";
    /**
     * 公司注册完善信息
     */
    public static final String URL_POST_REGISTER_PERFECT_BOSS = URL_ROOT
            + "Apicompany/companyEdit";
    /**
     * 搜索学校数据
     */
    public static final String URL_JSON_SCHOOL = URL_ROOT
            + "ApiSchool/schoolList";
    /**
     * 搜索学校数据(关键词搜索),后面添加学校名
     */
    public static final String URL_JSON_SCHOOL_SEARCH = URL_ROOT
            + "ApiSchool/searchSchool?sName=";
    /**
     * 搜索学校数据(由近到远),后面添加纬度和经度
     */
    public static final String URL_JSON_SCHOOL_DISTANCE = URL_ROOT
            + "ApiSchool/getdistance?lat=";
    /**
     * 借款申请上传数据
     */
    public static final String URL_POST_CREDIT = URL_CREDIT_ROOT + "reply2";
    /**
     * 借款申请查询额度,传用户ID
     */
    public static final String URL_GET_CREDIT_LIMIT = URL_CREDIT_ROOT
            + "ind?uid=";
    /**
     * 借款申请查询信用信息,传用户ID
     */
    public static final String URL_JSON_CREDIT = URL_CREDIT_ROOT
            + "mydispos?uid=";
    /**
     * 贷款还款
     */
    public static final String URL_POST_CREDIT_REPAYMENT = URL_CREDIT_ROOT
            + "hk";
    /**
     * 小卖部类目数据和第一个类目的商品信息
     */
    public static final String URL_JSON_CAT = URL_ROOT
            + "ApiShop/shopgoods?sid=";
    /**
     * 小卖部商品数据,后面添加类目ID
     */
    public static final String URL_JSON_GOODS = URL_ROOT
            + "ApiShop/getGoodsList?catId=";
    /**
     * 小卖部商品数据提交
     */
    public static final String URL_POST_GOODS_PAY = URL_ROOT
            + "ApigoodsPay/pay";
    /**
     * 小卖部商品数据提交
     */
    public static final String URL_POST_GOODS_PAY_SUCCESS = URL_ROOT
            + "ApigoodsPay/truepay";
    /**
     * 小卖部订单列表数据,后面添加用户ID
     */
    public static final String URL_JSON_GOODS_ORDER = URL_ROOT
            + "ApiShop/ordersList?userId=";
    /**
     * 小卖部订单详情数据,后面添加订单ID
     */
    public static final String URL_JSON_GOODS_ORDER_DETAIL = URL_ROOT
            + "ApiShop/orderDetail?orderId=";
    /**
     * 小卖部确认收货,后面添加订单ID
     */
    public static final String URL_POST_GOODS_ORDER_SURE = URL_ROOT
            + "ApiShop/ReceivedGoods?oid=";
    /**
     * 小卖部删除订单,后面添加订单ID
     */
    public static final String URL_POST_GOODS_ORDER_DELETE = URL_ROOT
            + "ApiShop/orderCancel?oid=";
    /**
     * 小卖部确认收货,后面添加订单ID
     */
    public static final String URL_POST_GOODS_ORDER_PAY = URL_ROOT
            + "ApigoodsPay/truepay";
    /**
     * 小卖部投诉
     */
    public static final String URL_POST_GOODS_ORDER_COMPLAIN = URL_ROOT
            + "ApiShop/orderComplain";
    /**
     * 小卖部订单评价
     */
    public static final String URL_POST_GOODS_ORDER_APPRAISE = URL_ROOT
            + "ApiShop/pingjia";
    /**
     * 客服中心意见反馈
     */
    public static final String URL_POST_FEEDBACK = URL_ROOT + "Apikefu/suggest";
    /**
     * 客服中心问答,后面拼接type
     */
    public static final String URL_JSON_CUSTOM = URL_ROOT + "Apikefu/ind";
    /**
     * 小卖部订单退款
     */
    public static final String URL_POST_GOODS_ORDER_REFUND = URL_ROOT
            + "ApiShop/refund";
    /**
     * 礼物盒子类目数据
     */
    public static final String URL_JSON_GIFT_CATEGORY = URL_ROOT
            + "Apigiftcats/catlist";
    /**
     * 礼物盒子商品列表数据,后面要添加商品类目ID(综合排序)
     */
    public static final String URL_JSON_GIFT_GOODS_LIST = URL_ROOT
            + "Apigiftcats/catgoods?catId=";
    /**
     * 礼物盒子商品列表数据,后面要添加商品类目ID(价格降序)
     */
    public static final String URL_JSON_GIFT_GOODS_LIST_PRICEDESC = URL_ROOT
            + "Apigiftcats/pricedesc?catId=";
    /**
     * 礼物盒子商品列表数据,后面要添加商品类目ID(价格升序)
     */
    public static final String URL_JSON_GIFT_GOODS_LIST_PRICEASC = URL_ROOT
            + "Apigiftcats/priceasc?catId=";
    /**
     * 礼物盒子商品列表数据,后面要添加商品类目ID(最新排序)
     */
    public static final String URL_JSON_GIFT_GOODS_LIST_TIMEASC = URL_ROOT
            + "Apigiftcats/timeasc?catId=";
    /**
     * 礼物盒子商品列表数据,后面要添加商品类目ID(销量排序)
     */
    public static final String URL_JSON_GIFT_GOODS_LIST_SOLDDESC = URL_ROOT
            + "Apigiftcats/solddesc?catId=";
    /**
     * 礼物盒子搜索商品列表数据,后面要添加搜索关键词和page
     */
    public static final String URL_JSON_GIFT_GOODS_LIST_SEARCH = URL_ROOT
            + "Apigiftcats/searchGoods?gName=";
    /**
     * 礼物盒子商品详情数据,后面要添加商品ID
     */
    public static final String URL_JSON_GIFT_GOODS = URL_ROOT
            + "Apigiftcats/catgoodsdetail?giftgoodsId=";
    /**
     * 礼物盒子商品收藏
     */
    public static final String URL_POST_GIFT_COLLECT = URL_ROOT
            + "ApiCollect/collectAdd";
    /**
     * 礼物盒子商品取消收藏
     */
    public static final String URL_POST_GIFT_COLLECT_CANCEL = URL_ROOT
            + "ApiCollect/collectDel";
    /**
     * 礼物盒子下订单post所有数据
     */
    public static final String URL_POST_GIFT_PAY = URL_ROOT
            + "ApigiftgoodsPay/pay";
    /**
     * 礼物盒子付款成功后post订单ID
     */
    public static final String URL_POST_GIFT_PAY_SUCCESS = URL_ROOT
            + "ApigiftgoodsPay/truepay";
    /**
     * 获取礼物盒子订单数据,后面添加用户ID
     */
    public static final String URL_JSON_GIFT_ORDER = URL_ROOT
            + "ApigiftgoodsPay/orderlist?userId=";
    /**
     * 获取礼物盒子订单详情数据,后面添加订单ID
     */
    public static final String URL_JSON_GIFT_ORDER_DETAIL = URL_ROOT
            + "ApigiftgoodsPay/orderDetail?orderId=";
    /**
     * 获取礼物盒子订单数据(送出的礼物),后面添加action,用户id,page(action = 0待付款 1待送出 2待发货 3待确认4已确认)
     */
    public static final String URL_JSON_GIFT_ORDER_SEND = URL_ROOT
            + "ApigiftgoodsPay/ordersSendsomeone?action=";
    /**
     * 获取礼物盒子订单数据(收到的礼物),后面添加action,用户id,page(action = 0待付款 1待发货 2待确认 3已确认)
     */
    public static final String URL_JSON_GIFT_ORDER_RECEIVE = URL_ROOT
            + "ApigiftgoodsPay/ordersSendself?action=";
    /**
     * 获取礼物盒子订单数据(凑一凑),后面添加action,用户id,page(action = 0待付款 1待发货 2待确认3已确认)
     */
    public static final String URL_JSON_GIFT_ORDER_TOGETHER = URL_ROOT
            + "ApigiftgoodsPay/GatherTogether?action=";
    /**
     * 礼物盒子订单凑一凑余额付款
     */
    public static final String URL_POST_GIFT_ORDER_TOGETHER_PAY = URL_ROOT
            + "ApigiftgoodsPay/GatherTogetherPay";
    /**
     * 礼物盒子订单凑一凑微信或支付宝付款
     */
    public static final String URL_POST_GIFT_ORDER_TOGETHER_PAY_OTHER = URL_ROOT
            + "ApigiftgoodsPay/reply";
    /**
     * 礼物盒子订单凑一凑备注提交
     */
    public static final String URL_POST_GIFT_ORDER_TOGETHER_REMARK = URL_ROOT
            + "ApigiftgoodsPay/remarkShare";
    /**
     * 删除或取消礼物盒子订单数据,后面添加订单ID
     */
    public static final String URL_POST_GIFT_ORDER_DELETE = URL_ROOT
            + "ApigiftgoodsPay/orderCancel?oid=";
    /**
     * 提醒发货礼物盒子订单数据,后面添加订单ID
     */
    public static final String URL_POST_GIFT_ORDER_REMIND = URL_ROOT
            + "ApigiftgoodsPay/ReminderDelivery?oid=";
    /**
     * 确认礼物盒子订单数据,后面添加订单ID
     */
    public static final String URL_POST_GIFT_ORDER_SURE = URL_ROOT
            + "ApigiftgoodsPay/ReceivedGoods?oid=";
    /**
     * 礼物盒子凑一凑详情(?oid=922&page=1)
     */
    public static final String URL_JSON_GIFT_TOGETHER_DETAIL = URL_ROOT
            + "ApigiftgoodsPay/replyList?oid=";
    /**
     * 社团首页数据
     */
    public static final String URL_JSON_COMMUNITY_HOME = URL_COMMUNITY_ROOT
            + "ind?sid=";
    /**
     * 搜索社团或加入社团,后面添加搜索社团号
     */
    public static final String URL_JSON_COMMUNITY_SEARCH = URL_COMMUNITY_ROOT
            + "joincomn?id=";
    /**
     * 加入社团提交数据
     */
    public static final String URL_POST_COMMUNITY_JOIN = URL_COMMUNITY_ROOT
            + "joinin";
    /**
     * 退出社团提交数据
     */
    public static final String URL_POST_COMMUNITY_OUT = URL_COMMUNITY_ROOT
            + "joinout";
    /**
     * 退出社团提交数据
     */
    public static final String URL_POST_COMMUNITY_OUT2 = URL_COMMUNITY_ROOT
            + "joinouta";
    /**
     * 社团活动数据,后面要添加活动类型ID
     */
    public static final String URL_JSON_COMMUNITY_ACTIVITY = URL_COMMUNITY_ROOT
            + "getcatid?id=";
    /**
     * 某一个社团的活动数据,后面要添加社团ID
     */
    public static final String URL_JSON_COMMUNITY_ACTIVITY_MORE = URL_COMMUNITY_ROOT
            + "getcomnacti?id=";
    /**
     * 我的社团需求,后面要添加社团ID
     */
    public static final String URL_JSON_COMM_NEED_MORE = URL
            + "App/index.php/Home/Apicommunity1/getcomnneed";
    /**
     * 社团详情数据,后面要添加社团ID
     */
    public static final String URL_JSON_COMMUNITY_DETAIL = URL_COMMUNITY_ROOT
            + "getcomnid?id=";
    /**
     * 我的其它社团详情数据,后面要添加社团ID
     */
    public static final String URL_JSON_MYCOMMUNITY_DETAIL = URL_COMMUNITY_ROOT
            + "othercomn?id=";
    /**
     * 社团资金,后面要添加社团ID
     */
    public static final String URL_JSON_MYCOMMUNITY_MONEY = URL_COMMUNITY_ROOT
            + "comnmoney?id=";
    /**
     * 我的社团数据
     */
    public static final String URL_JSON_MYCOMMUNITY = URL_COMMUNITY_ROOT
            + "mycomn?uid=";
    /**
     * 我的活动数据,后面添加用户ID
     */
    public static final String URL_JSON_MYACTIVITY = URL_COMMUNITY_ROOT
            + "myacti?uid=";
    /**
     * 修改社团口号
     */
    public static final String URL_POST_COMMUNITY_SLOGAN = URL_COMMUNITY_ROOT
            + "kouhaos";
    /**
     * 修改社团简介
     */
    public static final String URL_POST_COMMUNITY_SUMMARY = URL_COMMUNITY_ROOT
            + "infos";
    /**
     * 修改社团公告
     */
    public static final String URL_POST_COMMUNITY_NOTICE = URL_COMMUNITY_ROOT
            + "notices";
    /**
     * 移除社团成员
     */
    public static final String URL_POST_COMMUNITY_REMOVE_MEMBER = URL_COMMUNITY_ROOT
            + "delmemb";
    /**
     * 审核社团成员数据
     */
    public static final String URL_JSON_COMMUNITY_AUDITING_MEMBER = URL_COMMUNITY_ROOT
            + "shenhe?cid=";
    /**
     * 社团成员审核通过
     */
    public static final String URL_POST_COMMUNITY_AUDITING_MEMBER = URL_COMMUNITY_ROOT
            + "agree";
    /**
     * 社团成员审核拒绝通过
     */
    public static final String URL_POST_COMMUNITY_AUDITING_MEMBER_REFUSE = URL_COMMUNITY_ROOT
            + "notagree";
    /**
     * 活动详情数据,后面要添加活动ID
     */
    public static final String URL_JSON_ACTIVITY_DETAIL = URL_COMMUNITY_ROOT
            + "getactid?id=";
    /**
     * 活动收藏
     */
    public static final String URL_POST_ACTIVITY_COLLECT = URL_COMMUNITY_ROOT
            + "zan?aid=";
    /**
     * 活动取消收藏
     */
    public static final String URL_POST_ACTIVITY_COLLECT_CANCEL = URL_COMMUNITY_ROOT
            + "qxzan?aid=";
    /**
     * 创建社团post社团名称(返回0则社团名已存在,返回其它数据则为社团号)
     */
    public static final String URL_POST_COMMUNITY_CREATE = URL_COMMUNITY_ROOT
            + "creat1";
    /**
     * 创建社团post所有社团数据(返回1表示进入审核)
     */
    public static final String URL_POST_COMMUNITY_CREATE2 = URL_COMMUNITY_ROOT
            + "creat2";
    /**
     * 学生版社团活动支持,余额支付
     */
    public static final String URL_POST_COMMUNITY_ACTIVITY_SUPPORT = URL_COMMUNITY_ROOT
            + "GatherTogetherPay";
    /**
     * 社团活动免费参加
     */
    public static final String URL_POST_COMM_ACTI_FREE_JOIN = URL
            + "App/Home/Apiindex/freepay";
    /**
     * 企业版社团活动支持,余额支付
     */
    public static final String URL_POST_COMM_ACTI_SUPPORT_BOSS = URL
            + "App/Home/Apiindex/moneypay";
    /**
     * 社团活动筹集详情
     */
    public static final String URL_JSON_COMMUNITY_TOGETHER_DETAIL = URL_COMMUNITY_ROOT
            + "chouji?id=";
    /**
     * 社团活动支持,微信或支付宝支付
     */
    public static final String URL_POST_COMMUNITY_ACTIVITY_SUPPORT_OTHER = URL_COMMUNITY_ROOT
            + "wxcouacti";
    /**
     * 企业版社团活动支持,微信或支付宝支付
     */
    public static final String URL_POST_COMM_ACTI_PAY_OTHER = URL
            + "App/Home/Apiindex/wxpay";
    /**
     * 收货地址数据(后面要添加用户ID)
     */
    public static final String URL_JSON_ADDRESS = URL_ROOT
            + "Apiuser/userAddress?uid=";
    /**
     * 添加收货地址
     */
    public static final String URL_POST_ADDRESS_ADD = URL_ROOT
            + "Apiuser/userAddressAdd";
    /**
     * 编辑收货地址
     */
    public static final String URL_POST_ADDRESS_EDIT = URL_ROOT
            + "Apiuser/userAddressREdit";
    /**
     * 删除收货地址(后面要添加地址ID)
     */
    public static final String URL_POST_ADDRESS_DELETE = URL_ROOT
            + "Apiuser/userAddressDel?aid=";
    /**
     * 获取用户昵称(后面要添加用户ID)
     */
    public static final String URL_JSON_NICKNAME = URL_ROOT
            + "Apiuser/userName?uid=";
    /**
     * 修改用户昵称
     */
    public static final String URL_POST_NICKNAME_EDIT = URL_ROOT
            + "Apiuser/userNameEdit";
    /**
     * 修改公司的用户昵称
     */
    public static final String URL_POST_NICKNAME_EDIT_BOSS = URL_ROOT
            + "Apicompany/nickNameEdit";
    /**
     * 获取用户信息(后面要添加用户ID)
     */
    public static final String URL_JSON_USERINFO = URL_ROOT
            + "Apiuser/getHeadshow?uid=";
    /**
     * 获取公司信息(后面要添加公司ID)
     */
    public static final String URL_JSON_COMPANYINFO = URL_ROOT
            + "Apicompany/cuserDetail?cuid=";
    /**
     * 上传用户头像
     */
    public static final String URL_POST_HEADSHOT = URL_ROOT
            + "Apiuser/myHeadshow";
    /**
     * 上传公司头像
     */
    public static final String URL_POST_HEADSHOT_BOSS = URL_ROOT
            + "Apicompany/myHeadshow";
    /**
     * 获取用户基本信息,后面添加用户ID
     */
    public static final String URL_JSON_USERINFO_BASIC = URL_ROOT
            + "Apiuser/userDetail?uid=";
    /**
     * 修改用户基本信息
     */
    public static final String URL_POST_USERINFO_EDIT = URL_ROOT
            + "Apiuser/userDetailEdit";
    /**
     * 修改企业的用户基本信息
     */
    public static final String URL_POST_USERINFO_EDIT_BOSS = URL_ROOT
            + "Apicompany/cuserDetailEdit";
    /**
     * 快递物流数据(后面要添加物流名称和单号)
     */
    public static final String URL_JSON_DELIVER = URL_ROOT
            + "ApigiftgoodsPay/logistics?coms=";
    /**
     * 提现到银行卡,后面添加用户ID
     */
    public static final String URL_POST_CASHOUT_BANK = URL_CASH_ROOT + "txbank";
    /**
     * 社团资金转出,后面添加用户ID
     */
    public static final String URL_POST_CASHOUT_BANK_COMM = URL_CASH_ROOT
            + "communitytx";
    /**
     * 提现到支付宝,后面添加用户ID
     */
    public static final String URL_POST_CASHOUT_ALIPAY = URL_CASH_ROOT
            + "txalipay";
    /**
     * 提现记录,后面添加用户ID
     */
    public static final String URL_JSON_CASHOUT_HISTORY = URL_CASH_ROOT
            + "txlog?userid=";
    /**
     * 充值
     */
    public static final String URL_POST_CASHIN = URL_ROOT
            + "ApiwxNative2/chongzhi";
    /**
     * 企业版充值
     */
    public static final String URL_POST_CASHIN_BOSS = URL
            + "App/Home/Apiindex/chongzhi";
    /**
     * 消费记录,后面添加action(1一周 2一个月 3三个月)和用户ID
     */
    public static final String URL_JSON_CONSUME_HISTORY = URL_ROOT
            + "ApiRecord/info?action=";
    /**
     * 企业版消费记录,后面添加action(1一周 2一个月 3三个月)和用户ID
     */
    public static final String URL_JSON_CONSUME_HISTORY_BOSS = URL
            + "App/Home/Apiindex/moneyRecord?action=";
    /**
     * 积分记录,后面添加action(1一周 2一个月 3三个月)和用户ID
     */
    public static final String URL_JSON_POINT_HISTORY = URL_ROOT
            + "ApiScoreRecord/info?uid=";
    /**
     * 企业版积分记录,后面添加action(1一周 2一个月 3三个月)和用户ID
     */
    public static final String URL_JSON_POINT_HISTORY_BOSS = URL
            + "App/Home/Apiindex/scoreRecord?action=";
    /**
     * 提交支付密码(返回 3成功 4失败)
     */
    public static final String URL_POST_PAY_PW = URL_ROOT + "ApiPay/pay";
    /**
     * 愿望墙首页,后面添加学校ID
     */
    public static final String URL_JSON_PICTURE_WALL = URL_ROOT
            + "ApiWishing/wishingList?sid=";
    /**
     * 愿望墙(本校),后面添加学校ID和用户ID
     */
    public static final String URL_JSON_PICTURE_WALL_SCHOOL = URL_ROOT
            + "ApiWishing/wishingLists?sid=";
    /**
     * 愿望墙(全国),后面添加用户ID
     */
    public static final String URL_JSON_PICTURE_WALL_COUNTRY = URL_ROOT
            + "ApiWishing/wishingListOrder?uid=";
    /**
     * 愿望墙发布
     */
    public static final String URL_POST_PICTURE_WALL_PUBLISH = URL_ROOT
            + "ApiWishing/addWishing";
    /**
     * 愿望墙赞或踩
     */
    public static final String URL_POST_PICTURE_WALL_ATTITUDE = URL_ROOT
            + "ApiWishing/addAttitude";
    /**
     * 愿望墙管理
     */
    public static final String URL_JSON_PICTURE_WALL_MANAGE = URL_ROOT
            + "ApiWishing/Manager?uid=";
    /**
     * 愿望墙删除,后面添加愿望ID
     */
    public static final String URL_POST_PICTURE_WALL_DELETE = URL_ROOT
            + "ApiWishing/delWishing?wid=";
    /**
     * 举报
     */
    public static final String URL_POST_REPORT = URL_ROOT
            + "ApiReport/reportAdd";
    /**
     * 首页顶部图片
     */
    public static final String URL_JSON_HOME_PIC_TOP = URL_ROOT
            + "ApiAds/indexAd";
    /**
     * 校园圈数据(?sid=40&type=0&uid=183&status=1&page=1)
     */
    public static final String URL_JSON_CAMPUS = URL_ROOT
            + "Apimoments1/getlist?sid=";
    /**
     * 校园圈发表圈圈
     */
    public static final String URL_POST_CAMPUS_PUBLISH = URL_ROOT
            + "Apimoments1/addm";
    /**
     * 校园圈发表评论
     */
    public static final String URL_POST_CAMPUS_APPRAISE = URL_ROOT
            + "Apimoments1/reply";
    /**
     * 校园圈删除圈圈,后面添加圈圈id
     */
    public static final String URL_POST_CAMPUS_DELETE = URL_ROOT
            + "Apimoments1/del?mid=";
    /**
     * 校园圈删除评论,后面添加评论id
     */
    public static final String URL_POST_CAMPUS_DELETE_REPLY = URL_ROOT
            + "Apimoments1/delReply?rid=";
    /**
     * 校园圈赞,后面拼接用户ID和圈圈ID
     */
    public static final String URL_POST_CAMPUS_LIKE = URL_ROOT
            + "Apimoments1/zan";
    /**
     * 校园圈详情,后面添加圈圈id和uid
     */
    public static final String URL_JSON_CAMPUS_DETAILS = URL_ROOT
            + "Apimoments1/mDetail?mid=";
    /**
     * 校园圈消息没阅读的条数,后面添加uid
     */
    public static final String URL_JSON_CAMPUS_NOREAD_COUNT = URL_ROOT
            + "Apimoments1/counts?uid=";
    /**
     * 校园圈消息,后面添加uid和page
     */
    public static final String URL_JSON_CAMPUS_MESSAGE = URL_ROOT
            + "Apimoments1/msgList?uid=";
    /**
     * 校园圈暗恋ta,后面添加mid和wid
     */
    public static final String URL_POST_CAMPUS_LOVE = URL_ROOT
            + "Apicrush1/addCrush";
    /**
     * 校园圈判断是否暗恋ta,后面添加mid和wid
     */
    public static final String URL_JSON_CAMPUS_ISLOVE = URL_ROOT
            + "Apicrush1/show";
    /**
     * 校园圈取消暗恋,post cid
     */
    public static final String URL_POST_CAMPUS_NOLOVE = URL_ROOT
            + "Apicrush1/del";
    /**
     * 校园圈暗恋,后面添加用户ID
     */
    public static final String URL_JSON_CAMPUS_LOVE = URL_ROOT
            + "Apicrush1/crush_list?uid=";
    /**
     * 校园圈互换,get：uid自己id,touid对方id,type互换手机传0，互换微信传
     */
    public static final String URL_POST_CAMPUS_CHANGE = URL_ROOT
            + "Apimoments1/change";
    /**
     * 校园圈同意互换,type：同意互换手机号传0，互换微信传1。uid自己uid。touid:ta的uid。
     */
    public static final String URL_POST_CAMPUS_CHANGE_AGREE = URL_ROOT
            + "Apimoments1/agrees";
    /**
     * 校园圈拒绝互换,type：同意互换手机号传0，互换微信传1。uid自己uid。touid:ta的uid。
     */
    public static final String URL_POST_CAMPUS_CHANGE_NOAGREE = URL_ROOT
            + "Apimoments1/n_agrees";
    /**
     * 判断QQ是否填写
     */
    public static final String URL_JSON_CAMPUS_CHANGE_QQ_STATUS = URL_ROOT
            + "Apimoments1/usersStatus?uid=";
    /**
     * 校园圈互换,get：uid用户id，thisuid别人的id
     */
    public static final String URL_JSON_CAMPUS_CHANGE_STATUS = URL_ROOT
            + "Apimoments1/cstatus";
    /**
     * 校园圈友友列表,get：uid用户id
     */
    public static final String URL_JSON_CAMPUS_FRIEND = URL_ROOT
            + "Apimoments1/changelist?uid=";
    /**
     * 校园圈友友请求列表,get：uid用户id
     */
    public static final String URL_JSON_CAMPUS_FRIEND_REQUEST = URL_ROOT
            + "Apimoments1/changesh?uid=";
    /**
     * 校园圈友友请求同意,get：id（记录id），uid（用户id），type（类型，传0,1）
     */
    public static final String URL_POST_CAMPUS_FRIEND_REQUEST_AGREE = URL_ROOT
            + "Apimoments1/agree";
    /**
     * 校园圈友友请求拒绝,get：id（记录id）
     */
    public static final String URL_POST_CAMPUS_FRIEND_REQUEST_NOAGREE = URL_ROOT
            + "Apimoments1/noagree";
    /**
     * 校园圈友友删除
     */
    public static final String URL_POST_CAMPUS_FRIEND_DEL = URL_ROOT
            + "Apimoments1/delc";
    /**
     * 校园圈暗恋未读个数,传uid
     */
    public static final String URL_JSON_CAMPUS_NOREAD = URL_ROOT
            + "Apicrush1/crush_num?uid=";
    /**
     * 校园圈友友未读个数,传uid
     */
    public static final String URL_JSON_CAMPUS_NOREAD_FRIEND = URL_ROOT
            + "Apimoments1/readcount?uid=";
    /**
     * 校园拍档申请
     */
    public static final String URL_POST_CAMPUS_PARTNER = URL_ROOT
            + "Apiuser/regShops";
    /**
     * 校园拍档申请
     */
    public static final String URL_POST_CAMPUS_LEADER = URL
            + "App/Home/ApiSchoolHandler/add";
    /**
     * 获取省份
     */
    public static final String URL_JSON_PROVINCE = URL_ROOT + "Apiuser/city";
    /**
     * 获取市,后面添加省份ID
     */
    public static final String URL_JSON_CITY = URL_ROOT
            + "Apiuser/city?areaId=";
    /**
     * 获取银行列表
     */
    public static final String URL_JSON_BANK = URL_ROOT + "Apitixian/banklist";
    /**
     * 送水,后面添加用户ID
     */
    public static final String URL_JSON_WATER = URL
            + "App/Home/Apiwater/ind?uid=";
    /**
     * 送水点击需要,后面添加用户ID
     */
    public static final String URL_POST_WATER = URL
            + "App/Home/Apiwater/zan?uid=";
    /**
     * 海归首页列表信息,添加uid和page
     */
    public static final String URL_JSON_OVERSEAS_LIST = URL_ROOT
            + "Apihg/ind?uid=";
    /**
     * 海归学校收藏
     */
    public static final String URL_POST_OVERSEAS_COLLECT = URL_ROOT
            + "Apihg/zan?sid=";
    /**
     * 海归学校取消收藏
     */
    public static final String URL_POST_OVERSEAS_COLLECT_CANCEL = URL_ROOT
            + "Apihg/qxzan?sid=";
    /**
     * 海归学校详情
     */
    public static final String URL_JSON_OVERSEAS_DETAIL = URL_ROOT
            + "Apihg/school?sid=";
    /**
     * 海归问答数据
     */
    public static final String URL_JSON_OVERSEAS_QUESTION = URL_ROOT
            + "Apihg/helplist";
    /**
     * 海归问答数据删除
     */
    public static final String URL_POST_OVERSEAS_QUESTION_DEL = URL_ROOT
            + "Apihg/dele";
    /**
     * 海归提问
     */
    public static final String URL_POST_OVERSEAS_QUESTION_PUBLISH = URL_ROOT
            + "Apihg/post_ques";
    /**
     * 海归提问详情,后面添加问题ID和用户ID
     */
    public static final String URL_POST_OVERSEAS_QUESTION_DETAIL = URL_ROOT
            + "Apihg/replyList?id=";
    /**
     * 海归评论
     */
    public static final String URL_POST_OVERSEAS_APPRAISE = URL_ROOT
            + "Apihg/reply";
    /**
     * 我的收藏，礼物盒子
     */
    public static final String URL_JSON_MYCOLLECT_GIFT = URL_ROOT
            + "ApiCollect/collectList?uid=";
    /**
     * 我的收藏，社团活动
     */
    public static final String URL_JSON_MYCOLLECT_ACTI = URL_ROOT
            + "ApiCollect/collect_acti?uid=";
    /**
     * 我的收藏，海归
     */
    public static final String URL_JSON_MYCOLLECT_OVERSEAS = URL_ROOT
            + "ApiCollect/collect_hg?uid=";
    /**
     * 校园圈添加黑名单(post: uid 我 delid 他)
     */
    public static final String URL_POST_CAMPUS_BLACK = URL
            + "App/Home/Apimoments1/blacklist";
    /**
     * 校园圈取消黑名单(post: uid 我 delid 他)
     */
    public static final String URL_POST_CAMPUS_BLACK_DEL = URL
            + "App/Home/Apimoments1/cancel";
    /**
     * 企业认证
     */
    public static final String URL_POST_IDENTIFICATION_BOSS = URL
            + "App/Home/Apicompany/identification";
    /**
     * 企业简介
     */
    public static final String URL_JSON_SUMMARY_BOSS = URL
            + "App/Home/Apicompany/companyProfile?cuid=";
    /**
     * 修改企业简介
     */
    public static final String URL_POST_SUMMARY_BOSS = URL
            + "App/Home/Apicompany/companyProfileEdit";
    /**
     * 修改公司名称
     */
    public static final String URL_POST_COMPANY_EDIT_BOSS = URL
            + "App/Home/Apicompany/companyNameEdit";
    /**
     * 企业版首页信息
     */
    public static final String URL_JSON_HOME_BOSS = URL
            + "App/Home/Apiindex/ind";
    /**
     * 企业版社团列表
     */
    public static final String URL_JSON_COMM_BOSS = URL
            + "App/Home/Apiindex/hotcomn";
    /**
     * 企业版最给力企业列表
     */
    public static final String URL_JSON_COMPANY_BOSS = URL
            + "App/Home/Apiindex/hotorg";
    /**
     * 企业版活动详情
     */
    public static final String URL_JSON_ACTI_DETAILS_BOSS = URL
            + "App/Home/Apiindex/acti?id=";
    /**
     * 企业版赞助过的社团
     */
    public static final String URL_JSON_COMM_SUPPORT_BOSS = URL
            + "App/Home/Apiindex/mycomn?uid=";
    /**
     * 企业版赞助过的活动
     */
    public static final String URL_JSON_ACTI_SUPPORT_BOSS = URL
            + "App/Home/Apiindex/myacti?uid=";
    /**
     * 企业版收藏活动
     */
    public static final String URL_POST_ACTI_COLLECT_BOSS = URL
            + "App/Home/Apiindex/actizan";
    /**
     * 企业版取消收藏活动
     */
    public static final String URL_POST_ACTI_COLLECT_CANCEL_BOSS = URL
            + "App/Home/Apiindex/actiqxzan";
    /**
     * 企业版收藏的活动
     */
    public static final String URL_JSON_ACTI_MYCOLLECT_BOSS = URL
            + "App/Home/Apiindex/collect_acti?uid=";
    /**
     * 企业版关注社团
     */
//    public static final String URL_POST_COMM_COLLECT_BOSS = URL
//            + "App/Home/Apiindex/comnzan";
    /**
     * 企业版取消关注社团
     */
//    public static final String URL_POST_COMM_COLLECT_CANCEL_BOSS = URL
//            + "App/Home/Apiindex/comnqxzan";
    /**
     * 企业版关注的社团
     */
//    public static final String URL_JSON_COMM_INTEREST_BOSS = URL
//            + "App/Home/Apiindex/oncomn?uid=";
    /**
     * 企业版社团成员列表
     */
    public static final String URL_JSON_COMM_MEMBER_BOSS = URL
            + "App/Home/Apicommunity1/getmember?cid=";
    /**
     * 获取全部城市列表
     */
    public static final String URL_JSON_ALL_CITY = URL
            + "App/Home/Apiindex/allcity";
    /**
     * 获取热门城市列表
     */
    public static final String URL_JSON_HOT_CITY = URL
            + "App/Home/Apiindex/hotcity";
    /**
     * 获取活动列表
     */
    public static final String URL_JSON_ACTI_BOSS = URL
            + "App/Home/Apiindex/newactilist";
    /**
     * 获取校园爱心活动列表
     */
    public static final String URL_JSON_ACTI_HEART = URL
            + "App/Home/Apiindex/weal";
    /**
     * 获取社团活动打赏情况
     */
    public static final String URL_JSON_ACTI_REWARD = URL
            + "App/Home/Apiindex/tip?aid=";
    /**
     * 获取社团活动报名情况
     */
    public static final String URL_JSON_ACTI_MEM_JOIN = URL
            + "App/Home/Apicommunity1/enter?aid=";
    /**
     * 企业版社团需求支持,微信或支付宝支付
     */
    public static final String URL_POST_COMM_NEED_PAY_OTHER = URL
            + "App/Home/Apineed/wxpay";
    /**
     * 企业版收藏需求
     */
    public static final String URL_POST_NEED_COLLECT_BOSS = URL
            + "App/Home/Apineed/needzan";
    /**
     * 企业版取消收藏需求
     */
    public static final String URL_POST_NEED_COLLECT_CANCEL_BOSS = URL
            + "App/Home/Apineed/needqxzan";
    /**
     * 获取需求列表
     */
    public static final String URL_JSON_NEED_BOSS = URL
            + "App/Home/Apineed/needlist";
    /**
     * 获取需求详情(get: id(需求id))
     */
    public static final String URL_JSON_NEED_DETAIL_BOSS = URL
            + "App/Home/Apineed/need?id=";
    /**
     * 获取需求赞助列表(get: id(需求id))
     */
    public static final String URL_JSON_NEED_LIST_BOSS = URL
            + "App/Home/Apineed/chouji?id=";
    /**
     * 调查问卷列表
     */
    public static final String URL_JSON_QUESTION = URL
            + "App/Home/Questionnaire/index";
    /**
     * 调查问卷详情(get: id(问卷id))
     */
    public static final String URL_JSON_QUESTION_DETAIL = URL
            + "App/Home/Questionnaire/view?id=";
    /**
     * 提交调查问卷
     */
    public static final String URL_POST_QUESTION_SUBMIT = URL
            + "App/Home/Questionnaire/submit";
    /**
     * 判断用户是否提交过调查问卷(?qid=16&uid=494)
     */
    public static final String URL_JSON_QUESTION_IS_SUBMIT = URL
            + "App/Home/Questionnaire/hasSubmitted?qid";
    /**
     * 企业版社团需求支持,余额支付
     */
    public static final String URL_POST_COMM_NEED_PAY_BOSS = URL
            + "App/Home/Apineed/moneypay";
    /**
     * 校园圈更换背景图(post：uid pic)
     */
    public static final String URL_POST_CONTACTS_UPDATE_BG = URL
            + "App/index.php/Home/Apimoments1/change_bg";
    /**
     * 社团更换图片(post：cid 社团id，type(bg:背景,logo:主图))
     */
    public static final String URL_POST_COMM_UPDATE_BG = URL
            + "App/index.php/Home/Apicommunity1/change_img";
    /**
     * 新建活动(post:([name(名)],[classify(类别)],[cartid(分类)],[money(所需金额)],[tian(持续天数
     * )],[headimg(封面)],[letter(介绍)],[pic1,pic2...(图片)])、
     * {[type(仅限本校参与,全网1)],[time
     * (截止时间)],[money1(参与人数)],[tian1(活动费用)],[address(地点)
     * ],[starttime(开始时间)],[endtime(结束时间)]})
     */
    public static final String URL_POST_COMM_ACTI_NEW = URL
            + "App/index.php/Home/Apicommunity1/insert_acti";
    /**
     * 新建活动时获取分类列表(get：classify)
     */
    public static final String URL_JSON_COMM_ACTI_TYPE = URL
            + "App/index.php/Home/Apicommunity1/cartid";
    /**
     * 新建需求
     */
    public static final String URL_POST_COMM_NEED_NEW = URL
            + "App/index.php/Home/Apicommunity1/insert_aneed";
    /**
     * 消息列表(1小卖部  2礼物盒子  3社团   4江湖救急)
     */
    public static final String URL_JSON_MESSAGE_LIST = URL
            + "App/Home/Apimsg/msg?uid=";
    /**
     * 消息详情列表(1小卖部  2礼物盒子 ?uid=496&type=1)
     */
    public static final String URL_JSON_MESSAGE_DETAIL_LIST = URL
            + "App/Home/Apimsg/msglist?uid=";
    /**
     * 判断输入的推荐人号码是否存在(注册完善信息和创建社团时)post:agentPhone
     */
    public static final String URL_POST_ISAGENTEXITS = URL
            + "App/Home/Apiuser/if_agent";
    /**
     * 推荐列表(get：uid用户id，type（0用户,1社团）)
     */
    public static final String URL_JSON_AGENTLIST = URL
            + "App/Home/Apiuser/agentlist";
    /**
     * 消息未读条数
     */
    public static final String URL_JSON_MESSAGE_NOREAD = URL
            + "App/Home/Apimsg/msg_count?uid=";
    /**
     * 删除消息
     */
    public static final String URL_POST_MESSAGE_DEL = URL
            + "App/Home/Apimsg/delmsg";
    /**
     * 礼物盒子领取页面信息
     */
    public static final String URL_JSON_GIFT_GET = URL
            + "App/Home/ApigiftgoodsPay/GetTheGift";
    /**
     * 礼物盒子领取
     */
    public static final String URL_POST_GIFT_GET = URL
            + "App/Home/ApigiftgoodsPay/trueGetTheGift";
    /**
     * 礼物盒子送给ta
     */
    public static final String URL_POST_GIFT_SEND = URL
            + "App/Home/ApigiftgoodsPay/sendTa";


    /**
     * 订单状态
     */
    public static final String STATE_RECEIVED = "商家已接单";
    public static final String STATE_NON_PAYMENT = "未付款";
    public static final String STATE_COMMITTED = "订单已提交";
    public static final String STATE_SENDING = "配送中";
    public static final String STATE_REFUND_SUCCEED = "退款成功";
    public static final String STATE_REFUNDING = "退款中";
    public static final String STATE_FINISH = "已完成";

    /**
     * 图库
     */
    public static final int ALBUM_REQUEST_CODE = 1;
    public static final int ALBUM_REQUEST_CODE2 = 3;
    public static final int CAMERA_REQUEST_CODE = 2;
    public static final int CROP_REQUEST_CODE = 4;
    public static final int CROP_REQUEST_CODE2 = 5;
    public static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 目录
     */
    public static final String FILE_URI = MyUtils.getInnerSDCardPath()
            + "/loosoo";
    // public static final String FILE_URI2 = MyUtils.getInnerSDCardPath()
    // + "/loosoo";

    /**
     * 用户头像
     */
    // public static final String IMAGGE_URI = FILE_URI + "/userIcon.png";
    public static final String IMAGGE_URI = FILE_URI + "/";
    /**
     * 简历头像
     */
    public static final String RESUME_IMAGE_URI = FILE_URI + "/resumeIcon.png";

    /**
     * 搜索学校
     */
    public static final int SCHOOL_CODE01 = 1;
    public static final int SCHOOL_CODE02 = 2;
    public static final int SCHOOL_CODE_COMMUNITY = 3;
    public static final int SCHOOL_CODE_STORE = 4;
    public static final int SCHOOL_CODE_HOME = 5;
    /**
     * 搜索学校
     */
    public static final String SCHOOL_SEARCH = "school";
    public static final String SCHOOL_SEARCH_ID = "schoolId";

    /**
     * 单周课程表存放地址
     */
    public static final String COURSE_SINGLE_URI = FILE_URI
            + "/course_single.txt";
    /**
     * 双周课程表存放地址
     */
    public static final String COURSE_DOUBLE_URI = FILE_URI
            + "/course_double.txt";
    /**
     * 是否正在下载更新
     */
    public static boolean isCheckVersion = false;
    // /**
    // * 校园圈查看过最新一条圈圈的发表时间
    // */
    // public static long lastCampusTime = 0;
    /**
     * 二维码扫描请求码
     */
    public static int QRCODE = 1001;
    /**
     * 图片地址
     */
    public static final String PIC_AVATAR = URL + "app/";
    /**
     * 判断是否为好友
     */
    public static final String IS_FRIEND = URL + "app/home/friend/getRel";
    /**
     * 添加好友请求
     */
    public static final String ADD_FRIEND = URL + "app/home/friend/addfriendReq";
    /**
     * 获取好友请求列表
     */
    public static final String GET_FRIEND_REQS_LIST = URL + "app/home/friend/getFriendReqs";
    /**
     * 删除好友
     */
    public static final String DELETE_FRIEND = URL + "app/home/friend/delFriend";
    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = URL + "app/home/friend/getUser";
    /**
     * 更新好友请求状态
     */
    public static final String UPDATE_REQU_STATUS = URL + "app/home/friend/updateFriendReq";
    /**
     * 获取好友列表
     */
    public static final String MY_FRIEND_LIST = URL + "app/home/friend/getFriends";
    /**
     * 获取未入群的好友列表
     */
    public static final String MY_AVAILABLE_FRIEND = URL + "app/home/group/getAvailableFriends";
    /**
     * 把好友或者非好友加入黑名单
     */
    public static final String MOVE_TO_BLACK_LIST = URL + "app/home/friend/addBlacklist";
    /**
     * 获取黑名单列表
     */
    public static final String GET_BLACK_LIST = URL + "app/home/friend/getBlacklist";
    /**
     * 从黑名单中移除
     */
    public static final String REMOVE_FROM_BLACK_LIST = URL + "app/home/friend/rmvBlacklist";
    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = URL + "app/home/group/createGroup";
    /**
     * 更新群组信息
     */
    public static final String UPDATE_GROUP_INFO = URL + "app/home/group/updateGroup";
    /**
     * 获取个人加入的群组列表
     */
    public static final String GET_PERSON_GROUP = URL + "app/home/group/getMyGroups";
    /**
     * 获取群成员
     */

    public static final String GET_GROUP_NUMBER = URL + "app/home/group/getGroupMems";
    /**
     * 邀请好友进入群组
     */
    public static final String ADD_GROUP_MEMBERS = URL + "app/home/group/addGroupMems";
    /**
     * 解散群组
     */
    public static final String DISMESS_GROUP = URL + "app/home/group/dismissGroup";
    /**
     * 退出群组
     */
    public static final String QUIET_GROUP = URL + "app/home/group/rmvGroupMems";
    /**
     * 获取群组详情信息
     */
    public static final String GET_GROUP_DETAIL = URL + "app/home/group/getGroup";
    /**
     * 修改群头像
     */
    public static final String UPDATE_GROUP_AVATAR = URL + "app/home/group/changeGroupAvatar";
    /**
     * 重新获取用户token（适用于token出错的情况下）
     */
    public static final String GET_TOKEN_FROM_RONGYUN = URL + "app/home/friend/refreshToken";
    /**
     * 获取一个群信息
     */
    public static final String GET_GROUP_INFO = URL + "app/home/group/getGroup";
    /**
     * 获取企业信息
     */
    public static final String GET_COMPANY_INFO = URL + "app/home/friend/getCompany";
    /**
     * 获取关注列表
     */
    public static final String GET_FOLLOWS = URL + "app/home/friend/getFollows";
    /**
     * 是否有关注该社团
     */
    public static final String GET_IS_FOCUS = URL + "app/home/friend/isFollowed";
    /**
     * 添加公司关注
     */
    public static final String ADD_FOLLOWS = URL + "app/home/friend/addFollow";
    /**
     * 取消公司关注
     */
    public static final String DEL_FOLLOWS = URL + "app/home/friend/delFollow";
    /**
     * 获取企业关注的社团信息（单个）
     */
    public static final String GET_COMPANY_MEMBER = URL + "app/home/friend/getFollowee";
    /**
     * 根据手机号或昵称进行用户搜索（最多20条）
     */
    public static final String SEARCH_USER = URL + "app/home/search/searchUser";
    /**
     * 限制为同学校搜索时，初始列表（最多20条）
     */
    public static final String SEARCH_SCHOOL_MATE = URL + "app/home/search/schoolSearchIndex";
    /**
     * 根据用户id（社团长），获取社团信息
     */
    public static final String GET_COMMUNITY_ID = URL + "app/home/friend/getCommunity";
    /**
     * 是否有未读好友请求(to_uid:用户)
     */
    public static final String GET_FRIEND_NO_READ = URL + "app/home/friend/isFriendReqsRead";
    /**
     * 读取所有未读好友请求(to_uid:用户)
     */
    public static final String FRIEND_READ = URL + "app/home/friend/readFriendReqs";
    /**
     * 授权群成员
     */
    public static final String GRANT_GROUP_MEMS = URL + "app/home/group/grantGroupMems";
    /**
     * 检查群成员是否有权限
     */
    public static final String IS_GROUP_MEM_GRANTED = URL + "app//home/group/isGroupMemGranted";
    /**
     * 重置管理员权限
     */
    public static final String RESET_GRANT_GROUP_MEMBER = URL + "app/home/group/resetGroupMems";
}

