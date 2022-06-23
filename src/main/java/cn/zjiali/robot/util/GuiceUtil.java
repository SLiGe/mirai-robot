package cn.zjiali.robot.util;

import cn.zjiali.robot.main.ApplicationBootStrap;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import java.util.List;

/**
 * @author zJiaLi
 * @since 2022-06-01 14:40
 */
public class GuiceUtil {

    public static <T> T getBean(Class<T> clz) {
        Injector injector = ApplicationBootStrap.getInstance().getInjector();
        return injector.getInstance(clz);
    }

    public static <T> T getBean(String name, Class<T> clz) {
        Injector injector = ApplicationBootStrap.getInstance().getInjector();
        return injector.getInstance(Key.get(clz, Names.named(name)));
    }

    public static <T> List<T> getMultiBean(Class<T> clz) {
        List<T> beanList = Lists.newArrayList();
        Injector injector = ApplicationBootStrap.getInstance().getInjector();
        List<Binding<T>> bindingList = injector.findBindingsByType(TypeLiteral.get(clz));
        bindingList.forEach(value -> beanList.add(value.getProvider().get()));
        return beanList;
    }
}
