package com.labs64.netlicensing.examples;

import org.junit.jupiter.api.Test;

public class RunExamples {

    @Test
    public void testCallEveryAPIMethod() {
        new CallEveryAPIMethod().execute();
    }

    @Test
    public void testOfflineValidation() {
        new OfflineValidation().execute();
    }

}
