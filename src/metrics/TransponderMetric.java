package metrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import simulator.Simulator;
import models.CustomRequest;
import utilities.RequestsUtil;
import utilities.NetworkTopology;

public class TransponderMetric{
	
	private static enum BANDWIDTH_DISTRIBUTION { RANDOM, GAUSSIAN, UNIFORM };
	private static enum EMBEDDING_METHOD { BACKUP, WO_BACKUP }; 
	
	private final String PROJECT_DIRECTORY =  new File(".").getCanonicalPath();
	private final String RESULTS_DIRECTORY = "/src/results/";
	
	public TransponderMetric()throws IOException{}
	
	//take in topology type, bandwidth distribution type, routing type, threshold, custom request file path
	public void start(NetworkTopology topology, int threshold) throws IOException{;
		//getResultsSimple(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP);

		//getResultsNSFNET(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.RING8,30,false);

	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE8,40);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.MESH8,50);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE16,60);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE16,70);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE16,80);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE16,90);
	//	getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP,NetworkTopology.HYPERCUBE16,110);
		//getResultsHypercube16(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP);		
		getResults(BANDWIDTH_DISTRIBUTION.RANDOM,EMBEDDING_METHOD.BACKUP, topology, threshold, false);

	}
	
	public void getResults(BANDWIDTH_DISTRIBUTION distributionType, EMBEDDING_METHOD method, NetworkTopology topology, int threshold, boolean customRequest) throws IOException{
		String fileName = "";
		switch(topology) {
			case SIMPLE:
				fileName = "_transponder_new_ring4_";
				break;
			case NSFNET:
				fileName = "_transponder_new_nsfnet_";
				break;
			case US_MESH:
				fileName = "_transponder_new_us_mesh_";
				break;
			case HYPERCUBE8:
			//	fileName = "_transponder_new_hypercube8_";
				fileName = "_transponder_ODU_new_hypercube8_";
			//	fileName = "_transponder_MUX_new_hypercube8_";
				break;
			case HYPERCUBE16:
				fileName = "_transponder_new_hypercube16_";
				break;
			case RING8:
			//	fileName = "_transponder_new_ring8_";
				fileName = "_transponder_ODU_new_ring8_";
			//	fileName = "_transponder_MUX_new_ring8_";
				break;
			case MESH8:
			//	fileName = "_transponder_new_mesh8_";
				fileName = "_transponder_ODU_new_mesh8_";
			//	fileName = "_transponder_MUX_new_mesh8_";
				break;
		}
		File file = new File(PROJECT_DIRECTORY + RESULTS_DIRECTORY + threshold + fileName + distributionType.name().toLowerCase() + "_" + method.name().toLowerCase() + ".csv");
		PrintWriter pw = new PrintWriter(file);
		
	//	pw.println("Max_Bandwidth,ODU,OPT,Hybrid,# ODU better than OPT ");
		
		pw.println("Max_Bandwidth,SPF,LUF,MUF ");
		
	//	pw.println("Max_Bandwidth,MUX ");

		/*for(int i = 20; i <= 500; i+=20){
			System.out.println("Starting Transponder Metric with max bandwidth: " + i);
			int oduWins = 0;
			
			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			for(int j = 0; j < 1000; j++){
				Simulator simulator = new Simulator(topology,Integer.MAX_VALUE, 100000);
				simulator.setMaxNodes(0);// setting requests with only two nodes.
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				//simulator.generateSpecificRequests();
				//simulator.setRequests();
				
				int oduTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 1, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
				int optTransponders = simulator.getTransponderOPT(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
				int hybridTransponders = simulator.getTranspondersHybrid(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, threshold);
				
				if(oduTransponders < optTransponders) oduWins++;
				
				sum1 += oduTransponders; 
				sum2 += optTransponders;
				sum3 += hybridTransponders;
			}				
			pw.println(i + "," + sum1/1000 + "," + sum2/1000 + "," + sum3/1000 + "," + oduWins);
		}*/
		
		ArrayList<CustomRequest> requests = new ArrayList<CustomRequest>();
		if(customRequest) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter location of file.");
			requests = RequestsUtil.createCustomRequests(sc.nextLine());
			sc.close();
		}

		for(int i = 20; i <= 40; i+=20){

			System.out.println("Starting Transponder Metric with max bandwidth: " + i);
			
			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			for(int j = 0; j < 1000; j++){
				Simulator simulator = new Simulator(topology,Integer.MAX_VALUE, 100000);
				simulator.setMaxNodes(0);// setting requests with only two nodes.
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				//simulator.generateSpecificRequests();
				//simulator.setRequests();
				
				int SPFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 0, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				int LUFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 1, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				int MUFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 2, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				
				
				
				sum1 += SPFTransponders; 
				sum2 += LUFTransponders;
				sum3 += MUFTransponders;
			}				
			pw.println(i + "," + sum1/1000 + "," + sum2/1000 + "," + sum3/1000);
		}
		
		/*for(int i = 20; i <= 500; i+=20){
			System.out.println("Starting Transponder Metric with max bandwidth: " + i);
			
			int sum1 = 0;
			for(int j = 0; j < 1000; j++){
				Simulator simulator = new Simulator(topology,Integer.MAX_VALUE, 100000);
				simulator.setMaxNodes(0);// setting requests with only two nodes.
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				//simulator.generateSpecificRequests();
				//simulator.setRequests();
				
				int MUXTransponders = simulator.getTransponderMUX(100,i, distributionType.name().toLowerCase(),(method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
			
				sum1 += MUXTransponders; 
			}				
			pw.println(i + "," + sum1/1000);
		}*/
		
		pw.close();
		
		System.out.println("********************** done *******************");
	}
	
	public static void getResultsTest(BANDWIDTH_DISTRIBUTION distributionType, EMBEDDING_METHOD method, NetworkTopology topology, int threshold, boolean customRequest) throws IOException{
		String PROJECT_DIRECTORY =  new File(".").getCanonicalPath();
		String RESULTS_DIRECTORY = "/src/results/";
		String fileName = "_transponder_new_" + distributionType.name().toLowerCase() + "_";
		switch(topology) {
			case SIMPLE:
				fileName = "_transponder_new_ring4_";
				break;
			case NSFNET:
				fileName = "_transponder_new_nsfnet_";
				break;
			case US_MESH:
				fileName = "_transponder_new_us_mesh_";
				break;
			case HYPERCUBE8:
				fileName = "_transponder_new_hypercube8_";
				break;
			case HYPERCUBE16:
				fileName = "_transponder_new_hypercube16_";
				break;
			case RING8:
				fileName = "_transponder_new_ring8_";
				break;
			case MESH8:
				fileName = "_transponder_new_mesh8_";
				break;
		}
		File file = new File(PROJECT_DIRECTORY + RESULTS_DIRECTORY + threshold + fileName + distributionType.name().toLowerCase() + "_" + method.name().toLowerCase() + ".csv");
		PrintWriter pw = new PrintWriter(file);
		
		
		pw.println("Max_Bandwidth,SPF,LUF,MUF,OPT,Hybrid,MUX ");
		
		ArrayList<CustomRequest> requests = new ArrayList<CustomRequest>();
		if(customRequest) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter location of file.");
			requests = RequestsUtil.createCustomRequests(sc.nextLine());
			sc.close();
		}
		
		//default 500
		for(int i = 20; i <= 60; i+=20){
			System.out.println("Starting Transponder Metric with max bandwidth: " + i);
			
			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			int sum4 = 0;
			int sum5 = 0;
			int sum6 = 0;
			for(int j = 0; j < 1000; j++){
				Simulator simulator = new Simulator(topology,Integer.MAX_VALUE, 100000);
				simulator.setMaxNodes(0);// setting requests with only two nodes.
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				//simulator.generateSpecificRequests();
				//simulator.setRequests();
				
				int SPFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 0, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				int LUFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 1, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				int MUFTransponders = simulator.getTranspondersODU(100,i, distributionType.name().toLowerCase(), 2, (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests);
				int optTransponders = simulator.getTransponderOPT(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
				int hybridTransponders = simulator.getTranspondersHybrid(100,i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, threshold);
				int MUXTransponders = simulator.getTransponderMUX(100,i, distributionType.name().toLowerCase(),(method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true);
				
				
				
				sum1 += SPFTransponders; 
				sum2 += LUFTransponders;
				sum3 += MUFTransponders;
				sum4 += optTransponders;
				sum5 += hybridTransponders;
				sum6 += MUXTransponders;
			}				
			pw.println(i + "," + sum1/1000 + "," + sum2/1000 + "," + sum3/1000 + "," + sum4/1000 + "," + sum5/1000 + "," + sum6/1000);
		}
		
		pw.close();
		
		System.out.println("********************** done *******************");
	}
	
	public static void getResultsTestTwo(BANDWIDTH_DISTRIBUTION distributionType, EMBEDDING_METHOD method, NetworkTopology topology, String routingType, int maxBandwidth, int transponderCapacity, int hybridThreshold, boolean customRequest) throws IOException{
		String PROJECT_DIRECTORY =  new File(".").getCanonicalPath();
		String RESULTS_DIRECTORY = "/src/results/";
		String fileName = "_transponder_new_" + distributionType.name().toLowerCase() + "_" + routingType.toLowerCase() + "_";
		File file = new File(PROJECT_DIRECTORY + RESULTS_DIRECTORY + hybridThreshold + fileName + distributionType.name().toLowerCase() + "_" + method.name().toLowerCase() + ".csv");
		PrintWriter pw = new PrintWriter(file);
		
		
		pw.println("Max_Bandwidth," + routingType + " ");
		
		ArrayList<CustomRequest> requests = new ArrayList<CustomRequest>();
		if(customRequest) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter location of file.");
			requests = RequestsUtil.createCustomRequests(sc.nextLine());
			sc.close();
		}
		
		//default 500
		for(int i = 20; i <= maxBandwidth; i+=20){
			System.out.println("Starting Transponder Metric with max bandwidth: " + i);
			
			int sum = 0;
			
			for(int j = 0; j < 1000; j++){
				Simulator simulator = new Simulator(topology,Integer.MAX_VALUE, 100000);
				simulator.setMaxNodes(0);// setting requests with only two nodes.
				simulator.setNumberOfRequest(500);
				simulator.generateRequests();
				
				int transponders = getTranspondersByType(routingType, simulator, transponderCapacity, i, distributionType.name().toLowerCase(), (method.equals(EMBEDDING_METHOD.WO_BACKUP))?false:true, requests, hybridThreshold);
				
				sum += transponders; 
				
			}				
			pw.println(i + "," + sum/1000);
		}
		
		pw.close();
		
		System.out.println("********************** done *******************");
	}
	
	public static int getTranspondersByType(String routingType, Simulator simulator, int transponderCapacity, int maxBandwidth, String distribution,
			boolean backupPath, ArrayList<CustomRequest> customRequest, int hybridThreshold) {
		int transponders = 0;
		switch(routingType) {
		case "SPF":
			transponders = simulator.getTranspondersODU(transponderCapacity, maxBandwidth, distribution, 0, backupPath, customRequest);
			break;
		case "LUF":
			transponders = simulator.getTranspondersODU(transponderCapacity, maxBandwidth, distribution, 1, backupPath, customRequest);
			break;
		case "MUF":
			transponders = simulator.getTranspondersODU(transponderCapacity, maxBandwidth, distribution, 2, backupPath, customRequest);
			break;
		case "OPT":
			transponders = simulator.getTransponderOPT(transponderCapacity, maxBandwidth, distribution, backupPath);
			break;
		case "Hybrid":
			transponders = simulator.getTranspondersHybrid(transponderCapacity, maxBandwidth, distribution, backupPath, hybridThreshold);
			break;
		case "MUX":
			transponders = simulator.getTransponderMUX(transponderCapacity, maxBandwidth, distribution, backupPath);
			break;
		default:
			break;
		}
		return transponders;
	}
	
	public static void main(String args[]) throws IOException{
		
		new TransponderMetric().start(NetworkTopology.valueOf(args[0]), Integer.parseInt(args[1]));
	}
}
