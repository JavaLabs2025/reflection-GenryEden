package org.example.generator;

import org.example.classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratorTest {
    
    private Generator generator;
    
    @BeforeEach
    void setUp() {
        generator = new Generator();
    }
    
    @Test
    void testGenerateRectangle() throws Exception {
        Object result = generator.generateValueOfType(Rectangle.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Rectangle);
    }
    
    @Test
    void testGenerateTriangle() throws Exception {
        Object result = generator.generateValueOfType(Triangle.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Triangle);
    }
    
    @Test
    void testGenerateProduct() throws Exception {
        Object result = generator.generateValueOfType(Product.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Product);
    }
    
    @Test
    void testGenerateCart() throws Exception {
        Object result = generator.generateValueOfType(Cart.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Cart);
    }
    
    @Test
    void testGenerateBinaryTreeNode() throws Exception {
        Object result = generator.generateValueOfType(BinaryTreeNode.class);
        
        assertNotNull(result);
        assertTrue(result instanceof BinaryTreeNode);
    }
    
    @Test
    void testGenerateShape() throws Exception {
        Object result = generator.generateValueOfType(Shape.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Shape);
    }
    
    @Test
    void testGenerateExample() throws Exception {
        Object result = generator.generateValueOfType(Example.class);
        
        assertNotNull(result);
        assertTrue(result instanceof Example);
    }
}
