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
    String cacheKey() default "";
}
