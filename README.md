# Simple Proxy

Welcome to Simple Proxy project!

This project aims to be a very simple TCP proxy.

It has been used in real scenarios for DevOps and IT engineers in order
to reach production servers through an intermediate machine.

The usage is very simple:

```bash
charlie@imperator$java -cp simple-proxy-1.0-SNAPSHOT.jar com.carlosprados.lab.simpleproxy.Proxy <localport> <host> <port> <timeout_ms>
```

You can buil it using Maven:

```bash
charlie@imperator$ mvn clean package
```
