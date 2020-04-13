package com.fluxtion.ext.streaming.api.stream;

import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.api.partition.LambdaReflection.SerializableSupplier;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.ConstantNumber;
import java.lang.reflect.Method;
import lombok.Data;

/**
 * Representation of a an argument
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 * @param <T>
 */
@Data
public class Argument<T> {

    public Object source;
    public Method accessor;
    public boolean cast;
    
    public static <T, S> Argument<S> arg(SerializableFunction<T, S> supplier) {
        final Class containingClass = supplier.getContainingClass();
        return new Argument(StreamOperator.service().select(containingClass), supplier.method(), true);
    }    

    public static <T extends Number> Argument<T> arg(Double d) {
        SerializableFunction<Number, Double> s = Number::doubleValue;
        return new Argument<>(new ConstantNumber(d), s.method(), true);
    }

    public static <T extends Number> Argument<T> arg(int d) {
        SerializableFunction<Number, Integer> s = Number::intValue;
        return new Argument<>(new ConstantNumber(d), s.method(), true);
    }

    public static <T extends Number> Argument<T> arg(long d) {
        SerializableFunction<Number, Long> s = Number::longValue;
        return new Argument<>(new ConstantNumber(d), s.method(), true);
    }

    public static <T extends Number> Argument<Number> arg(Wrapper<T> wrapper) {
        return arg(wrapper, Number::doubleValue);
    }

    public static <T, S> Argument<S> arg(Wrapper<T> wrapper, SerializableFunction<T, S> supplier) {
        return new Argument<>(wrapper, supplier.method(), true);
    }

    public static <T> Argument<T> arg(SerializableSupplier<T> supplier) {
        return new Argument<>(supplier.captured()[0], supplier.method(), true);
    }

    public static <T> Argument<T> arg(Class<T> clazz){
        Wrapper<T> select = StreamOperator.service().select(clazz);
        return arg(select);
    }
    
    public static <T> Argument<T> arg(Object supplier) {
        return new Argument(supplier, null, true);
    }

    public Argument(Object source, Method accessor, boolean cast) {
        this.source = source;
        this.accessor = accessor;
        this.cast = cast;
    }
}
