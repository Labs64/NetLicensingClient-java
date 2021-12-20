package com.labs64.netlicensing.util;

import java.lang.reflect.Method;

// Inspired by: https://www.javaworld.com/article/2077602/learn-java/java-tip-98--reflect-on-the-visitor-design-pattern.html
public class Visitor {

    public void visit(final Object object) throws Exception {
        final Method method = getMethod(object.getClass());
        method.invoke(this, object);
    }

    public void visitDefault(final Object object) throws Exception {
        // Do nothing
    }

    protected Method getMethod(final Class<?> targetClass) throws NoSuchMethodException {
        Class<?> superClass = targetClass;
        while (superClass != Object.class) {
            try {
                return getClass().getMethod("visit", superClass);
            } catch (final NoSuchMethodException e) {
                superClass = superClass.getSuperclass();
            }
        }
        final Class<?>[] ifaces = targetClass.getInterfaces();
        for (final Class<?> iface : ifaces) {
            try {
                return getClass().getMethod("visit", iface);
            } catch (final NoSuchMethodException e) {
            }
        }
        return getClass().getMethod("visitDefault", Object.class);
    }
}
