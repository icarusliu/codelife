package com.liuqi.tools.codelife.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Test {
    @Column(name = "id", columnDefinition = "varchar(255) not null")
    private String id;

    @Column(name = "age", columnDefinition = "bigint not null")
    private Long age;

    private String test;

    private ArticleStatus articleStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Test id(String id) {
        this.id = id;
        return this;
    }

    public Test test(String test) {
        this.test = test;
        return this;
    }

    public Test age(Long age) {
        this.age = age;
        return this;
    }

    public ArticleStatus getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Test articleStatus(ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
        return this;
    }
}
