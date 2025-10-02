package org.example;


import org.example.classes.Cart;
import org.example.classes.Example;
import org.example.classes.Shape;
import org.example.generator.Generator;

public class GenerateExample {
    public static void main(String[] args) {
        var gen = new Generator();
        try {
            Object generated = gen.generateValueOfType(Shape.class);
            System.out.println(generated);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}