package com.eltorofuerte.assertj.demo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Maps;
import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * @author xinyu.zhang
 * @since 2022/11/7 11:42
 */
public class AssertJTest {

    /**
     * 断言
     * import static org.assertj.core.api.Assertions.*;
     */
    @Test
    public void first() {
        Assertions.assertThat("hello world").isEqualTo("world");
    }

    /**
     * 断言对象
     */
    @Test
    public void assertJForObject() {
        Human el_toro = new Human().setName("El Toro").setAge(27);
        Human james_bond = new Human().setName("James Bond").setAge(36);
        Assertions.assertThat(el_toro).isEqualTo(james_bond);
    }

    /**
     * 断言对象，按字段递归比较
     */
    @Test
    public void assertJObjectByField() {
        Human el_toro = new Human().setName("El Toro").setAge(27);
        Human el_toro_2 = new Human().setName("El Toro").setAge(27);

        // assertion succeeds as the data of both objects are the same.
        Assertions.assertThat(el_toro).usingRecursiveComparison().isEqualTo(el_toro_2);
    }

    @Data
    @Accessors(chain = true)
    static class Human {
        private String name;
        private int age;
    }

    /**
     * 检查两个数值在一定精度范围内是否相等, withPrecision(Double offset) 方法生成偏移量对象
     */
    @Test
    public void testNumeric() {

        Assertions.assertThat(5.1d).isEqualTo(5, Assertions.withPrecision(1d));
    }

    /**
     * 断言 布尔类型
     */
    @Test
    public void assertJBoolean() {
        Assertions.assertThat("".isEmpty()).isTrue();
    }

    /**
     * 断言 字符
     *
     * 字符类型断言一般涉及比较和检查字符是否来自Unicode表
     */
    @Test
    public void assertCharacter() {
        Assertions.assertThat('c').isNotEqualTo('a').inUnicode().isGreaterThanOrEqualTo('a').isLowerCase();
    }

    /**
     * 断言 Iterable/Array
     */
    @Test
    public void assertArrayOrIterable() {
        List<String> sweetWords = Arrays.asList("i", "love", "you");
        Assertions.assertThat(sweetWords).isNotEmpty().doesNotContainNull().contains("love").doesNotContain("world");
    }

    /**
     * 断言 Map
     */
    @Test
    public void assertMap() {
        Map<Integer, String> map = Maps.newHashMap(1, "value1");
        Assertions. assertThat(map)
            .isNotEmpty()
            .containsKey(2)
            .doesNotContainKeys(10)
            .contains(Assertions.entry(2, "a"));

    }

    /**
     * 类断言
     *
     * 类断言一般检查其字段和类的类型，注解是否存在、是否为final类
     */
    @Test
    public void assertClass() {
        Assertions.assertThat(Runnable.class).isInterface();

        Assertions.assertThat(Exception.class).isAssignableFrom(NoSuchElementException.class);

    }

    /**
     * 断言 文件
     *
     * File断言检查文件实例是否存在，是目录或文件，有一定内容，是否可读，具有特定扩展名。
     */
    @Test
    public void assertFile() {
        File file = new File("./pom.xml");
        Assertions.assertThat(file)
            .exists()
            .isFile()
            .canRead()
            .canWrite();
    }

    /**
     * 断言 Throwable
     */
    @Test
    public void testThrowable() {
        try {
            int i = 1 / 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            Assertions.assertThat(ex).hasNoCause().hasMessageEndingWith("zero");
        }
    }

    /**
     * 断言，添加描述
     */
    @Test
    public void testDescription() {
        Human el_toro = new Human().setName("El Toro").setAge(27);
        Assertions.assertThat(el_toro.getAge())
            .as("%s's age should be equal to 100", el_toro.getName())
            .isEqualTo(100);
    }
}