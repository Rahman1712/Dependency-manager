package com.ar.util;

public class Dependency {
    private String dependency;
    private String groupId;
    private String artifactId;
    private String version;
    private String description;
    private String scope;
    private String optional;

    public Dependency() {
    }

    
    public Dependency(String dependency, String groupId, String artifactId, String version, String description, String scope, String optional) {
        this.dependency = dependency;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.description = description;
        this.scope = scope;
        this.optional = optional;
    }

    
    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    @Override
    public String toString() {
        return "Dependency{" + "dependency=" + dependency + ", groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + ", description=" + description + ", scope=" + scope + ", optional=" + optional + '}'+"\n";
    }
    
    
}
