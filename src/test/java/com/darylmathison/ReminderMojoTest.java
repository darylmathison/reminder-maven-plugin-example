package com.darylmathison;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * Created by Daryl on 3/31/2015.
 */
public class ReminderMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJustMessage() throws Exception {
        File pom = getTestFile("src/test/resources/unit/reminder-mojo/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());
        ReminderMojo myMojo = (ReminderMojo) lookupMojo("remind", pom);
        assertNotNull(myMojo);
        myMojo.execute();
    }
}