package com.beeline.beelineapplication.inspectors;

public class DataValidateInspector {
    protected DataValidateInspector () {}

    public boolean objectIsNotNull (
            final Object o
    ) {
        return o != null;
    }
}