# **basis  MVC**
- 基于servlet与java注解的Web MVC 框架
- _**basis** 意思： n. 基础；底部；主要成分；基本原则或原理_


## 基于Java注解和servlet3.0+实现通过注解方式访问的web MVC框架

### 已实现
- 实现缓存扫描注解action
- 实现参数自动绑定
    - 参数与javabean自动绑定
    - 文件上传自动绑定
- 实现文件过滤器
    - 文件后缀格式控制
    - 文件上传大小限制
- 实现视图控制器
    - jsp页面返回
    - json对象返回
    - 404、500页面
    - 文件下载
- jsp页面返回的处理优化
- 添加@Setup 启动执行任务
- 绑定durid连接池

### 正在添加中
- 简易的orm功能
- aop的实现
- 暂未想到的功能.....


### CODE
```
@WebController("/mainController")
public class MainController {

  //@OK("rd:testpage.jsp")//重定向
  //@OK("json")//返回JSON对象
    @OK("jsp:testpage.jsp")//返回jsp页面
  //@OK("fw:testpage.jsp")//转发
    @GET//请求方式
    @Path//默认使用方法名
    public AjaxRsult index(
            @Parameter("id") int id,
            @Parameter("name") String name,
            @Parameter("age") int age,
            @Parameter("doubleNum") double doubleNum,
            @Parameter("flag") boolean flag,
            @Parameter("ids") String[] ids,
            HttpServletRequest request) {
        System.out.println("----" + id + "----" + name + "----" + age);
        System.out.println("mian index");
        request.setAttribute("test", "测试request.setAttribute");
        return new AjaxRsult(true, "呵呵呵", "json哦");
    }

    @OK("rd:testpage.jsp")
    @GET
    @Path("/testpage")
    public void testpage() {
        System.out.println("---testpage");
    }

    @OK("json")
    @POST
    @Path("/buildBeanFile")
    public AjaxRsult buildBeanFile(@Parameter("data>>") TestbuildBean bean, @Parameter("docName")TempFile docName) {
        System.out.println(new Gson().toJson(bean));
        System.out.println(docName.getName());
        try {
            IoTool.writeFile(docName.getInputStream(),"d:\\"+docName.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AjaxRsult(true, "呵呵呵", bean);
    }

    @OK("json")
    @POST
    @Path("/buildBeanFiles")
    public AjaxRsult buildBeanFiles(@Parameter("data>>") TestbuildBean bean, @Parameter("docName")TempFile[] docName) {
        System.out.println(new Gson().toJson(bean));
        for(TempFile file:docName){
            System.out.println(file.getName());
            System.out.println(file.getContentType());
        }
        return new AjaxRsult(true, "批量文件上传", bean);
    }


    @OK("json")
    @POST
    @Path("/buildBean")
    public AjaxRsult buildBean(@Parameter("data>>") TestbuildBean bean) {
        System.out.println(new Gson().toJson(bean));
        Connection connection=DBConn.getDbConn();
        System.out.println(connection);
        return new AjaxRsult(true, "呵呵呵", bean);
    }


    @OK("file")
    @GET
    @Path("/dowload")
    public File dowloadFile() {
        return new File("D:/site-1.8.9.zip");
    }

}
```