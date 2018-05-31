package org.tapbej.proyectofinal.modelo;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.util.Duration;

import java.util.Queue;

public class SearchingChart extends BarChart
{
	private int[] bars;
	private Searcher searcher;
	private Timeline animation;

	// TODO: java doc
	public SearchingChart(SearchMethod method, int[] bars, int target)
	{
		super(new CategoryAxis(), new NumberAxis());
		this.bars = bars;
		this.searcher = new Searcher(method, bars, target);
		graphArray();
		setDefaults();
	}


	// TODO: java doc & tracking
	public void search(int interval)
	{
		double timePassed = searcher.search() / 1000;
		setTimePassed(timePassed);

		Queue<Comparison> comparisons = searcher.getComparisons();

		animation = new Timeline(new KeyFrame(Duration.millis(interval), action -> {
			try
			{
				if (comparisons.size() != 0)
				{
					//todo
					Comparison comparison = comparisons.poll();
					System.out.println("colorizing " + comparison.getBarIndex());
					if (comparison.isSuccessful())
					{
						colorizeBar(comparison.getBarIndex(), "green");
					}
					else
					{
						colorizeBar(comparison.getBarIndex(), "red");
					}
				}
				else
				{
					animation.stop();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}));
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}


	private void setDefaults()
	{
		this.setAnimated(false);
		this.setLegendVisible(false);
		hideAxis();
		setGaps(1, 1);
	}

	/**
	 * Swap two bars position.
	 *
	 * @param firstPosition
	 * @param secondPosition
	 */
	public void swapBars(int firstPosition, int secondPosition)
	{
		Integer temp = bars[firstPosition];
		bars[firstPosition] = bars[secondPosition];
		bars[secondPosition] = temp;
		graphArray();
	}

	/**
	 * Colorizes bar
	 *
	 * @param position
	 * @param color
	 */
	private void colorizeBar(int position, String color)
	{
		Data<String, Number> bar = (Data<String, Number>) ((Series) this.getData().get(0)).getData().get(position);
		System.out.println("Colorizing bar " + position + " (value: " + bar.getYValue() + ") to color " + color);

		bar.getNode().setStyle("-fx-bar-fill: " + color + ";");
	}

	/**
	 * Sets and displays time passed
	 *
	 * @param timePassed
	 */
	public void setTimePassed(double timePassed)
	{
		this.setTitle(timePassed + "s");
	}

	/**
	 * Hides time passed
	 */
	public void hideTime()
	{
		this.setTitle("");
	}


	/**
	 * Converts the values of the bars array list to a chart
	 */
	private void graphArray()
	{
		this.getData().clear();
		int position = 0;
		Series<String, Number> dataSeries = new Series<>();
		for (int item : bars)
		{
			dataSeries.getData().add(new Data<>(position + "", item));
			//			System.out.println("Bar added: <" + position + ", " + item + ">" );
			position++;
		}
		this.getData().add(dataSeries);
		System.out.println("Bars: " + bars.toString());
	}

	/**
	 * Completely hide both X and Y axises.
	 */
	private void hideAxis()
	{
		this.setHorizontalGridLinesVisible(false);
		this.setVerticalGridLinesVisible(false);
		this.getXAxis().setTickLabelsVisible(false);
		this.getYAxis().setTickLabelsVisible(false);
		this.getYAxis().setTickMarkVisible(false);
		this.getYAxis().setVisible(false);
		this.getYAxis().setOpacity(0);
		this.getXAxis().setOpacity(0);
	}

	/**
	 * Sets the spacing between each bar.
	 *
	 * @param categoryGap
	 * @param barGap
	 */
	public void setGaps(double categoryGap, double barGap)
	{
		this.setCategoryGap(categoryGap);
		this.setBarGap(barGap);
	}
}
