package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenyv
 * @create 2022-09-14 18:25
 */
@Repository
public interface PersonRepository extends CrudRepository<Person,Long> {

    /**
     * 查询地址在XXX的所有人
     * @param address
     * @return
     */
    List<Person> findAllByAddressLike(String address);

    /**
     * 查询年龄小于等于X的所有人
     * @param age
     * @return
     */
    List<Person> findAllByAgeLessThanEqual(Integer age);

    @Query("{\n" +
            "    \"match\": {\n" +
            "      \"address\":\"?0\"\n" +
            "    }\n" +
            "  }")
    List<Person> findSomeOne(String add);
}
