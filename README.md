# Silk
一个基于java bean的响应式框架
###Why we need this?

假设我们有这么一个需求：
我们登陆成功之后通过服务器拿到一个useraccount对象,useraccount对象长这个样子
```java
public class UserAccount{
  private String userName;//姓名
  private int age;//年龄
  private String userImage;//头像

  public void setUserName(String userName){
    this.userName = userName;
  }
  
  public String getUserName(){
    return userName;
  }
  
  //后略......
}
```
我们会在界面上展示登陆用户的姓名，头像等等:
```java
UserAccount useraccount = getUserAccount();//获取到了useraccount对象
TextView tvName = findViewById(R.id.tv_name);
tvName.setText(useraccount.getUserName());
```
OK，接下来如果我们需要修改用户的姓名，那么以往我们是这样的：
```java
useraccount.setUserName("mike");
//其他的工作
tvName.setText(useraccount.getUserName());
//其他的展示
```
这里面，我们一般是给对象set了某个值之后，再更新UI。

###Now,We change

使用silk之后，现在我们的useraccount定义是这样的：(就多了一个注解而已)
```java
@RxBean
public class UserAccount{
  private String userName;//姓名
  private int age;//年龄
  private String userImage;//头像

  public void setUserName(String userName){
    this.userName = userName;
  }
  
  public String getUserName(){
    return userName;
  }
  
  //后略......
}
```
我们接下来这样展示里面的数据：
```java
TextView tvName = findViewById(R.id.tv_name);
SilkBrite<UserAccount> brite = SilkBrite.create();//初始化了一个brite
UserAccount useraccount = brite.asSilkBean(getUserAccount());//获取useraccount对象，其实这是一个“换了包”的对象
brite.asModeObservable()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Action1<UserAccount>() {
                    @Override
                    public void call(UserAccount info) {
                        tvName.setText(useraccount.getUserName());
                    }
                });
```
然后我们要修改用户姓名了！
```java
//你只需要做下面这件事，UI上面就会同步
useraccount.setUserName("mike");
```
所以，用了silk，我们就不需要每次修改了bean之后还要去同步UI。

##引用

* gradle，在你的app的build.gradle文件中引入依赖
```gradle
dependencies {
    ...
    compile 'com.github.landscapeside:SilkLib:1.04'
    apt 'com.github.landscapeside:SilkComplier:0.3'
    ...
}
```
然后在你的app的build.gradle中应用apt插件：
```gradle
apply plugin: 'com.neenbedankt.android-apt'
```
project的build.gradle中申明：
```gradle
dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
```
用的是jitpack仓库，所以需要在project的build.gradle中注明
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

##用法

* 如上所说，在你需要silk功能的java bean类上使用RxBean注解，然后重新构建一下项目
* 然后要新建一个SilkBrite出来`SilkBrite<UserAccount> brite = SilkBrite.create();`，注意，目前来说，一个brite就对应处理一个bean对象
* 接下来需要将原有的bean对象设置一个代理`UserAccount useraccount = brite.asSilkBean(getUserAccount());`
* 上一步生成的代理对象才能真正嵌入到Silk响应式框架之中，通过bean的setter即可触发相应
* 我们需要监听到事件源发出的消息并作出反应
```java
brite.asModeObservable()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Action1<UserAccount>() {
                    @Override
                    public void call(UserAccount info) {
                        tvName.setText(useraccount.getUserName());
                    }
                });
```
* 当然，还可以嵌套使用，比如一个Parent对象有2个Child，不管是修改Parent的属性或者是child属性，我们都希望能得到通知，则两个类都标记上RxBean注解即可
```java
@RxBean
public class Parent{
  private String userName;//姓名
  private int age;//年龄
  private String userImage;//头像
  private Child child1;
  private Child child2;

  public void setUserName(String userName){
    this.userName = userName;
  }
  
  public String getUserName(){
    return userName;
  }
  
  //后略......
}

@RxBean
public class Child{
  private String userName;//姓名
  private int age;//年龄
  //......
}
```
* 有时候我们有这样的需求，显示UserAccount的nick的时候希望能加上如果为空的默认显示
```java
brite.asNodeObservable("nick")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(o1 -> {
            String name = String.valueOf(o1);
            if (TextUtils.isEmpty(name)) {
                return "unknown user";
            }
            return name;
        })
        .subscribe(new Action1<String>() {
                @Override
                public void call(String name) {
                    tvName.setText(name);
                }
            });

```
* 如果是要加上child的默认显示
```java
brite.asNodeObservable("child1::userName")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(o1 -> {
            String name = String.valueOf(o1);
            if (TextUtils.isEmpty(name)) {
                return "unknown user";
            }
            return name;
        })
        .subscribe(new Action1<String>() {
                @Override
                public void call(String name) {
                    tvName.setText(name);
                }
            });
```


##限制

* 首先，一个brite就是用来处理一个bean对象，所以并不太适合列表类的应用，当然后续会考虑改进
* 而且就如Silk的开发目的一样，它就只是为了解决基于bean的响应式编程，如果是基于sqlite的响应式需求，请移步[sqlbrite](https://github.com/square/sqlbrite)

##最后
感谢如下项目给我的指导和参考：
* [sqlbrite](https://github.com/square/sqlbrite)
* [ViewFinder](https://github.com/brucezz/ViewFinder)


