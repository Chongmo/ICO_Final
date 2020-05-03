package PVC.Agent;

import java.util.ArrayList;
import java.util.List;

import PVC.Agent.TestAgent.Status;
import PVC.Data.CityData;
import PVC.Definitions.Route;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Center_Agent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Route initialRoute;
	
	
	protected void setup() {
		CityData Data = new CityData(100);
        this.initialRoute = new Route(Data.getCities());
        String a[][]= {{"Tabou"},{"Recuit"},{"Genetic"}};
        
        //Runtime rt = Runtime.instance();  //Création d'une instance de JADE unique par ORDI
		//ProfileImpl pMain = new ProfileImpl(null, 2222,"maPlateforme"); //Création du Profil (Port + Nom) du conteneur Principal
		//AgentContainer ac =rt.createMainContainer(pMain); //Création du conteneur Principale
		
        AgentContainer ac=(AgentContainer) getContainerController();
		try {
			for(int i=0;i<3;i++) {
			AgentController aa=((ContainerController) ac).createNewAgent(a[i][0], "PVC.Agent.TestAgent",a[i]);
			aa.start();
			System.out.println(String.format("An agent: %s is built",a[i][0]));
			}
		}catch(Exception e) {
			System.out.println("Create agent error");
		}
		this.addBehaviour(new CenterBehave(initialRoute));
		
	}

	public Center_Agent() {
		// TODO Auto-generated constructor stub
	}
	
	enum Status {
        INIT, END, SEND, WAIT, COMP
    }
	
	class CenterBehave extends Behaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Route initR;
		private Route bestR ;
		private ArrayList<Float> distances = new ArrayList<Float>();
		private float bestdistance;
		private String RecuRoute;
		private Status status = Status.SEND;
		private String receiver = null;
		private boolean end=false;
		
		
		public CenterBehave(Route r) {
			this.initR = new Route(r);
			this.bestR = new Route (r);
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			switch(this.status) {
			case END:
				this.onEnd();
				break;
			case SEND:
                this.send_();
                break;
            case WAIT:
                this.wait_();
                break;
            case COMP:
                this.comp_();
                break;
            default:
                System.out.println(this.status);
                System.out.println("SomeError in center");
                break;
			}
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return this.status == Status.END;
		}
		@Override
        public int onEnd() {  //done()方法返回真时调用
           
            myAgent.doDelete();
            System.out.println("finished");
            return super.onEnd();
        }
		private void send_() {
        	doWait(3000);
            ACLMessage acl=new ACLMessage(ACLMessage.INFORM); //通知
            ArrayList<String> relist =new ArrayList<String>();
            if(receiver == null) 
            { relist.add("Tabou");
            relist.add("Recuit");
            relist.add("Genetic");
            }
            else {relist.add(receiver);}
            for(String name:relist) {
            	AID r=new AID();
            	System.out.println("send to "+name);
                r.setLocalName(name);  //设置接收Agent的本地名
                acl.addReceiver(r);  
            }
            acl.setSender(getAID());  //设置发送者本地名，这里可以省略
            String cont=this.end?"END":bestR.toString();
            acl.setContent(cont); //设置内容
            send(acl);
            doWait(3000);  
            String mess = this.end?String.format("Finally, the best distance is %f", bestR.getTotalDistance()):"Sending finishing, a new round has begun";
            System.out.println(mess);
 

            this.status =this.end?Status.END: Status.WAIT;
        }
		private void wait_() {
			// wait for the results of other agents
        	ACLMessage acl1=receive();
            if(acl1!=null) {
                System.out.println("receiving");
                doWait(1000);
                System.out.println(getLocalName()+" receive a message");
                System.out.println("the message is: "+acl1.getContent());
                System.out.println("the message is from: "+acl1.getSender().getLocalName());
                this.receiver = acl1.getSender().getLocalName();
                String message=acl1.getContent();
                this.RecuRoute = message;
            	this.status = Status.COMP;

            }else {
            	this.status = Status.WAIT;
            }
		}
		private void comp_() {
			Route rt = new Route(this.RecuRoute);
			this.bestR = this.bestR.getTotalDistance()<rt.getTotalDistance()?this.bestR:rt;
			distances.add(this.bestR.getTotalDistance());
			if(distances.size()>10) {
				distances.remove(0);
				
			}
			
			if(distances.size() == 10) {
				float res=0;
				for(int i=0;i<distances.size()-1;i++) {
					res+=Math.abs(distances.get(i)-distances.get(i+1));
				}
				res/=this.initR.getTotalDistance();
				if(res<0.001f) {
				this.receiver = null;
				this.end=true;
				}
				}
			this.status = Status.SEND;
			System.out.println(String.format("distance [%f] is recorded\n", this.bestR.getTotalDistance()));
		}
		
	}

}
