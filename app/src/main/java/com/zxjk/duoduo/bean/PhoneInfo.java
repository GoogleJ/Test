package com.zxjk.duoduo.bean;

/**
 * @author Administrator
 * @// TODO: 2019\3\30 0030 获取手机联系人的实体 
 */
public class PhoneInfo {
    private String name;
    private String number;
  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

   



    public PhoneInfo(String name, String number) {
        this.name = name;
        this.number = number;
      
    }

}
