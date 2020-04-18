# easy-agent-plugin-istio-tracing
This project is plugin of easy-agent for istio distributed tracing.

<br/>

## How to use
#### 1. Add dependency to pom.xml (maven)

```xml
<dependency>
    <groupId>software.fitz</groupId>
    <artifactId>easy-agent-core</artifactId>
    <version>0.4.0-RELEASE</version>
</dependency>
<dependency>
    <groupId>software.fitz</groupId>
    <artifactId>easy-agent-plugin-istio-tracing</artifactId>
    <version>0.4.0-RELEASE</version>
</dependency>
``` 

<br/>

#### 2. Initialize the easy-agent and add IstioInterceptor in your premain-class

```java
public class PremainClass {

    public static void premain(String agentArgs, Instrumentation instrumentation) {

        EasyAgentBootstrap.initialize(agentArgs, instrumentation)
                .addPlugin(new IstioDistributedTracePlugin())
                .start();
    }
}
```

<br/>

#### 3. Add maven plugin to your pom.xml for build agent jar
1. Replace `{your-jar-name}` with the name of the jar you want.
2. Replace `{package.to.premain-class}` with a class that implements the `premain` method.

Reference : [easy-agent repository](https://github.com/joongsoo/easy-agent)

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>2.2</version>
    <executions>
        <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
            <configuration>
                <finalName>{your-jar-name}</finalName>
                <appendAssemblyId>false</appendAssemblyId>
                <archive>
                    <manifest>
                        <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                        <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
                    </manifest>
                    <manifestEntries>
                        <Premain-Class>{package.to.premain-class}</Premain-Class>
                        <Can-Retransform-Classes>true</Can-Retransform-Classes>
                        <Can-Redefine-Classes>true</Can-Redefine-Classes>
                        <Boot-Class-Path>{your-jar-name}.jar</Boot-Class-Path>
                    </manifestEntries>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### 4. Used it
Pass your agent jar file path to JVM argument

```
java -javaagent:/path/{your-agent}.jar -jar application.jar
``` 


<br/>
<br/>
<br/>
