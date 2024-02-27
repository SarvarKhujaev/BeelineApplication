package com.beeline.beelineapplication.inspectors;

public class DataValidateInspector extends TimeInspector {
    public boolean objectIsNotNull (
            final Object o
    ) {
        return o != null;
    }
}