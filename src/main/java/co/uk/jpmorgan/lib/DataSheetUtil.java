package co.uk.jpmorgan.lib;



import java.util.HashMap;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Assert;

public class DataSheetUtil 
{

	Workbook objExcelWorkBook;
	static Sheet objExcelInputSheet;
	static HashMap[][] objHashMap;
	private static int strInputSheetCols;
	private static int strInputSheetRows;
	public static String strDataSheetLocation = "src/resources/datasheets/";
	public Object[][] getTestDataFromExcel(String strDataSheet, String strInputSheet)
	{
		try
		{		
			// Open the Excel datasheet at the relevant input sheet page
			objExcelWorkBook = WorkbookFactory.create(new FileInputStream(strDataSheetLocation+strDataSheet));
			objExcelInputSheet = objExcelWorkBook.getSheet(strInputSheet);
			
			// Check that the headings are on the first row to verify the format of the datasheet and if not, exit the test
			if (isColHeadingsRowOne() == false)
			{
				System.out.println("getTestDataFromExcel: Headings not correct on dataSheet");
				Assert.fail("getTestDataFromExcel: Headings not correct on dataSheet");
			}
			// Calculate the rows & cols (including headings) in the relevant input sheet
						strInputSheetCols = getDataSheetCols();
						strInputSheetRows = getDataSheetRows();
						
						// Get the test data from the datasheet
						getTestDataSet();
					}
					catch (Exception e)
					{
						
						Assert.fail("getTestDataFromExcel: Could not load data - " + e.getMessage());
					}
		return objHashMap;
	}
	@SuppressWarnings("unchecked")
	protected static void getTestDataSet()
	{
		// Re-Initialise the objTest string array so the table is the same size as the datasheet values
		// without the headings row included

		objHashMap = new HashMap[strInputSheetRows-1][1];
		// Starting with the first test on Row 1, populate the test data set row by row
		for (int intRowCounter = 1; intRowCounter < strInputSheetRows; intRowCounter++)
		{
			objHashMap[intRowCounter-1][0] = new HashMap<String, String>();
			for (int intColCounter = 0; intColCounter < strInputSheetCols; intColCounter++)
			{
					objHashMap[intRowCounter-1][0].put(getCellValueAsString(0,intColCounter), getCellValueAsString(intRowCounter,intColCounter));	 
			}
		}
	}
	protected static boolean isColHeadingsRowOne()
	{
		// Set return code to false as default
		boolean blnHeadingCheck = false;

		// Get cell value at location A1 which should be "TESTNAME"
	    String strTestNameCheck = getCellValueAsString(0,0);
		
		// Get cell value at location B1 which should be "BROWSER"
	    String strBrowserNameCheck = getCellValueAsString(0,1);
	    
	    // Now check that the values have been set correctly 
		if ((strTestNameCheck.equalsIgnoreCase("TESTNAME") && (strBrowserNameCheck.equalsIgnoreCase("BROWSER"))))
		{
			blnHeadingCheck = true;
		}
		return blnHeadingCheck;
	}
	protected static String getCellValueAsString(int intRowRef,int intColRef)
	{
		// Get cell format type
		String strCellValue = null;
		int intCellFormat = getCellFormatType(intRowRef,intColRef);
        
		// Cast the cell value into a String 
		switch (intCellFormat) 
		{
        	// STRING TYPE
        	case Cell.CELL_TYPE_STRING:
        		strCellValue = objExcelInputSheet.getRow(intRowRef).getCell(intColRef).getStringCellValue();
            break;

            // BOOLEAN TYPE
	        case Cell.CELL_TYPE_BOOLEAN:
	        	boolean blnCellValue = objExcelInputSheet.getRow(intRowRef).getCell(intColRef).getBooleanCellValue();
	        	if (blnCellValue == true)
	        	{
	        		strCellValue = "true";
	        	}
	        	else
	        	{
	        		strCellValue = "false";
	        	}
	        break;
	        
	        // INTEGER TYPE
	        case Cell.CELL_TYPE_NUMERIC:
	        	strCellValue = Integer.toString((int)(objExcelInputSheet.getRow(intRowRef).getCell(intColRef).getNumericCellValue()));
	        break;
	        
	        // BLANK OR NULL TYPE
	        case Cell.CELL_TYPE_BLANK:
	        	strCellValue = "BLANK";
	        break;
	        case 9:
	        	strCellValue = "BLANK";
	        break;
	            
	        // OTHER
	        default:
	            System.out.println("====== getCellValueAsString is a non recognised format: Type is " + intCellFormat);
		}
		return strCellValue;
	}
	protected static int getCellFormatType(int intRowRef,int intColRef)
	{
		//Initialise return value to be a non valid, null return code
		int intCellFormatType = 9;
		
		// Get cell type value. Be aware that if an Excel 2007+ cell is empty, an exception is thrown which we should
		// catch and ignore. Any method calling this must be able to handle a null return code of 9
		try
		{
			intCellFormatType = objExcelInputSheet.getRow(intRowRef).getCell(intColRef).getCellType();
		}
		catch (Exception e)
		{
		}		
		return intCellFormatType;
	}
	protected static int getDataSheetCols()
	{
		// Count through the columns starting at A1 and return no of cols with actual values
		int intColCounter = 0;
		while (getCellValueAsString(0,intColCounter).equalsIgnoreCase("BLANK") == false) 
		{
			intColCounter = intColCounter + 1;
		}	
		
		return intColCounter;
	}
	protected static int getDataSheetRows()
	{
		// Count through the rows starting at A1 and return no of rows with actual values
		int intRowCounter = 0;
		while (getCellValueAsString(intRowCounter,0).equalsIgnoreCase("BLANK") == false) 
		{
			intRowCounter = intRowCounter + 1;
		}
		
		return intRowCounter;
	}
}
