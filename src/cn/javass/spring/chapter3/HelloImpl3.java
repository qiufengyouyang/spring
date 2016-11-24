package cn.javass.spring.chapter3;


import cn.javass.spring.chapter2.helloworld.HelloApi;

public class HelloImpl3 implements HelloApi {
  //@java.beans.ConstructorProperties({"message", "index"})
    
    
    private String message;
    private int index;
   /* public HelloImpl3(){
        
    }*/

    public HelloImpl3(String message, int index) {
        this.message = message;
        this.index = index;
    }
    
    @Override
    public void sayHello() {
        System.out.println(index + ":" + message);
    }
    
}
