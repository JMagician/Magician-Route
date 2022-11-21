<h1> 
    <a href="https://magician-io.com">Magician-Route</a> ·
    <img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/jdk-8+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>
</h1>

Magician-Route is the official web component of Magician, it is developed from Magician-Web, mainly by removing the reflection inside and sacrificing a little ease of use to make the performance higher.

## Documentation

[https://magician-io.com](https://magician-io.com)

## Example

### Importing dependencies

```xml
<!-- This is the jar package build by this project -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician-Route</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- This is Magician -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician</artifactId>
    <version>2.0.7</version>
</dependency>

<!-- This is the log package, which supports any package that can be bridged with slf4j -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>1.7.12</version>
</dependency>
```

### Creating core handlers

```java
@HttpHandler(path="/")
public class DemoHandler implements HttpBaseHandler {

    @Override
    public void request(MagicianRequest magicianRequest, MagicianResponse response) {
        try{
            MagicianRoute.request(magicianRequest);
        } catch (Exception e){
        }
    }
}
```

### Creating Route

```java
@Route
public class DemoRoute implements MagicianInitRoute {

    @Override
    public void initRoute(MagicianRouteCreate routeCreate) {
        routeCreate.get("/demo/getForm", request -> {
            DemoVO demoVO = ConversionUtil.conversionAndVerification(request, DemoVO.class);
            System.out.println(request.getParam("name") + "---" + demoVO.getName());
            return "{\"msg\":\"hello login\"}";
        });

        routeCreate.post("/demo/json", request -> {
            DemoVO demoVO = ConversionUtil.conversionAndVerification(request, DemoVO.class);
            System.out.println(request.getJsonParam() + "---" + demoVO.getName());
            return "{\"msg\":\"hello json\"}";
        });
    }
}
```


### Start HTTP service

```java
Magician.createHttp()
                .scan("handler和controller所在的包名")
                .bind(8080);
```