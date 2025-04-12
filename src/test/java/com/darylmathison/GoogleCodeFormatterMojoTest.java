package com.darylmathison;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * Created by Daryl on 3/31/2015.
 */
public class GoogleCodeFormatterMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJustFormat() throws Exception {
        File pom = getTestFile("src/test/resources/unit/code-formatter-mojo/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());
        GoogleCodeFormatterMojo myMojo = (GoogleCodeFormatterMojo) lookupMojo("format", pom);
        assertNotNull(myMojo);
        myMojo.execute();
    }
}