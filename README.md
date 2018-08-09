# PieChartView
Android实现Pie Chart（饼状图），可设置为环形图

项目添加依赖：
project/build.gradle中添加：

	allprojects {
	    repositories {
	        ...
	        maven { url 'https://jitpack.io' }
	    }
	}

project/app/build.gradle中添加：
	
	dependencies {
        implementation 'com.github.luweibin3118:PieChartView:v1.1.0'
    }


 1. 在布局文件中引入RadarChartView：

     
    <com.lwb.piechart.PieChartView
        android:id="@+id/pie_chart_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        


 2. Java代码中，通过以下方法添加一条属性：

        PieChartView pieChartView = findViewById(R.id.pie_chart_view);
        pieChartView.addItemType(new PieChartView.ItemType("苹果", 25, 0xff20B2AA));
        pieChartView.addItemType(new PieChartView.ItemType("华为", 17, 0xff68228B));
        pieChartView.addItemType(new PieChartView.ItemType("小米", 13, 0xff8B5A00));
        pieChartView.addItemType(new PieChartView.ItemType("三星", 8, 0xffCD3700));
        pieChartView.addItemType(new PieChartView.ItemType("OPPO", 6, 0xff8968CD));
        pieChartView.addItemType(new PieChartView.ItemType("VIVO", 5, 0xff437145));
        pieChartView.addItemType(new PieChartView.ItemType("魅族", 4, 0xff8DB6CD));
        pieChartView.addItemType(new PieChartView.ItemType("联想", 2, 0xff6B8E23));
        pieChartView.addItemType(new PieChartView.ItemType("其他品牌", 20, 0xff999999));

 ![image](https://github.com/luweibin3118/PieChartView/blob/master/app/Screenshot_20180112-211844.png)
  
 4. 设置以下方法可平移饼图：
 
        pieChartView.setPieCell(10); // 10 位平移距离大小

    效果如图：
    
 ![image](https://github.com/luweibin3118/PieChartView/blob/master/app/Screenshot_20180112-212035.png)
 
 
 5. 设置以下方法可以实现环形图： 
      
        pieChartView.setCell(5);            //设置环形图的间距
        pieChartView.setInnerRadius(0.4f);  //设置环形图内环半径比例 0 - 1.0f
   
 ![image](https://github.com/luweibin3118/PieChartView/blob/master/app/Screenshot_20180112-211903.png)
 
 6. 其他方法：
 
        pieChartView.setBackGroundColor(0xffFFFFE0);    //设置背景颜色
        pieChartView.setItemTextSize(30);               //设置字体大小
        pieChartView.setTextPadding(10);                //设置字体与横线距离
 