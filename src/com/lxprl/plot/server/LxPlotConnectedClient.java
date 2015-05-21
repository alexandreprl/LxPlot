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
	private LxPlotServer lxPlotServer;

	public LxPlotConnectedClient(final Socket clientSocket,
			final LxPlotServer _lxPlotServer) {
		try {
			lxPlotServer = _lxPlotServer;
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			new Thread(this).start();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = in.readLine()) != null) {
				final String[] res = line.split(";");
				if (res[0].equals("config")) {
					lxPlotServer
					.setChartType(res[1], ChartType.valueOf(res[2]));
				} else if (res[0].equals("add")) {
					if (res[2].isEmpty()) {
						lxPlotServer.getChart(res[1]).add(
								Double.parseDouble(res[3]),
								Double.parseDouble(res[4]));
					} else {
						lxPlotServer.getChart(res[1]).add(res[2],
								Double.parseDouble(res[3]),
								Double.parseDouble(res[4]));
					}
				} else if (res[0].equals("close")) {
					lxPlotServer.getChart(res[1]).close();
				}
			}
		} catch (final IOException e) {

		}
	}

}
