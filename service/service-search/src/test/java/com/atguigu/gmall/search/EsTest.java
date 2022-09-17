package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-14 18:36
 */
@SpringBootTest
public class EsTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    @Test
    void testSave() {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("三");
        person1.setListName("张");
        person1.setAge(3);
        person1.setAddress("西安市雁塔区");

        Person person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("四");
        person2.setListName("李");
        person2.setAge(18);
        person2.setAddress("上海外滩");

        Person person3 = new Person();
        person3.setId(3L);
        person3.setFirstName("五");
        person3.setListName("王");
        person3.setAge(10);
        person3.setAddress("北京市中南海");


        personRepository.save(person1);
        personRepository.save(person2);
        personRepository.save(person3);
        System.out.println("完成！");
    }

    @Test
    void testQuery() {
//        Optional<Person> byId = personRepository.findById(0L);
//        System.out.println(byId.get());
        List<Person> personList = personRepository.findAllByAddressLike("北京市");
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
    }

    @Test
    void testSome() {
        List<Person> someOne = personRepository.findSomeOne("上海");
        for (Person person : someOne) {
            System.out.println("person = " + person);
        }
    }
}
