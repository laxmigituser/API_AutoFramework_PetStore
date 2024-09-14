package api.utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class Dataproviders {
	@DataProvider(name = "Data")
	public String [][] getAllUserData() throws IOException{
		String path=System.getProperty("user.dir")+"//src//test//resources//data//UserData.xlsx";
		XLUtility xl=new XLUtility(path);
		int rowNum=xl.getRowCount("User");
		int colCount=xl.getCellCount("User", 1);
		String apiData[][]=new String [rowNum][colCount];
		for(int i=1;i<=rowNum;i++) {
			for(int j=0;j<colCount;j++) {
				apiData[i-1][j]=xl.getCellData("User", i, j);
			}
		}
		return apiData;
	}
	
	@DataProvider(name="UserNames")
	public String[] getUserNames() throws IOException {
		String path=System.getProperty("user.dir")+"//src//test//resources//data//UserData.xlsx";
		XLUtility xl=new XLUtility(path);
		int rowNum=xl.getRowCount("User");
		String apiData[] = new String [rowNum];
		for(int i=1;i<=rowNum;i++) {
			apiData[i-1] = xl.getCellData("Sheet1", i, 1);
		}
		return apiData;
	}
	
	
}
