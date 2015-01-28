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
	private Map<String, ILxPlotChart> charts = new TreeMap<String, ILxPlotChart>();
	private Socket socket;
	private PrintWriter out;

	public LxPlotDistantServer(String _host, int _port) {
		try {
			socket = new Socket(_host, _port);
			out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ILxPlotChart getChart(String _name) {
		if (!charts.containsKey(_name)) {
			charts.put(_name, new LxPlotDistantChart(_name, ChartType.LINE, out));
		}
		return charts.get(_name);
	}

	@Override
	public void configChart(String _name, ChartType _chartType) {
		charts.put(_name, new LxPlotDistantChart(_name, _chartType, out));
	}

}
