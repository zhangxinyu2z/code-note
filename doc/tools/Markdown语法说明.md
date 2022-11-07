# 记忆

# 任务列表

```md
- [ ] 这是一个未完成任务列表
- [x] 这是一个完成任务列表
```

# 链接

常用：
```md
[an example](http://baidu.com)
```

## 参考链接

```md
this is [an example][id]
[id]:http:/hello.html "title"

隐式的写法：

[google][]
[google]:http:google.com
```

引用方式

```md
I get 10 times more traffic from [Google][1] than from [Yahoo][2] or [MSN][3].  

[1]: http://google.com/        "Google" 
[2]: http://search.yahoo.com/  "Yahoo Search" 
[3]: http://search.msn.com/    "MSN Search"
```

## 内部链接：

锚点 -> 锚点目标 

### 定义锚点目标：

```md
<span id="目标">文字</span>

<a name="目标">文字</a>

<h1 id="标识符" style="display:none;"></h1>
```

### 定义锚点：

* 行内式：`[内部链接](#目标 "标题")` 	
* 标题可省略参考式：`[参考标识符]: #目标 "标题"`
* 方式三：

```md
<a href="#锚点目标：比如id、name、标题等">随意</a>

[任意描述](#锚点目标：比如id、name、标题等)
```

方式三：先定义参考式，再通过这种格式定义：`[内容][参考标识符]` 内容和参考标识符一样可以不写：`[内容][]`，进行跳转。

```md
[hah]: #iloveyou

[hah][]
```

## 跳转到其他文件目标位置

本地不支持

```md
[标题](路径#标识id)
```

# 图片

语法：`![图片名称](/path/to/img.jpg "title")`

引用方式：

```md
![alt text][id]

[id]: /path/to/img.jpg "Title"
```

Typora使图片居左的方式：1、鼠标到语法最后，table  2、鼠标到！前，空格

使用html语法定义图片的样式：

```html
<div align="center">
<img src="地址" width="50%" height="50%">
</div>
```

```html
<!-- 左右对齐 -->
<img src='图片地址' align='right' style=' width:300px;height:100 px'/>

<img src='图片地址' style='float:right; width:300px;height:100 px'/>
```
> align属性：
> * justify：对行进行伸展，这样每行都可以有相等的长度（就像在报纸和杂志中）
> * center/left/right

```html
<!--修改边框-->

style="border-radius:3px; box-shadow:rgba(0,0,0,0.15) 0 0 8px;background:#FBFBFB;border:1px solid #ddd;margin:10px auto;margin-left: 15px;padding:5px;"

<img src='图片地址' align='right' style=' width:300px;height:100px 
BORDER-RIGHT:#007979 5px solid；
BORDER-TOP:#007979 5px solid；
BORDER-LEFT：#007979 5px solid；
BORDER-BOTTOM:#007979 5px solid；
'/>
```

# 地址

```md
<https://www.baidu.com>
```

# 删除线

```md
~~Haha~~
```

# 表情

```md
:smile:
```

# 上下标

```md
上标： 
    2^12 需要语法支持，比如typora    
    2<sup>12</sup> html支持
下标：
    2~12~
    2<sub>12</sub>

```

# 脚注

```md
这是一个链接到谷歌的[^脚注]。

[^脚注]: http://www.google.com
```

# 背景色

```html
<table><tr><td bgcolor=yellow>背景色yellow</td></tr></table>
```

# Reference doc
[Markdown语法说明-zh](https://www.appinn.com/markdown/)

[typora-语法说明](https://www.cnblogs.com/shiwanghualuo/p/12373717.html)

[md->html online](https://daringfireball.net/projects/markdown/dingus)

[官网文档](https://daringfireball.net/projects/markdown/basics)

[扩展markdown语法和样式](https://casual-effects.com/markdeep/)

[Markdown Editor Online](https://stackedit.io/)