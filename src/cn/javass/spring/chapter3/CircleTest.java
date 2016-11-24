package cn.javass.spring.chapter3;

import org.junit.Test;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircleTest {
    /**
     * 构造器循环依赖：表示通过构造器注入构成的循环依赖，此依赖是无法解决的，
     * 只能抛出BeanCurrentlyInCreationException异常表示循环依赖。
     * 如在创建CircleA类时，构造器需要CircleB类，那将去创建CircleB，
     * 在创建CircleB类时又发现需要CircleC类，则又去创建CircleC，
     * 最终在创建CircleC时发现又需要CircleA；从而形成一个环，没办法创建。 Spring容器将每一个正在创建的Bean
     * 标识符放在一个“当前创建Bean池”中， Bean标识符在创建过程中将一直保持在这个池中，
     * 因此如果在创建Bean过程中发现自己已经在“当前创建Bean池”里时将抛出
     * BeanCurrentlyInCreationException异常表示循环依赖； 而对于创建完毕的Bean将从“当前创建Bean池”中清除掉。
     * 
     * @throws Throwable
     */
    @Test
    public void testcircleByConstructor() throws Throwable {
        /**
         * 让我们分析一下吧： 1、Spring容器创建“circleA”
         * Bean，首先去“当前创建Bean池”查找是否当前Bean正在创建，如果没发现，则继续准备其需要的构造器参数“circleB”，并将“
         * circleA” 标识符放到“当前创建Bean池”； 2、Spring容器创建“circleB”
         * Bean，首先去“当前创建Bean池”查找是否当前Bean正在创建，如果没发现，则继续准备其需要的构造器参数“circleC”，并将“
         * circleB” 标识符放到“当前创建Bean池”； 3、Spring容器创建“circleC”
         * Bean，首先去“当前创建Bean池”查找是否当前Bean正在创建，如果没发现，则继续准备其需要的构造器参数“circleA”，并将“
         * circleC” 标识符放到“当前创建Bean池”； 4、到此为止Spring容器要去创建“circleA”Bean，发现该Bean
         * 标识符在“当前创建Bean池”中，因为表示循环依赖，抛出BeanCurrentlyInCreationException。
         */
        try {
            new ClassPathXmlApplicationContext(
                    "chapter3/circleInjectByConstructor.xml");
        } catch (Exception e) {
            // 因为要在创建circle3时抛出；
            System.out.println(1);
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }
    }

    @Test
    public void testCircleBySetterAndSingleton1() throws Throwable {
        /**
         * 具体步骤如下： 1、Spring容器创建单例“circleA” Bean，首先根据无参构造器创建Bean，
         * 并暴露一个“ObjectFactory ”用于返回一个提前暴露一个创建中的Bean，并将“circleA”
         * 标识符放到“当前创建Bean池”； 然后进行setter注入“circleB”； 2、Spring容器创建单例“circleB”
         * Bean，首先根据无参构造器创建Bean， 并暴露一个“ObjectFactory”用于返回一个提前暴露一个创建中的Bean，
         * 并将“circleB” 标识符放到“当前创建Bean池”，然后进行setter注入“circleC”；
         * 3、Spring容器创建单例“circleC” Bean，首先根据无参构造器创建Bean， 并暴露一个“ObjectFactory
         * ”用于返回一个提前暴露一个创建中的Bean， 并将“circleC”
         * 标识符放到“当前创建Bean池”，然后进行setter注入“circleA”；
         * 进行注入“circleA”时由于提前暴露了“ObjectFactory”工厂从而使用它返回提前暴露一个创建中的Bean；
         * 4、最后在依赖注入“circleB”和“circleA”，完成setter注入。
         */
        new ClassPathXmlApplicationContext(
                "chapter3/circleInjectBySetterAndSingleton.xml");
    }

    @Test(expected = BeanCurrentlyInCreationException.class)
    public void testCircleBySetterAndSingleton2() throws Throwable {
        try {
            //对于“singleton”作用域Bean，可以通过“setAllowCircularReferences(false);”来禁用循环引用：
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
            ctx.setConfigLocation(
                    "chapter3/circleInjectBySetterAndSingleton.xml");
            ctx.refresh();
        } catch (Exception e) {
            // 因为要在创建circle3时抛出；
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }

    }

    @Test(expected = BeanCurrentlyInCreationException.class)
    public void circleBySetterAndPrototypeTest() throws Throwable {
        /**
         *  对于“prototype”作用域Bean，Spring容器无法完成依赖注入，
         *  因为“prototype”作用域的Bean，Spring容器不进行缓存，因此无法提前暴露一个创建中的Bean。
         */
        try {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                    "chapter3/circleInjectBySetterAndPrototype.xml");
            System.out.println(ctx.getBean("circleA"));
        } catch (Exception e) {
            // 因为要在创建circle3时抛出；
            Throwable e1 = e.getCause().getCause().getCause();
            throw e1;
        }
    }

}
