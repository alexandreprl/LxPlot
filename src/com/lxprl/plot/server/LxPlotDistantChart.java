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

	private final String name;
	private final PrintWriter out;

	public LxPlotDistantChart(final String _name, final ChartType _chartType,
			final PrintWriter _out) {
		name = _name;
		out = _out;
		out.println("config;" + _name + ";" + _chartType.toString());
		out.flush();
	}

	@Override
	public void add(final double _x, final double _y) {
		add("", _x, _y);
	}

	@Override
	public void add(final String _serieName, final double _x, final double _y) {
		out.println("add;" + name + ";" + _serieName + ";" + _x + ";" + _y);
		out.flush();
	}

	@Override
	public void close() {
		out.println("close;" + name);
		out.flush();
	}

}
