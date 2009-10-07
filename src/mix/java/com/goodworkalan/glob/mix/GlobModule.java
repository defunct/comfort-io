package com.goodworkalan.glob.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class GlobModule extends BasicJavaModule {
    public GlobModule() {
        super(new Artifact("com.goodworkalan", "glob", "0.1-SNAPSHOT"));
        addDependency(new Artifact("com.goodworkalan", "cassandra", "0.7-SNAPSHOT"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addTestDependency(new Artifact("org.mockito", "mockito-core", "1.6"));
    }
}
