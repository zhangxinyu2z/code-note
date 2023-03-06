package com.wk.xin.utils.demo.beans;

import junit.framework.TestCase;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author xinyu.zhang
 * @since 2022/11/7 14:05
 */
public class BeanUtilsTest extends TestCase {

    /**
     * org.springframework.beans.BeanUtils
     * 1、 source 属性使用包装类型，target使用primitive => IllegalArgumentException
     * 2、 String 不会处理空格，null    会被copy到target对象中
     * 3、 update数据库记录，慎用，最好是，查询原数据，然后使用set修改更新的数据，再update回数据库
     */
    public void testCopy() {
        OrderDTO orderDTO = new OrderDTO().setId(1L).setUserId(1l).setOrderNo(" 20210518000001 ").setGmtCreated(new Date())
            .setReceivedAmount(null).setRemark("已收金额，前端未传");

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderDTO, orderVO, OrderVO.class);

        System.out.println(orderVO);
    }

    @Data
    @Accessors(chain = true)
    static class OrderDTO {
        private long id;

        private Long userId;

        private String orderNo;

        private Date gmtCreated;

        /**
         * 已收金额
         * 单位：分
         */
        private Long receivedAmount;

        /**
         * 备注
         */
        private String remark;
    }

    @Data
    static class OrderVO {
        private long id;

        private long userId;

        private String orderNo;

        private Date gmtCreated;

        /**
         * 已收金额
         * 单位：分
         */
        private Long receivedAmount;

        /**
         * 备注
         */
        private String remark;
    }
}
