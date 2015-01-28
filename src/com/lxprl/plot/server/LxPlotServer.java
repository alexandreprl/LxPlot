package com.lxprl.plot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import com.lxprl.plot.commons.ChartType;
import com.lxprl.plot.interfaces.ILxPlotChart;
import com.lxprl.plot.interfaces.ILxPlotServer;

/**
 * Server which is able to receive requests from local or distant client.
 * 
 * @author Alexandre Perles
 * 
 */
public class LxPlotServer implements ILxPlotServer, Runnable {

	private ServerSocket socket;
	private boolean running = false;
	private Map<String, ILxPlotChart> charts = new TreeMap<String, ILxPlotChart>();

	@Override
	public ILxPlotChart getChart(String _name) {
		if (!charts.containsKey(_name)) {
			charts.put(_name, new LxPlotChart(_name));
		}
		return charts.get(_name);
	}

	/**
	 * Create server NOT accessible through the network
	 * 
	 * @param _name
	 */
	public LxPlotServer(String _name) {
		LxPlotChart.prepareWindow(_name);
	}

	/**
	 * Create server accessible through the network and locally
	 * 
	 * @param _name
	 * @param _port
	 */
	public LxPlotServer(String _name, int _port) {
		this(_name + " - localhost:" + _port);
		try {
			socket = new ServerSocket(_port);
			running = true;
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket clientSocket = socket.accept();
				new LxPlotConnectedClient(clientSocket, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void configChart(String _name, ChartType _chartType) {
		charts.put(_name, new LxPlotChart(_name, _chartType));
	}

}
