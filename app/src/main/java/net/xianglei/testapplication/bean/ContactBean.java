package net.xianglei.testapplication.bean;

import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-09-05 17:55
 * Description:联系人信息
 */
public class ContactBean {

    private String name;
    private List<String> phones;

    public ContactBean() {
    }

    public ContactBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
