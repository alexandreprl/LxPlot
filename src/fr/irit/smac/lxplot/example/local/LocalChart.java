package fr.irit.smac.lxplot.example.local;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.lxplot.server.LxPlotChart;

/**
 * Example showing how to locally display charts.
 *
 * @author Alexandre Perles
 *
 */
public class LocalChart {
	public static void main(final String[] args) {

		// First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Layer", ChartType.PLOT);

		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Layer").add(1, 2);
		LxPlot.getChart("Layer").add(2, 2.5);
		LxPlot.getChart("Layer").add(2, 3);

		// Add plots to the chart "Layer" for serie "My serie"
		LxPlot.getChart("Layer").add("My serie", 21, 2);
		LxPlot.getChart("Layer").add("My serie", 12, 2.5);

		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		// Add plots to the chart "My Chart" which will be created during this
		// call
		LxPlot.getChart("My chart").add(2, 3);
		LxPlot.getChart("My chart").add(3, 3);
		LxPlot.getChart("My chart").add(3, 4);

		// Access the JFreeChart object (only from the server side)
		final JFreeChart chart = ((LxPlotChart) (LxPlot.getChart("Layer")))
				.getJFreeChart();

		final XYItemRenderer r = ((XYPlot) (chart.getPlot())).getRenderer();

		r.setSeriesStroke(0, new BasicStroke(0.1f));
		r.setSeriesShape(0, new Rectangle(3, 3));
		r.setSeriesShape(0, new Ellipse2D.Float(3, 3, 3, 3));
		r.setSeriesPaint(0, Color.green);
		


		// First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Bar chart", ChartType.BAR);

		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Bar chart").add(1, 2);
		LxPlot.getChart("Bar chart").add(2, 2.5);
		LxPlot.getChart("Bar chart").add(2, 3);
		
		//Dispose test
		for (int i=0;i<1000;i++){
			LxPlot.getChart("Bar chart").add(i, 3);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		
		// Close chart
		// LxPlot.getChart("My chart").close();
		// LxPlot.getChart("Layer").close();
	}

}
