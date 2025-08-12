# JMH Spaces in Agent Paths Bug

This project demonstrates a bug in JMH where `-jvmArgs` containing file paths with spaces are incorrectly parsed, causing agent loading failures.

## Setup

Build the JMH jar:

```shell
./gradlew jmhJar
```

## Demonstrating the Bug

### 1. First, run the benchmark normally (this works):

```shell
java -jar build/libs/jmh-spaces-bug-reproduction-jmh.jar
```

This should run successfully and show benchmark results.

### 2. Now test with agent path containing spaces (this fails):

Create a directory with spaces and copy a dummy agent file:
```shell
mkdir -p "/tmp/agent with spaces"
touch "/tmp/agent with spaces/dummy.jar"
```

Try to run JMH with an agent path containing spaces:
```shell
java -jar build/libs/jmh-spaces-bug-reproduction-jmh.jar \
  -jvmArgs "-javaagent:/tmp/agent with spaces/dummy.jar"
```

**Expected**: JMH should pass the full agent path to the JVM  
**Actual**: JMH incorrectly splits the argument at the space, causing JVM error

### 3. Test the same agent path WITHOUT spaces (this works):

Create the same agent without spaces:
```shell
cp "/tmp/agent with spaces/dummy.jar" "/tmp/agent-no-spaces.jar"
```

Run JMH with the space-free path:
```shell
java -jar build/libs/jmh-spaces-bug-reproduction-jmh.jar \
  -jvmArgs "-javaagent:/tmp/agent-no-spaces.jar"
```

**Result**: This works because there are no spaces to cause incorrect parsing.

## The Bug

JMH incorrectly parses `-jvmArgs` by splitting on whitespace, even inside quoted strings. This means:

- `-jvmArgs "-javaagent:/tmp/agent with spaces/dummy.jar"` becomes multiple arguments
- The JVM receives malformed options and fails to start

## Workaround

Create symlinks without spaces:
```shell
ln -sf "/path/with spaces/agent" "/tmp/agent-link"
# Use /tmp/agent-link in JMH arguments instead
```
