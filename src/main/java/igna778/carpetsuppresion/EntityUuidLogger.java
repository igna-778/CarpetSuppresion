package igna778.carpetsuppresion;

import carpet.logging.HUDLogger;

import java.lang.reflect.Field;

public class EntityUuidLogger extends HUDLogger {
    public static EntityUuidLogger create() {
        try {
            return new EntityUuidLogger(CarpetSuppresion.class.getDeclaredField("__EntityUUID"), "EntityUUID", "all", new String[]{"all", "total", "overworld", "nether", "end"}, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public EntityUuidLogger(Field field, String logName, String def, String[] options, boolean strictOptions) {
        super(field, logName, def, options, strictOptions);
    }
}
