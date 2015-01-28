package com.lxprl.plot.server;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.lxprl.plot.commons.ChartType;
import com.lxprl.plot.interfaces.ILxPlotChart;

/**
 * Real chart displayed by a server.
 * 
 * @author Alexandre Perles
 * 
 */
public class LxPlotChart implements ILxPlotChart {
	private static JFrame frame;
	private static JPanel chartContainer;
	private XYSeriesCollection dataset;
	private static int chartCount = 0;
	private int seriesCount = 0;
	private JFreeChart chart;
	private Map<String, XYSeries> series = new TreeMap<String, XYSeries>();
	private String name;
	private TitledBorder border;
	private ChartType chartType = ChartType.PLOT;
	private ChartPanel chartPanel;
	private String firstSerie;
	protected static int untitledCount = 1;
	private static String frameName;

	public LxPlotChart() {
		this("Untitled " + (untitledCount++));
	}

	public LxPlotChart(String _name) {
		this(_name, ChartType.LINE);
	}

	public LxPlotChart(String _name, ChartType _chartType) {
		name = _name;
		chartType = _chartType;
		chartCount++;
		getChartContainer(true).add(getChartPanel());
		getFrame();
		getChartContainer().revalidate();
		getChartContainer().repaint();
	}

	private static JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame(frameName);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JScrollPane jScrollPane = new JScrollPane(getChartContainer());
			jScrollPane.setPreferredSize(new Dimension(800, 600));
			frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		return frame;
	}

	private static JPanel getChartContainer() {
		return getChartContainer(false);
	}

	private static JPanel getChartContainer(boolean _refresh) {
		if (chartContainer == null) {
			chartContainer = new JPanel();
		}
		if (_refresh) {
			switch (chartCount) {
			case 1:
			case 2:
				chartContainer.setLayout(new GridLayout(0, chartCount));
				break;
			default:
				chartContainer.setLayout(new GridLayout(0, 3));
				break;
			}
		}
		return chartContainer;
	}

	private ChartPanel getChartPanel() {
		// we put the chart into a panel
		if (chartPanel == null) {
			chartPanel = new ChartPanel(getJFreeChart());
			border = BorderFactory.createTitledBorder(name);
			chartPanel.setBorder(border);
			// default size
			chartPanel.setPreferredSize(new Dimension(300, 300));
		}
		return chartPanel;
	}

	private XYSeriesCollection getDataset() {
		if (dataset == null) {
			dataset = new XYSeriesCollection();
		}
		return dataset;
	}

	private XYSeries getSeries(String _serieName) {
		if (firstSerie == null)
			firstSerie = _serieName;
		if (!series.containsKey(_serieName)) {
			XYSeries xySeries = new XYSeries(_serieName, true,
					(chartType == ChartType.PLOT));

			series.put(_serieName, xySeries);
			getDataset().addSeries(xySeries);
			// ((XYPlot)(getChart().getPlot())).getRenderer().setSeriesStroke(seriesCount,
			// new BasicStroke(3f));
			seriesCount++;
		}
		return series.get(_serieName);
	}

	public JFreeChart getJFreeChart() {
		if (chart == null) {
			switch (chartType) {
			case LINE:
				chart = ChartFactory.createXYLineChart("", // chart
						// title
						"X", // x axis label
						"Y", // y axis label
						getDataset(), // data
						PlotOrientation.VERTICAL, true, // include legend
						true, // tooltips
						false // urls
						);
				break;
			case PLOT:
				chart = ChartFactory.createScatterPlot("", // chart
						// title
						"X", // x axis label
						"Y", // y axis label
						getDataset(), // data
						PlotOrientation.VERTICAL, true, // include legend
						true, // tooltips
						false // urls
						);
				break;

			}
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setBackgroundPaint(Color.white);
			plot.setRangeGridlinePaint(Color.black);
		}
		return chart;
	}

	public void close() {
		getChartContainer().remove(getChartPanel());
		getChartContainer().revalidate();
		getChartContainer().repaint();
	}

	@Override
	public void add(double _x, double _y) {
		if (firstSerie == null)
			firstSerie = "Default";
		add(firstSerie, _x, _y);
	}

	@Override
	public void add(String _serieName, double _x, double _y) {
		if (chartType == ChartType.PLOT)
			getSeries(_serieName).add(_x, _y);
		else
			getSeries(_serieName).addOrUpdate(_x, _y);

	}

	public static void prepareWindow(String _name) {
		frameName = _name;
		getFrame();
	}
}
