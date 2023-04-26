# LxPlot

A library for simple local or distant plotting

## Set up (Gradle)

* Open build.gradle

* Add jitpack to the repositories
```
repositories {
    ...
    // Add the following line
    maven { url "https://jitpack.io" }
}
```

* Add AMAK to the dependencies
```
dependencies {

    // Add the two following lines
    // AMAK
    implementation 'com.github.alexandreprl:lxplot:main-SNAPSHOT'
    
    ...
}
```

* Click on the Gradle Refresh button


## Use

Simply add

```
#!java

LxPlot.getChart("Name of your chart").add(xCoordinate, yCoordinate);
```
to draw a point at a specific coordinate.

Or refer to the examples java source files situated in the example directory. Examples are also included in the jar file in the following package fr.irit.smac.lxplot

## Change the color of a line (in local mode)

```
#!java
XYPlot plot = (XYPlot) ((LxPlotChart)LxPlot.getChart("NAME OF THE CHART")).getJFreeChart().getPlot();
//Set the color of the first series (Identified by 0) to red
plot.getRenderer().setSeriesPaint(0, Color.RED);
```