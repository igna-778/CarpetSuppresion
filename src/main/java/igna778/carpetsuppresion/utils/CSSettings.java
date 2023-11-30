package igna778.carpetsuppresion.utils;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.*;
public class CSSettings {


    //By Igna778
    @Rule(
            desc = "something",
            category = {EXPERIMENTAL,OPTIMIZATION}
    )
    public static boolean doOldSyncUpdates = true;
}
