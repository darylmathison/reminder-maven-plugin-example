package com.darylmathison;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "format",
    defaultPhase = LifecyclePhase.PROCESS_SOURCES,
    requiresOnline = false, requiresProject = true,
    threadSafe = false)
public class GoogleCodeFormatterMojo extends AbstractMojo {

  @Parameter(property = "srcBaseDir", defaultValue = "${project.build.sourceDirectory}")
  protected File srcBaseDir;

  private final Formatter formatter;

  @Inject
  public GoogleCodeFormatterMojo(Formatter formatter) {
    this.formatter = formatter;
  }

  public void execute() throws MojoExecutionException {
    getLog().info("basedir is " + srcBaseDir.getAbsolutePath());

    // Ensure the base directory is valid
    if (!srcBaseDir.isDirectory()) {
      getLog().error("Invalid base directory: " + srcBaseDir.getAbsolutePath());
      return;
    }

    File[] files = srcBaseDir.listFiles();
    if (files == null || files.length == 0) {
      getLog().info("No files to process in directory: " + srcBaseDir.getAbsolutePath());
      return;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        processDirectory(file);
      } else if (file.isFile() && file.getName().endsWith(".java")) {
        processFile(file);
      }
    }
  }



  private void processDirectory(File directory) {
    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }
    for (File file : files) {
      if (file.isDirectory()) {
        processDirectory(file);
      } else if (file.isFile() && file.getName().endsWith(".java")) {
        processFile(file);
      }
    }
  }

  private void processFile(File file) {
    try {
        String fileContent = readFile(file);
        if (!fileContent.isEmpty()) {
          String formattedSource = formatter.formatSource(fileContent);

          getLog().info("Formatted file: " + file.getName());
          getLog().info(formattedSource);

          writeFile(file, formattedSource);
        } else {
          getLog().info("Skipping empty file: " + file.getName());
        }
      } catch (IOException e) {
        getLog().error("I/O error processing file: " + file.getName(), e);
      } catch (FormatterException formatterException) {
        getLog().error("Error formatting file: " + file.getName(), formatterException);
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