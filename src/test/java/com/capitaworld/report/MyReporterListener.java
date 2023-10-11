package com.capitaworld.report;

import com.capitaworld.commonutil.*;
import com.capitaworld.proxy.MethodDetails;
import org.testng.*;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyReporterListener extends MyReporterListenerAdapter {

	private static final Logger L = Logger.getLogger(MyReporterListener.class);
	DataUtil util = null;
	Map<String, Object> details;

	public MyReporterListener() {
		util = DataUtil.getInstance();
		details = util.getDetails();
	}

	// ~ Instance fields ------------------------------------------------------

	private PrintWriter m_out;

	private int m_row;

	private Integer m_testIndex;

	private int m_methodIndex;

	private Scanner scanner;

	private int pass;

	private int fail;

	private int skip;

	private int total;

	// ~ Methods --------------------------------------------------------------

	/** Creates summary of the run */
	@Override
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
		try {
			m_out = createWriter(outdir);
		} catch (IOException e) {
			L.error("output file", e);
			return;
		}

		startHtml(m_out);
		generateSuiteSummaryReport(suites);
		// generateMethodSummaryReport(suites);
		generateMethodDetailReport(suites);
		endHtml(m_out);
		m_out.flush();
		m_out.close();

		// Mailer

		List<String> list = new ArrayList<String>();
		List<String> ccList = new ArrayList<String>();
		total = pass + fail + skip;
		String str = CommonMethods.returnStringforMail(pass, fail, total, skip);

//		list.add("tushar.patel@onlinepsbloans.com");
//		ccList.add("sahil.patel@onlinepsbloans.com");
		ccList.add("vinjal.patel@onlinepsbloans.com");
		list.add("daksh.dhameliya@onlinepsbloans.com");

		String productName=null;
		for (ISuite suite : suites) {
			
			if(suite.getName().equalsIgnoreCase(Constants.email.SUITE_NAME_UAP)) {
				productName=Constants.email.UAP;
			}
		}
		Email email=new Email(productName+" Automation Script Report For OPL", str, list).setCc(ccList);
		List<String> filePaths = new ArrayList<String>();
		// Path for Run As TestNG Test
		File scheduleUploadFile = new File("target/surefire-reports/EmailableReport" + Constants.TODAY_DATE + ".html");
		//File scheduleUploadFile= new File("test-output/EmailableReport" + Constants.TODAY_DATE+".html");
		System.out.println("Emailable Report File Path is -->" + scheduleUploadFile.getAbsolutePath());
		filePaths.add(scheduleUploadFile.getAbsolutePath());
		email.setFilePaths(filePaths);
		System.out.println("Email is -->" + email.getReceiver().get(0));
		MailUtil.sendMail(email);

	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		// java.util.Date now = new Date();
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "EmailableReport" + Constants.TODAY_DATE + ".html"))));
		
	}

	/**
	 * Creates a table showing the highlights of each test method with links to the
	 * method details
	 */
	protected void generateMethodSummaryReport(List<ISuite> suites) {
		m_methodIndex = 0;
		startResultSummaryTable("methodOverview");
		int testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() > 1) {
				titleRow(suite.getName(), 5);
			}
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				String testName = testContext.getName();
				m_testIndex = testIndex;
				resultSummary(suite, testContext.getFailedConfigurations(), testName, "failed",
						" (configuration methods)");
				resultSummary(suite, testContext.getFailedTests(), testName, "failed", "");
				resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped",
						" (configuration methods)");
				resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
				resultSummary(suite, testContext.getPassedTests(), testName, "passed", "");
				testIndex++;
			}
		}
		m_out.println("</table>");
	}

	/** Creates a section showing known results for each method */
	protected void generateMethodDetailReport(List<ISuite> suites) {
		m_methodIndex = 0;
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				if (r.values().size() > 0) {
					m_out.println(
							"<h1><div id='" + testContext.getName() + "'>" + testContext.getName() + "</h1></div>");
				}
				resultDetail(testContext.getFailedConfigurations());
				resultDetail(testContext.getFailedTests());
				resultDetail(testContext.getSkippedConfigurations());
				resultDetail(testContext.getSkippedTests());
				resultDetail(testContext.getPassedTests());
			}
		}
	}

	/**
	 * @param tests
	 */
	private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {
		if (tests.getAllResults().size() > 0) {
			StringBuffer buff = new StringBuffer();
			String lastClassName = "";
			int mq = 0;
			int cq = 0;
			for (ITestNGMethod method : getMethodSet(tests, suite)) {
				m_row += 1;
				m_methodIndex += 1;
				ITestClass testClass = method.getTestClass();
				String className = testClass.getName();
				if (mq == 0) {
					String id = (m_testIndex == null ? null : "t" + Integer.toString(m_testIndex));
					titleRow(testname + " &#8212; " + style + details, 5, id);
					m_testIndex = null;
				}
				if (!className.equalsIgnoreCase(lastClassName)) {
					if (mq > 0) {
						cq += 1;
						m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
						if (mq > 1) {
							m_out.print(" rowspan=\"" + mq + "\"");
						}
						m_out.println(">" + lastClassName + "</td>" + buff);
					}
					mq = 0;
					buff.setLength(0);
					lastClassName = className;
				}
				Set<ITestResult> resultSet = tests.getResults(method);
				long end = Long.MIN_VALUE;
				long start = Long.MAX_VALUE;
				for (ITestResult testResult : tests.getResults(method)) {
					if (testResult.getEndMillis() > end) {
						end = testResult.getEndMillis();
					}
					if (testResult.getStartMillis() < start) {
						start = testResult.getStartMillis();
					}
				}
				mq += 1;
				if (mq > 1) {
					buff.append("<tr class=\"" + style + (cq % 2 == 0 ? "odd" : "even") + "\">");
				}
				String description = method.getDescription();
				String testInstanceName = resultSet.toArray(new ITestResult[] {})[0].getTestName();
				buff.append("<td><a href=\"#m" + m_methodIndex + "\">" + qualifiedName(method) + " "
						+ (description != null && description.length() > 0 ? "(\"" + description + "\")" : "") + "</a>"
						+ (null == testInstanceName ? "" : "<br>(" + testInstanceName + ")") + "</td>"
						+ "<td class=\"numi\">" + resultSet.size() + "</td>" + "<td>" + start + "</td>"
						+ "<td class=\"numi\">" + (end - start) + "</td>" + "</tr>");
			}
			if (mq > 0) {
				cq += 1;
				m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
				if (mq > 1) {
					m_out.print(" rowspan=\"" + mq + "\"");
				}
				m_out.println(">" + lastClassName + "</td>" + buff);
			}
		}
	}

	/** Starts and defines columns result summary table */
	private void startResultSummaryTable(String style) {
		tableStart(style, "summary");
		m_out.println("<tr><th>Class</th>"
				+ "<th>Method</th><th># of<br/>Scenarios</th><th>Start</th><th>Time<br/>(ms)</th></tr>");
		m_row = 0;
	}

	private String qualifiedName(ITestNGMethod method) {
		StringBuilder addon = new StringBuilder();
		String[] groups = method.getGroups();
		int length = groups.length;
		if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
			addon.append("(");
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					addon.append(", ");
				}
				addon.append(groups[i]);
			}
			addon.append(")");
		}

		return "<b>" + method.getMethodName() + "</b> " + addon;
	}

	private void resultDetail(IResultMap tests) {
		for (ITestResult result : tests.getAllResults()) {
			ITestNGMethod method = result.getMethod();
			m_methodIndex++;
			// String cname = method.getTestClass().getName();
			// m_out.println("<h2 id=\"m" + m_methodIndex + "\">" + cname + ":"
			// + method.getMethodName() + "</h2>");
			Set<ITestResult> resultSet = tests.getResults(method);
			generateForResult(result, method, resultSet.size());
			m_out.println("<hr>");
			m_out.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");

		}
	}

	private void generateForResult(ITestResult ans, ITestNGMethod method, int resultSetSize) {
		Object[] parameters = ans.getParameters();
		boolean hasParameters = parameters != null && parameters.length > 0;
		/*if (hasParameters) {
			tableStart("result", null);
			m_out.print("<tr class=\"param\">");
			for (int x = 1; x <= parameters.length; x++) {
				m_out.print("<th>Param." + x + "</th>");
			}
			m_out.println("</tr>");
			m_out.print("<tr class=\"param stripe\">");
			for (Object p : parameters) {
				m_out.println("<td>" + Utils.escapeHtml(Utils.toString(p)) + "</td>");
			}
			m_out.println("</tr>");
		}*/
		List<String> msgs = Reporter.getOutput(ans);
		boolean hasReporterOutput = msgs.size() > 0;

		Throwable exception = ans.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasReporterOutput || hasThrowable) {
			if (hasParameters) {
				m_out.print("<tr><td");
				if (parameters.length > 1) {
					m_out.print(" colspan=\"" + parameters.length + "\"");
				}
				m_out.println(">");
			} else {
				m_out.println("<div>");
			}
			if (hasReporterOutput) {
				if (hasThrowable) {
					// m_out.println("<h3>Test Messages</h3>");
				}
				for (String line : msgs) {
					m_out.println(line + "<br/>");
				}
			}
			if (hasThrowable) {
				boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
				if (hasReporterOutput) {
					m_out.println("<h3>" + (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</h3>");
				}
				generateExceptionReport(exception, method);
			}
			if (hasParameters) {
				m_out.println("</td></tr>");
			} else {
				m_out.println("</div>");
			}
		}
		/*if (hasParameters) {
			m_out.println("</table>");
		}*/
	}

	@SuppressWarnings("deprecation")
	protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
		m_out.print("<div class=\"stacktrace\">");

		String str = Utils.stackTrace(exception, true)[0];
		scanner = new Scanner(str);
		String firstLine = scanner.nextLine();
		m_out.println(firstLine);
		m_out.println("</div>");
	}

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {
		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				r.add(im);
			}
		}
		Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();

		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			result.add(m.getTestMethod());
		}

		// Add all the methods that weren't invoked (e.g. skipped) that we
		// haven't added yet
		for (ITestNGMethod m : tests.getAllMethods()) {
			if (!result.contains(m)) {
				result.add(m);
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	public void generateSuiteSummaryReport(List<ISuite> suites) {
		tableStart("testOverview", "summary");
		m_out.print("<tr>");

		tableColumnStart("Test CaseID");
		tableColumnStart("Test Scenarios");

		// tableColumnStart("Methods<br/>Passed");
		tableColumnStart("Result");
		//tableColumnStart("Audit Trail");
		// tableColumnStart("# failed");
		// tableColumnStart("Error messages");
		// tableColumnStart("Parameters");
		tableColumnStart("Start Time-<br/>End Time");

		// tableColumnStart("End<br/>Time");
		tableColumnStart("Total<br/>Time");
		// tableColumnStart("Included<br/>Groups");
		// tableColumnStart("Excluded<br/>Groups");

		m_out.println("</tr>");
		NumberFormat formatter = new DecimalFormat("#,##0.0");
		SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yy hh:mm:ss a");
		int qty_tests = 0;
		int qty_pass_m = 0;
		int qty_pass_s = 0;
		int qty_skip = 0;
		int qty_fail = 0;
		int total_pass_s = 0;
		int total_fail = 0;
		int total_skip = 0;
		long time_start = Long.MAX_VALUE;
		long time_end = Long.MIN_VALUE;
		m_testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() >= 1) {
				titleRow(suite.getName(), 7);
			}
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult r : tests.values()) {
				qty_tests += 1;

				// Getting Method Name

				ITestContext overview = r.getTestContext();

				MethodDetails MD = (MethodDetails) details.get(overview.getName());
				System.out.println("Method Details Adding For- : " + overview.getName());
				startSummaryRow(MD.getTestCaseID());
				printTestCaseName(overview.getName());

				// get Passed Methods Number
				int q = getMethodSet(overview.getPassedTests(), suite).size();
				/*
				 * qty_pass_m += q; summaryCell(q, Integer.MAX_VALUE);
				 */
				// Get passed Tests Number
				q = overview.getPassedTests().size();
				qty_pass_s += q;
				// summaryCell(q, Integer.MAX_VALUE);
				// Get Skipped Tests Number
				q = getMethodSet(overview.getSkippedTests(), suite).size();
				qty_skip += q;
				// summaryCell(q, 0);
				// Get Failed Tests Number
				q = getMethodSet(overview.getFailedTests(), suite).size();
				qty_fail += q;
				// summaryCell(q, 0);
				if (qty_pass_s > 0) {
					summaryCell("<center><label style='color:green;'>Passed</label></center>");
					total_pass_s += qty_pass_s;
					qty_pass_s = 0;

				} else if (qty_fail > 0) {
					summaryCell("<center><label style='color:red;'>Failed</label></center>");
					total_fail += qty_fail;
					qty_fail = 0;
				} else if (qty_skip > 0) {
					summaryCell("<center><label style='color:yellow;'>Skipped</label></center>");
					total_skip += qty_skip;
					qty_skip = 0;
				}

				/*if (MD.getAuditTrail().equalsIgnoreCase(Constants.AUDITTRAIL_PRESENT)) {
					summaryCell("<center><label>Audit Trail Generated.</label></center>");
				} else if (MD.getAuditTrail().equalsIgnoreCase(Constants.AUDITTRAIL_NOTREQUIRE)) {
					summaryCell("<center><label>NA</label></center>");
				} else if (MD.getAuditTrail().equalsIgnoreCase(Constants.AUDITTRAIL_MISSING)) {
					summaryCell("<center><label style='color:red;'>Missing Audit Trail.</label></center>");
				}*/

				// NEW
				// Insert error found
				/*
				 * m_out.print("<td class=\"numi" + (true ? "" : "_attn") + "\">");
				 * getShortException(overview.getFailedTests());
				 * getShortException(overview.getSkippedTests()); m_out.println("</td>");
				 */

				// NEW
				// Add parameters for each test case (failed or passed)
				/*
				 * m_out.print("<td class=\"numi" + (true ? "" : "_attn") + "\">");
				 * 
				 * // Write OS and Browser //
				 * m_out.println(suite.getParameter("os").substring(0, 3) + // " | " // +
				 * suite.getParameter("browser").substring(0, 3) + " | ");
				 * 
				 * getParameters(overview.getFailedTests());
				 * getParameters(overview.getPassedTests());
				 * getParameters(overview.getSkippedTests()); m_out.println("</td>");
				 */

				// NEW ----DateFunctions.dateToDayAndTime(overview.getStartDate())
				summaryCell(df.format(overview.getStartDate()).toString() + " -- "
						+ df.format(overview.getEndDate()).toString(), true);
				m_out.println("</td>");
				/*
				 * summaryCell( df.format(overview.getEndDate()).toString(), true);
				 */
				m_out.println("</td>");

				time_start = Math.min(overview.getStartDate().getTime(), time_start);
				time_end = Math.max(overview.getEndDate().getTime(), time_end);
				summaryCelltotal(
						formatter.format((overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000.)
								+ " seconds",
						true);
				// summaryCell(overview.getIncludedGroups());
				// summaryCell(overview.getExcludedGroups());
				m_out.println("</tr>");
				m_testIndex++;
			}
		}
		if (qty_tests >= 1) {
			m_out.println("<tr class=\"total\"><td>Total</td>");
			m_out.println("<td>Passed:" + total_pass_s + "<br/>Failed:" + total_fail + "<br/>Skipped:" + total_skip
					+ "</td>");
			// summaryCell(qty_pass_m, Integer.MAX_VALUE);
			// summaryCell(qty_pass_s, Integer.MAX_VALUE);
			// summaryCell(qty_skip, 0);
			// summaryCell(qty_fail, 0);
			// summaryCell(" ", true);
			// summaryCell(" ", true);
			summaryCell(" ", true);
			summaryCell(" ", true);
			pass = total_pass_s;
			fail = total_fail;
			skip = total_skip;
			System.out.println("pass test cases:-" + pass);
			System.out.println("fail test cases:-" + fail);
			summaryCellEnd(formatter.format(((time_end - time_start) / 1000.) / 60.) + " minutes", true);
			// m_out.println("<td colspan=\"3\">&nbsp;</td></tr>");
		}
		m_out.println("</table>");
	}

	private void summaryCell(String val) {
		/*
		 * StringBuffer b = new StringBuffer(); for (String v : val) { b.append(v +
		 * " "); }
		 */
		summaryCelltotal(val, true);
	}

	private void summaryCell(String v, boolean isgood) {
		m_out.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v + "</td>");
	}

	private void summaryCelltotal(String v, boolean isgood) {
		m_out.print("<td class=\"numi2" + (isgood ? "" : "_attn") + "\">" + v + "</td>");
	}

	private void summaryCellEnd(String v, boolean isgood) {
		m_out.print("<td colspan=2 class=\"numi" + (isgood ? "" : "_attn") + "\"><center>" + v + "</center></td>");
	}

	private void startSummaryRow(String label) {
		m_row += 1;
		m_out.print("<tr" + (m_row % 2 == 0 ? " class=\"stripe\"" : "") + "><td width='5%'>" + label + "</td>");
	}

	private void printTestCaseName(String label) {
		m_row += 1;
		m_out.print("<td style=\"text-align:left;padding-right:2em;width:20%\"><a href='#" + label + "'>" + label
				+ "</a>" + "</td>");
	}

	/*
	 * private void summaryCell(int v, int maxexpected) {
	 * summaryCell(String.valueOf(v), v <= maxexpected); }
	 */

	private void tableStart(String cssclass, String id) {
		m_out.println("<table cellspacing=\"0\" cellpadding=\"0\""
				+ (cssclass != null ? " class=\"" + cssclass + "\"" : " style=\"padding-bottom:2em\"")
				+ (id != null ? " id=\"" + id + "\"" : "") + ">");
		m_row = 0;
	}

	private void tableColumnStart(String label) {
		m_out.print("<th>" + label + "</th>");
	}

	private void titleRow(String label, int cq) {
		titleRow(label, cq, null);
	}

	private void titleRow(String label, int cq, String id) {
		m_out.print("<tr");
		if (id != null) {
			m_out.print(" id=\"" + id + "\"");
		}
		m_out.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
		m_row = 0;
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		out.println(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<title>OPL Automation Report-TestNG Report</title>");
		out.println("<style type=\"text/css\">");
		out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
		out.println("td,th {border:1px solid #009;padding:.25em .5em}");
		out.println(".result th {vertical-align:bottom}");
		out.println(".param th {padding-left:1em;padding-right:1em}");
		out.println(".param td {padding-left:.5em;padding-right:2em}");
		out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
		out.println(".numi,.numi_attn {width : 18%}");
		out.println(".numi2,.numi2_attn {width : 8%}");
		out.println(".total td {font-weight:bold}");
		out.println(".passedodd td {background-color: #0A0}");
		out.println(".passedeven td {background-color: #3F3}");
		out.println(".skippedodd td {background-color: #CCC}");
		out.println(".skippedodd td {background-color: #DDD}");
		out.println(".failedodd td,.numi_attn {background-color: #F33}");
		out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
		out.println(".stacktrace {white-space:pre;font-family:monospace}");
		out.println(".totop {font-size:60%;text-align:center;border-bottom:2px solid #000}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("<center> Report customized by OPL- QA Team</center>");
		out.println("</body></html>");
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	private class TestSorter implements Comparator<IInvokedMethod> {
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		public int compare(IInvokedMethod o1, IInvokedMethod o2) {
			// System.out.println("Comparing " + o1.getMethodName() + " " +
			// o1.getDate()
			// + " and " + o2.getMethodName() + " " + o2.getDate());
			return (int) (o1.getDate() - o2.getDate());
			// int r = ((T) o1).getTestClass().getName().compareTo(((T)
			// o2).getTestClass().getName());
			// if (r == 0) {
			// r = ((T) o1).getMethodName().compareTo(((T) o2).getMethodName());
			// }
			// return r;
		}
	}
}