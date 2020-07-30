import java.awt.Color;

public class Controller extends Thread {
	Event64[] evToRed;//array of events to red
	Event64[]evToGreen;//array of events to green
	Event64[] evAtRed;//array of events at red(get from all ramzors )
	Event64[] evToChol;//array of events to chol mode
	Event64[] evToShabat;//array of events to shabat mode
	Event64 evShabatButt;//event of press shabat button
	Event64 evCholButt;//event of press chol button
	Event64 evRegl;//event of press walker button
	MyTimer72 timer;//timer
	Event64 evTimer;//event of timer

	//the groups of ramzors who can act together
	int group0[]={0,6,7,13,12,10,9};
	int group1[]={1,4,5,7,6,13,12,10,9};
	int group2[]={2,4,5,6,7,8,11,12,13,14,15};
	int group3[]={3,4,5,15,14,8,11,10,9 };

	enum OutState {ON_CHOL,ON_SHABAT}//state of shabat mode and chol mode
	OutState outState;

	enum State {GROUP0_GREEN, GROUP0_TURNING_RED,GROUP0_RED,GROUP1_GREEN,GROUP1_TURNING_RED,GROUP1_RED,
		GROUP2_GREEN,GROUP2_TURNING_RED,GROUP2_RED,GROUP3_GREEN,GROUP3_TURNING_RED,GROUP3_RED,
		CONDITION1,CONDITION2,CONDITION3,CONDITION4}///enum to all states in state chart (state+condition)
	State state;
	
	String name;
/***
 constructor of the controller
 * @param evToRed array of events to red
 * @param evToGreen array of events to green
 * @param evAtRed array of events at red(get from all ramzors )
 * @param evToChol array of events to chol mode
 * @param evToShabat array of events to shabat mode
 * @param evShabatButt event of press shabat button
 * @param evCholButt event of press chol button
 * @param evRegl event of press walker button
 */
	public Controller(Event64[] evToRed, Event64[] evToGreen, Event64[] evAtRed, Event64[] evToChol,
			Event64[] evToShabat, Event64 evShabatButt, Event64 evCholButt, Event64 evRegl) {
		super();
		//set all the fields.
		this.evToRed = evToRed;
		this.evToGreen = evToGreen;
		this.evAtRed = evAtRed;
		this.evToChol = evToChol;
		this.evToShabat = evToShabat;
		this.evShabatButt = evShabatButt;
		this.evCholButt = evCholButt;
		this.evRegl = evRegl;
		start();//start the function that control the events.
	}
	/***
	 * this function describe the controller's behavior in every state
	 */
	public void run()
	{
		state=State.GROUP0_GREEN;//start with groups 0 in green
		outState=OutState.ON_CHOL;//we start in chol mode
		setGroup0Green();//function that set all the ramzors in group 0 to green
		evTimer=new Event64();//create new timer
		timer= new MyTimer72(3000,evTimer);//3 seconds
		while (true)//all time check
		{
			switch(outState)//Which event we get
			{
			case ON_CHOL://we get chol event
				while(outState==OutState.ON_CHOL)//not get shabat events
				{
					switch (state) {//Which event we get
					case GROUP0_GREEN://we get group 0 green event
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors to shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else{
								if(evTimer.arrivedEvent())//tm(3000) is finish
								{
									evTimer.waitEvent();
									setGroup0Red();//function that set all the ramzors in group 0 to red
									state=State.GROUP0_TURNING_RED;//set the state to g0 turning red
									break;
								}
								else
									yield();//releases the cpu
							}						
						}
						break;
					case GROUP0_TURNING_RED:// we get g0 turning red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in mode shabat
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								//check a ramzors in group that in red mode
								if(evAtRed[0].arrivedEvent()&&evAtRed[6].arrivedEvent()&&evAtRed[7].arrivedEvent()&&evAtRed[13].arrivedEvent()
										&&evAtRed[12].arrivedEvent()&&evAtRed[10].arrivedEvent()&&evAtRed[9].arrivedEvent()){

									for (int i = 0; i < group0.length; i++)//all the ramzors in group 
										evAtRed[group0[i]].waitEvent();
									evTimer=new Event64();//create new event timer
									new MyTimer72(400,evTimer);//set 4 ms
									state=State.GROUP0_RED;//set the state to g0 red
									break;
								}else
									yield();//releases the cpu
						}

						break;
					case GROUP0_RED://we get g0 red 
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press on shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in mode shabat
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								if(evRegl.arrivedEvent())//we press on walker button
								{
									name=(String)evRegl.waitEvent();//get the ramzor who press the button
									state=State.CONDITION2;//go to condition 2 state
									break;
								}
								else{
									//tm(400)
									if(evTimer.arrivedEvent()){//the timer ending
										evTimer.waitEvent();
										setGroup1Green();//function that set all the ramzors in group 1 to green
										evTimer=new Event64();//create new timer event
										new MyTimer72(3000,evTimer);//set 3 second
										state=State.GROUP1_GREEN;//set state to g1 green
										break;
									}else yield();//releases the cpu
								}

						}
						break;
					case GROUP1_GREEN://we get g1 green
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press on shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in mode shabat
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else{
								//tm(3000)
								if(evTimer.arrivedEvent()){//the timer ending
									evTimer.waitEvent();
									setGroup1Red();//function that set all the ramzors in group 1 to red
									state=State.GROUP1_TURNING_RED;//set state to g1 turning red
									break;
								}
								else
									yield();//releases the cpu
							}
						}
						break;
					case GROUP1_TURNING_RED://we get g1 turning red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in mode shabat
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								//check the ramzors in group1 1 that in red
								if(evAtRed[1].arrivedEvent()&&evAtRed[4].arrivedEvent()&&evAtRed[5].arrivedEvent()&&evAtRed[7].arrivedEvent()
										&&evAtRed[6].arrivedEvent()&&evAtRed[13].arrivedEvent()
										&&evAtRed[12].arrivedEvent()&&evAtRed[10].arrivedEvent()&&evAtRed[9].arrivedEvent()){
									for (int i = 0; i < group1.length; i++)//get over all the ramzors in group 1 
										evAtRed[group1[i]].waitEvent();
									evTimer=new Event64();//create new timer event
									timer= new MyTimer72(400,evTimer);//4 ms
									state=State.GROUP1_RED;//set state to g1 red
									break;
								}else
									yield();//releases the cpu
						}
						break;
					case GROUP1_RED://we get group1 red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//we press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								if(evRegl.arrivedEvent())//press on some walker button
								{
									name=(String)evRegl.waitEvent();//get the ramzor who press the button
									state=State.CONDITION4;//set state to condition 4
									break;
								}
								else{//tm(400)
									if(evTimer.arrivedEvent()){//the timer ending
										evTimer.waitEvent();		
										setGroup2Green();//function that set all the ramzors in group 2 to green
										evTimer=new Event64();//create new timer event
										timer= new MyTimer72(3000,evTimer);//set 3 seconds
										state=State.GROUP2_GREEN;//set state to g1 green
										break;
									}else yield();//releases the cpu
								}
						}
						break;
					case GROUP2_GREEN://ew get g2 green
						while(true)
						{
							if(evShabatButt.arrivedEvent())//press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors to shabat mode
								outState=OutState.ON_SHABAT;//chanfe outState to shabat
								break;
							}
							else{//tm(3000)
								if(evTimer.arrivedEvent()){//the timer ending
									evTimer.waitEvent();
									setGroup2Red();//function that set all the ramzors in group 2 to red
									state=State.GROUP2_TURNING_RED;//set state to g2 turning red
									break;
								}else yield();//release the cpu
							}

						}
						break;
					case GROUP2_TURNING_RED://we get g2 turning red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								//check all ramzors in group2 that in red
								if(evAtRed[2].arrivedEvent()&&evAtRed[4].arrivedEvent()&&evAtRed[5].arrivedEvent()&&evAtRed[6].arrivedEvent()
										&&evAtRed[7].arrivedEvent()&&evAtRed[8].arrivedEvent()&&evAtRed[11].arrivedEvent()
										&&evAtRed[12].arrivedEvent()&&evAtRed[13].arrivedEvent()&&evAtRed[14].arrivedEvent()&&evAtRed[15].arrivedEvent()){
									for (int i = 0; i < group2.length; i++)//over all group2's ramzors 
										evAtRed[group2[i]].waitEvent();
									evTimer=new Event64();//create new timer event
									timer= new MyTimer72(400,evTimer);//4 ms
									state=State.GROUP2_RED;//set state to g2 red
									break;
								}else
									yield();//releases the cpu
						}
						break;
					case GROUP2_RED://we get g2 red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//pres shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								if(evRegl.arrivedEvent())//walker button is pressed
								{
									name=(String)evRegl.waitEvent();//get the name of ramzor that pressed
									state=State.CONDITION3;//set state to condition 3
									break;
								}
								else{//tm(400)
									if(evTimer.arrivedEvent()){//the timer ending
										evTimer.waitEvent();
										setGroup3Green();//function that set all the ramzors in group 3 to green
										evTimer=new Event64();//create new timer event
										timer= new MyTimer72(3000,evTimer);//3 seconds
										state=State.GROUP3_GREEN;//set state to g3 green
										break;
									}else yield();//releases the cpu
								}
						}
						break;
					case GROUP3_GREEN:// we get g3 green
						while(true)
						{
							if(evShabatButt.arrivedEvent())//press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else{//tm(3000)
								if(evTimer.arrivedEvent()){//timer ending
									evTimer.waitEvent();
									setGroup3Red();////function that set all the ramzors in group 3 to green
									state=State.GROUP3_TURNING_RED;//set state 2 g3 turning red
									break;
								}else yield();//releases the cpu
							}

						}
						break;
					case GROUP3_TURNING_RED://we get g3 turning red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								//if all ramzors in group 3 in red
								if(evAtRed[3].arrivedEvent()&&evAtRed[4].arrivedEvent()&&evAtRed[5].arrivedEvent()&&evAtRed[15].arrivedEvent()
										&&evAtRed[14].arrivedEvent()&&evAtRed[8].arrivedEvent()&&evAtRed[11].arrivedEvent()
										&&evAtRed[10].arrivedEvent()&&evAtRed[9].arrivedEvent()){
									for (int i = 0; i < group3.length; i++)//get over all group3 
										evAtRed[group3[i]].waitEvent();
									evTimer=new Event64();//create new timer event
									timer= new MyTimer72(400,evTimer);//set 4 ms
									state=State.GROUP3_RED;//set state to g3 red
									break;
								}else yield();//releases the cpu
						}
						break;
					case GROUP3_RED://we get g3 red
						while(true)
						{
							if(evShabatButt.arrivedEvent())//press shabat button
							{
								evShabatButt.waitEvent();
								setShabat();//function that set all the ramzors in shabat mode
								outState=OutState.ON_SHABAT;//set outState to shabat
								break;
							}
							else
								if(evRegl.arrivedEvent())//press on walker button
								{
									name=(String)evRegl.waitEvent();//get the ramzor that press te button
									state=State.CONDITION1;//change state to condition 1
									break;
								}
								else{//tm(400)
									if(evTimer.arrivedEvent()){//timer ending
										evTimer.waitEvent();
										setGroup0Green();//function that set all the ramzors in group 0 to green
										state=State.GROUP0_GREEN;//set state to g0 green
										evTimer=new Event64();//create new event time
										timer= new MyTimer72(3000,evTimer);//3 seconds
										break;
									}else yield();//releases the cpu
								}
						}
						break;
					case CONDITION1://we get condition 1
						try {

							if(inG0(name))//the press button from group 0
							{
								setGroup0Green();//function that set all the ramzors in group 0 to green
								state=State.GROUP0_GREEN;//change state
							}
							else if(inG1(name))//the press button from g1
							{
								setGroup1Green();//function that set all the ramzors in group 1 to green
								state=State.GROUP1_GREEN;//change state
							}
							else if(inG2(name))//the press button from group 2
							{
								setGroup2Green();//function that set all the ramzors in group 2 to green
								state=State.GROUP2_GREEN;//change state
							}
							else if(inG3(name))//the press button from group 3
							{
								setGroup3Green();//function that set all the ramzors in group 3 to green
								state=State.GROUP3_GREEN;//change state
							}
							else
								throw new Exception();
						} catch (Exception e) {
							System.out.println("no such traffic light1");
						}
						break;

					case CONDITION2:
						System.out.println(name);
						try {

							if(inG1(name))//the press button from group 1
							{
								setGroup1Green();//function that set all the ramzors in group 1 to green
								state=State.GROUP1_GREEN;//change state
							}
							else if(inG2(name))//the press button from group 2
							{
								setGroup2Green();//function that set all the ramzors in group 2 to green
								state=State.GROUP2_GREEN;//change state
							}
							else if(inG3(name))//the press button from group 3
							{
								setGroup3Green();//function that set all the ramzors in group 3 to green
								state=State.GROUP3_GREEN;//change state
							}
							else if(inG0(name))//the press button from group 0
							{
								setGroup0Green();//function that set all the ramzors in group 0 to green
								state=State.GROUP0_GREEN;//change state
							}
							else
								throw new Exception();
						} catch (Exception e) {
							System.out.println("no such traffic light2");
						}
						evTimer=new Event64();//create new timer event
						timer= new MyTimer72(3000,evTimer);//3 second
						break;
					case CONDITION3:
						try {						

							if(inG3(name))//the press button from group 3
							{
								setGroup3Green();//function that set all the ramzors in group 3 to green
								state=State.GROUP3_GREEN;//change state
							}
							else if(inG0(name))//the press button from group 0
							{
								setGroup0Green();//function that set all the ramzors in group 0 to green
								state=State.GROUP0_GREEN;//change state

							}else if(inG1(name))//the press button from group 1
							{
								setGroup1Green();//function that set all the ramzors in group 1 to green
								state=State.GROUP1_GREEN;//change state
							}
							else if(inG2(name))//the press button from group 2
							{
								
								setGroup2Green();//function that set all the ramzors in group 2 to green
								state=State.GROUP2_GREEN;//change state
							}
							else
								throw new Exception();
						} catch (Exception e) {
							System.out.println("no such traffic light3");
						}
						evTimer=new Event64();//create new timer event
						timer= new MyTimer72(3000,evTimer);//3 second
						break;
					case CONDITION4:
						try {						
							if(inG2(name))//the press button from group 2
							{
								setGroup2Green();//function that set all the ramzors in group 2 to green
								state=State.GROUP2_GREEN;//change state
							}
							else if(inG3(name))//the press button from group 3
							{
								setGroup3Green();//function that set all the ramzors in group 3 to green
								state=State.GROUP3_GREEN;//change state
							}
							else if(inG0(name))//the press button from group 0
							{
								setGroup0Green();//function that set all the ramzors in group 0 to green
								state=State.GROUP0_GREEN;//change state
							}
							else if(inG1(name))//the press button from group 1
							{
								setGroup1Green();//function that set all the ramzors in group 1 to green
								state=State.GROUP1_GREEN;//change state
							}
							else
								throw new Exception();
						} catch (Exception e) {
							System.out.println("no such traffic light4");
						}
						evTimer=new Event64();//create new timer event
						timer= new MyTimer72(3000,evTimer);//3 second
						break;
					default:
						break;
					}
				}
				break;
			case ON_SHABAT://we get shabat event
				while(true){
					if(evCholButt.arrivedEvent()){//press on cho button
						evCholButt.waitEvent();
						setChol();//function that set all the ramzors in mode chol
						setGroup0Green();//function that set all the ramzors in g0 to green
						state=State.GROUP0_GREEN;//change the state
						evTimer=new Event64();//create new timer event
						timer= new MyTimer72(3000,evTimer);
						outState=OutState.ON_CHOL;//change outState to chol
					}else
						yield();//releases cpu
					break;
				}
				break;
			default:
				break;
			}

		}
	}
/***
 * this function set all the ramzors in g0 to red
 */
	private void setGroup0Red() {
		for (int i = 0; i < group0.length; i++) 
			evToRed[group0[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g1 to red
	 */
	private void setGroup1Red() {
		for (int i = 0; i < group1.length; i++) 
			evToRed[group1[i]].sendEvent();

	}
	/***
	 * this function set all the ramzors in g2 to red
	 */
	private void setGroup2Red() {
		for (int i = 0; i < group2.length; i++) 
			evToRed[group2[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g3 to red
	 */
	private void setGroup3Red() {
		for (int i = 0; i < group3.length; i++) 
			evToRed[group3[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g0 to green
	 */
	private void setGroup0Green() {
		for (int i = 0; i < group0.length; i++) 
			evToGreen[group0[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g1 to green
	 */
	private void setGroup1Green() {
		for (int i = 0; i < group1.length; i++) 
			evToGreen[group1[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g2 to green
	 */
	private void setGroup2Green() {
		for (int i = 0; i < group2.length; i++) 
			evToGreen[group2[i]].sendEvent();
	}
	/***
	 * this function set all the ramzors in g3 to green
	 */
	private void setGroup3Green() {
		for (int i = 0; i < group3.length; i++) 
			evToGreen[group3[i]].sendEvent();
	}

	/***
	 * this function check if the ramzor that press on button walker is in g0
	 * @param name the name of the event of press
	 * @return true if the ramzor include in the group
	 */
	private boolean inG0(String name){
		if(name.equals("6"))
			return true;
		if(name.equals("7"))
			return true;
		if(name.equals("13"))
			return true;
		if(name.equals("12"))
			return true;
		if(name.equals("10"))
			return true;
		if(name.equals("9"))
			return true;
		return false;
	}
	/***
	 * this function check if the ramzor that press on button walker is in g1
	 * @param name the name of the event of press
	 * @return true if the ramzor include in the group
	 */
	private boolean inG1(String name){
		if(name.equals("4"))
			return true;
		if(name.equals("5"))
			return true;
		if(name.equals("6"))
			return true;
		if(name.equals("7"))
			return true;
		if(name.equals("10"))
			return true;
		if(name.equals("9"))
			return true;
		if(name.equals("12"))
			return true;
		if(name.equals("13"))
			return true;
		return false;
	}
	/***
	 * this function check if the ramzor that press on button walker is in g2
	 * @param name the name of the event of press
	 * @return true if the ramzor include in the group
	 */
	private boolean inG2(String name){
		if(name.equals("6"))
			return true;
		if(name.equals("7"))
			return true;
		if(name.equals("4"))
			return true;
		if(name.equals("5"))
			return true;
		if(name.equals("8"))
			return true;
		if(name.equals("11"))
			return true;
		if(name.equals("12"))
			return true;
		if(name.equals("13"))
			return true;
		if(name.equals("14"))
			return true;
		if(name.equals("15"))
			return true;
		return false;
	}
	/***
	 * this function check if the ramzor that press on button walker is in g3
	 * @param name the name of the event of press
	 * @return true if the ramzor include in the group
	 */
	private boolean inG3(String name){
		if(name.equals("4"))
			return true;
		if(name.equals("5"))
			return true;
		if(name.equals("15"))
			return true;
		if(name.equals("14"))
			return true;
		if(name.equals("8"))
			return true;
		if(name.equals("11"))
			return true;
		if(name.equals("10"))
			return true;
		if(name.equals("9"))
			return true;
		return false;
	}
	/***
	 * this function set all the ramzors in shabat mode
	 */
	private void setShabat() {
		for (int i = 0; i < evToShabat.length; i++) 
			evToShabat[i].sendEvent();
	}
	/***
	 * this function set all the ramzors in chol mode
	 */
	private void setChol() {
		for (int i = 0; i < evToChol.length; i++) 
			evToChol[i].sendEvent();

	}

}
