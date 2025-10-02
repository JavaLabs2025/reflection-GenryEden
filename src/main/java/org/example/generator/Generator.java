package org.example.generator;

import java.lang.reflect.*;
import java.util.*;

public class Generator {
    private final Random random = new Random();
    private final Set<Class<?>> generatable = GeneratableIndex.loadAnnotatedClasses();
    private final Map<Class<?>, Integer> classCount = new HashMap<>();

    public Object generateValueOfType(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return generate(clazz);
    }
    
    private double getCycleBreakProbability(Class<?> clazz) {
        int count = classCount.getOrDefault(clazz, 0);
        if (count == 0) {
            return 0.0;
        }
        return 1.0 - Math.pow(0.5, count);
    }

    private Object generate(Type type) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (type instanceof Class<?> clazz) {
            return generate(clazz);
        } else if (type instanceof ParameterizedType parameterizedType) {
            return generate(parameterizedType);
        }
        return null;
    }

    private Object generate(ParameterizedType parameterizedType) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class<?> rawClass) {
            if (rawClass.isAssignableFrom(List.class)) {
                return generateList(parameterizedType.getActualTypeArguments()[0]);
            }
        }
        return null;
    }

    private Object generate(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (clazz.isArray()) {
            return generateArray(clazz.componentType());
        }
        if (clazz.isInterface()) {
            return generateForInterface(clazz);
        }
        if (clazz.isPrimitive()) {
            return generatePrimitive(clazz);
        }
        if (clazz.isAssignableFrom(String.class)) {
            return generateRandomString();
        }
        if (clazz.isAssignableFrom(Integer.class)) {
            return random.nextInt();
        }
        if (clazz.isAssignableFrom(Double.class)) {
            return random.nextDouble();
        }
        if (clazz.isAssignableFrom(Long.class)) {
            return random.nextLong();
        }
        if (clazz.isAssignableFrom(Boolean.class)) {
            return random.nextBoolean();
        }
        
        int currentCount = classCount.getOrDefault(clazz, 0);
        double breakProbability = getCycleBreakProbability(clazz);
        
        if (currentCount > 0 && random.nextDouble() < breakProbability) {
            return null;
        }
        
        classCount.put(clazz, currentCount + 1);
        
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            int randomConstructorIndex = new Random().nextInt(constructors.length);
            Constructor<?> randomConstructor = constructors[randomConstructorIndex];
            Type[] params = randomConstructor.getGenericParameterTypes();
            Object[] objects = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                objects[i] = generate(params[i]);
            }
            return randomConstructor.newInstance(objects);
        } finally {
            // Уменьшаем счетчик после создания объекта
            classCount.put(clazz, Math.max(0, classCount.get(clazz) - 1));
        }
    }

    private String generateRandomString() {
        int length = random.nextInt(256);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) random.nextInt(32, 127));
        }
        return sb.toString();
    }

    private List<Object> generateList(Type actualTypeArgument) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int length = random.nextInt(256);
        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(generate(actualTypeArgument));
        }
        return list;
    }

    private Object generatePrimitive(Class<?> clazz) {
        if (clazz.isAssignableFrom(Integer.TYPE)) {
            return random.nextInt();
        } else if (clazz.isAssignableFrom(Boolean.TYPE)) {
            return random.nextBoolean();
        } else if (clazz.isAssignableFrom(Long.TYPE)) {
            return random.nextLong();
        } else if (clazz.isAssignableFrom(Double.TYPE)) {
            return random.nextDouble();
        }
        return null;
    }

    private Object generateForInterface(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (clazz.isAssignableFrom(List.class)) {
            return generateList(clazz.getGenericInterfaces()[0]);
        }
        List<Class<?>> assignable = new LinkedList<>();
        for (Class<?> classes: generatable) {
            if (clazz.isAssignableFrom(classes) && !classes.isInterface()) {
                assignable.add(classes);
            }
        }
        return generateValueOfType(assignable.get(random.nextInt(assignable.size())));
    }

    private Object[] generateArray(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int length = random.nextInt(256);
        Object[] ans = (Object[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            ans[i] = generateValueOfType(clazz);
        }
        return ans;
    }
}
