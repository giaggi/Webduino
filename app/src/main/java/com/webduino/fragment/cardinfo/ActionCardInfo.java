package com.webduino.fragment.cardinfo;

import com.webduino.scenarios.Action;
import com.webduino.scenarios.ProgramAction;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ActionCardInfo extends CardInfo {
    public Action action;

    public ActionCardInfo() {
        type = TYPE_ACTION;
    }
}