package com.lxprl.plot.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import com.lxprl.plot.commons.ChartType;
import com.lxprl.plot.interfaces.ILxPlotChart;
import com.lxprl.plot.interfaces.ILxPlotServer;

/**
 * Representation of the server made by a client
 *
 * @author Alexandre Perles
 *
 */
public class LxPlotDistantServer implements ILxPlotServer {
	private final Map<String, ILxPlotChart> charts = new TreeMap<String, ILxPlotChart>();
	private Socket socket;
	private PrintWriter out;
	private boolean uniqueWindow;

	public LxPlotDistantServer(final String _host, final int _port) {
		try {
			socket = new Socket(_host, _port);
			out = new PrintWriter(socket.getOutputStream());
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ILxPlotChart getChart(final String _name) {
		if (!charts.containsKey(_name)) {
			charts.put(_name,
					new LxPlotDistantChart(_name, ChartType.LINE, out));
		}
		return charts.get(_name);
	}

	@Override
	public boolean getUniqueWindow() {
		return uniqueWindow;
	}

	@Override
	public void setChartType(final String _name, final ChartType _chartType) {
		charts.put(_name, new LxPlotDistantChart(_name, _chartType, out));
	}

	@Override
	public void setGridSize(final int _cols, final int _rows) {
		out.println("gridSize;" + _cols + ";" + _rows);
		out.flush();
	}

	@Override
	public void setUniqueWindow(final boolean _uniqueWindow) {
		uniqueWindow = _uniqueWindow;
		out.println("uniqueWindow;" + (_uniqueWindow ? 1 : 0));
		out.flush();
	}

}
