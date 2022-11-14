
# eg

```java

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSelectByWrapperSupper {
    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 查询出指定列的数据
     */
    @Test
    public void test1() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id", "last_name")
                .like("last_name", "J");
        employeeMapper.selectList(queryWrapper)
                .forEach(System.out::println);
    }

    /**
     * 指定不查询的列
     * select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) 自己写 断言 方法进行排除
     */
    @Test
    public void test2() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Employee.class, (e) -> !e.getColumn().equals("last_name") && !e.getColumn().equals("user_id"));
        employeeMapper.selectList(queryWrapper)
                .forEach(System.out::println);
    }
}

```

# Reference

[QueryWrapper总结](https://blog.csdn.net/weixin_40482816/article/details/117355222)

[QueryWrapper总结](https://blog.csdn.net/weixin_34503526/article/details/111725076)