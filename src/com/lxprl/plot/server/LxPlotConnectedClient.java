package com.lxprl.plot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.lxprl.plot.commons.ChartType;

/**
 * Class handling the connection o a new client to the server.
 * 
 * @author Alexandre Perles
 * 
 */
public class LxPlotConnectedClient implements Runnable {

	private BufferedReader in;
	private LxPlotServer LxPlotServer;

	public LxPlotConnectedClient(Socket clientSocket,
			LxPlotServer _LxPlotServer) {
		try {
			LxPlotServer = _LxPlotServer;
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			new Thread(this).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = in.readLine()) != null) {
				String[] res = line.split(";");
				if (res[0].equals("config")) {
					LxPlotServer
							.configChart(res[1], ChartType.valueOf(res[2]));
				} else if (res[0].equals("add")) {
					if (res[2].isEmpty())
						LxPlotServer.getChart(res[1]).add(
								Double.parseDouble(res[3]),
								Double.parseDouble(res[4]));
					else
						LxPlotServer.getChart(res[1]).add(
								res[2],
								Double.parseDouble(res[3]),
								Double.parseDouble(res[4]));
				} else if (res[0].equals("close")) {
					LxPlotServer.getChart(res[1]).close();
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
