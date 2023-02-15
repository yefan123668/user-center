项目已经部署，立即前往首页=====>：**http://www.minsf.top**

[前往首页]: http://www.minsf.top



### 新手如何利用ant design pro前端开发一个新页面

###### 以注册页面为例子：

###### 1.查看官方文档初始命令，并新建一个项目

根据ant design pro官方文档

```
https://pro.ant.design/zh-CN/docs/getting-started/
```

```
# 使用 npm
npm i @ant-design/pro-cli -g
pro create myapp

? 🐂 使用 umi@4 还是 umi@3 ? (Use arrow keys)
❯ umi@4
  umi@3
  
? 🚀 要全量的还是一个简单的脚手架? (Use arrow keys)
❯ simple
  complete
  
# 开始安装依赖
$ cd myapp && npm install
```

至此，前端项目初始化完成！



###### 2.前端项目简化

这个根据需要，自行简化，脚手架自身也提供了一些脚本，比如在package.json里

```
"script": {
	...
	"i18n-remove": "pro i18n-remove --locale=zh-CN --write",
	...
}
```

这个脚本帮助我们移出国际化，如果不涉及到，一般建议移出，然后删除locale文件夹

...



###### 3.创建一个新的页面

一般不会从0开始手写，这里直接复制Login组件，将文件夹名改为Register

![输入图片说明](https://pic.imgdb.cn/item/63ec9004f144a01007b83dd0.png)

![输入图片说明](https://pic.imgdb.cn/item/63ec9084f144a01007b954c5.png)

根据需要修改html、css、js

（建议顺序=======>html===>css===>js）

**注意：**

有时候，我们的页面并不需要经过登录认证，对于前端来说，需要进行特定的配置

![输入图片说明](https://pic.imgdb.cn/item/63ec9084f144a01007b954c5.png)

这里逻辑是，如果不是登录页，就会请求后端当前用户的用户态，如果返回值不符合规则或出现异常会重定向到登录页

https://pic.imgdb.cn/item/63ec910ff144a01007ba9038.png

onPageChange相当于ant design pro提供的生命周期狗子，当前端的页面发生改变时，会触发这里的逻辑，这里还是跳转到登录页

基于上述情况，我们可以配置一个白名单，当页面跳转或验证某个页面时，不需要进行认证（比如注册）

```
刚进入页面时，在我们的入口文件app.tsx有两个逻辑：
1、判断是否为登录路由，如果不是会去调用用户信息接口，调用发生错误会重定向到登录页

2、改变页面时，会触发onPageChange钩子，会判断是否是登录页和是否有用户的登录信息，不满足规则会重定向到登录页

// 以下配置在入口文件app.tsx
import {history} from 'umi';

const LOGIN_ROUTE = '/user/login';
const REGISTER_ROUTE = '/user/register';
const WRITE_LIST = [LOGIN_ROUTE, REGISTER_ROUTE];

const current_path = history.


...
if (!writeList.includes(history.location.pathname)
{
	// TODO不是白名单需要执行的逻辑
}

...
```



###### 4.配置路由

![输入图片说明](https://pic.imgdb.cn/item/63ec914cf144a01007bb2628.png)

在routers.ts文件中配置好注册页面的路由

**注意：**

有时候我们的组件是嵌入到另一个父组件里的，我们需要在父组件作一些配置，比如Admin.tsx：

```
const Admin: React.FC = (props) => {
  const {children} = props;
  return (
    <PageHeaderWrapper>
      {children}
    </PageHeaderWrapper>
  );
};

export default Admin;
```

从组件函数的入参里拿到props，获得子组件对象，放到return里



###### 5.引入组件：

这时我们需要register的组件：

![输入图片说明](https://pic.imgdb.cn/item/63ec916ff144a01007bb72b8.png)

有时候找不到头绪，可以尝试看下源码，ts是它定义的类型，我们去js里查看

![输入图片说明](https://pic.imgdb.cn/item/63ec9193f144a01007bbbe07.png)

我们可以把ant design pro封装的LoginForm组件改写为我们的注册组件，但是按钮是登录，我们需要改写为  “注册”，翻阅官方文档和百度都差不多时，部分简单看下源码（点进去搜索“登录”），最后发现了这个对象可以改按钮名字

```
<ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined className={styles.prefixIcon}/>,
                }}
                placeholder="请输入确认密码"
                rules={[
                  {
                    required: true,
                    message: "密码是必填项",
                  },
                  {
                    min: 8,
                    type: "string",
                    message: "密码至少八位"
                  }
                ]}
              />
```

当我们提交后，框架会将name和key放到一个对象里

![输入图片说明](https://pic.imgdb.cn/item/63ec91c8f144a01007bc43f5.png)

在ts中定义一个变量，？表示这个属性可以不存在，不是必选的

这个变量定义的目的，相当于给js的变量做一个约束，一个规范


![输入图片说明](https://pic.imgdb.cn/item/63ec91f6f144a01007bcb455.png)

这个相当于  xxx?redirect=/aaa

当我们登录成功后，会跳转到这里



ctrl+o可以去掉未用到的引用

```
history.push({
          pathname: '/user/login',
          query
        })
```

push除了传一个字符串，也可以支持这种



![输入图片说明](https://pic.imgdb.cn/item/63ec923bf144a01007bd3a54.png)

加一个p标签，段落有自动换行的效果

Link标签中的to一般用于跳转到站内某个页面

href一般用于站外


https://pic.imgdb.cn/item/63ec9271f144a01007bd9f5e.png

还是放到自动登录旁边，放一个分隔栏，垂直分割



![输入图片说明](https://pic.imgdb.cn/item/63ec92baf144a01007be1f15.png)

有需要还可以加个水印



![输入图片说明](https://pic.imgdb.cn/item/63ec92d9f144a01007be56b6.png)

access.tsx是pro专门用来控制管理员权限的



###### 6.ProComponents高级表单

找其他组件官网地址：

```
https://procomponents.ant.design/components
```



1. 通过columns定义表格有哪些列
2. columns属性
   - dataIndex对象返回对象的属性
   - title表格列名
   - copyable是否允许复制 
   - ellipsos是否允许缩略
   - valueType申明这一列的类型
   - search用来定制该字段在表单上的查询，如果为false则关闭在表单上的查询


每行的值默认是以字符串的形式渲染的，通过rander函数可以改变

```
{
    title: '头像',
    dataIndex: 'avatarUrl',
    render: (_, record) => (
      <div>
        <Image src={record.avatarUrl} width={100} />
      </div>
    ),
  },
```

record代表这一行的数据

Image是ant design中的一个组件

```
{
    title: '角色',
    dataIndex: 'userRole',
    valueType: 'select',
    valueEnum: {
      0: { text: '普通用户', status: 'Default' },
      1: {
        text: '管理员',
        status: 'Success',
      },
    },
  },
```

这是一种select类型，支持枚举值映射



```
 {
    title: '创建时间',
    dataIndex: 'createTime',
    copyable: true,
    valueType: 'dateRange',
    hideInTable: true,
    search: {
      transform: (value) => {
        return {
          startTime: value[0],
          endTime: value[1],
        };
      },
    }
  },
  
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'date',
    search: false,
  },
```

这个表格中的时间，我们可以通过选择一个时间范围来进行筛选

下面个时间则是用来在表格中展示



###### 7、tip：

```
改写组件名字

ctrl+shift+r替换关键字

ctrl+alt+l快速格式化

shift+f6替换与之关联的关键字

ctrl+r范围选择替换

state就是组件里存的一些变量，当变量发生变化时，页面会重新渲染

MFSU:前端编译优化

Ant Design组件库=>React

Ant Design Procomponents=>Ant Design

Procomponents把一些常用的组件进行了组合，比如登录表单，进行了进一步封装，和业务的关系更紧密一些，也可以理解为一种业务组件，更加定制化

Ant Design Pro是一个后台管理系统=>Ant Design、Ant Design Procomponents、react和其他库组合而成
```



###### 8、效果展示

![输入图片说明](https://pic.imgdb.cn/item/63ec9386f144a01007bf79ae.png)

![输入图片说明](https://pic.imgdb.cn/item/63ec93a9f144a01007bfb984.png)