package com.leyou.geteway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-26 20:49
 */
@ConfigurationProperties(prefix = "leyou.filter")
public class Filterproperties {
    private List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
