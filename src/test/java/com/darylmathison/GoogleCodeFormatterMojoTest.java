package com.darylmathison;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;


public class GoogleCodeFormatterMojoTest extends AbstractMojoTestCase {

  private final String baseTestDirectory = "src/test/resources/unit/code-formatter-mojo";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    cleanupPath(baseTestDirectory);
    super.tearDown();
  }

  private void cleanupPath(String path) throws IOException, GitAPIException {
    File basedir = new File(getBasedir());
    try(Git git = Git.open(basedir)) {
      git.checkout().addPath(path).call();
    }
  }

  public void testFormat() throws Exception {
    File pom = getTestFile(baseTestDirectory + "/pom.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());
    GoogleCodeFormatterMojo myMojo = (GoogleCodeFormatterMojo) lookupMojo("format", pom);
    assertNotNull(myMojo);
    myMojo.execute();
    assertTrue(testFiles(baseTestDirectory + "/src"));
  }

  public void testForEmptyDirectory() throws Exception {
    File pom = getTestFile(baseTestDirectory + "/pom-empty-directory.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());
    GoogleCodeFormatterMojo myMojo = (GoogleCodeFormatterMojo) lookupMojo("format", pom);
    assertNotNull(myMojo);
    myMojo.execute();
  }

    public void testFormatEmptyFiles() throws Exception {
    File pom = getTestFile(baseTestDirectory + "/pom-empty-file.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());
    GoogleCodeFormatterMojo myMojo = (GoogleCodeFormatterMojo) lookupMojo("format", pom);
    assertNotNull(myMojo);
    myMojo.execute();
  }

  private boolean testFiles(String path) throws IOException, FormatterException {
    File basedir = new File(path);
    if (basedir.isDirectory()) {
      for (File file : basedir.listFiles()) {
        String fileContent = readFile(file);
        if (fileContent.isEmpty()) {
          return false;
        }
        String formattedSource = new Formatter().formatSource(fileContent);
        return fileContent.equals(formattedSource);
      }
    }
    return false;
  }

  private String readFile(File file) throws IOException {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
      reader.lines().forEach(line -> content.append(line).append(System.lineSeparator()));
      return content.toString();
    }
  }
}