package org.example.generator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_23)
@SupportedAnnotationTypes("org.example.generator.Generatable")
public class GeneratableProcessor extends AbstractProcessor {
    private static final String INDEX_PATH = "META-INF/org/example/generator/GeneratableIndex";
    private final Set<String> collected = new LinkedHashSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(
                processingEnv.getElementUtils().getTypeElement("org.example.generator.Generatable"))) {
            if (e.getKind() == ElementKind.CLASS || e.getKind() == ElementKind.INTERFACE) {
                String fqcn = ((TypeElement) e).getQualifiedName().toString();
                collected.add(fqcn);
            }
        }

        if (roundEnv.processingOver()) {
            try {
                writeIndex();
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Failed to write index: " + ex.getMessage());
            }
        }
        return true;
    }

    private void writeIndex() throws IOException {
        FileObject fo = processingEnv.getFiler()
                .createResource(StandardLocation.CLASS_OUTPUT, "", INDEX_PATH);
        try (Writer w = fo.openWriter()) {
            for (String cn : collected) {
                w.write(cn);
                w.write('\n');
            }
        }
    }
}
