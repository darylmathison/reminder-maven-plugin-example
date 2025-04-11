System.err.println("Basedir for verification is " + basedir);
def file = new File(basedir, "/src/main/java/java/test/SampleApp.java");
BufferedReader reader = new BufferedReader(new FileReader(file));
long numLines = reader.lines().count();
return numLines > 5;