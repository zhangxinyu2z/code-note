package com.eltorofuerte.utils.demo.beans;

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
     * 使用Spring的BeanUtils工具转换对象
     * 1、 数据类型不一致，可能导致异常，比如包装类型可以接收null，无法转成基本数据类型
     * 2、 字符串数据，未做处理：空格等，也会copy进去
     * 3、 会copy null数据， 如果做数据库记录更新，先查数据，只能处理指定字段
     */
    public void testCopy() {
        OrderDTO orderDTO = new OrderDTO().setId(1L).setUserId(123L).setOrderNo(" 20210518000001 ").setGmtCreated(new Date())
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
