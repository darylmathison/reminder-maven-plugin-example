package com.darylmathison;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "format",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresOnline = false, requiresProject = true,
    threadSafe = false)
public class GoogleCodeFormatterMojo extends AbstractMojo {

  @Parameter(property = "basedir", required = true)
  protected File basedir;

  public void execute() throws MojoExecutionException {
    getLog().info("basedir is " + basedir.getAbsolutePath());

    // Ensure the base directory is valid
    if (!basedir.isDirectory()) {
      getLog().error("Invalid base directory: " + basedir.getAbsolutePath());
      return;
    }

    File[] files = basedir.listFiles();
    if (files == null || files.length == 0) {
      getLog().info("No files to process in directory: " + basedir.getAbsolutePath());
      return;
    }

    for (File file : files) {
      if (!file.isFile() || !file.getName().endsWith(".java")) {
        // Skip non-Java files or directories
        getLog().info("Skipping non-Java file or directory: " + file.getName());
        continue;
      }

      try {
        String fileContent = readFile(file);
        if (fileContent.isEmpty()) {
          getLog().info("Skipping empty file: " + file.getName());
          continue;
        }

        String formattedSource = new Formatter().formatSource(fileContent);

        getLog().info("Formatted file: " + file.getName());
        getLog().info(formattedSource);

        writeFile(file, formattedSource);

      } catch (IOException e) {
        getLog().error("I/O error processing file: " + file.getName(), e);
      } catch (FormatterException formatterException) {
        getLog().error("Error formatting file: " + file.getName(), formatterException);
      }
    }
  }

  private String readFile(File file) throws IOException {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
      reader.lines().forEach(line -> content.append(line).append(System.lineSeparator()));
      return content.toString();
    }
  }

  private void writeFile(File file, String content) throws IOException {
    try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
      writer.write(content);
    }
  }
}