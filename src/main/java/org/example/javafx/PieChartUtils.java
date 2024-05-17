package org.example.javafx;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PieChartUtils {
    private PieChart pieChart;
    private ObservableList<PieChart.Data> pieChartData;
    public PieChartUtils(PieChart pieChart)
    {
        pieChartData = FXCollections.observableArrayList();
        this.pieChart = pieChart;
    }
    private void addData(String name, double value)
    {
        pieChartData.add(new PieChart.Data(name, value));
    }
    private void showChart()
    {
        pieChart.setData(pieChartData);
    }
    private void setDataColor(int index, String color)
    {
        pieChartData.get(index).getNode().setStyle("-fx-background-color: "+ color);
    }
    private void setMarkVisible(boolean b)
    {
        pieChart.setLabelsVisible(b);
    }
    private void setLegendColor(HashMap<Integer, String> colors)
    {
        String setColor = "";
        for(Map.Entry<Integer, String> entry:colors.entrySet())
        {
            int index = entry.getKey();
            String color = entry.getValue();
            setColor = setColor.concat("CHART_COLOR_" + (index+1) + ":" + color+";");
        }
        pieChart.setStyle(setColor);
    }
    private void setLegendSide(String side)
    {
        if(side.equalsIgnoreCase("top"))
            pieChart.setLegendSide(Side.TOP);
        else if(side.equalsIgnoreCase("bottom"))
            pieChart.setLegendSide(Side.BOTTOM);
        else if(side.equalsIgnoreCase("left"))
            pieChart.setLegendSide(Side.LEFT);
        else{
            pieChart.setLegendSide(Side.RIGHT);
        }
    }
    private void setTitle(String title)
    {
        pieChart.setTitle(title);
    }
    public void operatePieChart(Map<String, Double> data )
    {
        PieChartUtils pieChartUtils = new PieChartUtils(pieChart);
        pieChartUtils.addData("管理员", data.get("admin"));
        pieChartUtils.addData("教师", data.get("tea"));
        pieChartUtils.addData("学生", data.get("stu"));

        //显示图表
        pieChartUtils.showChart();
        //必须在图标显示之后才能修改数据区域颜色
        pieChartUtils.setDataColor(0, "#cb4040");
        pieChartUtils.setDataColor(1, "#ff6200");
        pieChartUtils.setDataColor(2, "#66c77e");
        //取消标示
        pieChartUtils.setMarkVisible(false);
        //设置扇形图数据系列的颜色
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "#cb4040");
        hashMap.put(1, "#ff6200");
        hashMap.put(2, "#66c77e");
        //设置图例颜色和方位
        pieChartUtils.setLegendColor(hashMap);
        pieChartUtils.setLegendSide("Bottom");
        //设置图表的标题
        pieChartUtils.setTitle("人数统计");
    }
}
