package com.example.demo.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AuthorityConfigGenerator.
 *
 * @author Thou
 * @date 2022/10/6
 */
@SuppressWarnings("all")
public class AuthorityConfigGenerator {

    private static final List<Class<? extends Annotation>> CONTROLLER_TYPE_LIST = new ArrayList<>();
    private static final List<Class<? extends Annotation>> MAPPING_TYPE_LIST = new ArrayList<>();
    private static final List<String> CLASSPATH_LIST = new ArrayList<>();
    private static final List<Class<?>> CLASS_LIST = new ArrayList<>();
    private static final List<String> URI_LIST = new ArrayList<>();

    static {
        CONTROLLER_TYPE_LIST.add(RestController.class);
        CONTROLLER_TYPE_LIST.add(Controller.class);

        MAPPING_TYPE_LIST.add(RequestMapping.class);
        MAPPING_TYPE_LIST.add(GetMapping.class);
        MAPPING_TYPE_LIST.add(PostMapping.class);
        MAPPING_TYPE_LIST.add(PutMapping.class);
        MAPPING_TYPE_LIST.add(DeleteMapping.class);
        MAPPING_TYPE_LIST.add(PatchMapping.class);
    }

    private static void scanClass(String packageName) throws Exception {
        String path = packageName.replace(".", File.separator);
        String classpath = AuthorityConfigGenerator.class.getResource("/").getPath();
        String srcPath = classpath + path;
        doPath(new File(srcPath));
        for (String s : CLASSPATH_LIST) {
            s = s.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "")
                    .replace("\\", ".")
                    .replace(".class", "");
            Class<?> clazz = Class.forName(s);
            for (Class<? extends Annotation> annoClazz : CONTROLLER_TYPE_LIST) {
                if (clazz.isAnnotationPresent(annoClazz)) {
                    CLASS_LIST.add(clazz);
                }
            }
        }
    }

    private static void doPath(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                doPath(f);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                CLASSPATH_LIST.add(file.getPath());
            }
        }
    }

    private static void parseClass() throws Exception {
        for (Class<?> c : CLASS_LIST) {
            URI_LIST.add("");
            String keyPrefix = "";
            RequestMapping mapping = (RequestMapping) c.getAnnotation(MAPPING_TYPE_LIST.get(0));
            if (Objects.nonNull(mapping)) {
                String value = mapping.value()[0];
                keyPrefix = value;
            }
            URI_LIST.add("### " + keyPrefix);
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                String keySuffix = "";
                String key = null;
                for (Class<? extends Annotation> clazz : MAPPING_TYPE_LIST) {
                    if (method.isAnnotationPresent(clazz)) {
                        Annotation anno = method.getDeclaredAnnotation(clazz);
                        if (anno instanceof RequestMapping) {
                            RequestMapping annoMapping = (RequestMapping)anno;
                            if (annoMapping.value().length != 0) {
                                keySuffix = annoMapping.value()[0];
                            }
                            key = keyPrefix + keySuffix;
                        }
                        if (anno instanceof GetMapping) {
                            GetMapping annoMapping = (GetMapping)anno;
                            if (annoMapping.value().length != 0) {
                                keySuffix = annoMapping.value()[0];
                            }
                            key = "GET" + keyPrefix + keySuffix;
                        }
                        if (anno instanceof PostMapping) {
                            PostMapping annoMapping = (PostMapping)anno;
                            if (annoMapping.value().length != 0) {
                                keySuffix = annoMapping.value()[0];
                            }
                            key = "POST" + keyPrefix + keySuffix;
                        }
                        if (anno instanceof PutMapping) {
                            PutMapping annoMapping = (PutMapping)anno;
                            if (annoMapping.value().length != 0) {
                                keySuffix = annoMapping.value()[0];
                            }
                            key = "PUT" + keyPrefix + keySuffix;
                        }
                        if (anno instanceof DeleteMapping) {
                            DeleteMapping annoMapping = (DeleteMapping)anno;
                            if (annoMapping.value().length != 0) {
                                keySuffix = annoMapping.value()[0];
                            }
                            key = "DELETE" + keyPrefix + keySuffix;
                        }
                        URI_LIST.add(key + "=");
                        break;
                    }
                }
            }
        }
    }

    private static void generator(String packageName) throws Exception {
        scanClass(packageName);
        parseClass();

        URI_LIST.remove(0);
        String pathname = System.getProperty("user.dir") + "/authority-config.properties";
        Files.write(Paths.get(pathname), URI_LIST);
    }

    public static void main(String[] args) throws Exception {
        generator("com.example.demo.controller");
    }
}
