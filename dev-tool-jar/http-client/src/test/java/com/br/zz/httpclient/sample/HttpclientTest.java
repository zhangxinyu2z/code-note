package com.br.zz.httpclient.sample;

import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Pattern;

/**
 * @author xinyu.zhang
 * @since 2022/11/15 15:33
 */
public class HttpclientTest {

    @Test
    public void t1() {
        String s1  = "meorient";
        String s2 = "meorientha";
        boolean b = StrUtil.containsIgnoreCase(s1, s2);
        System.out.println(b);
    }

    @Test
    public void tl() {
        LocalDateTime of = LocalDateTime.of(LocalDate.of(2022, 12, 21), LocalTime.of(15, 15, 23));
        System.out.println(of);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate of1 = LocalDate.of(2022, 1, 1);
        LocalDate lastDay = of1.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime of2 = LocalDateTime.of(lastDay, LocalTime.MIN);
        System.out.println(of2);

        LocalDate localDate = LocalDate.now().minusMonths(4);
        String format = dtf.format(localDate);
        System.out.println(format);
    }

    @Test
    public void tc() {
        // 2022-10-26  2022-09-26
        System.out.println(LocalDate.now().minusDays(60));
        System.out.println(LocalDate.of(2022, 10, 24).minusDays(30));
        System.out.println(LocalDateTime.of(LocalDate.of(2022, 9, 24), LocalTime.MIN));

        LocalDateTime of = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.out.println(LocalDate.now().getMonthValue());
        System.out.println(of);

        Pattern compile = Pattern.compile("^([1-9]\\d?)$");
        System.out.println(compile.matcher("").matches());
    }
}
