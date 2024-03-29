package com.xadmin.accountHeaders;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xadmin.tools.Parameters;
import com.xadmin.tools.TransactionAttribute;
import com.xadmin.tools.AccountTools;

public class AccountAnalysis {

	public static TreeMap<String, String[]> mixedMonthDatabase = new TreeMap<String, String[]>();
	public static TreeMap<String, TransactionAttribute> individualTransaction = new TreeMap<String, TransactionAttribute>();
	public static double lastCurrentAmount = 0;

	static TreeMap<String, TreeMap<String, TransactionAttribute>> listOfTransactions = new TreeMap<String, TreeMap<String, TransactionAttribute>>();
	static ArrayList<String> listWithDate = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		try {

			List<String> listOfStatements = new AccountTools().filesFromFolder();
			
			for (String singleStatement : listOfStatements) {
				refactoringStatement(singleStatement);
			}
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void refactoringStatement(String singleMonth) {

		//System.out.println(singleMonth);
		int count = -1;
		Pattern p = Pattern.compile(Parameters.REFACTORING_REGEX_PATTERN);

		String refactoredStatement = singleMonth.replaceAll(Parameters.CHARACTER_SEQUENCE, Parameters.SEPARATOR_1).replaceAll(Parameters.NEXT_LINE, Parameters.SEPARATOR_2).replaceAll(Parameters.TOBEREPLACED_1, Parameters.TOBEREPLACEDWITH_1);
		
		String allTransactions = refactoredStatement.replaceAll(Parameters.INDIVIDUAL_TRANSACTION_SEPARATOR,
				Parameters.NEXT_LINE);

	//	System.out.println(allTransactions);
		String[] listOfTransactions = allTransactions.split(Parameters.NEXT_LINE);
		String filtered = new String();

		for (String string : listOfTransactions) {

			Matcher matches = p.matcher(string);
			if (matches.find()) {
				System.out.println(matches.group());
				filtered += (++count + " " + matches.group() + Parameters.NEXT_LINE);
			} else {
				System.out.println(string);
				filtered += (++count + " " + string + Parameters.NEXT_LINE);
			}
		}

		filteredStringOperation(filtered);

	}

	private static void filteredStringOperation(String filtered) {

		String[] stringArray = filtered.split(Parameters.NEXT_LINE);
		int count = -1;
		String lastString = lastString(stringArray[stringArray.length - 1]);
		System.out.println("Last String "+lastString);
		stringArray[stringArray.length - 1] = lastString;
		for (int i = 0; i < stringArray.length; i++) {
			String[] date=null;
		   System.out.println(stringArray[i]);
			String[] oneline = stringArray[i].split(" ");
			
			if (!oneline[oneline.length - 3].matches("../../..")) {
			  continue;   
			}
			date = oneline[oneline.length - 3].split("/");
			//   System.out.println(date[0]);
			
		   individualTransaction.put(date[2]+"/"+date[1]+"/"+date[0]+"_"+oneline[oneline.length - 4], new TransactionAttribute(oneline[oneline.length - 3], i+1, oneline[oneline.length - 4], oneline[oneline.length-5]));
		    
			//System.out.println(i+ " " + oneline[oneline.length - 1] + "   " + oneline[oneline.length - 2] + "   "+ oneline[oneline.length - 3] + "   " + oneline[oneline.length - 4] + "   " + oneline[0]);
		}
		
		lastString(stringArray[stringArray.length - 1]);
		count=0;
		for (Entry<String, TransactionAttribute> entry : individualTransaction.entrySet()) {
			String key = entry.getKey();
			//System.out.println(++count+" "+key+" "+entry.getValue().getDate()+" " +entry.getValue().getRefrenceNum()+" "+entry.getValue().getAmouintLeft());
			
		}
		
		lastString(stringArray[stringArray.length - 1]);
	}

	public static String lastString(String lastTransaction) {
		//lastTransactionDivision ;
		String[] arr2 =lastTransaction.split("#");
		String arr3=arr2[0];
		String[] oneliner = arr3.split(" ");
	//System.out.println("last" + " " + oneliner[oneliner.length - 1] + "   " + oneliner[oneliner.length - 2] + "   "
			//+ oneliner[oneliner.length - 3] + "   " + oneliner[oneliner.length - 4] + "   " + oneliner[0]);
	
		String[] arr1 = lastTransaction.split(" ");
		String close_Balance = arr1[arr1.length - 1];
		String opening_Balance = arr1[arr1.length - 6];
		String dr_Count = arr1[arr1.length - 5];
		String cr_Count = arr1[arr1.length - 4];
		String debits = arr1[arr1.length - 3];
		String credits = arr1[arr1.length - 2];

	//	System.out.println(close_Balance + " " + debits+ " " +credits+ " " +dr_Count+ " " +cr_Count+ " " +opening_Balance);
		return arr2[0];

	}

}
