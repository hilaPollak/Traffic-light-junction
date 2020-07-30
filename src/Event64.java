// event with Object,   Tishrey 5764
// filename  : Event64.java

import java.lang.Thread;
class Event64 
{
	private Object localData;
	private boolean inUse;
	private boolean isWaitingForMe;

	public Event64()	
	{
		resetEvent();
	}

	public void resetEvent()
	{
		isWaitingForMe = false;
		localData = null;
		inUse = false;
	}

	public boolean arrivedEvent()	
	{
		return inUse;
	}

	public synchronized Object waitEvent()
	{
		if (isWaitingForMe)
			notify();

		if (!inUse) 
		{
			isWaitingForMe = true;
			try
			{ 
				wait();
			} catch(InterruptedException e){ } ;
		}

		Object s ;
		s = localData;    
		resetEvent();
		return s;
	}

	public void sendEvent()
	{
		while (!trySendEvent(null)) Thread.yield();
	}

	void sendEvent(Object aData)
	{
		while (!trySendEvent(aData)) Thread.yield();
	}

	public synchronized boolean trySendEvent(Object aData)
	{ 
		if (inUse)
			return false;

		inUse = true;
		localData = aData;

		if (isWaitingForMe) 
			notify();

		return true;
	}

	public void sendSyncEvent()
	{
		while (!trySendSyncEvent(null)) Thread.yield();
	}

	public void sendSyncEvent(Object aData)
	{
		while (!trySendSyncEvent(aData)) Thread.yield();
	}

	public synchronized boolean trySendSyncEvent(Object aData)
	{
		if (inUse)
			return false;

		inUse = true;
		localData = aData;

		if (isWaitingForMe) 
			notify();
		else 
		{
			isWaitingForMe = true;
			try 
			{
				wait();
			} catch(InterruptedException exce) {};
		}

		return true;
	}

}