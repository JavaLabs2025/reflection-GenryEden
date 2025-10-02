package org.example.generator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeneratableIndex {
    private static final String INDEX_PATH = "META-INF/org/example/generator/GeneratableIndex";

    public static Set<Class<?>> loadAnnotatedClasses() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        var url = cl.getResource(INDEX_PATH);
        if (url == null) {
            return Set.of();
        }

        Set<Class<?>> result = new LinkedHashSet<>();
        try (var in = url.openStream();
             BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = r.readLine()) != null) {
                String cn = line.trim();
                if (cn.isEmpty()) continue;
                try {
                    Class<?> c = Class.forName(cn, false, cl);
                    result.add(c);
                } catch (Throwable ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }
}
