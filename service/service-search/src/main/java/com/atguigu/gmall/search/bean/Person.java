package com.atguigu.gmall.search.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author chenyv
 * @create 2022-09-14 14:09
 */
@Data
@Document(indexName = "person", shards = 1, replicas = 1)
public class Person {

    @Id
    private long id;    //主键

    @Field(value = "first", type = FieldType.Keyword)
    private String firstName;

    @Field(value = "last", type = FieldType.Keyword)
    private String listName;

    @Field(value = "age")
    private Integer age;

    @Field(value = "address",analyzer = "ik_smart",type = FieldType.Text)
    private String address;

}
