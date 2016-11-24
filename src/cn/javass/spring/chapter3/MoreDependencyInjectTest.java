package cn.javass.spring.chapter3;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.javass.spring.chapter3.bean.DependentBean;


//依赖注入 初始化以及销毁顺序
public class MoreDependencyInjectTest {
    /**
     *    那“depends-on”有什么好处呢？主要是给出明确的初始化及销毁顺序，
     *    比如要初始化“decorator”时要确保“helloApi”Bean的资源准备好了，
     *    否则使用“decorator”时会看不到准备的资源；
     *    而在销毁时要先在“decorator”Bean的把对“helloApi”资源的引用释放掉才能销毁“helloApi”，
     *    否则可能销毁 “helloApi”时而“decorator”还保持着资源访问，造成资源不能释放或释放错误。
     * @throws IOException
     */
    @Test
    public void testDependOn() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("chapter3/depends-on.xml");
        /**
         * 但，如果你正在一个非web应用的环境下使用Spring的IoC容器，如dubbo服务，
         * 你想让容器优雅的关闭，并调用singleton的bean相应destory回调方法，你需要在JVM里注册一个“关闭钩子”（shutdown hook）。
         * 这一点非常容易做到，并且将会确保你的Spring IoC容器被恰当关闭，以及所有由单例持有的资源都会被释放
         */
        context.registerShutdownHook();
        DependentBean dependentBean = context.getBean("dependentBean", DependentBean.class);
       
        dependentBean.write("aaa");
    }
    
   
}

