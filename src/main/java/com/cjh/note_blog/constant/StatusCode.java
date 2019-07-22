package com.cjh.note_blog.constant;

/**
 * ：
 * 状态码定义
 * @author ChangJiahong
 * @date 2019/7/17
 */
public enum StatusCode {
    /**
     * 请求成功：200~299
     */
    OK(200,"OK","成功"),
    /**
     * 账户已登录
     */
    TheAccountHasBeenLoggedIn(201, "Your Account Is Logged In ,No login is required！", "你的账户已登录!不用重新登录！"),
    /**
     * 注销成功
     */
    LogoutOfSuccess(202, "Cancellation Of Success", "注销成功"),

    /* 失败：>= 300   */

    /**
     * 请求失败
     */
    FAIL(500, "The Request Failed", "请求失败"),

    /*  服务器错误  */

    /**
     * 服务器错误
     */
    ServerError(1000, "Server Error", "服务器错误"),

    /*  参数错误：10001~19999   */

    /**
     * 参数无效
     */
    ParameterIsInvalid(10001, "Parameter Is Invalid", "参数无效"),
    /**
     * 参数为空
     */
    ParameterIsNull(10002, "Parameter Is Null", "参数为空"),
    /**
     * 参数类型错误
     */
    ParameterTypeError(10003, "Parameter Type Error", "参数类型错误"),
    /**
     * 参数缺失
     */
    ParametersAreMissing(10004, "Parameters Are Missing", "参数缺失"),

    /* 用户错误：20001~29999   */

    /**
     * 用户未登录
     */
    UserNotLoggedIn(20001, "User Not Logged In", "用户未登录"),
    /**
     * 账号不存在或密码错误
     */
    NoAccountOrPasswordError(20002, "No Account Or Password Error", "账号不存在或密码错误"),
    /**
     * 账号已被禁用
     */
    TheAccountHasBeenDisabled(20003, "The AccountHas Been Disabled", "账号已被禁用"),
    /**
     * 用户不存在
     */
    UsersDonTExist(20004, "Users DonT Exist","用户不存在"),
    /**
     * 用户已存在
     */
    TheUserAlreadyExists(20005, "The User Already Exists","用户已存在"),
    /**
     * 密码错误
     */
    PasswordMistake(20006, "Password Mistake", "密码错误"),

    /* 业务错误：30001-39999 */

    /**
     * 某业务出现问题
     */
    SomethingIsWrongWithTheBusiness(30001, "Something Is Wrong With The Business", "某业务出现问题"),
    /**
     * 数据审核提交失败
     */
    DataAuditSubmissionFailed(30002, "Data Audit Submission Failed", "数据审核提交失败"),
    /**
     * 文章内容为空
     */
    TheArticleIsEmpty(30003, "The Content Of The Post Is Not Empty", "文章内不能为空"),
    /**
     * 文章标题为空
     */
    TheTitleOfTheArticleIsEmpty(30004, "The Title Of The Post Is Not Empty","文章标题不能为空"),
    /**
     * 文章状态为空
     */
    ArticleStatusIsEmpty(30005, "The Post Status Is Not Empty", "文章状态不能为空"),

    /**
     * 文章种类不存在
     */
    CategoryNotExist(30006, "Article Category Not Exist", "文章种类不存在"),


    /* 系统错误：40001-49999 */

    /**
     * 系统繁忙,请稍后重试
     */
    SystemIsBusy(40001, "The System Is Busy,Please Try Again Later","系统繁忙,请稍后重试"),

    /* 数据错误：50001-599999 */

    /**
     * 数据未找到
     */
    DataNotFound(50001, "Data Not Found", "数据未找到"),
    /**
     * 数据有误
     */
    TheDataIsWrong(50002, "The Data Is Wrong", "数据有误"),
    /**
     * 数据已存在
     */
    DataAlreadyExists(50003, "Data Already Exists", "数据已存在"),
    /**
     * 查询出错
     */
    QueryError(50004, "Query Error", "查询出错"),
    /**
     * 执行数据库错误
     */
    ExecutionDatabaseError(50005, "Execution Database Error", "执行数据库错误"),


    /* 接口错误：60001-69999 */

    /**
     * 内部系统接口调用异常
     */
    TheInternalSystemInterfaceCallsExceptions(60001, "The Internal System Interface Calls Exceptions", "内部系统接口调用异常"),
    /**
     * 外部系统接口调用异常
     */
    ExternalSystemInterfaceCallsException(60002, "External System Interface Calls Exception", "外部系统接口调用异常"),
    /**
     * 该接口禁止访问
     */
    TheInterfaceIsInaccessible(60003, "The Interface Is Inaccessible", "该接口禁止访问") ,
    /**
     * 接口地址无效
     */
    InterfaceAddressInvalid(60004, "Interface Address Invalid", "接口地址无效"),
    /**
     * 接口请求超时
     */
    InterfaceRequestTimeout(60005, "Interface Request Timeout", "接口请求超时"),
    /**
     * 过度界面加载
     */
    ExcessiveInterfaceLoad(60006, "Excessive Interface Load", "过度界面加载"),

    /* 权限错误：70001-79999 */

    /**
     * 无权限访问
     */
    UnprivilegedAccess(70001, "Unprivileged Access", "没有访问权限"),

    /*  token错误：80001-89999 */

    /**
     * token过期
     */
    TokenExpired(80001, "Token Expired", "token已过期"),
    /**
     * token不合法
     */
    TokenIsNotValid(80002, "Token Is Not Valid", "token不合法"),
    /**
     * token为空
     */
    TokenIsEmpty(80003, "Token Not Found", "没有找到token"),
    /**
     * 登录状态失效
     */
    LogonStateFailure(80004, "Logon State Failure", "登录状态失效"),
    ;





    /**
     * 英文信息
     */
    private final String msgUS;
    /**
     * 中文信息
     */
    private final String msgCN;
    private final int code;

    private StatusCode(int code, String msgUS, String msgCN){
        this.msgUS = msgUS;
        this.msgCN = msgCN;
        this.code = code;
    }

    private StatusCode(){
        this.msgUS = "";
        this.msgCN = "";
        this.code = 000;
    }


    public String msgUS() {
        return msgUS;
    }

    public String msgCN() {
        return msgCN;
    }

    public int code() {
        return code;
    }

    /**
     * 判断状态码是否有效
     * @return
     */
    public boolean isSuccess(){
        return isSuccess(this.code);
    }

    public static boolean isSuccess(int code){
        if (code >= 200 && code <= 299){
            return true;
        }
        return false;
    }
    /**
     * 返回状态码
     * @param code
     * @return
     */
    public static StatusCode valueOf(int code) {
        for (StatusCode statusCode : values()) {
            if (statusCode.code() == code) {
                return statusCode;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");

    }

    /**
     * 返回中文msg
     * @param code
     * @return
     */
    public static String valueOfCN(int code) {
        return valueOf(code).msgCN;
    }

    /**
     * 英文msg
     * @param code
     * @return
     */
    public static String valueOfUS(int code){
        return valueOf(code).msgUS;
    }


}
