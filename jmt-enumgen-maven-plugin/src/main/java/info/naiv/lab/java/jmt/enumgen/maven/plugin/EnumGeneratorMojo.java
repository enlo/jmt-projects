/*
 * The MIT License
 *
 * Copyright 2017 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.enumgen.maven.plugin;

import com.google.common.collect.Multimap;
import static info.naiv.lab.java.jmt.Strings.*;
import info.naiv.lab.java.jmt.io.NIOUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 *
 * @author enlo
 */
@Mojo(
        name = "jmt-enumgen",
        threadSafe = true,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        requiresProject = true
)
public class EnumGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter
    protected String classTemplate;

    @Parameter(property = "project.build.sourceEncoding", defaultValue = "UTF-8")
    protected String encoding;

    @Parameter(defaultValue = "")
    protected String enumSourceCsv;

    @Parameter(defaultValue = "")
    protected String enumSourceDirectory;

    @Parameter(required = true, defaultValue = "*")
    protected String filter;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/jmt")
    protected String outputDirectory;

    @Parameter(required = true)
    protected String packageName;

    @Parameter
    protected String propertiesSourceEnumName;

    @Parameter(defaultValue = "")
    protected String propertiesSourceFieldPrefix;

    @Parameter
    protected List<String> propertiesSourceList;

    @Parameter(required = true, defaultValue = "\\s+")
    protected String delimiter;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        try {
            final Path path = buildOutputDirectory();
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            final Charset charset = Charset.forName(encoding);
            final CompiledTemplate clsTempl = getClassTemplate();
            final EnumGenerator builder = new EnumGenerator(packageName, clsTempl);
            final Properties props = getProperties();
            if (!props.stringPropertyNames().isEmpty()) {
                log.info("properties found. ");
                CodeData code = builder.propertiesToCode(propertiesSourceEnumName,
                                                         propertiesSourceFieldPrefix,
                                                         props);
                log.info("generate " + code.getName());
                writeCode(path, code, charset);
            }
            if (isNotBlank(enumSourceCsv)) {
                log.info("enumSourceCsv found. ");
                Multimap<String, EnumEntry> entries
                        = EnumSource.createEnumEntries(Paths.get(enumSourceCsv), charset, delimiter);
                for (String key : entries.keySet()) {
                    CodeData code = builder.entriesToCode(key, entries.get(key));
                    log.info("generate " + code.getName());
                    writeCode(path, code, charset);
                }
            }
            if (isNotBlank(enumSourceDirectory)) {
                List<Path> fileList = new ArrayList<>();
                for (String dir : project.getCompileSourceRoots()) {
                    Path p = Paths.get(dir, enumSourceDirectory);
                    List<Path> files = NIOUtils.listPaths(p, filter);
                    log.info("listup " + p + "," + files.size());
                    fileList.addAll(files);
                }
                Properties np = new Properties();
                Pattern packagePattern = Pattern.compile("^package\\s+([^;]+);");
                Pattern classPattern = Pattern.compile("class\\s+(\\w+)");
                for (Path key : fileList) {
                    String className = "";
                    String packName = "";
                    try (BufferedReader reader = Files.newBufferedReader(key, UTF_8);) {
                        String line;
                        do {
                            line = reader.readLine();
                            Matcher mp = packagePattern.matcher(line);
                            if (mp.find()) {
                                packName = mp.group(1) + ".";
                            }
                            Matcher mc = classPattern.matcher(line);
                            if (mc.find()) {
                                className = mc.group(1);
                                break;
                            }
                        }
                        while (line != null);
                    }
                    String fullName = packName + className;
                    np.setProperty(fullName, className);
                }
                CodeData code = builder.propertiesToCode(propertiesSourceEnumName,
                                                         propertiesSourceFieldPrefix,
                                                         np);
                log.info("generate " + code.getName());
                writeCode(path, code, charset);
            }
        }
        catch (IOException ex) {
            log.error(ex);
            throw new MojoFailureException("bean creation failed. ", ex);
        }
    }

    public CompiledTemplate getClassTemplate() throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(classTemplate))) {
            String templ = NIOUtils.toString(is, Charset.forName(encoding));
            return TemplateCompiler.compileTemplate(templ);
        }
    }

    public Properties getProperties() throws IOException {

        if (propertiesSourceList == null || propertiesSourceList.isEmpty()) {
            return new Properties();
        }

        final AntPathMatcher matcher = new AntPathMatcher();
        final PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        Resource[] resources = new Resource[propertiesSourceList.size()];
        for (int i = 0; i < propertiesSourceList.size(); i++) {
            FileSystemResource p = new FileSystemResource(propertiesSourceList.get(i));
            resources[i] = p;
        }
        pfb.setLocations(resources);
        pfb.afterPropertiesSet();
        Properties source = pfb.getObject();
        Properties dest = new Properties();
        for (String key : source.stringPropertyNames()) {
            if (matcher.match(filter, key)) {
                dest.setProperty(key, source.getProperty(key));
            }
        }
        return dest;
    }

    protected Path buildOutputDirectory() throws IOException {
        final Path outputDir = Paths.get(this.outputDirectory);
        Files.createDirectories(outputDir);
        if (project != null) {
            project.addCompileSourceRoot(outputDir.toString());
        }

        final String packagePath = convertClassNameToResourcePath(packageName);
        final Path path = outputDir.resolve(packagePath);
        return path;
    }

    protected void writeCode(Path dir, CodeData code, Charset charset) {
        try {
            Path file = dir.resolve(code.getName() + ".java");
            try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
                writer.append(code.getCode());
            }
        }
        catch (IOException ex) {
            getLog().error(ex);
        }
    }

}
