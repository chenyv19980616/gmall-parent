package com.atguigu.gmall.item.cache.annotation;

import java.lang.annotation.*;

/**
 * @author chenyv
 * @create 2022-09-07 09:11
 * 缓存注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GmallCache {
    String cacheKey() default ""; //cacheKey

    String bloomName() default "";  //如果指定了布隆过滤器的名字就用

    String bloomValue() default "";  //如果指定了布隆过滤器的名字就用

    String lockName() default "lock:global";  //锁名,传入精确锁就用精确锁，否则用全局锁
}
