package fr.irit.smac.lxplot.interfaces;

import fr.irit.smac.lxplot.commons.ChartType;

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
	 * Get or create the chart which is called _name and set the chart type
	 *
	 * @param _name
	 * @param _chartType
	 * @return
	 */
	public ILxPlotChart getChart(String _name, ChartType _chartType);

	/**
	 * Get whether charts must be in different windows or not
	 *
	 * @return
	 */
	public boolean getUniqueWindow();

}
