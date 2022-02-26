package com.kt.component.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * @author JavisChen
 * @desc 业务错误码，参考阿里规约
 * @date 2018/5/29 下午2:55
 */
/**
 * 错误码为字符串类型，共 5 位，分成两个部分：错误产生来源+四位数字编号。
 *         说明：错误产生来源分为 A/B/C，A 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付
 *         超时等问题；B 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题；C 表示错误来源
 *         于第三方服务，比如 CDN 服务出错，消息投递超时等问题；四位数字编号从 0001 到 9999，大类之间的
 *         步长间距预留 100。
 *  错误码分为一级宏观错误码、二级宏观错误码、三级宏观错误码。
 *  说明：在无法更加具体确定的错误场景中，可以直接使用一级宏观错误码
 *  分别是：A0001（用户端错误）、 B0001（系统执行出错）、C0001（调用第三方服务出错）。
 *
 *  个人认为：在实际系统开发中，内部应用交互接口没必要把错误码分得太细，这样只会导致增加工作量。
 *          只需要用以上三个一级宏观错误码+具体错误信息返回即可。
 *          如果是暴露给外部应用的能力，才需要考虑把错误码进行细分。
 */
@AllArgsConstructor
@Getter
public enum BizErrorCode {

    /**
     * 成功编码
     */
    OK("000000", "ok"),

    /**
     * 用户端错误
     */
    USER_ERROR("U0001", "User Error"),
    /**
     * 系统执行错误
     */
    SERVER_ERROR("S0001", "Server Error"),
    /**
     * 第三方服务出错
     */
    THIRD_PARTY_ERROR("T0001", "Third Service Error");

    private final String code;
    private final String msg;

}
