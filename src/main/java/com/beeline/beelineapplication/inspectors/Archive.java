package com.beeline.beelineapplication.inspectors;

import java.util.Map;

public class Archive extends LogInspector {
    protected Archive () {}

    protected final Map< String, String > CONNECTION_ERRORS = Map.of(
            "08000", "DATABASE IS NOT INSTALLED",
            "08001", "CANNOT ESTABLISH CONNECTION",
            "08003", "CONNECTION WAS LOST",
            "08004", "DATABASE REJECTED THE CONNECTION",
            "08006", "FAIL TO CONNECT TO DATABASE",
            "08007",  "TRANSACTION RESOLUTION UNKNOWM"
    );
}
