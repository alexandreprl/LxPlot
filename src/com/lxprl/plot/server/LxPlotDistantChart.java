package com.lxprl.plot.server;

import java.io.PrintWriter;

import com.lxprl.plot.commons.ChartType;
import com.lxprl.plot.interfaces.ILxPlotChart;

/**
 * Representation of a distant chart made by a client.
 * 
 * @author Alexandre Perles
 * 
 */
public class LxPlotDistantChart implements ILxPlotChart {

	private String name;
	private PrintWriter out;

	public LxPlotDistantChart(String _name, ChartType _chartType, PrintWriter _out) {
		name = _name;
		out = _out;
		out.println("config;" + _name + ";" + _chartType.toString());
		out.flush();
	}

	@Override
	public void add(double _x, double _y) {
		add("", _x, _y);
	}

	@Override
	public void add(String _serieName, double _x, double _y) {
		out.println("add;" + name + ";" + _serieName + ";" + _x + ";" + _y);
		out.flush();
	}

	@Override
	public void close() {
		out.println("close;" + name);
		out.flush();
	}

}
