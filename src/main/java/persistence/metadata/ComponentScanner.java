package persistence.metadata;

import jakarta.persistence.Entity;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentScanner {

    private static final String DELIMITER = ".";
    private static final String SEPARATOR = "/";
    private static final String CLASS_EXTENSION = ".class";

    public ComponentScanner() {
    }

    public List<Class<?>> scan(final String basePackage) {

        String path = basePackage.replace(DELIMITER, SEPARATOR);
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());

        if (baseDir.exists() && baseDir.isDirectory()) {
            return getEntityClasses(basePackage, baseDir).stream()
                    .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<Class<?>> getEntityClasses(final String basePackage,
                                                   final File baseDir) {
        List<Class<?>> classes = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(baseDir.listFiles()))
                .forEach(file -> {
                    if (file.isDirectory()) {
                        directoryScan(classes, basePackage, file);
                        return;
                    }
                    fileScan(classes, basePackage, file);
                });
        return classes;
    }

    private void directoryScan(final List<Class<?>> classes, final String basePackage, final File file) {
        classes.addAll(scan(basePackage + DELIMITER + file.getName()));
    }

    private void fileScan(final List<Class<?>> classes, final String basePackage, final File file) {
        if (file.getName().endsWith(CLASS_EXTENSION)) {
            String className = basePackage + DELIMITER + file.getName().substring(0, file.getName().length() - 6);
            classes.add(getClass(className));
        }
    }

    private Class<?> getClass(final String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new NotFoundClassException();
        }
    }

}
