package cn.javass.spring.chapter3.bean;

public class SingleA {
    public SingleA(){
        System.out.println("创建a");
    }
    ProtoB b;
    public ProtoB getB() {
        return b;
    }
    public void setB(ProtoB b) {
        this.b = b;
    }
    
}
