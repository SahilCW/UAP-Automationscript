package com.capitaworld.report;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.util.List;

public class MyReporterListenerAdapter implements IReporter {

public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {}
}
