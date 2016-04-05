/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.naiv.lab.java.jmt.pojogen.maven.plugin;

import info.naiv.lab.java.jmt.Misc;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.io.NIOUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.AntPathMatcher;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 *
 * @author enlo
 */
@Mojo(
        name = "jmt-pojogen",
        threadSafe = true,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        requiresProject = true
)
public class PojoGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/jmtPojo")
    protected String outputDirectory;

    @Parameter(property = "project.build.sourceEncoding", defaultValue = "UTF-8")
    protected String encoding;

    @Parameter
    protected String url;

    @Parameter
    protected String schema;

    @Parameter
    protected String username;

    @Parameter
    protected String password;

    @Parameter(required = true)
    protected String packageName;

    @Parameter(defaultValue = "")
    protected String classNameSuffix;

    @Parameter
    protected String classTemplate;

    @Parameter
    protected List<String> excludeTables;

    public CompiledTemplate getClassTemplate() throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(classTemplate))) {
            String templ = NIOUtils.toString(is, Charset.forName(encoding));
            return TemplateCompiler.compileTemplate(templ);
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        try {
            final Path path = buildOutputDirectory();
            if (Files.exists(path)) {
                // ディレクトリがすでに存在する場合は終了.
                log.info(path + " is exists.");
                return;
            }

            final DataSource dataSource = new DriverManagerDataSource(url, username, password);
            final Charset charset = Charset.forName(encoding);
            final CompiledTemplate clsTempl = getClassTemplate();
            final PojoGenerator builder = new PojoGenerator(schema, packageName, classNameSuffix, clsTempl);
            final AntPathMatcher matcher = new AntPathMatcher();

            Files.createDirectories(path);
            JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {
                    List<String> tableNames = getTableNames(dbmd);
                    for (String tableName : tableNames) {
                        if (isExcludeTarget(tableName)) {
                            log.info(tableName + " is exclude.");
                            continue;
                        }
                        CodeData code = builder.tableToBean(dbmd, tableName);
                        writeCode(path, code);
                    }
                    return null;
                }

                protected boolean isExcludeTarget(final String tableName) {
                    if (isEmpty(excludeTables)) {
                        return false;
                    }
                    return Misc.contains(excludeTables, new Predicate1<String>() {
                        @Override
                        public boolean test(String obj) {
                            return matcher.match(obj, tableName);
                        }
                    });
                }

                protected void writeCode(Path dir, CodeData code) {
                    try {
                        Path file = dir.resolve(code.getClassName() + ".java");
                        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
                            writer.append(code.getCode());
                        }
                    }
                    catch (IOException ex) {
                        log.error(ex);
                    }
                }

                protected List<String> getTableNames(DatabaseMetaData dbmd) throws SQLException {
                    List<String> tableNames = new ArrayList<>();
                    try (ResultSet rs = dbmd.getTables(null, schema, "%", new String[]{"TABLE", "VIEW"})) {
                        while (rs.next()) {
                            tableNames.add(rs.getString("TABLE_NAME"));
                        }
                    }
                    return tableNames;
                }
            });
        }
        catch (MetaDataAccessException | IOException ex) {
            log.error(ex);
            throw new MojoFailureException("bean creation failed. ", ex);
        }

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
