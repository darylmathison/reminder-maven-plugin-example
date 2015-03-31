package com.darylmathison;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mojo(name = "remind",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        requiresOnline = true, requiresProject = true,
        threadSafe = false)
public class ReminderMojo extends AbstractMojo {

    @Parameter(property = "timestampLocation", defaultValue = "${project.basedir}", required = true)
    protected File timestampFileLocation;

    public void execute() throws MojoExecutionException {
        File f = timestampFileLocation;

        if (!f.exists()) {
            f.mkdirs();
        }

        File file = new File(f, "remind.txt");

        FileWriter w = null;
        try {
            w = new FileWriter(file);

            w.write(String.valueOf(System.currentTimeMillis()));
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + file, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
