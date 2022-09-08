package com.atguigu.gmall.item.cache.aspect;

import com.atguigu.gmall.item.cache.CacheOpsService;
import com.atguigu.gmall.item.cache.annotation.GmallCache;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author chenyv
 * @create 2022-09-07 09:14
 */
@Aspect
@Component
public class CacheAspect {

    @Autowired
    CacheOpsService cacheOpsService;

    //创建表达式解析器，这个是线程安全的
    SpelExpressionParser parser = new SpelExpressionParser();

    ParserContext context = new TemplateParserContext();

    @Around("@annotation(com.atguigu.gmall.item.cache.annotation.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Object arg = joinPoint.getArgs()[0];    //方法的参数 skuId
        String cacheKey = determinCacheKey(joinPoint);

        //先查缓存
        Type returnType = getMethodGenericReturnType(joinPoint);    //获得方法的带泛型的返回值类型
        Object cacheData = cacheOpsService.getCacheData(cacheKey, returnType);   //通过缓存获得的字符串转为returnType类型的对象

        //缓存
        if (cacheData == null) {
            //准备回源
            //先问布隆，但是有些场景不一定要问布隆。比如三级分类（比如三级分类）
            //boolean contains = cacheOpsService.bloomContains(arg);
            String bloomName = determinBloomKey(joinPoint);
            if (!StringUtils.isEmpty(bloomName)) {
                //指定开启了布隆
                Object bVal = determinBloomValue(joinPoint);
                boolean contains = cacheOpsService.bloomContains(bloomName, bVal);
                if (!contains) {
                    return null;
                }
            }
            //布隆说有，准备回源，有击穿风险
            boolean tryLock = false;
            try {
                tryLock = cacheOpsService.tryLock((Long) arg);
                if (tryLock) {
                    //获取到锁，回源查询
                    result = joinPoint.proceed(joinPoint.getArgs());
                    //调用成功
                    cacheOpsService.saveData(cacheKey, result);
                    return result;
                } else {
                    Thread.sleep(1000);
                    return cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
                }
            } finally {
                if (tryLock) {
                    cacheOpsService.unlock((Long) arg);
                }
            }
        }
        //缓存中有直接返回
        return cacheData;
    }

    /**
     * 根据布隆过滤器表达式计算出布隆需要判定的对象值
     * @param joinPoint
     * @return
     */
    private Object determinBloomValue(ProceedingJoinPoint joinPoint) {
        // 1、拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 2、拿到注解
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        // 3、拿到布隆值表达式
        String bloomValue = cacheAnnotation.bloomValue();
        Object o = evaluationExpression(bloomValue, joinPoint, Object.class);
        return o;
    }

    /**
     * 获取布隆过滤器的名字
     *
     * @param joinPoint
     * @return
     */
    private String determinBloomKey(ProceedingJoinPoint joinPoint) {
        // 1、拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 2、拿到注解
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        // 3、拿到布隆名
        String bloomName = cacheAnnotation.bloomName();
        return bloomName;
    }

    /**
     * 获取目标方法的精确返回值类型
     *
     * @param joinPoint
     * @return
     */
    private Type getMethodGenericReturnType(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Type type = method.getGenericReturnType();
        return type;
    }

    /**
     * 根据当前整个连接点的执行信息，确定缓存用什么key
     *
     * @param joinPoint
     * @return
     */
    private String determinCacheKey(ProceedingJoinPoint joinPoint) {
        // 1、拿到目标方法上的@GmallCache注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 2、拿到注解
        GmallCache cacheAnnotation = method.getDeclaredAnnotation(GmallCache.class);
        String expression = cacheAnnotation.cacheKey();
        //根据表达式计算缓存键
        String cacheKey = evaluationExpression(expression, joinPoint, String.class);
        return cacheKey;
    }

    private <T> T evaluationExpression(String expression,
                                       ProceedingJoinPoint joinPoint,
                                       Class<T> clz) {
        //得到表达式
        Expression exp = parser.parseExpression(expression, context);
        //sku:info:#{#params[0]}
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //取出所有参数，绑定到上下文
        Object[] args = joinPoint.getArgs();
        evaluationContext.setVariable("params", args);
        //得到表达式的值
        T expValue = exp.getValue(evaluationContext, clz);
        return expValue;
    }

}
