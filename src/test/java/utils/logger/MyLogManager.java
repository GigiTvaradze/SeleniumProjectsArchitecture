package utils.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MyLogManager {
    private static final String LOG_DIRECTORY = "./target/logs/";
    private static final String configFile = "src/test/resources/log4j2.xml";
    protected static final String FILE_PROPERTY_NAME = "logFilePath";


    public static ActionLogger getActionLogger(Method method, int currentInvocationCount) {
        String loggerName = getUniqueTestName(method, currentInvocationCount);
        String filePath = LOG_DIRECTORY + loggerName + ".log";
        deleteOldLogFile(filePath);
        Configurator.initialize(null, configFile);
        setUpFilePath(filePath);

        Logger logger = LogManager.getLogger(loggerName);
        return new ActionLogger(logger, filePath);
    }

    protected synchronized static void setUpFilePath(String filePath) {
        ThreadContext.put(FILE_PROPERTY_NAME, filePath);
        Configurator.reconfigure();
    }

    public static String getUniqueTestName(Method method, int currentInvocationCount) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        return className + "_" + methodName + "[" + currentInvocationCount + "]";
    }

    private static void deleteOldLogFile(String logFilePath) {
        Path path = Paths.get(logFilePath);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException ignore) {
            }
        }
    }
}