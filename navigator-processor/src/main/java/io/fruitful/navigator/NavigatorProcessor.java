package io.fruitful.navigator;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("io.fruitful.navigator.HasNavigator")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class NavigatorProcessor extends AbstractProcessor {

    private static final String HOST_NAME = "host";
    private static final String NAVIGATOR_ARRAY_NAME = "navigators";
    private static final String CLASS_HAS_NAVIGATOR_BINDER_NAME = "HasNavigatorBinder";
    private static final String CLASS_BINDER_NAME = "NavigatorBinder";
    private static final String METHOD_BIND_NAME = "bind";
    private static final String CLASS_NAVIGATOR_OWNER_KIND = "io.fruitful.navigator.internal.NavigatorOwnerKind";
    private static final String KIND = "kind";
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("======================= Process @HasNavigator =======================");
        final TypeElement hasNavigatorAnnotation = elementUtils.getTypeElement(HasNavigator.class.getName());
        final Set<? extends Element> annotatedHasNavigatorFields = roundEnv.getElementsAnnotatedWith(hasNavigatorAnnotation);
        if (annotatedHasNavigatorFields.isEmpty()) {
            return true;
        }

        verifyFieldsAccessible(annotatedHasNavigatorFields);

        note("Processing @HasNavigator" + annotatedHasNavigatorFields.size() + " fields");

        Map<Element, List<Element>> hasNavigatorByHostElement = new HashMap<>();
        for (Element hasNavigator : annotatedHasNavigatorFields) {
            final Element hostElement = hasNavigator.getEnclosingElement();
            List<Element> hasNavigators = hasNavigatorByHostElement.get(hostElement);
            if (hasNavigators == null) {
                hasNavigators = new LinkedList<>();
                hasNavigatorByHostElement.put(hostElement, hasNavigators);
            }
            hasNavigators.add(hasNavigator);
        }

        try {
            for (Map.Entry<Element, List<Element>> entry : hasNavigatorByHostElement.entrySet()) {
                generateHasNavigatorBinder(entry.getValue(), entry.getKey());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file", e);
        }


        System.out.println("========= Process @RootNavigator, @OwnNavigator, @ParentNavigator ==========");
        final TypeElement rootNavigatorAnnotation = elementUtils.getTypeElement(RootNavigator.class.getName());
        final TypeElement parentNavigatorAnnotation = elementUtils.getTypeElement(ParentNavigator.class.getName());
        final TypeElement ownNavigatorAnnotation = elementUtils.getTypeElement(OwnNavigator.class.getName());
        final Set<? extends Element> annotatedRootNavigatorFields = roundEnv.getElementsAnnotatedWith(rootNavigatorAnnotation);
        final Set<? extends Element> annotatedParentNavigatorFields = roundEnv.getElementsAnnotatedWith(parentNavigatorAnnotation);
        final Set<? extends Element> annotatedOwnNavigatorFields = roundEnv.getElementsAnnotatedWith(ownNavigatorAnnotation);

        Map<Element, List<Element>> navigatorByHostElement = new HashMap<>();
        TypeMirror navigatorType = null;
        final Set<? extends Element>[] annotatedFields = new Set[]{annotatedRootNavigatorFields, annotatedParentNavigatorFields, annotatedOwnNavigatorFields};
        for (Set<? extends Element> set : annotatedFields) {
            for (Element navigatorField : set) {
                navigatorType = navigatorField.asType();
                final Element hostElement = navigatorField.getEnclosingElement();
                List<Element> navigatorMembers = navigatorByHostElement.get(hostElement);
                if (navigatorMembers == null) {
                    navigatorMembers = new LinkedList<>();
                    navigatorByHostElement.put(hostElement, navigatorMembers);
                }
                navigatorMembers.add(navigatorField);
            }
        }

        try {
            for (Map.Entry<Element, List<Element>> entry : navigatorByHostElement.entrySet()) {
                generateNavigatorBinder(navigatorType, entry.getValue(), entry.getKey());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed writing class file", e);
        }

        return true;
    }

    private void generateNavigatorBinder(TypeMirror navigatorType, List<Element> elements, Element hostElement)
            throws IOException {
        PackageElement packageElement = elementUtils.getPackageOf(hostElement);
        final String hostClassName = hostElement.getSimpleName().toString();
        final String simpleClassName = binderNavigatorName(hostClassName);
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        note("writing class " + qualifiedClassName);
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                qualifiedClassName, elements.toArray(new Element[elements.size()]));

        ClassName hostElementName = ClassName.bestGuess(hostClassName);
        MethodSpec bindMethod = MethodSpec.methodBuilder(METHOD_BIND_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(hostElementName, HOST_NAME)
                .addParameter(ArrayTypeName.of(TypeName.get(navigatorType)), NAVIGATOR_ARRAY_NAME)
                .varargs()
                .returns(void.class)
                .addCode(generateBindMethod(hostElement, elements))
                .build();

        TypeSpec classType = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(bindMethod)
                .build();

        final Writer writer = sourceFile.openWriter();
        JavaFile.builder(packageElement.getQualifiedName().toString(), classType)
                .build()
                .writeTo(writer);
        writer.close();
    }

    private void generateHasNavigatorBinder(List<? extends Element> elements, Element hostElement)
            throws IOException {
        PackageElement packageElement = elementUtils.getPackageOf(hostElement);
        final String hostClassName = hostElement.getSimpleName().toString();
        final String simpleClassName = binderHasNavigatorName(hostClassName);
        final String qualifiedClassName = packageElement.getQualifiedName() + "." + simpleClassName;

        note("writing class " + qualifiedClassName);
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                qualifiedClassName, elements.toArray(new Element[elements.size()]));

        ClassName hostElementName = ClassName.bestGuess(hostClassName);
        MethodSpec bindMethod = MethodSpec.methodBuilder(METHOD_BIND_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(hostElementName, HOST_NAME)
                .addParameter(ClassName.bestGuess(CLASS_NAVIGATOR_OWNER_KIND), KIND)
                .returns(void.class)
                .addCode(generateBindMethodOfHasNavigator(hostElement, elements))
                .build();

        TypeSpec classType = TypeSpec.classBuilder(simpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(bindMethod)
                .build();

        final Writer writer = sourceFile.openWriter();
        JavaFile.builder(packageElement.getQualifiedName().toString(), classType)
                .build()
                .writeTo(writer);
        writer.close();
    }

    private CodeBlock generateBindMethodOfHasNavigator(Element hostElement, List<? extends Element> elements) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Element element : elements) {
            TypeMirror elementType = element.asType();
            ClassName binderClass = ClassName.bestGuess(binderNavigatorName(elementType.toString()));
            builder.addStatement("$T.$N($N.$N, $N.$N($N), $N.$N($N), $N.$N($N))", binderClass, METHOD_BIND_NAME,
                    HOST_NAME, element.getSimpleName(),
                    KIND, "getRootNavigator", HOST_NAME,
                    KIND, "getParentNavigator", HOST_NAME,
                    KIND, "getOwnNavigator", HOST_NAME);
        }
        return builder.build();
    }

    private CodeBlock generateBindMethod(Element hostElement, List<? extends Element> elements) {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (elements.isEmpty()) return builder.build();
        for (Element element : elements) {
            int index = 0;
            List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
            for (AnnotationMirror annotation : annotations) {
                TypeName annotationType = TypeName.get(annotation.getAnnotationType());
                if (annotationType.equals(TypeName.get(RootNavigator.class))) index = 0;
                if (annotationType.equals(TypeName.get(ParentNavigator.class))) index = 1;
                if (annotationType.equals(TypeName.get(OwnNavigator.class))) index = 2;
            }
            builder.addStatement("$N = $N", HOST_NAME + "." + element.getSimpleName(), NAVIGATOR_ARRAY_NAME + "[" + index + "]");
        }
        return builder.build();
    }

    private String binderHasNavigatorName(String name) {
        return name + "$" + CLASS_HAS_NAVIGATOR_BINDER_NAME;
    }

    private String binderNavigatorName(String name) {
        return name + "$" + CLASS_BINDER_NAME;
    }

    private void verifyFieldsAccessible(Set<? extends Element> elements) {
        for (Element element : elements) {
            if (element.getModifiers().contains(Modifier.PRIVATE)) {
                throw new IllegalStateException("Annotated fields cannot be private: " +
                        element.getEnclosingElement() + "#" + element + "(" + element.asType() + ")");
            }
        }
    }

    private void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "NavigatorProcessor: " + message);
    }
}
