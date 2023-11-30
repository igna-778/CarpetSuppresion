package igna778.carpetsuppresion;

import carpet.logging.HUDLogger;

import java.lang.reflect.Field;

public class BlockEventQueueLogger extends HUDLogger {
    public static BlockEventQueueLogger create() {
        try {
            return new BlockEventQueueLogger(CarpetSuppresion.class.getDeclaredField("__blockEvents"), "blockEventQueue", "all", new String[]{"all", "total", "overworld", "nether", "end"}, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public BlockEventQueueLogger(Field field, String logName, String def, String[] options, boolean strictOptions) {
        super(field, logName, def, options, strictOptions);
    }
}