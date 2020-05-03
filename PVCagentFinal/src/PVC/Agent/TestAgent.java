package PVC.Agent;

import PVC.Algorithmes.Algorithme;
import PVC.Algorithmes.GeneticAlgorithm;
import PVC.Algorithmes.Recuit;
import PVC.Algorithmes.Tabou;
import PVC.Data.CityData;
import PVC.Definitions.City;
import PVC.Definitions.Optimizer;
import PVC.Definitions.Parametre;
import PVC.Definitions.Route;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class TestAgent extends Agent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Route initialRoute;
    private Route bestRoute;
    private ArrayList<City> cities = new CityData(10).getCities();
    private Algorithme a; 
    private String tab;
    protected void setup() {
    	tab=getArguments()[0].toString();
        //System.out.println("Hello World! My name is " + getLocalName());
        this.addBehaviour(new behave(tab));
    }


    enum Status {
        INIT, CALC, END, SEND, WAIT
    }
    class initbehave extends Behaviour{

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }

    class behave extends Behaviour {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Algorithme algorithme;
        private Status status = Status.INIT;
        private int count = 0;
        private String s;
        private ArrayList<Parametre> paras = new ArrayList<>();
        private Optimizer opti ;
        //private ArrayList<City> cities;

        public behave(String s) {
            this.s = s;
            switch(s) {
            case("Tabou"):
            	this.paras.add(new Parametre("T-length",5,1));
            	this.paras.add(new Parametre("Coeff",0.8f,0.4f));
            	break;
            case("Recuit"):
            	this.paras.add(new Parametre("Temperature",100,5));
        		this.paras.add(new Parametre("Refroid",0.5f,0.05f));
            	break;
            case("Genetic"):
            	this.paras.add(new Parametre("pCrossover",0.8f,0.02f));
    			this.paras.add(new Parametre("pMutation",0.01f,0.01f));
            	break;
            default: System.out.println("ERROR FORMAT INFORMATION");
            	break;
            }
        }

        @Override
        public void action() {
            switch (this.status) {
                case END:
                    //System.out.println(getLocalName()+" end");
                    this.end();
                    break;
                case CALC:
                    //System.out.println(getLocalName()+" cal");
                    this.calc();
                    break;
                case SEND:
                    //System.out.println(getLocalName()+" send");

                    this.send_();
                    break;
                case INIT:
                    //System.out.println(getLocalName()+" init");
                    this.init();
                    break;
                case WAIT:
                    //System.out.println(getLocalName()+" wait");

                    this.wait_();
                    break;
                default:
                    System.out.println(this.status);
                    System.out.println("SomeError in testagent");
                    break;
            }

        }

        @Override
        public boolean done() {
            return this.status == Status.END;
        }

        @Override
        public int onEnd() {  //done()方法返回真时调用
           
            myAgent.doDelete();
            //System.out.println("finished");
            return super.onEnd();
        }

        private void init() {
            // initialise
            //this.algorithme = new Tabou(this.cities);
        	ACLMessage acl1=receive();
            if(acl1!=null) {
                //System.out.println("receiving");
                doWait(1000);
                //System.out.println(getLocalName()+" receive a message");
                //System.out.println("the message is: "+acl1.getContent());
                //System.out.println("the message is: "+acl1.getSender().getLocalName());
                Route initialRoute = new Route(acl1.getContent());
                
                switch(s) {
                case("Tabou"):
                	this.opti = new Optimizer(paras,initialRoute.getTotalDistance());
                	this.algorithme=new Tabou(initialRoute,this.opti.getParam());
                	System.out.println("Recuit parametre: "+this.opti.getParam());

                	break;
                case("Recuit"):
                	this.opti = new Optimizer(paras,initialRoute.getTotalDistance());
                	this.algorithme=new Recuit(initialRoute,this.opti.getParam());
                	System.out.println("Recuit parametre: "+this.opti.getParam());
                	break;
                case("Genetic"):
                	this.opti = new Optimizer(paras,initialRoute.getTotalDistance());
                	this.algorithme=new GeneticAlgorithm(initialRoute,this.opti.getParam());
                	System.out.println("Recuit parametre: "+this.opti.getParam());
                	break;
                default: System.out.println("ERROR FORMAT INFORMATION");break;
                }
                System.out.println("**********************"+new Route(acl1.getContent()).getTotalDistance());
                
                //this.algorithme.setInitRoute(acl1.getContent());
                //doDelete();
                this.status = Status.CALC;
                //System.out.println("Agent initializing, ready for calculating");

            }else {
            	this.status = Status.INIT;
            }
        }

        private void calc() {
            // calculate
            this.algorithme.runtest();
            this.opti.Evalue(this.algorithme.getBestRoute().getTotalDistance());
            //System.out.println(this.algorithme.getBestRoute().getTotalDistance());
            //System.out.println("Calculate finishing, ready for sending");
            this.status = Status.SEND;
        }

        private void send_() {
            // send solution
        	doWait(3000);
            ACLMessage acl=new ACLMessage(ACLMessage.INFORM); //通知
            AID r=new AID();
            r.setLocalName("Center_Agent");  //设置接收Agent的本地名
            acl.addReceiver(r);     //添加到ACL消息中
            acl.setSender(getAID());  //设置发送者本地名，这里可以省略
            acl.setContent(this.algorithme.getBestRoute().toString()); //设置内容
            send(acl);   //向Rec发送消息
            //System.out.println("local name is:" + getLocalName());
            //System.out.println(getLocalName()+" send Hello to Rec");
            //System.out.println("**********************"+this.algorithme.getBestRoute().getTotalDistance());
            //System.out.println("send finished");
            doWait(3000);  
            //doDelete();
            //System.out.println("Sending finishing, see if the result meets demand");
            //int iter = 5;
            this.status = Status.WAIT;
        }

        private void end() {
            // end the agent
            System.out.println("Agent stopped");
        }

        private void wait_() {
        	ACLMessage acl1=receive();
            if(acl1!=null) {
                //System.out.println("receiving");
                doWait(1000);
                //System.out.println(getLocalName()+" receive a message");
                //System.out.println("the message is: "+acl1.getContent());
                //System.out.println("the message is: "+acl1.getSender().getLocalName());
                String message=acl1.getContent();
                if(message.equals("END")) {
                	this.status = Status.END;
                }
                else {
                	//this.algorithme.setInitRoute(acl1.getContent());
                	switch(s) {
                    case("Tabou"):
                    	this.algorithme=new Tabou(new Route(acl1.getContent()),this.opti.getParam());
                		System.out.println(this.opti.getParam());
                    	break;
                    case("Recuit"):
                    	this.algorithme=new Recuit(new Route(acl1.getContent()),this.opti.getParam());
                		System.out.println(this.opti.getParam());
                    	break;
                    case("Genetic"):
                    	this.algorithme=new GeneticAlgorithm(new Route(acl1.getContent()),this.opti.getParam());
                		System.out.println(this.opti.getParam());
                    	break;
                    default: System.out.println("ERROR FORMAT INFORMATION");break;
                    }
					/*
					 * if(this.s=="Recuit") { this.algorithme=new Recuit(new
					 * Route(acl1.getContent()),this.opti.getParam());
					 * System.out.println(this.opti.getParam()); } else {
					 * this.algorithme.setInitRoute(acl1.getContent()); }
					 */
                	System.out.println("**********************"+new Route(acl1.getContent()).getTotalDistance());
                    
                	//doDelete();
                	this.status = Status.CALC;
                	//System.out.println("Agent initializing, ready for calculating");
                }
            }else {
            	this.status = Status.WAIT;
            }
        }

    }
}
