package com.lxprl.plot.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
import com.lxprl.plot.interfaces.ILxPlotServer;

/**
 * Real chart displayed by a server.
 *
 * @author Alexandre Perles
 *
 */
public class LxPlotChart implements ILxPlotChart {
	private static JDesktopPane getDesktopPane() {
		if (LxPlotChart.desktopPane == null) {
			LxPlotChart.desktopPane = new JDesktopPane();

			LxPlotChart.desktopPane.setPreferredSize(new Dimension(900, 600));
		}
		return LxPlotChart.desktopPane;
	}

	private static JFrame getJFrame() {
		if (LxPlotChart.frame == null) {
			LxPlotChart.frame = new JFrame();
			LxPlotChart.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			LxPlotChart.frame.getContentPane().add(
					LxPlotChart.getDesktopPane(), BorderLayout.CENTER);
			LxPlotChart.frame.pack();
			LxPlotChart.frame.setVisible(true);
		}
		return LxPlotChart.frame;
	}

	public static void setFrameName(final String _name) {
		LxPlotChart.frameName = _name;
	}

	public static void setGridSize(final int _cols, final int _rows) {
		LxPlotChart.cols = _cols;
		LxPlotChart.rows = _rows;
	}

	private static JFrame frame;
	// private JPanel chartContainer;
	private XYSeriesCollection dataset;
	private static int chartCount = 0;
	private int seriesCount = 0;
	private JFreeChart chart;
	private final Map<String, XYSeries> series = new TreeMap<String, XYSeries>();
	private final String name;
	private TitledBorder border;
	private ChartType chartType = ChartType.PLOT;
	private ChartPanel chartPanel;
	private String firstSerie;
	private JFrame chartFrame;
	private final ILxPlotServer server;
	private JInternalFrame internalChartFrame;
	protected static int untitledCount = 1;

	private static String frameName;

	private static JDesktopPane desktopPane;

	private static int cols = 2;

	private static int rows = 2;

	public LxPlotChart(final ILxPlotServer _server) {
		this("Untitled " + (LxPlotChart.untitledCount++), _server);
	}

	// private JFrame getFrame() {
	// if (chartFrame == null) {
	// chartFrame = new JFrame(frameName);
	// chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// JScrollPane jScrollPane = new JScrollPane(getChartContainer());
	// jScrollPane.setPreferredSize(new Dimension(320, 240));
	// chartFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
	// chartFrame.pack();
	// chartFrame.setVisible(true);
	// }
	// return chartFrame;
	// }

	// private JPanel getChartContainer() {
	// return getChartContainer(false);
	// }

	public LxPlotChart(final String _name, final ChartType _chartType,
			final ILxPlotServer _server) {
		name = _name;
		chartType = _chartType;
		server = _server;
		LxPlotChart.chartCount++;
		// getChartContainer(true).add(getChartPanel());
		LxPlotChart.getJFrame();
		LxPlotChart.getDesktopPane().add(getChartInternalFrame());
		// getChartContainer().revalidate();
		// getChartContainer().repaint();
	}

	public LxPlotChart(final String _name, final ILxPlotServer _server) {
		this(_name, ChartType.LINE, _server);
	}

	@Override
	public void add(final double _x, final double _y) {
		if (firstSerie == null) {
			firstSerie = "Default";
		}
		add(firstSerie, _x, _y);
	}

	@Override
	public void add(final String _serieName, final double _x, final double _y) {
		if (chartType == ChartType.PLOT) {
			getSeries(_serieName).add(_x, _y);
		} else {
			getSeries(_serieName).addOrUpdate(_x, _y);
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		System.err.println("Trying to close uncloseable chart");
	}

	// public void close() {
	// getChartContainer().remove(getChartPanel());
	// getChartContainer().revalidate();
	// getChartContainer().repaint();
	// }

	// private JPanel getChartContainer(boolean _refresh) {
	// if (chartContainer == null) {
	// chartContainer = new JPanel();
	// }
	// if (_refresh) {
	// switch (chartCount) {
	// case 1:
	// case 2:
	// chartContainer.setLayout(new GridLayout(0, chartCount));
	// break;
	// default:
	// chartContainer.setLayout(new GridLayout(0, 3));
	// break;
	// }
	// }
	// return chartContainer;
	// }
	private JInternalFrame getChartInternalFrame() {
		if (internalChartFrame == null) {
			internalChartFrame = new JInternalFrame("Chart #"
					+ (LxPlotChart.chartCount), true, true, true, true);
			final int wx = 900 / LxPlotChart.cols;
			final int wy = 600 / LxPlotChart.rows;
			internalChartFrame
			.setBounds(
					wx
					* ((LxPlotChart.chartCount - 1) % LxPlotChart.cols),
					wy
					* ((LxPlotChart.chartCount - 1) / LxPlotChart.cols),
					wx, wy);

			internalChartFrame.add(getChartPanel());
			internalChartFrame.setVisible(true);
		}
		return internalChartFrame;
	}

	private ChartPanel getChartPanel() {
		// we put the chart into a panel
		if (chartPanel == null) {
			chartPanel = new ChartPanel(getJFreeChart());
			border = BorderFactory.createTitledBorder(name);
			chartPanel.setBorder(border);
			// default size
			// chartPanel.setPreferredSize(new Dimension(300, 300));
		}
		return chartPanel;
	}

	private XYSeriesCollection getDataset() {
		if (dataset == null) {
			dataset = new XYSeriesCollection();
		}
		return dataset;
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

			final XYPlot plot = (XYPlot) chart.getPlot();
			plot.setBackgroundPaint(Color.white);
			plot.setRangeGridlinePaint(Color.black);
		}
		return chart;
	}

	private XYSeries getSeries(final String _serieName) {
		if (firstSerie == null) {
			firstSerie = _serieName;
		}
		if (!series.containsKey(_serieName)) {
			final XYSeries xySeries = new XYSeries(_serieName, true,
					(chartType == ChartType.PLOT));

			series.put(_serieName, xySeries);
			getDataset().addSeries(xySeries);
			// ((XYPlot)(getChart().getPlot())).getRenderer().setSeriesStroke(seriesCount,
			// new BasicStroke(3f));
			seriesCount++;
		}
		return series.get(_serieName);
	}
}
