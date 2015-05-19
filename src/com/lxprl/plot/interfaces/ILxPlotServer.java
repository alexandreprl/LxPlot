package com.lxprl.plot.interfaces;

import com.lxprl.plot.commons.ChartType;

/**
 * Common server's interface between the server and the client.
 *
 * @author Alexandre Perles
 *
 */
public interface ILxPlotServer {
	/**
	 * Get or create the chart which is called _name.
	 *
	 * @param _name
	 * @return
	 */
	public ILxPlotChart getChart(String _name);

	/**
	 * Get whether charts must be in different windows or not
	 *
	 * @return
	 */
	public boolean getUniqueWindow();

	/**
	 * Preconfigure the chart _name with a chart's type
	 *
	 * @param _name
	 * @param _chartType
	 */
	public void setChartType(String _name, ChartType _chartType);

	/**
	 * Set grid size in desktop pane
	 *
	 * @param _cols
	 * @param _rows
	 */
	public void setGridSize(int _cols, int _rows);

	/**
	 * Set whether charts must displayed in multiple windows or not
	 *
	 * @param _uniqueWindow
	 */
	public void setUniqueWindow(boolean _uniqueWindow);
}
