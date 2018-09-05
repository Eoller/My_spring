package newspring;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class Register {

    Map<String, Object> objectRegister = new HashMap<String, Object>();

    Map<Field, Object> lazyInjectMap = new HashMap<Field, Object>();

    public Optional<Object> get(String name) {
        Object extracting = objectRegister.get(name);
        return Optional.ofNullable(extracting);
    }

    void add(String name, Object object) {
        if(objectRegister.containsKey(name)){
            throw new RuntimeException("Register contains the object with that name");
        }
        checkOnInjection(object);
        objectRegister.put(name,object);
    }

    private void checkOnInjection(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Inject.class)){
                lazyInjectMap.put(field, object);
            }
        }
    }

    void add(Object objectWithoutName) {
        add(objectWithoutName.getClass().getName(), objectWithoutName);
    }

    public<T> Optional<T> get(Class<T> toClass) {
        return (Optional<T>) get(toClass.getName());
    }

    void inject() {
        lazyInjectMap.forEach((field, object) -> {
            Object dependency = this.get(field.getType()).get();
            field.setAccessible(true); //To make private field accessible
            try {
                field.set(object, dependency);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
