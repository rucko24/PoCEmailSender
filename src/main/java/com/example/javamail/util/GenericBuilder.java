package com.example.javamail.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Para crear los pojo, java beans genericos
 * @implSpec Probar la inmutabilidad originada
 * @param <T>
 */
public class GenericBuilder<T> implements IBuilder<T> {

    private final Supplier<T> instanciador;

    private List<Consumer<T>> modificadorDeInstancia = new ArrayList<>();

    public GenericBuilder(final Supplier<T> intantiator) {
        this.instanciador = intantiator;
    }

    /**
     * Usar para instanciar cualquier bean con setters
     * @param instantiator
     * @param <T>
     * @return
     */
    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<>(instantiator);
    }

    public <V> GenericBuilder<T> with(final BiConsumer<T,V> bconsumer, V valueDeInstancia) {
        final Consumer<T> consumer = instance -> bconsumer.accept(instance,valueDeInstancia);
        modificadorDeInstancia.add(consumer);
        return this;
    }

    @Override
    public T build() {
        T value = instanciador.get();
        modificadorDeInstancia.forEach(e -> e.accept(value));
        /**
         * se limpia la lista de instancia creadas con anterioridad
         */
        modificadorDeInstancia.clear();
        return value;
    }
}
