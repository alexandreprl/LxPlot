package com.lxprl.plot;

import java.util.Map;
import java.util.TreeMap;

import com.lxprl.plot.interfaces.ILxPlotChart;
import com.lxprl.plot.interfaces.ILxPlotServer;
import com.lxprl.plot.server.LxPlotDistantServer;
import com.lxprl.plot.server.LxPlotServer;

/**
 * Main class giving access to all the functionnalities of the library
 * 
 * @author Alexandre Perles
 * 
 */
public class LxPlot {

	/**
	 * If no name is given for the server, this one will be used
	 */
	private static ILxPlotServer defaultServer;

	/**
	 * Server's map linking name and server
	 */
	private static Map<String, ILxPlotServer> servers = new TreeMap<String, ILxPlotServer>();

	/**
	 * Create a new server which will be accessible through network with the
	 * port _port
	 * 
	 * @param _port
	 */
	public static void createServer(int _port) {
		defaultServer = new LxPlotServer("default", _port);
	}

	/**
	 * Create a new server with the name _name and which will be accessible
	 * through network with the port _port. The name is only used in the
	 * server's side
	 * 
	 * @param _name
	 * @param _port
	 */
	public static void createServer(String _name, int _port) {
		servers.put(_name, new LxPlotServer(_name, _port));
	}

	/**
	 * Get default server if any. Otherwise create it.
	 * 
	 * @return
	 */
	public static ILxPlotServer getServer() {
		if (defaultServer == null)
			defaultServer = new LxPlotServer("default");
		return defaultServer;
	}

	/**
	 * Get server with the given name if it exists. Otherwise create a
	 * connection to it.
	 * 
	 * @param _name
	 * @return
	 */
	public static ILxPlotServer getServer(String _name) {
		if (!servers.containsKey(_name)) {
			servers.put(_name, new LxPlotServer(_name));
		}
		return servers.get(_name);
	}

	/**
	 * Get the chart with the given name if any. Otherwise, create it.
	 * 
	 * @param _name
	 * @return
	 */
	public static ILxPlotChart getChart(String _name) {
		return getServer().getChart(_name);
	}

	/**
	 * Get the chart _name located on the server _serverName if any. Otherwise,
	 * create it.
	 * 
	 * @param _name
	 * @param _serverName
	 * @return
	 */
	public static ILxPlotChart getChart(String _name, String _serverName) {
		return getServer(_serverName).getChart(_name);
	}

	/**
	 * Configure the connection to a server and set a local name. The name _name
	 * will be then used with the method getServer(String _name).
	 * 
	 * @param _name
	 * @param _host
	 * @param _port
	 */
	public static void configServer(String _name, String _host, int _port) {
		servers.put(_name, new LxPlotDistantServer(_host, _port));
	}

	/**
	 * Configure the connection with the default server.
	 * 
	 * @param _host
	 * @param _port
	 */
	public static void configServer(String _host, int _port) {
		defaultServer = new LxPlotDistantServer(_host, _port);
	}
}
