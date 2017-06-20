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
		/*LxPlot.getChart("My chart").add(2, 3);
		LxPlot.getChart("My chart").add(3, 3);
		
		//Override the value at x=3
		LxPlot.getChart("My chart").add(3, 4);*/

		// Access the JFreeChart object (only from the server side)
		final JFreeChart chart = ((LxPlotChart) (LxPlot.getChart("Layer")))
				.getJFreeChart();

		final XYItemRenderer r = ((XYPlot) (chart.getPlot())).getRenderer();

		r.setSeriesStroke(0, new BasicStroke(0.1f));
		r.setSeriesShape(0, new Rectangle(3, 3));
		r.setSeriesShape(0, new Ellipse2D.Float(3, 3, 3, 3));
		r.setSeriesPaint(0, Color.green);
		


		/*// First, we inform the server that the chart "Layer" must be of type
		LxPlot.getChart("Bar chart", ChartType.BAR);

		// Add plots to the chart "Layer" for serie #0
		LxPlot.getChart("Bar chart", ChartType.BAR).add(1, 2);
		LxPlot.getChart("Bar chart", ChartType.BAR).add(2, 2.5);
		LxPlot.getChart("Bar chart", ChartType.BAR).add(2, 3);
		
		LxPlot.getChart("Linear chart", ChartType.LINE);
		LxPlot.getChart("Linear chart", ChartType.LINE).add(1, 2);
		LxPlot.getChart("Linear chart", ChartType.LINE).add(2, 2.5);
		*/
		//Dispose test
		
		LxPlot.getChart("wafer chart", ChartType.WAFER);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(1,1.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(2,2.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(3,3.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(4,1.0);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(5,1.5);
		LxPlot.getChart("wafer chart", ChartType.WAFER).add(0,1.5);

		//LxPlot.getChart("scatter chart", ChartType.SCATTER).add(7, 10);
		for (int i=3;i<100;i++){
			/*LxPlot.getChart("Bar chart", ChartType.BAR).add(i, 3);
			if(i%4==0)
				LxPlot.getChart("Linear chart", ChartType.LINE).add(i,i);
			else
				LxPlot.getChart("Linear chart", ChartType.LINE).add(i,i+i);
			LxPlot.getChart("pie chart", ChartType.PIE).add(i%6, (i*i)%100);;*/
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		
		// Close chart
		// LxPlot.getChart("My chart").close();
		// LxPlot.getChart("Layer").close();
	}

}
