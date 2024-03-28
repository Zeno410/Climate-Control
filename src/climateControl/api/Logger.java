package climateControl.api;


public final class Logger {

    private static final org.apache.logging.log4j.Logger LOGGER;

    static {
        LOGGER = org.apache.logging.log4j.LogManager.getLogger("Geographicraft");
    }

    private Logger() {

    }

    public static void debug(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.DEBUG, format, data);
    }

    public static void trace(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.TRACE, format, data);
    }

    public static void info(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.INFO, format, data);
    }

    public static void warn(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.WARN, format, data);
    }

    public static void error(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.ERROR, format, data);
    }

    public static void fatal(String format, Object... data) {

        LOGGER.log(org.apache.logging.log4j.Level.FATAL, format, data);
    }

    public static void log(org.apache.logging.log4j.Level level, String format, Object... data) {

        LOGGER.log(level, format, data);
    }
}

