package com.zxjk.duoduo.bean;

import java.util.Objects;

/**
 * @author Administrator
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneInfo phoneInfo = (PhoneInfo) o;
        return Objects.equals(name, phoneInfo.name) &&
                Objects.equals(number, phoneInfo.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    public PhoneInfo(String name, String number) {
        this.name = name;
        this.number = number;
      
    }

    public static void main(String[] args) {
        PhoneInfo phoneInfo1 = new PhoneInfo("jin", "asd");
        PhoneInfo phoneInfo2 = new PhoneInfo("jin", "asd");
        PhoneInfo phoneInfo3 = new PhoneInfo("jin1", "asd");
        PhoneInfo phoneInfo4 = new PhoneInfo("jin1", "asd1");

        System.out.println(phoneInfo1.equals(phoneInfo2));
        System.out.println(phoneInfo1.equals(phoneInfo3));
        System.out.println(phoneInfo1.equals(phoneInfo4));


        System.out.println(phoneInfo3.equals(phoneInfo4));
        System.out.println(phoneInfo2.equals(phoneInfo3));
    }

}
