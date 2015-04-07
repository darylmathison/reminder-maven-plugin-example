package com.darylmathison;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Mojo(name = "remind",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresOnline = false, requiresProject = true,
        threadSafe = false)
public class ReminderMojo extends AbstractMojo {

    @Parameter(property = "basedir", required = true)
    protected File basedir;

    @Parameter(property = "message", required = true)
    protected String message;

    @Parameter(property = "numOfWeeks", defaultValue = "6", required = true)
    protected int numOfWeeks;

    public void execute() throws MojoExecutionException {

        File timestampFile = new File(basedir, "timestamp.txt");
        getLog().debug("basedir is " + basedir.getName());
        if(!timestampFile.exists()) {
            basedir.mkdirs();
            getLog().info(message);
            timestamp(timestampFile);
        } else {
            LocalDateTime date = readTimestamp(timestampFile);
            date.plus(numOfWeeks, ChronoUnit.WEEKS);
            if(date.isBefore(LocalDateTime.now())) {
                getLog().info(message);
                timestamp(timestampFile);
            }
        }
    }

    private void timestamp(File file) throws MojoExecutionException {
        try(FileWriter w = new FileWriter(file)) {
            LocalDateTime localDateTime = LocalDateTime.now();
            w.write(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + file, e);
        }
    }

    private LocalDateTime readTimestamp(File file) throws MojoExecutionException {
        try(FileReader r = new FileReader(file)) {
            char[] buffer = new char[1024];
            int len = r.read(buffer);
            LocalDateTime date = LocalDateTime.parse(String.valueOf(buffer, 0, len));
            return date;
        } catch(IOException ioe) {
            throw new MojoExecutionException("Error reading file " + file, ioe);
        }
    }
}
