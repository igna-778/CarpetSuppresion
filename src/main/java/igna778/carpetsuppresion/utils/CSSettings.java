package igna778.carpetsuppresion.utils;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.*;
public class CSSettings {
    //TODO: write description

    //By Igna778
    @Rule(
            desc = "something",
            category = {EXPERIMENTAL,OPTIMIZATION}
    )
    public static boolean doOldSyncUpdates = true;
    @Rule(
            desc = "TODO",
            category = {EXPERIMENTAL,OPTIMIZATION}
    )
    public static boolean tickEntity = true;
}
