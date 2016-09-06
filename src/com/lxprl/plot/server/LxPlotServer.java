package com.lxprl.plot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

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
	private final Map<String, ILxPlotChart> charts = new TreeMap<String, ILxPlotChart>();
	private boolean uniqueWindow;
	private ReentrantLock chartLock = new ReentrantLock();

	/**
	 * Create server NOT accessible through the network
	 *
	 * @param _name
	 */
	public LxPlotServer(final String _name) {
		LxPlotChart.setFrameName(_name);
	}

	/**
	 * Create server accessible through the network and locally
	 *
	 * @param _name
	 * @param _port
	 */
	public LxPlotServer(final String _name, final int _port) {
		this(_name + " - localhost:" + _port);
		try {
			socket = new ServerSocket(_port);
			running = true;
			new Thread(this).start();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ILxPlotChart getChart(final String _name) {
		chartLock.lock();
		if (!charts.containsKey(_name)) {
			charts.put(_name, new LxPlotChart(_name, this));
		}
		chartLock.unlock();
		return charts.get(_name);
	}

	@Override
	public boolean getUniqueWindow() {
		// TODO Auto-generated method stub
		return uniqueWindow;
	}

	@Override
	public void run() {
		while (running) {
			try {
				final Socket clientSocket = socket.accept();
				new LxPlotConnectedClient(clientSocket, this);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void setChartType(final String _name, final ChartType _chartType) {
		charts.put(_name, new LxPlotChart(_name, _chartType, this));
	}



}
