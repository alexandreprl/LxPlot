package fr.irit.smac.lxplot.example.local;

import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;

public class ScalabilityTest {

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 20000; i++) {
			LxPlot.getChart("test", ChartType.LINE, false).add(i, Math.sin(i/100.0));
			try {
				if (i % 1000 == 0)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("time : " + (System.currentTimeMillis() - time));
	}

}
