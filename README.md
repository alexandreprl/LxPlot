# LxPlot

A library for simple local or distant plotting

## Install
Add the file [lx-plot-standalone.jar (Latest stable version: 1.0)](https://bitbucket.org/perlesa/lx-plot/raw/1.0/lx-plot-standalone.jar) to your project folder. Right click on it->Build path->Add to build path.
	
## Use

Simply add

```
#!java

LxPlot.getChart("Name of your chart").add(xCoordinate, yCoordinate);
```
to draw a point at a specific coordinate.

Or refer to the examples java source files situated in the example directory. Examples are also included in the jar file in the following package fr.irit.smac.lxplot