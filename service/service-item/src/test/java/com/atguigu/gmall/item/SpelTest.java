package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author chenyv
 * @create 2022-09-07 13:58
 */
public class SpelTest {

    @Test
    void test04() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(
                "haha-#{T(java.util.UUID).randomUUID().toString()}",
                new TemplateParserContext());
        String value = (String) expression.getValue();
        System.out.println("value = " + value);

    }

    @Test
    void test03() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("new int[]{1,2,3,4,5}");
        int[] value = (int[]) expression.getValue();
        for (int i : value) {
            System.out.println("i = " + i);
        }
    }

    @Test
    void test02() {
        Object[] params1 = new Object[]{45L, 56};
        Object[] params2 = new Object[]{32L, 55};
        Object[] params3 = new Object[]{21L, 57};

        //创建一个表达式解析器
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("sku:info:#{#params[0]}", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();

        //变量和上下文环境绑定
        context.setVariable("params", params2);
        String value = expression.getValue(context, String.class);
        System.out.println("value = " + value);
    }

    @Test
    void test01() {
        //创建一个表达式解析器
        ExpressionParser parser = new SpelExpressionParser();

        //准备一个表达式 “hello #{1+1}”
        String myExpression = "hello #{1+1}";

        //得到一个表达式
        Expression expression = parser.parseExpression(myExpression, new TemplateParserContext());
        Object value = expression.getValue();
        System.out.println("value = " + value);
    }


}
