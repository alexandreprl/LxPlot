package fr.irit.smac.lxplot.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.irit.smac.lxplot.commons.ChartType;
import fr.irit.smac.lxplot.interfaces.ILxPlotChart;
import fr.irit.smac.lxplot.interfaces.ILxPlotServer;

/**
 * Real chart displayed by a server.
 *
 * @author Alexandre Perles
 */
public class LxPlotChart implements ILxPlotChart, Runnable {
	public static int cols = 2;
	private static JMenuBar menuBar;
	private static JMenu layoutMenu;
	private static MainWindow window;
	private static int chartCount = 0;
	private static JDesktopPane desktopPane;
	private static ReentrantLock frameLock = new ReentrantLock();
	private final Map<String, XYSeries> series = new TreeMap<String, XYSeries>();
	private final String name;
	private final ILxPlotServer server;
	// private JPanel chartContainer;
	private XYSeriesCollection dataset;
	private JFreeChart chart;
	private TitledBorder border;
	private ChartType chartType = ChartType.PLOT;
	private ChartPanel chartPanel;
	private String firstSerie;
	// private JFrame chartFrame;
	private JInternalFrame internalChartFrame;
	private DefaultCategoryDataset categoryDataset;
	private LinkedList<PointRequest> queue = new LinkedList<>();
	private Semaphore threadSemaphore = new Semaphore(-1);

	private PointRequest lastPoint;
	private ReentrantLock queueLock = new ReentrantLock();
	private boolean blocking;

	public LxPlotChart(final ILxPlotServer _server) {
		this("Untitled", _server);
	}

	public LxPlotChart(final String _name, final ChartType _chartType, final ILxPlotServer _server, final boolean _blocking) {
		name = _name;
		chartType = _chartType;
		server = _server;
		blocking = _blocking;
		LxPlotChart.chartCount++;
		// getChartContainer(true).add(getChartPanel());
		LxPlotChart.getDesktopPane().add(getChartInternalFrame());
		// getChartContainer().revalidate();
		// getChartContainer().repaint();

		new Thread(this).start();
	}

	public LxPlotChart(final String _name, final ILxPlotServer _server) {
		this(_name, ChartType.LINE, _server, true);
	}

	private synchronized static JDesktopPane getDesktopPane() {
		if (LxPlotChart.desktopPane == null) {
			LxPlotChart.desktopPane = new JDesktopPane() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component add(Component comp) {
					Component t = super.add(comp);
					refreshLayout();
					return t;
				}

			};
			
			LxPlotChart.desktopPane.setDesktopManager(new CustomDesktopManager());
			LxPlotChart.desktopPane.setPreferredSize(new Dimension(900, 600));
		}
		return LxPlotChart.desktopPane;
	}

	private synchronized static MainWindow getMainWindow() {
		frameLock.lock();
		if (LxPlotChart.window == null) {
			LxPlotChart.window = new MainWindow("LxPlot");
			LxPlotChart.window.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			LxPlotChart.window.getFrame().addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {

				}

				@Override
				public void windowIconified(WindowEvent e) {

				}

				@Override
				public void windowDeiconified(WindowEvent e) {

				}

				@Override
				public void windowDeactivated(WindowEvent e) {

				}

				@Override
				public void windowClosing(WindowEvent e) {

				}

				@Override
				public void windowClosed(WindowEvent e) {
					JInternalFrame[] allFrames = getDesktopPane().getAllFrames();
					for (JInternalFrame jInternalFrame : allFrames) {
						jInternalFrame.doDefaultCloseAction();
					}
				}

				@Override
				public void windowActivated(WindowEvent e) {

				}
			});
			LxPlotChart.window.getFrame().addComponentListener(new ComponentListener() {

				@Override
				public void componentShown(ComponentEvent e) {

				}

				@Override
				public void componentResized(ComponentEvent e) {
					refreshLayout();
				}

				@Override
				public void componentMoved(ComponentEvent e) {

				}

				@Override
				public void componentHidden(ComponentEvent e) {

				}
			});
			LxPlotChart.window.getFrame().getContentPane().add((LxPlotChart.getDesktopPane()), BorderLayout.CENTER);
			LxPlotChart.window.getFrame().pack();
			LxPlotChart.window.getFrame().setVisible(true);
		}
		frameLock.unlock();
		return LxPlotChart.window;
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

	public static void refreshLayout() {
		int x = 0;
		int width = 10, height = 10;
		JInternalFrame[] allFrames = getDesktopPane().getAllFrames();

		List<JInternalFrame> visibleFrames = new ArrayList<>();
		for (JInternalFrame jInternalFrame : allFrames) {
			if (!jInternalFrame.isIcon()) {
				visibleFrames.add(jInternalFrame);
			}
		}

		int w = getDesktopPane().getWidth();
		int h = getDesktopPane().getHeight() - (visibleFrames.size()!=allFrames.length?30:0);

		if (visibleFrames.isEmpty())
			return;
		if (visibleFrames.size() == 1) {
			width = w;
			height = h;
		} else if (visibleFrames.size() <= cols) {
			width = w / visibleFrames.size();
			height = h;
		} else {
			width = w / cols;
			int rowCount = (int) (visibleFrames.size() / cols) + (visibleFrames.size() % cols == 0 ? 0 : 1);
			height = h / rowCount;
		}
		if (width < 10)
			width = 10;
		if (height < 200)
			height = 200;

		for (JInternalFrame frame : visibleFrames) {
			frame.setLocation(width * (x % cols), height * ((int) (x / cols)));
			frame.setSize(width, height);
			x++;
		}
	}

	@Override
	public synchronized void add(final double _x, final double _y) {
		if (firstSerie == null) {
			firstSerie = "Default";
		}
		add(firstSerie, _x, _y);
	}

	@Override
	public synchronized void add(final String _serieName, final double _x, final double _y) {
		lastPoint = new PointRequest(_serieName, _x, _y);

		if (blocking)
			drawPoint(lastPoint);
		else {
			queueLock.lock();
			queue.add(lastPoint);
			threadSemaphore.release();
			queueLock.unlock();
		}
	}

	@Override
	public void close() {
		getChartInternalFrame().dispose();
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
	private synchronized JInternalFrame getChartInternalFrame() {
		if (internalChartFrame == null) {
			internalChartFrame = new JInternalFrame(getInternalFrameName(), true, true, true,
					true);
			internalChartFrame.addInternalFrameListener(new InternalFrameListener() {

				@Override
				public void internalFrameOpened(InternalFrameEvent e) {

				}

				@Override
				public void internalFrameIconified(InternalFrameEvent e) {

				}

				@Override
				public void internalFrameDeiconified(InternalFrameEvent e) {

				}

				@Override
				public void internalFrameDeactivated(InternalFrameEvent e) {

				}

				@Override
				public void internalFrameClosing(InternalFrameEvent e) {

				}

				@Override
				public void internalFrameClosed(InternalFrameEvent e) {

					internalChartFrame = null;
					chartPanel = null;
					chart = null;
					server.removeChart(name);
				}

				@Override
				public void internalFrameActivated(InternalFrameEvent e) {

				}
			});
			// final int wx = 900 / LxPlotChart.cols;
			// final int wy = 600 / LxPlotChart.rows;
			// internalChartFrame
			// .setBounds(
			// wx
			// * ((LxPlotChart.chartCount - 1) % LxPlotChart.cols),
			// wy
			// * ((LxPlotChart.chartCount - 1) / LxPlotChart.cols),
			// wx, wy);

			internalChartFrame.add(getChartPanel());
			internalChartFrame.setVisible(true);
		}
		return internalChartFrame;
	}

	private String getInternalFrameName() {
		return name + " (" + (LxPlotChart.chartCount) + ") "+(!blocking?"ASYNC":"");
	}

	private synchronized ChartPanel getChartPanel() {
		// we put the chart into a panel
		if (chartPanel == null) {
			chartPanel = new ChartPanel(getJFreeChart());
//			border = BorderFactory.createTitledBorder(name);
//			chartPanel.setBorder(border);
			chartPanel.setMinimumSize(new Dimension(10, 10));
			// default size
			// chartPanel.setPreferredSize(new Dimension(300, 300));
		}
		return chartPanel;
	}

	private synchronized XYSeriesCollection getDataset() {
		if (dataset == null) {
			dataset = new XYSeriesCollection();
		}
		return dataset;
	}

	public synchronized JFreeChart getJFreeChart() {
		if (chart == null) {
			XYPlot plot;
			NumberAxis range;
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

				plot = (XYPlot) chart.getPlot();

				range = (NumberAxis) plot.getRangeAxis();
				range.setAutoRange(true);
				range.setAutoRangeIncludesZero(false);
				plot.setBackgroundPaint(Color.white);
				plot.setRangeGridlinePaint(Color.black);
				// plot.setRenderer(new SamplingXYLineRenderer());
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

				plot = (XYPlot) chart.getPlot();

				range = (NumberAxis) plot.getRangeAxis();
				range.setAutoRange(true);
				range.setAutoRangeIncludesZero(false);
				plot.setBackgroundPaint(Color.white);
				plot.setRangeGridlinePaint(Color.black);
				break;
			case BAR:
				chart = ChartFactory.createBarChart("", "X", "Y", getCategoryDataset(), PlotOrientation.VERTICAL, true,
						true, false);
				break;

			}
		}
		return chart;
	}

	private synchronized DefaultCategoryDataset getCategoryDataset() {
		if (categoryDataset == null)
			categoryDataset = new DefaultCategoryDataset();
		return categoryDataset;
	}

	private synchronized XYSeries getSeries(final String _serieName) {
		if (firstSerie == null) {
			firstSerie = _serieName;
		}
		if (!series.containsKey(_serieName)) {
			final XYSeries xySeries = new XYSeries(_serieName, true, (chartType == ChartType.PLOT));

			series.put(_serieName, xySeries);
			getDataset().addSeries(xySeries);
		}
		return series.get(_serieName);
	}

	private class PointRequest {
		public final String serieName;
		public final double x, y;

		public PointRequest(String serieName, double x, double y) {
			super();
			this.serieName = serieName;
			this.x = x;
			this.y = y;
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				PointRequest point;
				getMainWindow().setStatus("READY");
				threadSemaphore.acquire();
				getMainWindow().setStatus("WORKING ...");
				queueLock.lock();
				point = queue.removeFirst();
				queueLock.unlock();
				drawPoint(point);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void drawPoint(PointRequest _pointRequest) {
		switch (chartType) {
		case PLOT:
			getSeries(_pointRequest.serieName).add(_pointRequest.x, _pointRequest.y);
			break;
		case LINE:
			getSeries(_pointRequest.serieName).addOrUpdate(_pointRequest.x, _pointRequest.y);
			break;
		case BAR:
			getCategoryDataset().addValue(_pointRequest.y, _pointRequest.serieName, String.valueOf(_pointRequest.x));
			break;
		}
		if (!getMainWindow().getFrame().isVisible())
			getMainWindow().getFrame().setVisible(true);

	}

	public static void minimizeAll() {
		for (JInternalFrame jif : desktopPane.getAllFrames()) {
			try {
				jif.setIcon(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setFrameName(String _name) {
		getMainWindow().setFrameName(_name);
	}
}
