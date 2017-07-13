package info.naiv.lab.java.jmt.enumgen.maven.plugin;

import lombok.Value;

/**
 *
 * @author enlo
 */
@Value
public class CodeData {

    String code;
    String name;

    public CodeData(String name, String code) {
        this.code = code;
        this.name = name;
    }

}
